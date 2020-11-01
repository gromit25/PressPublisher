package com.gromit25.presspublisher;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterXmlHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Publisher 추상 클래스
 * @author jmsohn
 */
public abstract class Publisher {
	
	/** root formatter */
	@Getter
	@Setter(value=AccessLevel.PACKAGE)
	private Formatter rootFormatter;
	
	/**
	 * format 파일을 파싱하기 위한
	 * xml 파서 생성 
	 * @return xml 파서
	 */
	protected abstract FormatterXmlHandler createXmlHandler() throws Exception;
	
	/**
	 * output stream으로 publish 수행 
	 * @param out 출력 스트림
	 * @param charset charater set
	 * @param values value container
	 */
	public abstract void publish(OutputStream out, Charset charset, ValueContainer values) throws Exception;
	
}
