package com.xpec.properties.test;

import org.junit.Assert;
import org.junit.Test;

import com.xpec.properties.annotations.Properties;
import com.xpec.properties.container.Container;

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
			Assert.assertEquals("First Assert", "http://www.xpectrumtech.com", pt.url);
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
	
	@Test
	public void testAnnotatedField() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		class PropTest{
			@Properties(path="src/test/resources/test.properties", key="sayHello")
			private String hello;
			public PropTest(){
				
			}
		}
		PropTest pt = new PropTest();
		Container.injectProperties(pt);
		Assert.assertEquals("Second Assert", "Hello World!!", pt.hello);
	}

		
}
