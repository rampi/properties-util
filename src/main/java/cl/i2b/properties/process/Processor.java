package cl.i2b.properties.process;

import cl.i2b.properties.annotations.Properties;


public interface Processor {

	void processClass(Properties properties);
	
	String processField(Properties properties, String fieldName);
	
}
