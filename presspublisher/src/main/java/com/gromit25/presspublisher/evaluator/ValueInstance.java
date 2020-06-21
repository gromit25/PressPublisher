package com.gromit25.presspublisher.evaluator;

import java.lang.reflect.Method;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * instance value 객체
 * @author jmsohn
 */
public class ValueInstance extends Value {

	/** value의 instance 객체 */
	@Getter(value=AccessLevel.PACKAGE)
	@Setter(value=AccessLevel.PACKAGE)
	private Object value;

	/**
	 * 생성자
	 * @param type value의 type(class)
	 * @param value value의 instance 객체
	 */
	public ValueInstance(Class<?> type, Object value) {
		super(type);
		this.setValue(value);
	}

	@Override
	ValueInstance invoke(String methodName, Object[] params) throws Exception {
		
		// 인스턴스 Object의 Class에서
		// 수행할 method를 가져옴
		Method method = EvalUtil.getMethod(this.getType(), methodName, params);
		
		if(method == null) {
			throw new Exception(methodName + " is not found in " + this.getType());
		}

		// 메소드 수행하고 결과를 리턴함
		ValueInstance result = new ValueInstance(
				method.getReturnType()
				, method.invoke(this.getValue(), params));

		return result;
	}
}
