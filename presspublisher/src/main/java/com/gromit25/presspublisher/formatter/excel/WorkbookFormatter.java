package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
	
	/**
	 * 현재 workbook
	 * 하위 formatter에서 사용함
	 */
	@Getter
	@Setter(value=AccessLevel.PRIVATE)
	private XSSFWorkbook workbook;
	
	@Override
	public void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		// 작업 workbook 설정
		try {
			this.setWorkbook(new XSSFWorkbook());
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
		// child formatter 호출 
		this.execChildFormatters(out, charset, values);
	}
}
