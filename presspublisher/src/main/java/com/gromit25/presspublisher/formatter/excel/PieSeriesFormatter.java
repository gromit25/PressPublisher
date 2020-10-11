package com.gromit25.presspublisher.formatter.excel;

import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFPieChartData;

import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="pie-series")
public class PieSeriesFormatter extends AbstractSeriesFormatter {
	
	/** series의 제목 */
	@Getter
	@Setter
	@FormatterAttr(name="title", mandatory=false)
	private String title;

	@Override
	protected XDDFChartData createChartData(ChartFormatter copy) throws FormatterException {
		
		// 1. chart data 객체 생성
		XDDFPieChartData chartData = (XDDFPieChartData)copy.getChart().createData(
				ChartTypes.PIE, null, null);
		
		// 2. series 객체 생성 및 세부 설정
		XDDFPieChartData.Series series =
				(XDDFPieChartData.Series)chartData.addSeries(this.getCategoryDS(), this.getValueDS());
		
		// title 설정
		// title이 null이 아니고 blank가 아닐때,
		// series에 title 설정
		if(null != this.getTitle() && false == this.getTitle().trim().equals("")) {
			series.setTitle(this.getTitle(), null);
		}

		return chartData;
	}

}
