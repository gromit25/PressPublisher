package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="category-ds, value-ds")
public class DataSourceFormatter extends AbstractChartComponent {
	
	@Getter
	@Setter
	@FormatterAttr(name="range", mandatory=true)
	private RangeEval range;
	
	@Getter
	@Setter
	@FormatterAttr(name="type", mandatory=true)
	private DataSourceTypes type;

	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		//
		if(false == (copyObj instanceof AbstractSeriesFormatter)) {
			throw new FormatterException(this, "output object is not AbstractSeriesFormatter.");
		}
		
		//
		AbstractSeriesFormatter copy = (AbstractSeriesFormatter)copyObj;
		
		//
		int startRow = 0;
		int startColumn = 0;
		int endRow = 0;
		int endColumn = 0;
		
		try {
			startRow = this.getRange().evalStartRow(values);
			startColumn = this.getRange().evalStartColumn(values);
			endRow = this.getRange().evalEndRow(values);
			endColumn = this.getRange().evalEndColumn(values);
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
		CellRangeAddress rangeAddr = new CellRangeAddress(startRow, endRow, startColumn, endColumn);
		XDDFDataSource<?> ds = this.getType().createDataSource(copy.getWorksheet(), rangeAddr);

		//
		if(true == this.getTagName().equals("category-ds")) {
			copy.setCategoryDS(ds);
		} else if(true == this.getTagName().equals("value-ds")) {
			if(ds instanceof XDDFNumericalDataSource<?>) {
				copy.setValueDS((XDDFNumericalDataSource<?>)ds);
			} else {
				throw new FormatterException(this, "value-ds is not NUMBER type.");
			}
		} else {
			throw new FormatterException(this, "unexpected tag name:" + this.getTagName());
		}

	}
}
