package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * background formatter
 * cellstyle에 배경색을 지정하기 위한 formatter
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="background")
public class BackgroundFormatter extends AbstractCellStyleComponentFormatter {
	
	/**
	 * 배경색(background color)
	 * 형식은 #FFFFFF
	 */
	@Getter
	@Setter
	@FormatterAttr(name="color", mandatory=false)
	private XSSFColor color;

	@Override
	protected void formatCellStyle(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		// 배경색을 설정함  
		if(this.getColor() != null) {
			
			// setFillBackgroundColor 메소드를 사용하면,
			// 색이 변경되지 않음 setFillForegroundColor를 사용해야 배경색이 변경됨
			this.getParentStyle().setFillForegroundColor(this.getColor());
			this.getParentStyle().setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
	}

}
