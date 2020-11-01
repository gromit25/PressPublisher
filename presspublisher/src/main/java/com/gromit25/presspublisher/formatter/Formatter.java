package com.gromit25.presspublisher.formatter;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.xml.sax.Attributes;

import com.gromit25.presspublisher.evaluator.ValueContainer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 추상화된 formatter 클래스
 * format 파일의 xml에 정의된 tag 들을 수행하기 위한 최상위 추상 객체
 * 
 * @author jmsohn
 */
public abstract class Formatter {

	/** tag 명 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private String tagName;
	
	/** tag의 시작 line number */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private int lineNumber;
	
	/** tag의 시작 column number */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private int columnNumber;
	
	/** 부모 Formatter */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Formatter parent;
	
	// xml에서 Formatter 생성시, callback되는 메소드 목록
	
	/**
	 * xml 테그의 attribute 읽기 수행시 callback
	 * -> FormatterAttr Annotation으로 처리가 불가능한 경우에 한하여 사용 
	 * @param attributes 테그의 attribute 값
	 */
	public void setAttributes(Attributes attributes) throws FormatterException {
		// do nothing
	}
	
	/**
	 * xml 테그의 text 읽기 수행시 callback 
	 * @param text 테그의 text
	 */
	public abstract void addText(String text) throws FormatterException;
	
	/**
	 * Formatter 내부에 새로운 자식 Formatter가 추가될 때 callback
	 * @param formatter 새로운 formatter
	 */
	public abstract void addChildFormatter(Formatter formatter) throws FormatterException;
	
	// formatting 수행 메소드
	
	/**
	 * formatter에 설정된 내용과 value container에 저장된 값을 이용하여
	 * 출력작업 수행
	 * @param out 출력 스트림
	 * @param charset 출력시 사용할 character set
	 * @param values value container
	 */
	public abstract void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException;
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	protected <T extends Formatter> T getParent(Class<T> type) throws FormatterException {
		return type.cast(this.getParent());
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	protected <T extends Formatter> T getParentInBranch(Class<T> type) throws FormatterException {
		
		Formatter parent = this.getParent();
		while(parent != null && false == type.isInstance(parent)) {
			parent = parent.getParent();
		}
		
		return type.cast(parent);
	}
}
