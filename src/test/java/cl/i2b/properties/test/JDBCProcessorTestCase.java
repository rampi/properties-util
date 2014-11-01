package cl.i2b.properties.test;


import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.junit.Test;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.container.Container;

public class JDBCProcessorTestCase {

	@BeforeClass	
	public static void setupClass() throws ClassNotFoundException, SQLException {
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
	
	@Test
	public void testConfigureOnClassDefaultProperty() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p")
		class TestDefaultProperty {
			@Properties
			String url;
			
			@Properties
			String country;
			
		}
		
		TestDefaultProperty tdp = new TestDefaultProperty();
		Container.injectProperties(tdp);
		
		assertEquals("http://www.i2b.cl", tdp.url);
		assertEquals("colombia", tdp.country);
	}
	
	@Test
	public void testConfigureOnClassNonDefaultProperty() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		
		@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p")
		class TestNonDefaultProperty {
			@Properties("url")
			String nonDefaultURL;
		}
		
		TestNonDefaultProperty tndp = new TestNonDefaultProperty();
		Container.injectProperties(tndp);
		assertEquals("http://www.i2b.cl", tndp.nonDefaultURL);
	}
	
	@Test
	public void testConfigureDirectOnPropertyDefaultName() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		class TestDirectOnPropertyDefaultName {
			@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p")
			String user;
		}
		TestDirectOnPropertyDefaultName t = new TestDirectOnPropertyDefaultName();
		Container.injectProperties(t);
		assertEquals("customer", t.user);
	}
	
	@Test
	public void testConfigureDirectOnPropertyNonDefaultName() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		class TestDirectOnPropertyDefaultName {
			@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p", key="user")
			String username;
		}
		TestDirectOnPropertyDefaultName t = new TestDirectOnPropertyDefaultName();
		Container.injectProperties(t);
		assertEquals("customer", t.username);
	}

	@Test
	public void testDirectOnPropertyMultipleDBDefaultName() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		
		createTestyDB();
		
		class TestDirectOnPropertyDefaultName {
			@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p")
			String user;
			
			@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/testy;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p")
			String customerName;
		}
		TestDirectOnPropertyDefaultName t = new TestDirectOnPropertyDefaultName();
		Container.injectProperties(t);
		assertEquals("customer", t.user);
		assertEquals("I2B Technologies", t.customerName);
	}

	@Test
	public void testDirectOnPropertyMultipleDBNonDefaultName() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		
		createTestyDB();
		
		class TestDirectOnProperty {
			@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p")
			String user;
			
			@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/testy;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p", key="language")
			String programmingLanguage;
		}
		TestDirectOnProperty t = new TestDirectOnProperty();
		Container.injectProperties(t);
		assertEquals("customer", t.user);
		assertEquals("java", t.programmingLanguage);
	}
	
	@Test
	public void testMultipleDBOnClassAndDirectBothDefaultName() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		
		createTestyDB();
		
		@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p")
		class TestOnClassAndDirect {
			@Properties
			String user;
			
			@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/testy;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", query="select p.paramKey, p.paramValue from parameters p")
			String language;
		}
		TestOnClassAndDirect t = new TestOnClassAndDirect();
		Container.injectProperties(t);
		assertEquals("customer", t.user);
		assertEquals("java", t.language);
	}
	
	private void createTestyDB() throws SQLException, ClassNotFoundException {
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
	
}
