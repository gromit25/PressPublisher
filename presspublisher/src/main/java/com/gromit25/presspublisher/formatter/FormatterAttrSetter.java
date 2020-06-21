package com.gromit25.presspublisher.formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Formatter의 속성(Attr)을 설정하기 위한 Setter 메소드 지정시 사용
 * 
 * setter 메소드의 형식
 *     public static void 메소드명(Formatter formatter, Method setMethod, String attrValue)
 *     formatter : 속성을 지정할 formatter 객체
 *     setMethod : formatter의 속성값 setMethod
 *     attrValue : formatter에 설정할 속성의 문자열값
 *     
 *     Setter 메소드는 formatter에 설정할 속성의 문자열값(attrValue)를
 *     formatter의 필드의 형식에 변환하여 setMethod를 통해 지정함
 * 
 * @author jmsohn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FormatterAttrSetter {
	
	/**
	 * setter 메소드가 지원하는 필드의 type(class) 목록 반환
	 * @return setter 메소드가 지원하는 필드의 type(class) 목록
	 */
	public Class<?>[] value();
	
}
