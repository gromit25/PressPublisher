package com.gromit25.presspublisher.formatter.flow;

import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * case formatter
 * switch 분기시, 분기된 case의 flow를 수행함
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="case")
public class CaseFormatter extends AbstractFlowComponentFormatter {
	
	/**
	 * case문의 매칭용 value
	 * switch문의 값과 일치 여부를 확인 및 메시지 출력, 정규표현식 사용 가능
	 */
	@Getter
	@Setter
	@FormatterAttr(name="value")
	private String value;

}
