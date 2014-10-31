package cl.i2b.properties.process.impl;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.process.Processor;

public class JDBCProcessor implements Processor{

	public void processClass(Properties properties) {
		System.out.println("Soy jdbc y tengo a "+properties);
	}

	public String processField(Properties properties, String fieldName) {
		return null;
	}

}
