package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFChartAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="category-axis, value-axis")
public class AxisFormatter extends AbstractChartComponent {
	
	@Getter
	@Setter
	@FormatterAttr(name="position", mandatory = true)
	private AxisPosition position;
	
	@Getter
	@Setter
	@FormatterAttr(name="title", mandatory = false)
	private String title;

	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		//
		if(false == (copyObj instanceof AxesFormatter)) {
			throw new FormatterException(this, "Invalid Formatter(AxesFormatter expected).");
		}
		
		//
		try {
			
			AxesFormatter copy = (AxesFormatter)copyObj;
			
			// category-axis인지, value-axis인지에 따라
			// 설정을 함
			if(true == this.getTagName().equals("category-axis")) {
				
				XDDFChartAxis axis = copy.getChart().createCategoryAxis(this.getPosition());
				copy.setCategoryAxis(axis);
				
			} else if(true == this.getTagName().equals("value-axis")) {
				
				XDDFValueAxis axis = copy.getChart().createValueAxis(this.getPosition());
				copy.setValueAxis((XDDFValueAxis)axis);
				
			} else {
				throw new FormatterException(this, "unknown axis type:" + this.getTagName());
			}
			
			// 하위 Formatter가 없기 때문에 하위 Formatter 실행을 하지 않음
			// this.execChildFormatters(copyObj, charset, values);
			
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
	}

}
