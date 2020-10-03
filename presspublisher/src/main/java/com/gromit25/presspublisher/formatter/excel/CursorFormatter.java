package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * worksheet의 커서의 정보를 설정하는 Formatter
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="cursor")
public class CursorFormatter extends AbstractExcelFormatter {
	
	@Getter
	@Setter
	@FormatterAttr(name="position")
	private RowColumnEval positionExpEval;

	@Override
	protected void formatExcel(WorksheetFormatter copy, Charset charset, ValueContainer values)	throws FormatterException {
		
		try {
			
			// row/column 위치를 계산함
			int rowPosition = this.getPositionExpEval().evalRowValue(values);
			int columnPosition = this.getPositionExpEval().evalColumnValue(values);
			
			// workbook 커서 위치를 설정함
			copy.setCursorPosition(rowPosition, columnPosition);
			
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
