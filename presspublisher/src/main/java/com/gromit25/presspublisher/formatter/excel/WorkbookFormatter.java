package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;
import com.gromit25.presspublisher.formatter.flow.BasicFlowFormatter;

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
	@Setter
	@FormatterAttr(name="template", mandatory=false)
	private XSSFWorkbook workbook;
	
	@Override
	protected void execFormat(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		// 작업 workbook 설정
		try {
			if(null == this.getWorkbook()) {
				this.setWorkbook(new XSSFWorkbook());
			}
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
		// child formatter 호출 
		this.execChildFormatters(out, charset, values);
	}
}
