package cl.i2b.properties.test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.container.Container;
import cl.i2b.properties.test.utils.TestUtil;


public class MultipleSourcesTest {

	@Test
	public void testSimpleMultipleSources() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		TestUtil.createTestDB();
		TestUtil.createTestyDB();

		@Properties(driverClassName = "org.h2.Driver", 
				    jdbc = "jdbc:h2:~/testy;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", 
				    query = "select dp.paramKey, dp.paramValue from parameters dp")
		class SimplePojo {

			@Properties
			String language; // injects language value from table parameters in testy

			@Properties(key = "os")
			String operatingSystem; // injects language os from table parameters in testy

			@Properties(path = "src/test/resources/filesource.properties")
			String name; // injects name from filesource.properties

			@Properties(driverClassName = "org.h2.Driver", 
				    jdbc = "jdbc:h2:~/test;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b", 
				    query = "select dp.paramKey, dp.paramValue from parameters dp",
				    key="url")
			String site; // injects url in site field from table parameters in test

			// ... getters and setters
		}
		
		SimplePojo sp = new SimplePojo();
		Container.injectProperties(sp);
		
		assertEquals("java", sp.language);
		assertEquals("linux", sp.operatingSystem);
		
		assertEquals("http://www.i2b.cl", sp.site);
		assertEquals("Someone Famous", sp.name);
	}

}
