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
 * 개발 중
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="for")
public class ForFormatter extends AbstractSubFlowFormatter {
	
	/** for문의 initExp속성(script) */
	@Getter
	@Setter
	@FormatterAttr(name="initExp")
	private String initExp;
	
	/** for문 initExp속성의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="initExp")
	private Evaluator initExpEval;

	/** for문의 conditionExp속성(script) */
	@Getter
	@Setter
	@FormatterAttr(name="condExp")
	private String conditionExp;

	/** for문 conditionExp속성의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="condExp")
	private Evaluator conditionExpEval;
	
	/** for문 stepExp속성(script) */
	@Getter
	@Setter
	@FormatterAttr(name="stepExp")
	private String stepExp;

	/** for문 stepExp속성의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="stepExp")
	private Evaluator stepExpEval;

	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		// 입력값 검증
		if(copyObj == null) {
			throw new FormatterException(this, "Copy Object is null");
		}

		if(values == null) {
			throw new FormatterException(this, "Value Container is null");
		}
		
		// for문 수행
		try {
			
			for(
				// for 문 인덱스 초기화 수행
				this.getInitExpEval().eval(values, void.class);
				// for 문 조건문 수행, true 이면 for 문 수행
				this.getConditionExpEval().eval(values, boolean.class) == true;
				// for 문 인덱스 증가문 수행
				this.getStepExpEval().eval(values, void.class)) {
				
				// for 문 내부 플로우 반복 수행
				this.getBasicFlowFormatter().format(copyObj, charset, values);
				
			}
		
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
