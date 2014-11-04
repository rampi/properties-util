package cl.i2b.properties.test;


import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.container.Container;
import cl.i2b.properties.test.utils.TestUtil;

public class JDBCProcessorSingleDBTest {

	@BeforeClass	
	public static void setupClass() throws ClassNotFoundException, SQLException {
		TestUtil.createTestDB();
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
		
}
