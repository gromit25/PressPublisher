package com.gromit25.presspublisher.evaluator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * static 클래스 객체
 * @author jmsohn
 */
public class ValueStaticClass extends Value {

	/**
	 * 생성자 
	 * @param type static 클래스의 type(class)
	 */
	public ValueStaticClass(Class<?> type) throws Exception {
		super(type);
	}

	@Override
	ValueInstance invoke(String methodName, Object[] params) throws Exception {
		
		// 인스턴스 Object의 Class에서
		// 수행할 method를 가져옴
		Method method = EvalUtil.getMethod(this.getType(), methodName, params);
		
		if(method == null) {
			throw new Exception(methodName + " is not found in " + this.getType());
		}
		
		// static method 여부를 검사
		//
		if(Modifier.isStatic(method.getModifiers()) == false) {
			throw new Exception(methodName + " is not static method");
		}

		// 메소드 수행하고 결과를 리턴함
		ValueInstance result = new ValueInstance(
				method.getReturnType()
				, method.invoke(null, params));

		return result;
	}

}
