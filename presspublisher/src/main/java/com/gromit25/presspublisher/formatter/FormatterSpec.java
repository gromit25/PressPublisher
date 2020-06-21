package com.gromit25.presspublisher.formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Formatter 스펙(Spec)을 지정하는 Annotation
 * 
 * @author jmsohn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FormatterSpec {
	/** Formatter가 속한 Group 명 */
	public String group();
	/** 처리할 Tag 명 */
	public String tag();
}
