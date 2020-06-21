package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * alignment formatter
 * cellstyle의 cell내 텍스트 수직 수평 배열 정의하기 위한 formatter
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="alignment")
public class AlignmentFormatter extends AbstractSubCellStyleFormatter {
	
	/**
	 * 수평 위치(horizontal)
	 * 속성값은 HorizontalAlignment 값의 문자열이어야 함
	 */
	@Getter
	@Setter
	@FormatterAttr(name="horizontal", mandatory=false)
	private HorizontalAlignment horizontal;
	
	/**
	 * 수직 위치(vertical)
	 * 속성값은 VerticalAlignment 값의 문자열이어야 함
	 */
	@Getter
	@Setter
	@FormatterAttr(name="vertical", mandatory=false)
	private VerticalAlignment vertical;
	
	@Override
	protected void formatCellStyle(XSSFCellStyle copy, Charset charset, ValueContainer values) throws FormatterException {
		
		// 수평 위치 값 설정
		if(this.getHorizontal() != null) {
			copy.setAlignment(this.getHorizontal());
		}
		
		// 수직 위치 값 설정
		if(this.getVertical() != null) {
			copy.setVerticalAlignment(this.getVertical());
		}
	}

}
