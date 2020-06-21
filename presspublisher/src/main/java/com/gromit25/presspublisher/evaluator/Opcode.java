package com.gromit25.presspublisher.evaluator;

import java.lang.reflect.Field;

/**
 * Evaluator에서 수행할 명령어(Opcode) 종류
 * 
 * @author jmsohn
 */
public enum Opcode {
	
	/** value container에서 값을 가져와 stack에 넣음 */
	LOAD_VALUE {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {
			
			if(cmd.isEmptyParams() == true) {
				throw new Exception("command param is not found");
			}
			
			// 
			Value value = values.get(cmd.getParam(0).toString(), Value.class);
			if(value == null) {
				throw new Exception("requested value(" + cmd.getParam(0) + ") is not found in value container");
			}
			
			//
			stack.push(value);
		}
	},
	/** stack에 있는 인스턴스의 attribute 값을 가져와서 stack에 넣음 */
	LOAD_ATTR {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {
			
			if(cmd.isEmptyParams() == true) {
				throw new Exception("command param is not found");
			}
			
			// Instance의 속성을 로드할 때와, Static Class의 속성을 로드할 경우를
			// 모두 처리함
			// Instance의 속성일 경우, 속성에서 값을 가져올때 instance를 넘겨야 하지만
			// Static Class의 속성일 경우, null을 넘겨주면됨
			Value value = stack.pop();
			Object instance = (value instanceof ValueInstance)?
					stack.popAsValueInstance().getValue():null;
			
			Field field = value.getType().getField(cmd.getParam(0).toString());
			stack.push(field.getType(), field.get(instance));
			
		}
	},
	/** stack에 parameter에 설정된 고정값을 push함 */
	LOAD_CONST {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {
			//
			if(cmd.isEmptyParams() == true) {
				throw new Exception("command param is not found");
			}
			
			//
			stack.push(cmd.getParam(0).getClass(), cmd.getParam(0));

		}
	},
	/** stack에 true값을 push함  */
	LOAD_BOOLEAN_TRUE {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {
			//
			stack.push(boolean.class, true);
		}
	},
	/** stack에 false값을 push함  */
	LOAD_BOOLEAN_FALSE {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {
			//
			stack.push(boolean.class, false);
		}
	},
	/** 스택에서 한개의 값을 인출하여 value container에 반입함 */
	ASSIGN_TO {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {
			//
			if(cmd.isEmptyParams() == true) {
				throw new Exception("command param is not found");
			}
			
			// 스택에서 값을 인출
			ValueInstance value = stack.popAsValueInstance();
			
			// value container에 추가
			values.put(cmd.getParam(0).toString(), value.getType(), value.getValue());
		}
	},
	/** 스택에서 두개의 값을 인출하여 덧셈 연산 수행 후 결과를 다시 스택에 반입함 */
	ADD {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {
			
			ValueInstance p2 = stack.popAsValueInstance();
			ValueInstance p1 = stack.popAsValueInstance();
			
			if(p1.getValue() instanceof String || p2.getValue() instanceof String) {
				
				stack.push(String.class, p1.getValue().toString() + p2.getValue().toString());
				
			} else {
				
				double p2Eval = EvalUtil.getNumber(p2);
				double p1Eval = EvalUtil.getNumber(p1);
				
				stack.push(double.class, p1Eval + p2Eval);
			}
		}
	},
	/** 스택에서 두개의 값을 인출하여 뺄셈 연산 수행 후 결과를 다시 스택에 반입함  */
	MINUS {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			double p2 = stack.popAsNumber();
			double p1 = stack.popAsNumber();
			
			stack.push(double.class, p1 - p2);
		}
	},
	/** 스택에서 두개의 값을 인출하여 곱셈 연산 수행 후 결과를 다시 스택에 반입함  */
	MUL {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			double p2 = stack.popAsNumber();
			double p1 = stack.popAsNumber();
			
			stack.push(double.class, p1 * p2);
		}
	},
	/** 스택에서 두개의 값을 인출하여 나눗셈 연산 수행 후 결과를 다시 스택에 반입함  */
	DIV {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			double p2 = stack.popAsNumber();
			double p1 = stack.popAsNumber();
			
			stack.push(double.class, p1 / p2);
		}
	},
	/** 스택에서 두개의 값을 인출하여 == 비교 연산 수행 후 결과를 다시 스택에 반입함  */
	CMP_EQ {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			ValueInstance p2 = stack.popAsValueInstance();
			ValueInstance p1 = stack.popAsValueInstance();
			
			if((p1.getValue().getClass() == Boolean.class || p1.getValue().getClass() == boolean.class)
				&& (p2.getValue().getClass() == Boolean.class || p2.getValue().getClass() == boolean.class)) {
				
				stack.push(boolean.class, (((boolean)p1.getValue()) == ((boolean)p2.getValue())));
				
			} else {
				Double p2Eval = EvalUtil.getNumber(p2);
				Double p1Eval = EvalUtil.getNumber(p1);
				
				// p1, p2가 객체로 인지 되기 때문에
				// == 연산을 직접 사용하면 p1, p2의 주소를 비교함
				// 따라서, equals 연산을 사용하여 값을 비교함
				stack.push(boolean.class, p1Eval.equals(p2Eval));
			}
		}
	},
	/** 스택에서 두개의 값을 인출하여 != 비교 연산 수행 후 결과를 다시 스택에 반입함  */
	CMP_NE {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			ValueInstance p2 = stack.popAsValueInstance();
			ValueInstance p1 = stack.popAsValueInstance();
			
			if((p1.getValue().getClass() == Boolean.class || p1.getValue().getClass() == boolean.class)
				&& (p2.getValue().getClass() == Boolean.class || p2.getValue().getClass() == boolean.class)) {
				
				stack.push(boolean.class, (((boolean)p1.getValue()) != ((boolean)p2.getValue())));
				
			} else {
				
				Double p2Eval = EvalUtil.getNumber(p2);
				Double p1Eval = EvalUtil.getNumber(p1);
				
				// p1, p2가 객체로 인지 되기 때문에
				// != 연산을 직접 사용하면 p1, p2의 주소를 비교함
				// 따라서, equals 연산을 사용하여 값을 비교후 !연산을 사용함
				stack.push(boolean.class, !p1Eval.equals(p2Eval));
			}
		}
	},
	/** 스택에서 두개의 값을 인출하여 > 비교 연산 수행 후 결과를 다시 스택에 반입함  */
	CMP_GT {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			double p2 = stack.popAsNumber();
			double p1 = stack.popAsNumber();
			
			stack.push(boolean.class, p1 > p2);
		}
	},
	/** 스택에서 두개의 값을 인출하여 >= 비교 연산 수행 후 결과를 다시 스택에 반입함  */
	CMP_GE {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			double p2 = stack.popAsNumber();
			double p1 = stack.popAsNumber();
			
			stack.push(boolean.class, p1 >= p2);
		}
	},
	/** 스택에서 두개의 값을 인출하여 < 비교 연산 수행 후 결과를 다시 스택에 반입함  */
	CMP_LT {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			double p2 = stack.popAsNumber();
			double p1 = stack.popAsNumber();
			
			stack.push(boolean.class, p1 < p2);
		}
	},
	/** 스택에서 두개의 값을 인출하여 <= 비교 연산 수행 후 결과를 다시 스택에 반입함  */
	CMP_LE {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			double p2 = stack.popAsNumber();
			double p1 = stack.popAsNumber();
			
			stack.push(boolean.class, p1 <= p2);
		}
	},
	/** 스택에서 두개의 값을 인출하여 AND 연산 수행 후 결과를 다시 스택에 반입함  */
	AND {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			boolean p2 = stack.popAsBoolean();
			boolean p1 = stack.popAsBoolean();
			
			stack.push(boolean.class, p1 && p2);
		}
	},
	/** 스택에서 두개의 값을 인출하여 OR 연산 수행 후 결과를 다시 스택에 반입함  */
	OR {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			boolean p2 = stack.popAsBoolean();
			boolean p1 = stack.popAsBoolean();
			
			stack.push(boolean.class, p1 || p2);
		}
	},
	/** 스택에서 한개의 값을 인출하여 NOT 연산 수행 후 결과를 다시 스택에 반입함  */
	NOT {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			boolean p1 = stack.popAsBoolean();
			
			stack.push(boolean.class, !p1);
		}
	},
	/** method 수행 */
	INVOKE_METHOD {
		@Override
		protected void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {

			if(cmd.isEmptyParams() == true) {
				throw new Exception("command param is not found");
			}
			
			// 메소드 호출시, 매개 변수를 가져옴
			// * 매개변수 개수에서 배열 수를 -1하는 이유는 
			//   가장 아래에 있는 인스턴스 Object는 제외하기 때문임
			Object[] methodParams = new Object[cmd.getParamPopCount() - 1];
			
			for(int index = cmd.getParamPopCount() - 2; index >= 0; index-- ) {
				Object param = stack.popAsValueInstance().getValue();
				methodParams[index] = param;
			}
			
			// 인스턴스 Object와 Class를 가져옴
			Value value = stack.pop();
			
			///////////////////////////////////////////////
			// 메소드 수행하고 결과를 다시 stack에 넣음
			// 만일, 결과의 타입이 void 일 경우에는 넣지 않음
			ValueInstance result = value.invoke(cmd.getParam(0).toString(), methodParams);
			
			if(result != null) {
				
				// 결과가 숫자형일 경우, Double로 데이터 형을 변경한 다음 stack에 넣음
				double resultNumber = EvalUtil.getNumber(result);
				if(Double.isNaN(resultNumber) == false) {
					result = new ValueInstance(double.class, resultNumber);
				}
				
				// stack에 넣음
				if(result.getType() != void.class) {
					stack.push(result);
				}
			}
		}
	};
	
	//------------------
	
	/**
	 * opcode 실행전 입력 파라미터 검사
	 * @param cmd 명령어 집합
	 * @param stack 스택
	 * @param values value container
	 * @throws Exception 입력값 검사 실패시 예외 발생
	 */
	protected void checkParams(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {
		// 입력값 검사
		if(cmd == null) {
			throw new Exception("command is null.");
		}
		
		if(stack == null) {
			throw new Exception("stack is null");
		}
		
		if(values == null) {
			throw new Exception("value container is null.");
		}

		//
		if(stack.size() < cmd.getParamPopCount()) {
			throw new Exception("stack size is less than pop count(" + cmd.getParamPopCount() + ")");
		}
	}
	
	/**
	 * 명령어 수행 - 외부(패키지 범위)에서 호출 가능
	 * - 명령어 수행시 공통 검사 부분 수행 후 명령어 실재 수행 호출
	 * @param cmd 명령어(Opcode)
	 * @param stack 스택
	 * @param values value container
	 */
	void execute(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception {
		this.checkParams(cmd, stack, values);
		this.executeOp(cmd, stack, values);
	}
	
	/**
	 * 명령어(Opcode) 수행 - 외부에서 호출 불가
	 * - 명령어 실제로 수행하는 부분
	 * @param cmd 명령어(Opcode)
	 * @param stack 스택
	 * @param values value container
	 */
	protected abstract void executeOp(EvalCmd cmd, ValueStack stack, ValueContainer values) throws Exception;

}
