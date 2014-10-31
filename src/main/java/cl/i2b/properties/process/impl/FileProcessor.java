package cl.i2b.properties.process.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.process.Processor;

public class FileProcessor implements Processor{

	private String path;
	
	private java.util.Properties propertiesFile;
	
	private ArrayList<java.util.Properties> propsArray = new ArrayList<java.util.Properties>();
	
	private HashMap<String, String> propertiesCache = new HashMap<String, String>();
	
	public void processClass(Properties properties) {
		try{			
			path = properties.value();
			path = path == null || (path != null && path.isEmpty()) ? properties.path() : path;
			File file = new File(path);
			if( file.isDirectory() ){
				File[] files = file.listFiles();
				for( File fs : files ){
					java.util.Properties p = new java.util.Properties();
					p.load( new FileInputStream(fs) );
					propsArray.add( p );
				}
			}
			else if( file.exists() ){
				propertiesFile = new java.util.Properties();
				propertiesFile.load(new FileInputStream(file));
			}			
		}catch(Exception ex){}
	}

	private String findProperty(String key){
		if( propertiesCache.containsKey(key) )return propertiesCache.get(key);		
		for( java.util.Properties p : propsArray ){
			String value = p.getProperty(key);
			if( value != null ){
				propertiesCache.put(key, value);
				return value;
			}
		}		
		return null;		
	}
	
	public String processField(Properties properties, String fieldName) {
		String key = properties.key() != null && !properties.key().isEmpty() ? properties.key() : fieldName;						
		if( key != null && !key.isEmpty() && propertiesFile != null ){			
			return propertiesFile.getProperty(key);
		}
		else if( key != null && !key.isEmpty() && !propsArray.isEmpty() ){
			return findProperty(key);
		}
		else if( propertiesFile != null ){			
			return propertiesFile.getProperty(fieldName);
		}
		else if( !propsArray.isEmpty() ){			
			return findProperty(fieldName);
		}		
		return null;
	}
	
}