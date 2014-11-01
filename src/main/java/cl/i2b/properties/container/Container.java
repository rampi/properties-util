package cl.i2b.properties.container;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Pattern;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.process.Processor;
import cl.i2b.properties.process.impl.FileProcessor;
import cl.i2b.properties.process.impl.JDBCProcessor;

public class Container {

	private static String jdbcPrefix = "jdbc:(.*)";
	private static String filePrefix = ".*/.*";
	
	private static Pattern jdbcPattern = Pattern.compile(jdbcPrefix);
	private static Pattern filePattern = Pattern.compile(filePrefix);
	
	private static HashMap<String, Processor> processorCache = new HashMap<String, Processor>();
	
	public static void injectProperties( Object obj ) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class<?> clazz = Class.forName(obj.getClass().getName());
		Processor proc = getClassProcessor(clazz);
		for(Field f:clazz.getDeclaredFields()) {
			Processor fieldProcessor = getFieldProcessor(proc, f);
			if(fieldProcessor != null) {
				Properties p = f.getAnnotation(Properties.class);
				if(p != null) {
					boolean oldAccesible = f.isAccessible();
					f.setAccessible(true);
					f.set(obj, fieldProcessor.processField(p, f.getName()));
					f.setAccessible(oldAccesible);
				}
			}
		}
		
	}
	
	private static Processor getClassProcessor(Class<?> clazz) {
		if( processorCache.containsKey(clazz.getName()) )
			return processorCache.get(clazz.getName());
		Properties prop = clazz.getAnnotation(Properties.class);
		if (prop == null)
	
			return null;
		Processor p = createProcessorFromProperties(clazz.getName(), prop);
		if (p != null)
			p.processClass(prop);
		return p;
	}
	
	private static Processor getFieldProcessor(Processor defaultProcessor, Field field) {
		Properties prop = field.getAnnotation(Properties.class);
		if(prop == null)
			return null;
		Processor p = createProcessorFromProperties(field.getDeclaringClass().getName() + "."  + field.getName(), prop);
		if (p == null) {
			return defaultProcessor;
		}
		return p; 
	}
	
	private static Processor createProcessorFromProperties(String name, Properties prop) {
		if(processorCache.containsKey(name))
			return processorCache.get(name);
		String value = prop.value();
		String jdbc = prop.jdbc();
		String driverClassname = prop.driverClassName();
		if(value.equals("")) value = null;
		if(jdbc.equals("")) jdbc = null;
		if(driverClassname.equals("")) driverClassname = null;
		Processor proc = null; 
		if((value != null || jdbc != null) && driverClassname != null){
			String p = jdbc == null?value:jdbc;
			if(jdbcPattern.matcher(p).find()) {
				proc = new JDBCProcessor();
			}
		}
		String path = prop.path();
		if ("".equals(path)) path = null;
		if(proc == null && (value != null || path != null) && driverClassname == null) {
			String p = path == null?value:path;
			if(filePattern.matcher(p).find()) {
				proc = new FileProcessor();
			}
		}
		
		if (proc == null)
			return null; 
		
		processorCache.put(name, proc);
		return proc;
	}
		
}
