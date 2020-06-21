package com.gromit25.presspublisher.formatter.text;

import java.util.HashSet;
import java.util.Set;

import com.gromit25.presspublisher.formatter.FormatterXmlHandler;
import com.gromit25.presspublisher.formatter.flow.AbstractSubFlowFormatter;

/**
 * 텍스트 출력을 위한 format 파일 파서
 * 
 * @author jmsohn
 */
public class TextFormatterXmlHandler extends FormatterXmlHandler {

	private Set<String> formatterGroupNames;

	/**
	 * 생성자
	 */
	public TextFormatterXmlHandler() throws Exception {
		super();
	}

	@Override
	protected Set<String> getFormatterGroupNames() {
		
		if(this.formatterGroupNames == null) {
			
			this.formatterGroupNames = new HashSet<String>();
			this.formatterGroupNames.add("flow");
			this.formatterGroupNames.add("text");
		}
		
		return this.formatterGroupNames;
	}

	@Override
	protected void setSubBasicFlow(AbstractSubFlowFormatter formatter) throws Exception {
		formatter.setBasicFlowFormatter(new TextFlowFormatter());
	}

}
