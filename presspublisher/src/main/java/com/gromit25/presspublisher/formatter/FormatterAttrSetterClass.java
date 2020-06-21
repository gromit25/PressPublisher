package com.gromit25.presspublisher.formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FormatterAttrSetter 메소드를 가진 type(class)을 지정시 사용
 * -> FormatterAttrSetter를 찾을때, 모든 클래스의 메소드를 검사하는 것 보다
 *    FormatterAttrSetterClass 어노테이션이 지정된 클래스의 메소드만 찾는 것이 빠르기 때문에 사용 
 * 
 * @author jmsohn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FormatterAttrSetterClass {
}
