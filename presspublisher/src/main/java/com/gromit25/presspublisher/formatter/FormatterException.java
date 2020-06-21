package com.gromit25.presspublisher.formatter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Formatter에서 발생한 예외
 * - 예외가 발생한 Formatter 객체 보유
 * - 예외가 발생한 attribute의 이름 보유
 * 
 * 용도)
 * 예외 발생시, Publisher Xml의 어느 부분인지 확인이 곤란하여 추가함
 * 
 * @author jmsohn
 */
public class FormatterException extends Exception {

	private static final long serialVersionUID = -1772099764515741751L;
	
	/** 예외가 발생한 Formatter 객체 */
	@Getter
	@Setter(value=AccessLevel.PRIVATE)
	private Formatter formatter;
	
	/** 예외가 발생한 attribute 명 */
	@Getter
	@Setter(value=AccessLevel.PRIVATE)
	private String attributeName;
	
	/**
	 * 생성자
	 * @param formatter 예외가 발생한 formatter 객체
	 * @param attributeName 예외가 발생한 attribute 명
	 * @param ex 발생 예외
	 */
	public FormatterException(Formatter formatter, String attributeName, Exception ex) {
		super(ex);
		this.setFormatter(formatter);
		this.setAttributeName(attributeName);
	}

	/**
	 * 생성자
	 * @param formatter 예외가 발생한 formatter 객체
	 * @param attributeName 예외가 발생한 attribute 명
	 * @param msg 발생 예외 메시지
	 */
	public FormatterException(Formatter formatter, String attributeName, String msg) {
		this(formatter, attributeName, new Exception(msg));
	}
	
	/**
	 * 생성자
	 * @param formatter 예외가 발생한 formatter 객체
	 * @param ex 발생 예외
	 */
	public FormatterException(Formatter formatter, Exception ex) {
		this(formatter, null, ex);
	}
	
	/**
	 * 생성자
	 * @param formatter 예외가 발생한 formatter 객체
	 * @param msg 발생 예외 메시지
	 */
	public FormatterException(Formatter formatter, String msg) {
		this(formatter, new Exception(msg));
	}
	
	/**
	 * 예외 발생 위치를 문자열로 변환하여 반환
	 * @return 예외 발생 위치 문자열
	 */
	public String getLocMessage() {
		
		StringBuffer locMessage = new StringBuffer(""); 
		
		locMessage.append("Publish Xml Tag:")
			.append(this.getFormatter().getTagName()).append("\n")
			.append("\tLine:")
			.append(this.getFormatter().getLineNumber())
			.append("\n")
			.append("\tColumn:")
			.append(this.getFormatter().getColumnNumber());
		
		// attribute 이름이 있는 경우에만 출력함
		if(this.getAttributeName() != null) {
			locMessage.append("\n\t")
				.append("Attribute :")
				.append(this.getAttributeName());
		}
		
		return locMessage.toString();
	}

}
