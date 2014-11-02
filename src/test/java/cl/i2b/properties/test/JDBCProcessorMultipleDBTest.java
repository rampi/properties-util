package cl.i2b.properties.test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.container.Container;
import cl.i2b.properties.test.utils.TestUtil;

public class JDBCProcessorMultipleDBTest {

	@Test
	public void testDirectOnPropertyMultipleDBDefaultName() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		
		TestUtil.createTestyDB();
		
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
		
		TestUtil.createTestyDB();
		
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
		
		TestUtil.createTestyDB();
		
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


}
