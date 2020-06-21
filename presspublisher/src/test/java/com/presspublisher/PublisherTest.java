package com.presspublisher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.gromit25.presspublisher.Publisher;
import com.gromit25.presspublisher.PublisherFactory;
import com.gromit25.presspublisher.PublisherType;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.evaluator.ValueInstance;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Publisher 단위 테스트 (Unit test)
 */
public class PublisherTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PublisherTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(PublisherTest.class);
    }

    /**
     * Publisher 생성 테스트
     */
    public void testCreatePublisher()
    {
    	try {
    		
    		ArrayList<String> messages = new ArrayList<String>();
    		messages.add("test message 1");
    		messages.add("test message 2");
    		
    		ValueContainer values = new ValueContainer();
    		values.put("messages", messages);
    		
    		////
    		
    		Publisher xmlpublisher = PublisherFactory.create(PublisherType.CONSOLE, new File("resources/testformat.xml"));
    		xmlpublisher.publish(null, Charset.defaultCharset(), values);
    		
    		assertTrue(true);
    		
    		////
    		
    		String formatStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    				"<format>\r\n" + 
    				"\r\n" + 
    				"	|   TEST MESSAGE - STRING FORMAT\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	<foreach element=\"message\" listExp=\"messages\">\r\n" + 
    				"	|      <style type=\"FG_WHITE_BRIGHT\"><print exp=\"message\"/></style>\r\n" + 
    				"	</foreach>\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	\r\n" + 
    				"</format>";
    		
    		Publisher stringpublisher = PublisherFactory.create(PublisherType.CONSOLE, formatStr);
    		stringpublisher.publish(null, Charset.defaultCharset(), values);
    		
    		assertTrue(true);
    		
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		assertTrue(false);
    	}
        
    }
    
    
    public void testPublishToExcel() {
    	
    	try (OutputStream outExcel = new FileOutputStream(new File("D:\\testPublisher.xlsx"))){
    		
    		ArrayList<String> messages = new ArrayList<String>();
    		messages.add("test message 1");
    		messages.add("test message 2");
    		
    		ValueContainer values = new ValueContainer();
    		values.put("messages", messages);
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, new File("resources/testExcelformat.xml"));
    		publisher.publish(outExcel, Charset.defaultCharset(), values);
    		
    		assertTrue(true);
    		
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		assertTrue(false);
    	}
    }
    
    /**
     * 
     */
    public void testSwitch() {
    	
    	try {
    		
    		ValueContainer values = new ValueContainer();
    		values.put("flag", "a");
    	
			String formatStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
					"<format>\r\n" + 
					"\r\n" + 
					"	|   SWITCH TEST\r\n" + 
					"	|   --------------------------------------------------------------------------\r\n" + 
					"	<switch exp=\"flag\">\r\n" + 
					"	<case value=\"a\">|      Type A</case>\r\n" + 
					"	<case value=\"b\">|      Type B</case>\r\n" +
					"	<case value=\"c\">|      Type C</case>\r\n" +
					"	<default>|      Type default</default>\r\n" +
					"	</switch>\r\n" + 
					"	|   --------------------------------------------------------------------------\r\n" + 
					"	\r\n" + 
					"</format>";
			
			/////
			// Type A 출력
			Publisher switch_1 = PublisherFactory.create(PublisherType.CONSOLE, formatStr);
			switch_1.publish(null, Charset.defaultCharset(), values);
			
			assertTrue(true);

			/////
			// Type C 출력
			values.put("flag", "c");
			switch_1.publish(null, Charset.defaultCharset(), values);
			
			assertTrue(true);

			/////
			// Type default 출력
			values.put("flag", "g");
			switch_1.publish(null, Charset.defaultCharset(), values);
			
			assertTrue(true);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			assertTrue(false);
			
		}
    }

    
    /**
     * if 분기문 확인
     */
    public void testIf() {
    	
    	try {

    		ValueContainer values = new ValueContainer();
    		
    		ArrayList<String> messages = new ArrayList<String>();
    		messages.add("test message 1");
    		messages.add("test message 2");
    		
    		values.put("messages", messages);
    		values.put("type", "a");
    		
    		String formatStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    				"<format>\r\n" + 
    				"\r\n" + 
    				"	|   IF TEST\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	<if exp=\"type.equals('a')\">\r\n" + 
    				"	|      <style type=\"FG_WHITE_BRIGHT\">type a</style>\r\n" + 
    				"	</if>\r\n" +
    				"	<if exp=\"type.equals('b')\">\r\n" + 
    				"	|      <style type=\"FG_WHITE_BRIGHT\">type b</style>\r\n" + 
    				"	</if>\r\n" +
    				"	<if exp=\"type.equals('c') == false\">\r\n" + 
    				"	|      <style type=\"FG_WHITE_BRIGHT\">type c</style>\r\n" + 
    				"	</if>\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	\r\n" + 
    				"</format>";
    		
    		Publisher stringpublisher = PublisherFactory.create(PublisherType.CONSOLE, formatStr);
    		stringpublisher.publish(null, Charset.defaultCharset(), values);
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    		
    	}
    	
    }
    
    public void testFor() {
    	
    	try {

    		ValueContainer values = new ValueContainer();
    		
    		ArrayList<String> messages = new ArrayList<String>();
    		messages.add("test message 1");
    		messages.add("test message 2");
    		
    		values.put("messages", messages);
    		
    		String formatStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    				"<format>\r\n" + 
    				"\r\n" + 
    				"	|   FOR LOOP TEST\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	<for initExp=\"index = 0\" condExp=\"index &lt; messages.size()\" stepExp=\"index = index+1\">\r\n" + 
    				"	|      <style type=\"FG_WHITE_BRIGHT\"><print exp=\"messages.get(index)\"/></style>\r\n" +
    				"	</for>\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	\r\n" + 
    				"</format>";
    		
    		Publisher stringpublisher = PublisherFactory.create(PublisherType.CONSOLE, formatStr);
    		stringpublisher.publish(null, Charset.defaultCharset(), values);
    		
    		assertEquals(values.get("index", double.class), (double)messages.size());
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    		
    	}
    }
    
    public void testValueContainer() {
    	try {
    		
	    	ValueContainer values = new ValueContainer();
	    	values.put("a", "TEST");
	    	assertEquals(String.class, values.get("a", String.class).getClass());
	    	assertEquals(ValueInstance.class, values.get("a", ValueInstance.class).getClass());
	    	
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);
		}

    }
}
