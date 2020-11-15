package com.gromit25.presspublisher.formatter.excel.chart;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author jmsohn
 */
public abstract class AbstractSeriesFormatter extends AbstractChartComponent {
	
	/**
	 * 현재 worksheet 객체
	 * DataSourceFormatter에서 DataSource 생성시 사용함
	 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private XSSFSheet worksheet;
	
	/**
	 * category data source 객체
	 * 하위 컴포넌트인 DataSourceFormatter(tag: category-ds)에서 설정함
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private XDDFDataSource<?> categoryDS;
	
	/**
	 * value data source 객체
	 * 하위 컴포넌트인 DataSourceFormatter(tag: value-ds)에서 설정함
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private XDDFNumericalDataSource<?> valueDS;
	
	/**
	 * 
	 * @param copy
	 * @return
	 */
	protected abstract XDDFChartData createChartData(ChartFormatter copy) throws FormatterException;
	
	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		
		if(formatter instanceof DataSourceFormatter) {
			super.addChildFormatter(formatter);
		} else {
			throw new FormatterException(formatter, "Invalid Formatter(DataSourceFormatter expected).");
		}
	}
	
	@Override
	protected void execFormat(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		try {
			
			// 1. chart formatter를 casting 하고
			//    하위 컴포넌트에서 사용할 수 있도록 member변수(worksheet)를 설정함
			ChartFormatter copy = this.getParent(ChartFormatter.class);
			this.setWorksheet(copy.getWorksheet());
			
			// 2. 하위 컴포넌트를 실행하여,
			//    categoryDS, dataDS를 설정함
			this.execChildFormatters(out, charset, values);
			
			// 3. chart data 객체 생성
			XDDFChartData chartData = this.createChartData(copy);

			// 4. chart를 그림
			copy.getChart().plot(chartData);
			
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}
}
