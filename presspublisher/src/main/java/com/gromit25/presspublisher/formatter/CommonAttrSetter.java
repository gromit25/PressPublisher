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
	 * 
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter(Class.class)
	public static void setClass(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, Class.forName(attrValue));
	}
	
	/**
	 * String type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter(String.class)
	public static void setString(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, attrValue);
	}
	
	/**
	 * boolean type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter({Boolean.class, boolean.class})
	public static void setBoolean(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, Boolean.parseBoolean(attrValue));
	}
	
	/**
	 * integer type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter({Integer.class, int.class})
	public static void setInt(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, Integer.parseInt(attrValue));
	}
	
	/**
	 * short type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter({Short.class, short.class})
	public static void setShort(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, Short.parseShort(attrValue));
	}
	
	/**
	 * Evaluator type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter(Evaluator.class)
	public static void setEvaluator(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, Evaluator.compile(attrValue));
	}
	
}
