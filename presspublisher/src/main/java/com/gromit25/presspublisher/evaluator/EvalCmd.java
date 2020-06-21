package com.gromit25.presspublisher.evaluator;

import lombok.Getter;
import lombok.Setter;

/**
 * Evaluator에서 수행할 명령어 집합
 * 명령어(opcode)와 파라미터(param), 사용할 stack의 수 로 구성됨 
 * 
 * @author jmsohn
 */
public class EvalCmd {
	
	/** 명령어 */
	@Getter
	@Setter(value=lombok.AccessLevel.PACKAGE)
	private Opcode opcode;
	
	/** 파라미터 */
	@Getter
	@Setter(value=lombok.AccessLevel.PACKAGE)
	private Object[] params;

	/** 명령어 수행시, 사용할 stack의 수 */
	@Getter
	@Setter(value=lombok.AccessLevel.PACKAGE)
	private int paramPopCount;
	
	/**
	 * 생성자
	 * @param opcode 명령어
	 * @param paramPopCount 명령어 수행시, 사용할 stack의 수
	 * @param params 파라미터들
	 */
	public EvalCmd(Opcode opcode, int paramPopCount, Object... params){
		this.setOpcode(opcode);
		this.setParamPopCount(paramPopCount);
		this.setParams(params);
	}
	
	/**
	 * 설정된 명령어 수행
	 * @param stack stack
	 * @param values value container
	 */
	void execute(ValueStack stack, ValueContainer values) throws Exception {
		this.getOpcode().execute(this, stack, values);
	}
	
	/**
	 * 특정 인덱스의 파라미터를 가져옴
	 * @param index 특정 인덱스
	 * @return 파라미터
	 */
	public Object getParam(int index) throws Exception {
		return this.getParams()[index];
	}
	
	/**
	 * 파라미터가 비어 있는지 여부 확인
	 * @return 파라미터 비어 있는지 여부
	 */
	public boolean isEmptyParams() {
		if(this.getParams() == null) {
			return true;
		}
		
		if(this.getParams().length == 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 현재 명령어 객체의 Description 반환
	 */
	public String toString() {
		
		StringBuilder builder = new StringBuilder("");
		
		builder
			.append(opcode.name()).append("\t")
			.append(paramPopCount);
		
		for(Object param : this.getParams()) {
			builder.append("\t" + param.toString());
		}
		
		return builder.toString();
	}

}
