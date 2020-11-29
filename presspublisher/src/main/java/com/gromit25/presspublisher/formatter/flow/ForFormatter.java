package com.gromit25.presspublisher.formatter.flow;

import java.io.OutputStream;
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
public class ForFormatter extends AbstractFlowComponentFormatter {
	
	/** for문 initExp속성의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="initExp", mandatory=true)
	private Evaluator initExp;

	/** for문 conditionExp속성의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="condExp", mandatory=true)
	private Evaluator conditionExp;
	
	/** for문 stepExp속성의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="stepExp", mandatory=true)
	private Evaluator stepExp;

	@Override
	protected void execFormat(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		// for문 수행
		try {
			
			for(
				// for 문 인덱스 초기화 수행
				this.getInitExp().eval(values, void.class);
				// for 문 조건문 수행, true 이면 for 문 수행
				this.getConditionExp().eval(values, boolean.class) == true;
				// for 문 인덱스 증가문 수행
				this.getStepExp().eval(values, void.class)
			) {
				
				// for 문 내부 플로우 반복 수행
				this.getBasicFlowFormatter().format(out, charset, values);
				
			}
		
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
