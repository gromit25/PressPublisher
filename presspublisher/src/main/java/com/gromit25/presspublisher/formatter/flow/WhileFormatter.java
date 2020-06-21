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
 * while formatter
 * while formatter는 판별식(exp)의 참이면, List를 반복수행 함
 * 하나의 FlowFormatter를 여러번 실행시키는 개념임
 * 
 * exp 속성 : 판별식
 *
 * ex)
 * <while exp="expression">...</while>
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="while")
public class WhileFormatter extends AbstractSubFlowFormatter {
	
	/** while문의 exp속성(script) */
	@Getter
	@Setter
	@FormatterAttr(name="exp")
	private String whileExp;
	
	/** while문 수행 여부를 확인 하기 위한 스크립트 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="exp")
	private Evaluator whileExpEval;
	
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
			
			// 설정된 조건이 TRUE 이면,
			// while문을 계속 수행함
			while(this.getWhileExpEval().eval(values, Boolean.class) == true) {
				this.getBasicFlowFormatter().format(copyObj, charset, values);
			}
			
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}
}
