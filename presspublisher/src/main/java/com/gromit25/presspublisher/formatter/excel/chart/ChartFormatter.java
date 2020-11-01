package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.xddf.usermodel.chart.XDDFChartAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
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

@FormatterSpec(group="excel", tag="chart")
public class ChartFormatter extends AbstractExcelFormatter {
	
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
	 * 생성할 chart 객체
	 * 하위 컴포넌트에서 사용됨
	 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private XSSFChart chart;
	
	/**
	 * category axis 객체
	 * 하위 컴포넌트인 AxesFormatter의 AxisFormatter(tag: category-axis) 객체에서 생성하여 설정함
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private XDDFChartAxis categoryAxis;
	
	/**
	 * value axis 객체
	 * 하위 컴포넌트인 AxesFormatter의 AxisFormatter(tag: value-axis) 객체에서 생성하여 설정함
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private XDDFValueAxis valueAxis;
	
	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		if(formatter instanceof AbstractChartComponent) {
			super.addChildFormatter(formatter);
		} else {
			throw new FormatterException(formatter, "Invalid Formatter(AbstractChartComponent expected):" + formatter.getClass());
		}
	}

	@Override
	protected void formatExcel(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		// 1. chart 객체를 생성함
		
		// 차트 객체의 위치를 설정함
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
		
		XSSFDrawing drawing = this.getWorksheet().createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(
				0, 0, 0, 0, startColumn, startRow, endColumn, endRow);

		// 하위 컴포넌트에서 사용하기 위해, 
		// chart 객체 생성 후 member변수(chart)에 설정함
		this.setChart(drawing.createChart(anchor));
		
		// 2. 차트 컴포넌트를 설정함(axis, series 등)
		//    실제 차트를 그리는 것은 하위 컴포넌트의 SeriesFormatter 들임
		this.execChildFormatters(out, charset, values);
	}
	
	/**
	 * 
	 * @return
	 */
	public XSSFSheet getWorksheet() throws FormatterException {
		return this.getParent(WorksheetFormatter.class).getWorksheet();
	}
}
