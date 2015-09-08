package com.xpec.properties.test;


import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xpec.properties.annotations.Properties;
import com.xpec.properties.container.Container;
import com.xpec.properties.test.utils.TestUtil;

public class JDBCProcessorSingleDBTest {

	@BeforeClass	
	public static void setupClass() throws ClassNotFoundException, SQLException {
		TestUtil.createTestDB();
	}
	
	@Test
	public void testConfigureOnClassDefaultProperty() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=admin;PASSWORD=admin", query="select p.paramKey, p.paramValue from parameters p")
		class TestDefaultProperty {
			@Properties
			String url;
			
			@Properties
			String country;
			
		}
		
		TestDefaultProperty tdp = new TestDefaultProperty();
		Container.injectProperties(tdp);
		
		assertEquals("http://www.xpectrumtech.com", tdp.url);
		assertEquals("colombia", tdp.country);
	}
	
	@Test
	public void testConfigureOnClassNonDefaultProperty() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		
		@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=admin;PASSWORD=admin", query="select p.paramKey, p.paramValue from parameters p")
		class TestNonDefaultProperty {
			@Properties("url")
			String nonDefaultURL;
		}
		
		TestNonDefaultProperty tndp = new TestNonDefaultProperty();
		Container.injectProperties(tndp);
		assertEquals("http://www.xpectrumtech.com", tndp.nonDefaultURL);
	}
	
	@Test
	public void testConfigureDirectOnPropertyDefaultName() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		class TestDirectOnPropertyDefaultName {
			@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=admin;PASSWORD=admin", query="select p.paramKey, p.paramValue from parameters p")
			String user;
		}
		TestDirectOnPropertyDefaultName t = new TestDirectOnPropertyDefaultName();
		Container.injectProperties(t);
		assertEquals("customer", t.user);
	}
	
	@Test
	public void testConfigureDirectOnPropertyNonDefaultName() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		class TestDirectOnPropertyDefaultName {
			@Properties(driverClassName="org.h2.Driver", value="jdbc:h2:~/test;IFEXISTS=TRUE;USER=admin;PASSWORD=admin", query="select p.paramKey, p.paramValue from parameters p", key="user")
			String username;
		}
		TestDirectOnPropertyDefaultName t = new TestDirectOnPropertyDefaultName();
		Container.injectProperties(t);
		assertEquals("customer", t.username);
	}
		
}
