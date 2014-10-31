package cl.i2b.properties.test;

import org.junit.Assert;
import org.junit.Test;

import cl.i2b.properties.annotations.Properties;
import cl.i2b.properties.container.Container;

public class FileProcessorTestCase {

	@Test
	public void testFilePropertiesDirectory() {		
		@Properties("src/test/resources/")
		class PropTest{
			@Properties
			private String url;
			@Properties(key="sayHello")
			private String hello;
			public PropTest(){
				
			}
		}
		try{
			PropTest pt = new PropTest();
			Container.injectProperties(pt);
			Assert.assertEquals("First Assert", "http://www.i2btech.com", pt.url);
			Assert.assertEquals("Second Assert", "Hello World!!", pt.hello);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFilePropertiesFile() {
		@Properties("src/test/resources/test.properties")
		class PropTest{
			@Properties(key="sayHello")
			private String hello;
			public PropTest(){
				
			}
		}
		try{
			PropTest pt = new PropTest();
			Container.injectProperties(pt);
			Assert.assertEquals("Second Assert", "Hello World!!", pt.hello);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
