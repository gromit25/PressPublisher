package com.gromit25.presspublisher.formatter.console;

import java.util.HashSet;
import java.util.Set;

import com.gromit25.presspublisher.formatter.FormatterXmlHandler;
import com.gromit25.presspublisher.formatter.flow.AbstractSubFlowFormatter;
import com.gromit25.presspublisher.formatter.text.TextFlowFormatter;

/**
 * console 출력을 위한 format 파일 파서
 * 
 * @author jmsohn
 */
public class ConsoleFormatterXmlHandler extends FormatterXmlHandler {
	
	private Set<String> formatterGroupNames;

	/**
	 * 생성자
	 */
	public ConsoleFormatterXmlHandler() throws Exception {
		super();
	}

	@Override
	protected Set<String> getFormatterGroupNames() {
		
		if(this.formatterGroupNames == null) {
			
			this.formatterGroupNames = new HashSet<String>();
			this.formatterGroupNames.add("flow");
			this.formatterGroupNames.add("text");
			this.formatterGroupNames.add("console");			
		}
		
		return this.formatterGroupNames;
	}

	@Override
	protected void setSubBasicFlow(AbstractSubFlowFormatter formatter) throws Exception {
		formatter.setBasicFlowFormatter(new TextFlowFormatter());
	}

}
