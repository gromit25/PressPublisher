package com.gromit25.presspublisher.formatter;

import java.lang.reflect.Method;

import com.gromit25.presspublisher.evaluator.Evaluator;

/**
 * 자바의 기본타입(primitive type) 또는 공통적으로 사용되는 타입(ex. Evaluator)에 대한
 * setter 메소드 정의
 * 
 * @author jmsohn
 */
@FormatterAttrSetterClass
public class CommonAttrSetter {
	
	/**
	 * String type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter(String.class)
	public static void setString(Formatter formatter, Method setMethod, String attrValue) throws FormatterException {
		try {
			setMethod.invoke(formatter, attrValue);
		} catch(Exception ex) {
			throw new FormatterException(formatter, ex);
		}
	}
	
	/**
	 * boolean type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter({Boolean.class, boolean.class})
	public static void setBoolean(Formatter formatter, Method setMethod, String attrValue) throws FormatterException {
		try {
			setMethod.invoke(formatter, Boolean.parseBoolean(attrValue));
		} catch(Exception ex) {
			throw new FormatterException(formatter, ex);
		}
	}
	
	/**
	 * integer type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter({Integer.class, int.class})
	public static void setInt(Formatter formatter, Method setMethod, String attrValue) throws FormatterException {
		try {
			setMethod.invoke(formatter, Integer.parseInt(attrValue));
		} catch(Exception ex) {
			throw new FormatterException(formatter, ex);
		}
	}
	
	/**
	 * short type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter({Short.class, short.class})
	public static void setShort(Formatter formatter, Method setMethod, String attrValue) throws FormatterException {
		try {
			setMethod.invoke(formatter, Short.parseShort(attrValue));
		} catch(Exception ex) {
			throw new FormatterException(formatter, ex);
		}
	}
	
	/**
	 * Evaluator type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter(Evaluator.class)
	public static void setEvaluator(Formatter formatter, Method setMethod, String attrValue) throws FormatterException {
		try {
			setMethod.invoke(formatter, Evaluator.compile(attrValue));
		} catch(Exception ex) {
			throw new FormatterException(formatter, ex);
		}
	}
}
