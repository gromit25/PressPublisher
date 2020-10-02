package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="series")
public class SeriesFormatter extends AbstractChartComponent {
	
	@Getter
	@Setter
	@FormatterAttr(name="title", mandatory=false)
	private String title;
	
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
	
	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		
		if(formatter instanceof DataSourceFormatter) {
			super.addChildFormatter(formatter);
		} else {
			throw new FormatterException(formatter, "Invalid Formatter(DataSourceFormatter expected).");
		}
	}
	
	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		//
		if(false == (copyObj instanceof LineChartFormatter)) {
			throw new FormatterException(this, "Invalid Formatter(LineChartFormatter expected).");
		}
		
		//
		try {
			
			// 1.
			LineChartFormatter copy = (LineChartFormatter)copyObj;
			this.setWorksheet(copy.getWorksheet());
			
			// 2. categoryDS, dataDS를 하위 컴포넌트(category, data)에서 설정함
			this.execChildFormatters(this, charset, values);
			
			// 3. series 객체 생성 및 세부 설정
			XDDFChartData.Series series =
					copy.getChartData().addSeries(this.getCategoryDS(), this.getValueDS());
			
			// title이 null이 아니고 blank가 아닐때,
			// series에 title 설정
			if(this.getTitle() != null && false == this.getTitle().trim().equals("")) {
				series.setTitle(this.getTitle(), null);
			}
			
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}
}
