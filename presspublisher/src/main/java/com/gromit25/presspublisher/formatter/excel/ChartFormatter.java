package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="line-chart")
public class ChartFormatter extends AbstractExcelFormatter {
	
	@Getter
	@Setter
	@FormatterAttr(name="type", mandatory = true)
	private ChartTypes type;
	
	/**
	 * 
	 */
	@Getter
	@Setter
	@FormatterAttr(name="range", mandatory = true)
	private RangeEval range;
	
	@Getter
	@Setter
	@FormatterAttr(name="title", mandatory = false)
	private String title;
	
	/**
	 * 현재의 worksheet
	 * 하위 컴포넌트에서 사용됨
	 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private XSSFSheet worksheet;
	
	/**
	 * 생성할 chart 객체
	 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private XSSFChart chart;
	
	/**
	 * chart 객체에 표시할 chart data 객체
	 * 하위 컴포넌트에서 사용됨
	 */
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
		
		// 현재 worksheet 설정
		this.setWorksheet(copy.getWorksheet());
		
		// 1. chart 객체를 생성함
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
		
		XSSFDrawing drawing = copy.getWorksheet().createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(
				0, 0, 0, 0, startColumn, startRow, endColumn, endRow);

		this.setChart(drawing.createChart(anchor));
		
		// 2. 차트 컴포넌트를 설정함(axis, series 등) 
		this.execChildFormatters(this, charset, values);
		
		// 3. 설정된 차트를 엑셀에 출력함
		this.getChart().plot(this.getChartData());
	}
}
