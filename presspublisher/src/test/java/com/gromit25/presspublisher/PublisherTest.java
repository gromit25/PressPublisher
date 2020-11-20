package com.gromit25.presspublisher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.evaluator.ValueInstance;
import com.gromit25.presspublisher.formatter.FormatterException;

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
    
    /**
     * 엑셀 파일 출력 테스트
     */
    public void testPublishToExcel() {
    	
    	File outFile = new File("D:\\testPublish.xlsx");
    	File formatFile = new File("resources/testExcelformat.xml");
    	
    	try (OutputStream outExcel = new FileOutputStream(outFile)){
    		
    		ArrayList<String> messages = new ArrayList<String>();
    		messages.add("test message 1");
    		messages.add("test message 2");
    		
    		ValueContainer values = new ValueContainer();
    		values.put("messages", messages);
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
    		publisher.publish(outExcel, Charset.defaultCharset(), values);
    		
    		assertTrue(true);
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    		
    	}
    }
    
    /**
     * 커서를 사용한 엑셀 파일 출력 테스트
     */
    public void testPublishToExcelByUsingCursor() {
    	
    	File outFile = new File("D:\\testPublishByUsingCursor.xlsx");
    	File formatFile = new File("resources/testExcelCursorformat.xml");
    	
    	try (OutputStream outExcel = new FileOutputStream(outFile)) {
    		
    		ArrayList<String> messages = new ArrayList<String>();
    		messages.add("test message 1");
    		messages.add("test message 2");
    		
    		ValueContainer values = new ValueContainer();
    		values.put("messages", messages);
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
    		publisher.publish(outExcel, Charset.defaultCharset(), values);
    		
    		assertTrue(true);
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    	}
    }
    
    /**
     * 엑셀 템플릿을 이용한 엑셀 파일 출력 테스트
     */
    public void testPublishToExcelTemplate() {
    	
    	File outFile = new File("D:\\testExcelTemplate.xlsx");
    	File formatFile = new File("resources/testExcelTemplate.xml");
    	
    	try (OutputStream outExcel = new FileOutputStream(outFile)) {
    		
    		ValueContainer values = new ValueContainer();
    		values.importStaticClass("Integer", Integer.class);
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
    		publisher.publish(outExcel, Charset.defaultCharset(), values);
    		
    		assertTrue(true);
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    	}
    }
    
    /**
     * 엑셀파일 암호화 테스트
     */
    public void testPublishToEncryptedExcel() {
    	
    	File outFile = new File("D:\\testPublishEncrypted.xlsx");
    	File formatFile = new File("resources/testExcelEncrypted.xml");
    	
    	try (OutputStream outExcel = new FileOutputStream(outFile)) {
    		
    		ArrayList<String> messages = new ArrayList<String>();
    		messages.add("test message 1");
    		messages.add("test message 2");
    		
    		ValueContainer values = new ValueContainer();
    		values.put("messages", messages);
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
    		publisher.publish(outExcel, Charset.defaultCharset(), values);
    		
    		assertTrue(true);
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    	}
    }
    
    /**
     * 엑셀 Line Chart 출력 테스트
     */
    public void testPublishLineChartToExcel() {
    	
    	File outFile = new File("D:\\testPublishLineChart.xlsx");
    	File formatFile = new File("resources/testExcelLineChart.xml");
    	
    	try (OutputStream outExcel = new FileOutputStream(outFile)) {
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
    		publisher.publish(outExcel, Charset.defaultCharset(), new ValueContainer());
    		
    		assertTrue(true);
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    	}
    }
    
    /**
     * 엑셀 Pie Chart 출력 테스트
     */
    public void testPublishPieChartToExcel() {
    	
    	File outFile = new File("D:\\testPublishPieChart.xlsx");
    	File formatFile = new File("resources/testExcelPieChart.xml");
    	
    	try (OutputStream outExcel = new FileOutputStream(outFile)) {
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
    		publisher.publish(outExcel, Charset.defaultCharset(), new ValueContainer());
    		
    		assertTrue(true);
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    	}
    }
    
    public void testFormatterException() {
    	
    	ValueContainer values = new ValueContainer();
    	
    	try {
    		
    		String formatStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    				"<format>\r\n" + 
    				"\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	|   한글:<print exp=\"%%\"/>\r\n" +
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	\r\n" + 
    				"</format>";
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.CONSOLE, formatStr);
    		publisher.publish(null, Charset.defaultCharset(), values);
    		
    		assertTrue(false);
    		
    	} catch(SAXException saxex) {
    		
    		saxex.printStackTrace();
    		assertTrue(saxex.getException() instanceof FormatterException);
    		
    		FormatterException fex = (FormatterException)saxex.getException();
    		assertTrue(5 == fex.getLineNumber());
    		assertTrue(8 == fex.getColumnNumber());
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    		
    	}
    	
    	try {
    		
    		String formatStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    				"<format>\r\n" + 
    				"\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	|   한글:<print \r\n" +
    				"            exp=\"%%\"/>\r\n" +
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	\r\n" + 
    				"</format>";
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.CONSOLE, formatStr);
    		publisher.publish(null, Charset.defaultCharset(), values);
    		
    		assertTrue(false);
    		
    	} catch(SAXException saxex) {
    		
    		saxex.printStackTrace();
    		assertTrue(saxex.getException() instanceof FormatterException);
    		
    		FormatterException fex = (FormatterException)saxex.getException();
    		assertTrue(5 == fex.getLineNumber());
    		assertTrue(8 == fex.getColumnNumber());
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    		
    	}
    	
    	try {
    		
    		String formatStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    				"<format>\r\n" + 
    				"\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	|   한글테스트:<print/>\r\n" +
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	\r\n" + 
    				"</format>";
    		
    		Publisher publisher = PublisherFactory.create(PublisherType.CONSOLE, formatStr);
    		publisher.publish(null, Charset.defaultCharset(), values);
    		
    		assertTrue(false);
    		
    	} catch(SAXException saxex) {
    		
    		saxex.printStackTrace();
    		assertTrue(saxex.getException() instanceof FormatterException);
    		
    		FormatterException fex = (FormatterException)saxex.getException();
    		assertTrue(5 == fex.getLineNumber());
    		assertTrue(11 == fex.getColumnNumber());
    		
    	} catch(Exception ex) {
    		
    		ex.printStackTrace();
    		assertTrue(false);
    		
    	}
    }
    
    /**
     * switch문 확인
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
    
    /**
     * for 문 확인
     */
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
    
    /**
     * Value Container에서 값을 가져올 경우(get),
     * 특정 클래스 type으로 반환하는지 확인 
     */
    public void testValueContainer() {
    	
    	try {
    		
    		// Value Container에 String 추가
	    	ValueContainer values = new ValueContainer();
	    	values.put("a", "TEST");
	    	
	    	// String 타입으로 반환 확인
	    	assertEquals(String.class, values.get("a", String.class).getClass());
	    	
	        // 어떤 객체라도 ValueInstance type으로 설정된 경우,
	        // ValueInstance 형식으로 반환되어야 한다.
	    	assertEquals(ValueInstance.class, values.get("a", ValueInstance.class).getClass());
	    	
		} catch(Exception ex) {
			
			ex.printStackTrace();
			assertTrue(false);
			
		}

    }
    
    /**
     * custom 테그 테스트
     */
    public void testCustom() {
    	
    	try {
    		
    		ValueContainer values = new ValueContainer();
    		
    		ArrayList<String> messages = new ArrayList<String>();
    		messages.add("test message 1");
    		messages.add("test message 2");
    		
    		values.put("messages", messages);
    		
    		String formatStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    				"<format>\r\n" + 
    				"\r\n" + 
    				"	|   CUSTOM TAG TEST\r\n" + 
    				"	|   --------------------------------------------------------------------------\r\n" + 
    				"	|   <custom class=\"com.gromit25.presspublisher.CustomShowMessages\"/>\r\n" + 
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
}