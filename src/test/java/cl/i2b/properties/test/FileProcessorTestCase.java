package cl.i2b.properties.test;

import org.junit.Assert;
import org.junit.Test;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.container.Container;

public class FileProcessorTestCase {

	@Test
	public void testFilePropertiesDirectory() {
		@Properties("/home/rampi/properties/")
		class PropTest{
			@Properties
			private String url;
			@Properties(key="saludo")
			private String sayHello;
			public PropTest(){
				
			}
		}
		try{
			PropTest pt = new PropTest();
			Container.injectProperties(pt);
			Assert.assertEquals("First Assert", "http://www.google.com.co", pt.url);
			Assert.assertEquals("Second Assert", "Hola Mundo!!", pt.sayHello);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFilePropertiesFile() {
		@Properties("/home/rampi/properties/test.properties")
		class PropTest{
			@Properties(key="saludo")
			private String sayHello;
			public PropTest(){
				
			}
		}
		try{
			PropTest pt = new PropTest();
			Container.injectProperties(pt);
			Assert.assertEquals("Second Assert", "Hola Mundo!!", pt.sayHello);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
