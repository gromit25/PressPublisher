package com.gromit25.presspublisher.formatter.excel;

import java.util.HashSet;
import java.util.Set;

import com.gromit25.presspublisher.formatter.FormatterXmlHandler;
import com.gromit25.presspublisher.formatter.flow.AbstractSubFlowFormatter;
import com.gromit25.presspublisher.formatter.flow.BasicFlowFormatter;
import com.gromit25.presspublisher.formatter.text.TextFlowFormatter;

/**
 * Excel Format XML 파서
 * 
 * @author jmsohn
 */
public class ExcelFormatterXmlHandler extends FormatterXmlHandler {
	
	/** 엑셀 출력에서 사용할 formatter의 group 목록들 */ 
	private Set<String> formatterGroupNames;

	/**
	 * 생성자
	 */
	public ExcelFormatterXmlHandler() throws Exception {
		super();
	}

	@Override
	protected Set<String> getFormatterGroupNames() {
		
		if(this.formatterGroupNames == null) {
			
			// 엑세에 출력할때 사용할 formatter group들을 추가함
			this.formatterGroupNames = new HashSet<String>();
			this.formatterGroupNames.add("flow");
			this.formatterGroupNames.add("text");
			this.formatterGroupNames.add("excel");			
		}
		
		return this.formatterGroupNames;
	}

	@Override
	protected void setSubBasicFlow(AbstractSubFlowFormatter formatter) throws Exception {
		
		// hasCellFormatterInParents() method 설명 참조
		if(this.hasCellFormatterInParents() == true) {
			formatter.setBasicFlowFormatter(new TextFlowFormatter());
		} else {
			formatter.setBasicFlowFormatter(new BasicFlowFormatter());
		}
	}
	
	/**
	 * 현재 formatter의 상위 formatter 중에 cell formatter가 있는지 여부 반환
	 * flow formatter들 처리시,
	 * 상위에 cell formatter가 있으면, cell에 설정할 text관련 처리를 수행
	 * 없으면, excel테그 관련 처리를 하기 위함
	 * @return 상위 formatter 중에 cell formatter가 있는지 여부
	 */
	private boolean hasCellFormatterInParents() {
		
		for(int index = this.getParentFormatterStack().size() - 1; index >= 0; index--) {
			if(this.getParentFormatterStack().get(index) instanceof CellFormatter) {
				return true;
			}
		}
		
		return false;
	}

}
