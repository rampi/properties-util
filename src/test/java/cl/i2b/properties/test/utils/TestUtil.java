package cl.i2b.properties.test.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtil {
	public static void createTestyDB() throws SQLException, ClassNotFoundException {
		Class.forName("org.h2.Driver");
        Connection conn = DriverManager. getConnection("jdbc:h2:~/testy;USER=i2b;PASSWORD=i2b");
        
        Statement dml = conn.createStatement();
        
        dml.addBatch("create table if not exists parameters (paramKey varchar(50) primary key, paramValue varchar(120) not null)");
        dml.addBatch("truncate table parameters");
        dml.addBatch("insert into parameters values ('customerName', 'I2B Technologies')");
        dml.addBatch("insert into parameters values ('language', 'java')");
        dml.addBatch("insert into parameters values ('os', 'linux')");
        
        dml.executeBatch();
        
        conn.close();		
	}
	
	public static void createTestDB() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
        Connection conn = DriverManager. getConnection("jdbc:h2:~/test;USER=i2b;PASSWORD=i2b");
        
        Statement dml = conn.createStatement();
        
        dml.addBatch("create table if not exists parameters (paramKey varchar(50) primary key, paramValue varchar(120) not null)");
        dml.addBatch("truncate table parameters");
        dml.addBatch("insert into parameters values ('url', 'http://www.i2b.cl')");
        dml.addBatch("insert into parameters values ('user', 'customer')");
        dml.addBatch("insert into parameters values ('country', 'colombia')");
        
        dml.executeBatch();
        
        conn.close();
		
	}

}
