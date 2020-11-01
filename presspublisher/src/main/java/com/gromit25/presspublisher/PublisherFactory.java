package com.gromit25.presspublisher;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.gromit25.presspublisher.formatter.FormatterXmlHandler;
import com.gromit25.presspublisher.formatter.XmlLocInputStream;

/**
 * 구체화 Publisher(concrete publisher) 생성 Factory 클래스
 * @author jmsohn
 */
public class PublisherFactory {
	
	/**
	 * 입력 스트림에서 구체화된 Publisher(concrete publisher) 생성
	 * @param type publisher의 종류
	 * @param formatXmlInput publisher를 구성하기 위한 format xml의 InputStream
	 * @return 생성된 Publisher
	 */
	public static Publisher create(PublisherType type, InputStream formatXmlInput) throws Exception {
		
		// 1. 입력값 검사
		if(type == null) {
			throw new Exception("publisher type is null.");
		}
		
		if(formatXmlInput == null) {
			throw new Exception("format xml input stream is null.");
		}
		
		// 2. publisher 종류에 따른 publisher 생성
		Publisher publisher = type.getConcreteType().newInstance();
		
		// 3. xml을 파싱하여, 생성된 publisher 구성함
		SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser formatXmlParser = factory.newSAXParser();
        FormatterXmlHandler formatXmlHandler = publisher.createXmlHandler();
        formatXmlHandler.setLocInputStream(new XmlLocInputStream(formatXmlInput));
        
        formatXmlParser.parse(formatXmlHandler.getLocInputStream(), formatXmlHandler);
        publisher.setRootFormatter(formatXmlHandler.getFormatter());
        
        // 4. publisher 반환
        return publisher;
	}
	
	/**
	 * XML 파일에서 구체화된 Publisher(concrete publisher) 생성
	 * @param type publisher의 종류
	 * @param formatFile publisher를 구성하기 위한 format xml file
	 * @return 생성된 Publisher
	 */
	public static Publisher create(PublisherType type, File formatFile) throws Exception {
		
		// 1. 입력값 검사
		if(formatFile == null) {
			throw new Exception("format file is null");
		}
		
		if(formatFile.canRead() == false) {
			throw new Exception("can't read format xml file: " + formatFile.getAbsolutePath());
		}

		// 2. publisher 생성 및 반환
        try(InputStream formatXmlInput = new FileInputStream(formatFile)) {
        	return PublisherFactory.create(type, formatXmlInput);
        }
	}
	
	/**
	 * XML 문자열에서 구체화된 Publisher(concrete publisher) 생성
	 * @param type type publisher의 종류
	 * @param formatString publisher를 구성하기 위한 format xml의 문자열
	 * @param cs format string의 character set(null 일경우, default character set 사용함)
	 * @return 생성된 Publisher
	 */
	public static Publisher create(PublisherType type, String formatString, Charset cs) throws Exception {

		// 1. 입력값 검사
		if(formatString == null) {
			throw new Exception("format string is null");
		}
		
		// character set이 설정되지 않았을 경우
		// 시스템의 default character set 사용
		if(cs == null) {
			cs = Charset.defaultCharset();
		}
		
		// 2. publisher 생성 및 반환
        try(InputStream formatXmlInput = new ByteArrayInputStream(formatString.getBytes(cs))) {
        	return PublisherFactory.create(type, formatXmlInput);
        }

	}
	
	/**
	 * XML 문자열에서 구체화된 Publisher(concrete publisher) 생성
	 * @param type type publisher의 종류
	 * @param formatString publisher를 구성하기 위한 format xml의 문자열(default character set으로 읽음)
	 * @return 생성된 Publisher
	 */
	public static Publisher create(PublisherType type, String formatString) throws Exception {
		return PublisherFactory.create(type, formatString, null);
	}

}
