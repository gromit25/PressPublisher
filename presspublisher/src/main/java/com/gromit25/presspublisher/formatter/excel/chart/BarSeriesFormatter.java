package com.gromit25.presspublisher.formatter.excel.chart;

import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;

import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="bar-series")
public class BarSeriesFormatter extends AbstractSeriesFormatter {
	
	/** series의 제목 */
	@Getter
	@Setter
	@FormatterAttr(name="title", mandatory=false)
	private String title;
	
	/** bar chart의 방향 */
	@Getter
	@Setter
	@FormatterAttr(name="direction", mandatory=false)
	private BarDirection direction;

	@Override
	protected XDDFChartData createChartData(ChartFormatter copy) throws FormatterException {
		
		// 1. chart data 객체 생성
		XDDFBarChartData chartData = (XDDFBarChartData)copy.getChart().createData(
				ChartTypes.BAR, copy.getCategoryAxis(), copy.getValueAxis());
		
		// 2. series 객체 생성 및 세부 설정
		XDDFBarChartData.Series series =
				(XDDFBarChartData.Series)chartData.addSeries(this.getCategoryDS(), this.getValueDS());
		
		// title 설정
		// title이 null이 아니고 blank가 아닐때,
		// series에 title 설정
		if(null != this.getTitle() && false == this.getTitle().trim().equals("")) {
			series.setTitle(this.getTitle(), null);
		}
		
		// bar chart의 방향 설정
		if(null != this.getDirection()) {
			chartData.setBarDirection(direction);
		}
		
		return chartData;
	}
	
}
