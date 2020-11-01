package com.gromit25.presspublisher.formatter.excel;

import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;

import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="line-series")
public class LineSeriesFormatter extends AbstractSeriesFormatter {
	
	/** series의 제목 */
	@Getter
	@Setter
	@FormatterAttr(name="title", mandatory=false)
	private String title;
	
	/** smooth 설정 */
	@Getter
	@Setter
	@FormatterAttr(name="smooth", mandatory=false)
	private Boolean smooth;
	
	/** mark의 스타일 설정 */
	@Getter
	@Setter
	@FormatterAttr(name="mark-style", mandatory=false)
	private MarkerStyle markStyle;
	
	/** mark의 크기 설정 */
	@Getter
	@Setter
	@FormatterAttr(name="mark-size", mandatory=false)
	private Short markSize;

	@Override
	protected XDDFChartData createChartData(ChartFormatter copy) throws FormatterException {
		
		// 1. chart data 객체 생성
		XDDFLineChartData chartData = (XDDFLineChartData)copy.getChart().createData(
				ChartTypes.LINE, copy.getCategoryAxis(), copy.getValueAxis());
		
		// 2. series 객체 생성 및 세부 설정
		XDDFLineChartData.Series series =
				(XDDFLineChartData.Series)chartData.addSeries(this.getCategoryDS(), this.getValueDS());
		
		// title 설정
		// title이 null이 아니고 blank가 아닐때,
		// series에 title 설정
		if(null != this.getTitle() && false == this.getTitle().trim().equals("")) {
			series.setTitle(this.getTitle(), null);
		}

		// smooth 설정
		if(null != this.getSmooth()) {
			series.setSmooth(this.getSmooth());
		}
			
		// mark style 설정
		if(null != this.getMarkStyle()) {
			series.setMarkerStyle(this.getMarkStyle());
		}
			
		// mark size 설정
		if(null != this.getMarkSize()) {
			series.setMarkerSize(this.getMarkSize());
		}
		
		return chartData;
	}
	
}
