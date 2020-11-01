package com.gromit25.presspublisher.formatter.flow;

import org.xml.sax.Attributes;

import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

/**
 * 대체(alt) flow formatter
 * foreach 등 대체(alternative) flow가 있는 경우 사용되며,
 * alt 테그의  내용을 처리함
 * FlowFormatter와 기능 상 완전 동일함  
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="alt")
public class AltFlowFormatter extends AbstractFlowComponentFormatter {

	@Override
	public void setAttributes(Attributes attributes) throws FormatterException {
		//do nothing
	}
}
