package com.gromit25.presspublisher.evaluator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Value Container에 저장되는 Value 추상 클래스
 * @author jmsohn
 */
public abstract class Value {
	
	/** value의 type(class) */
	@Getter(value=AccessLevel.PACKAGE)
	@Setter(value=AccessLevel.PACKAGE)
	private Class<?> type;

	/**
	 * value의 메소드 호출 결과
	 * ex) a.add(1,2), Integer.parseInt("12") 등
	 * 
	 * @param methodName 호출할 메소드 명
	 * @param params 메소드의 파라미터들
	 * @return 호출 수행 결과
	 */
	abstract ValueInstance invoke(String methodName, Object[] params) throws Exception;
	
	/**
	 * 생성자
	 * @param type value의 type(class)
	 */
	protected Value(Class<?> type) {
		if(type == null) {
			type = Object.class;
		} else {
			this.setType(type);
		}
	}
}
