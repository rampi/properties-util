package cl.i2b.properties.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.process.Processor;
import cl.i2b.properties.process.impl.FileProcessor;
import cl.i2b.properties.process.impl.JDBCProcessor;

public class Container {

	private static String jdbcPrefix = "(?<=//)[^:]*";
	
	private static Pattern jdbcPattern = Pattern.compile(jdbcPrefix);
	
	private static HashMap<String, Processor> processorCache = new HashMap<String, Processor>();
	
	public static void injectProperties( Object obj ) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class<?> clazz = Class.forName(obj.getClass().getName());
		Processor proc = processClassAnnotations(clazz);
		processFieldsAnnotations(obj, proc, clazz);
	}
	
	private static void processFieldsAnnotations(Object obj, Processor proc, Class<?> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Field[] fields = clazz.getDeclaredFields();
		for( Field field : fields ){
			field.setAccessible(true);
			Properties properties = field.getAnnotation(Properties.class);
			if( properties != null ){
				String value = proc.processField(properties, field.getName());
				if( field.getType().getSimpleName().equals("String") ){
					field.set(obj, value);
				}
			}
		}
	}
	
	private static Processor processClassAnnotations(Class<?> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		if( processorCache.containsKey(clazz.getName()) )return processorCache.get(clazz.getName());
		Annotation[] ann = clazz.getAnnotations();
		for( Annotation an : ann ){
			if( an.annotationType().equals(Properties.class) ){
				Properties prop = (Properties)an;
				String value = prop.value() != null && !prop.value().isEmpty() ? prop.value() : prop.path();
				if( value == null || (value != null && value.isEmpty()) )throw new IllegalArgumentException("You need specify an default value or an path value");				
				if( prop != null && value != null && !value.equals("") ){
					Matcher m = jdbcPattern.matcher(prop.value());
					Processor processor = m.find() ? new JDBCProcessor() : new FileProcessor(); 
					processorCache.put(clazz.getName(), processor);
					processor.processClass(prop);
					return processor;
				}
			}
		}
		return null;
	}
		
}
