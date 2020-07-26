package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gromit25.presspublisher.common.PublisherUtil;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;
import com.gromit25.presspublisher.formatter.flow.BasicFlowFormatter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * workbook formatter
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="workbook")
public class WorkbookFormatter extends BasicFlowFormatter {
	
	/** 작업 workbook */
	@Getter
	@Setter(value=AccessLevel.PRIVATE)
	private XSSFWorkbook workbook;
	
	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		// 작업 workbook 설정
		try {
			this.setWorkbook(PublisherUtil.cast(copyObj, XSSFWorkbook.class));
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
		// child formatter 호출 
		this.execChildFormatters(this, charset, values);
	}
}
