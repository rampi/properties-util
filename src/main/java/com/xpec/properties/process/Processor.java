package com.xpec.properties.process;

import com.xpec.properties.annotations.Properties;


public interface Processor {

	void processClass(Properties properties);
	
	String processField(Properties properties, String fieldName);
	
}
