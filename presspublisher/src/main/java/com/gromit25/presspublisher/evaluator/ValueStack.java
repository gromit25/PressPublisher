package com.gromit25.presspublisher.evaluator;

import java.util.Stack;

/**
 * 표현식 연산 및 결과 저장을 위한  Stack
 * @author jmsohn
 */
class ValueStack extends Stack<Value> {
	private static final long serialVersionUID = 8265278205182203510L;
	
	/**
	 * type 형식의 value 값을 stack에 넣음
	 * @param type value의 type(class)
	 * @param value stack에 넣을 값
	 */
	void push(Class<?> type, Object value) {
		super.push(new ValueInstance(type, value));
	}
	
	/**
	 * Stack의 최상단의 값을 ValueInstance 형식으로 반환(pop)
	 * @return Stack의 최상단의 ValueInstance
	 */
	ValueInstance popAsValueInstance() throws Exception {
		return EvalUtil.castValueInstance(super.pop());
	}
	
	/**
	 * Stack의 최상단의 값을 double 형식으로 반환(pop)
	 * @return Stack의 최상단의 값을 double 변환한 값
	 */
	double popAsNumber() throws Exception {
		return EvalUtil.getNumber(this.popAsValueInstance());
	}
	
	/**
	 * Stack의 최상단의 값을 boolean 형식으로 반환(pop)
	 * @return Stack의 최상단의 값을 boolean 변환한 값
	 */
	boolean popAsBoolean() throws Exception {
		return EvalUtil.getBoolean(this.popAsValueInstance());
	}
}
