package com.gromit25.presspublisher.formatter.flow;

import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.Evaluator;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * if formatter
 * if formatter는 판별식(exp)의 참/거짓을 판별하여, 
 * 참일 경우에만 if tag 내부의 명령어를 수행함
 * 
 * exp 속성 : 판별식
 *
 * ex)
 * <if exp="expression"></if>
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="if")
public class IfFormatter extends AbstractSubFlowFormatter {
	
	/** if문의 exp속성(script) */
	@Getter
	@Setter
	@FormatterAttr(name="exp")
	private String ifExp;
	
	/** if문 수행 여부를 확인 하기 위한 스크립트 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="exp")
	private Evaluator ifEval;

	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		// 입력값 검증
		if(copyObj == null) {
			throw new FormatterException(this, "Copy Object is null");
		}

		if(values == null) {
			throw new FormatterException(this, "Value Container is null");
		}
		
		try {
			
			// if 문 설정된 script의 수행결과가
			// TRUE 이면, basic flow를 수행함
			Boolean condition = this.getIfEval().eval(values, Boolean.class);
			
			if(condition == true) {
				this.getBasicFlowFormatter().format(copyObj, charset, values);
			}

		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
