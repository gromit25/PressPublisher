package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="legend")
public class LegendFormatter extends AbstractChartComponent {
	
	@Getter
	@Setter
	@FormatterAttr(name="position", mandatory=false)
	private LegendPosition position;

	@Override
	public void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		// 입력값 검사
		XDDFChartLegend legend = this.getParent(ChartFormatter.class)
									 .getChart().getOrAddLegend();
		
		if(null != this.getPosition()) {
			legend.setPosition(this.getPosition());
		}
		
	}

}
