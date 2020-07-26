package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

/**
 * 내부의 cell들의 커서이동을 오른쪽으로 이동하는 formatter 
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="rowcells")
public class RowCellsFormatter extends AbstractExcelFormatter {

	@Override
	protected void formatExcel(WorksheetFormatter copy, Charset charset, ValueContainer values) throws FormatterException {
	}

}
