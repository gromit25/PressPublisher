package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.gromit25.presspublisher.evaluator.RowColumnEval;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="line-chart")
public class LineChartFormatter extends AbstractExcelFormatter {
	
	@Getter
	@Setter
	@FormatterAttr(name="type", mandatory = true)
	private ChartTypes type;
	
	/**
	 * 
	 */
	@Getter
	@FormatterAttr(name="range", mandatory = true)
	private String range;
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private RowColumnEval startCell;
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private RowColumnEval endCell;
	
	@Getter
	@Setter
	@FormatterAttr(name="title", mandatory = false)
	private String title;
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private XSSFSheet worksheet;
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private XSSFChart chart;
	
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private XDDFChartData chartData;
	
	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		if(formatter instanceof AbstractChartComponent) {
			super.addChildFormatter(formatter);
		} else {
			throw new FormatterException(formatter, "Invalid Formatter(AbstractChartComponent expected):" + formatter.getClass());
		}
	}

	@Override
	protected void formatExcel(WorksheetFormatter copy, Charset charset, ValueContainer values) throws FormatterException {
		
		//
		this.setWorksheet(copy.getWorksheet());
		
		// 1.
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
		
		XSSFDrawing drawing = copy.getWorksheet().createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(
				0, 0, 0, 0, startColumn, startRow, endColumn, endRow);

		this.setChart(drawing.createChart(anchor));
		
		// 2. 차트 컴포넌트를 설정함(axis, series 등) 
		this.execChildFormatters(this, charset, values);
		
		// 3.
	}
	
	/**
	 * 
	 * @param range
	 */
	public void setRange(String range) throws Exception {
		
		if(null == range || true == range.trim().equals("")) {
			throw new Exception("range value is null or blank");
		}
		
		String[] splitedRange = range.split("~");
		if(2 != splitedRange.length) {
			throw new Exception("range value is invalid:" + range);
		}
		
		this.setRange(range);
		this.setStartCell(RowColumnEval.compile(splitedRange[0], "0", "0"));
		this.setEndCell(RowColumnEval.compile(splitedRange[1], "0", "0"));
	}
	
}
