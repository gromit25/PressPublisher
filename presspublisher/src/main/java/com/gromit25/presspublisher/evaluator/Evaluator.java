package com.gromit25.presspublisher.evaluator;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.parser.Parser;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * script에 대해 수행 가능한 명령어 목록으로 컴파일 수행
 * 및 컴파일된 명령어 목록을 수행하여 결과를 반환하는 Interpreter 클래스
 * 
 * @author jmsohn 
 */
public class Evaluator {
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private String script;
	
	/** script를 컴파일한 명령어 목록 */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ArrayList<EvalCmd> cmds = new ArrayList<EvalCmd>();
	
	/**
	 * 생성자 
	 * 외부에서 직접 new 연산자로 생성하지 못하도록 막는 용도
	 */
	private Evaluator() {
		// do nothing
	}
	
	/**
	 * script를 컴파일한 객체를 반환
	 * @param script 컴파일할 script
	 * @return 컴파일된 Evaluator
	 */
	public static Evaluator compile(String script) throws Exception {

		// 새로운 evaluator를 만듦
		Evaluator eval = new Evaluator();

		// script 컴파일 수행
		// 새로 생성된 evaluator 객체의 명령어(cmds) 목록에 추가하는 방식
		Reader reader = new StringReader(script);
		Parser.compile(reader, eval.getCmds());
		
		// 원본 script 설정함
		eval.setScript(script);
		
		return eval;
	}
	
	/**
	 * 컴파일된 명령어 목록을 수행하여 결과를 반환함
	 * @param values value container
	 * @param returnType 수행결과 Type
	 * @return 명령어 수행 결과
	 */
	public <T> T eval(ValueContainer values, Class<T> returnType) throws Exception {
		
		// 입력값 검사
		if(this.getCmds() == null) {
			throw new Exception("command list is null.");
		}
		
		if(values == null) {
			throw new Exception("value container is null.");
		}
		
		// 실행시 사용할 stack을 만듦
		ValueStack stack = new ValueStack();
		
		// 명령어를 하나씩 수행함
		for(EvalCmd cmd: this.getCmds()) {
			cmd.execute(stack, values);
		};
		
		// 실행 결과를 리턴함
		// stack에 하나만 남아 있지 않으면, null 리턴
		if(stack.size() == 1) { 
			
			Object result = EvalUtil.castValueInstance(stack.pop()).getValue();
			
			// returnType이 primitive Type(double, float, int, short,... 등등) 이면, 
			// primitive Type으로 casting
			//
			// object가 객체이면, casting 하여 반환함
			// 이외의 경우는 오류
			if(EvalUtil.isPrimitiveType(result, returnType) == true) {
					
				@SuppressWarnings("unchecked")
				T primitiveValue = (T)result;
				return primitiveValue;

			} else if(returnType.isInstance(result) == true) {
				
				return returnType.cast(result);
				
			} else {
				throw new Exception("obj(" + result.getClass().getName() + ") is not " + returnType.toString() + " type");
			}
			
		} else {
			return null;
		}
	}
}
