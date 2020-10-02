package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;

import com.gromit25.presspublisher.evaluator.RowColumnEval;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="category-ds, value-ds")
public class DataSourceFormatter extends AbstractChartComponent {
	
	@Getter
	@FormatterAttr(name="range", mandatory=true)
	private String range;
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private RowColumnEval startCell;
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private RowColumnEval endCell;
	
	@Getter
	@Setter
	@FormatterAttr(name="type", mandatory=true)
	private DataSourceTypes type;

	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		//
		if(false == (copyObj instanceof SeriesFormatter)) {
			throw new FormatterException(this, "output object is not SeriesFormatter.");
		}
		
		//
		SeriesFormatter copy = (SeriesFormatter)copyObj;
		
		//
		int startRow = 0;
		int startColumn = 0;
		int endRow = 0;
		int endColumn = 0;
		
		try {
			startRow = this.getStartCell().evalRowValue(values);
			startColumn = this.getStartCell().evalColumnValue(values);
			endRow = this.getEndCell().evalRowValue(values);
			endColumn = this.getEndCell().evalColumnValue(values);
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
			throw new FormatterException(this, "unknown tag name:" + this.getTagName());
		}

	}
	
	/**
	 * 
	 * @param range
	 */
	public void setRange(String range) throws Exception {
		
		if(null == range || true == range.trim().equals("")) {
			throw new Exception("range value is null or blank.");
		}
		
		// 범위의 형식을 시작셀 위치와 종료셀 위치를 분리함
		// ex) 0:0~2:3 -> 시작셀 위치 0:0, 종료셀 위치 2:3
		String[] splitedRange = range.split("~");
		if(2 != splitedRange.length) {
			throw new Exception("range value is invalid:" + range);
		}
		
		this.setRange(range);
		this.setStartCell(RowColumnEval.compile(splitedRange[0], "0", "0"));
		this.setEndCell(RowColumnEval.compile(splitedRange[1], "0", "0"));
	}

}
