package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.xddf.usermodel.chart.XDDFChartAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="axes")
public class AxesFormatter extends AbstractChartComponent {
	
	/**
	 * chart 객체
	 * AxisFormatter 객체에서 Axis 객체를 생성할 때 사용함
	 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private XSSFChart chart;
	
	/**
	 * category axis 객체
	 * 하위 컴포넌트인 AxisFormatter(tag: category-axis) 객체에서 생성하여 설정함
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private XDDFChartAxis categoryAxis;
	
	/**
	 * value axis 객체
	 * 하위 컴포넌트인 AxisFormatter(tag: value-axis) 객체에서 생성하여 설정함
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private XDDFValueAxis valueAxis;
	
	
	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		if(formatter instanceof AxisFormatter) {
			super.addChildFormatter(formatter);
		} else {
			throw new FormatterException(formatter, "Invalid Formatter(AxisFormatter expected).");
		}
	}
	
	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		//
		if(false == (copyObj instanceof LineChartFormatter)) {
			throw new FormatterException(this, "Invalid Formatter(LineChartFormatter expected).");
		}
		
		//
		LineChartFormatter copy = (LineChartFormatter)copyObj;
		this.setChart(copy.getChart());
		
		// category-axis와 value-axis를 설정함
		this.execChildFormatters(this, charset, values);
		
		// category-axis와 value-axis가 설정되어 있는지 검사
		if(null == this.getCategoryAxis()) {
			throw new FormatterException(this, "category-axis is not found.");
		}
		
		if(null == this.getValueAxis()) {
			throw new FormatterException(this, "value-axis is not found.");
		}
		
		// chartData 생성 및 설정
		XDDFChartData chartData = copy.getChart().createData(
				copy.getType(), this.getCategoryAxis(), this.getValueAxis());   
		copy.setChartData(chartData);
	}

}
