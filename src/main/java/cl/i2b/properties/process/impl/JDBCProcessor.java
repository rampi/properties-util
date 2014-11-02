package cl.i2b.properties.process.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.process.Processor;

public class JDBCProcessor implements Processor{
	private Map<String, String> keys;
	
	public void processClass(Properties properties) {
		keys = new HashMap<String, String>();
		String url = properties.jdbc();
		if(url == null || "".equals(url))
			url = properties.value();
		populateKeys(properties.driverClassName(), url, properties.query());
	}

	public String processField(Properties properties, String fieldName) {
		if(keys == null) {
			keys = new HashMap<String, String>();
			String url = properties.jdbc();
			if(url == null || "".equals(url))
				url = properties.value();
			
			this.populateKeys(properties.driverClassName(), url, properties.query());
		}
		if(properties.key() != null && !properties.key().equals("")) {
			return keys.get(properties.key());
		}
		if(properties.value()!=null && !"".equals(properties.value()) && (properties.driverClassName()==null ||properties.driverClassName().equals("")) ) {
			return keys.get(properties.value());
		}
		return keys.get(fieldName);
	}
	
	private void populateKeys(String driver, String url, String query) {
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url);
			ResultSet rs = conn.createStatement().executeQuery(query);
			while(rs.next()) {
				keys.put(rs.getString(1), rs.getString(2));
			}
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

}
