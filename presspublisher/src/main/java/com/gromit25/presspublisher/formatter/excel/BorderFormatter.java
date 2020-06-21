package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.xml.sax.Attributes;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * border formatter
 * cellstyle의 테두리 정의하기 위한 formatter
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="border")
public class BorderFormatter extends AbstractSubCellStyleFormatter {
	
	/** 상단 테두리 Style */
	@Getter(value=AccessLevel.PRIVATE)
	@Setter(value=AccessLevel.PRIVATE)
	private BorderStyle styleTop;
	
	/** 하단 테두리 Style */
	@Getter(value=AccessLevel.PRIVATE)
	@Setter(value=AccessLevel.PRIVATE)
	private BorderStyle styleBottom;
	
	/** 왼쪽 테두리 Style */
	@Getter(value=AccessLevel.PRIVATE)
	@Setter(value=AccessLevel.PRIVATE)
	private BorderStyle styleLeft;
	
	/** 오른쪽 테두리 Style */
	@Getter(value=AccessLevel.PRIVATE)
	@Setter(value=AccessLevel.PRIVATE)
	private BorderStyle styleRight;
	
	/** 테두리 색 */
	@Getter
	@Setter
	@FormatterAttr(name="color", mandatory=false)
	private XSSFColor color;
	
	@Override
	public void setAttributes(Attributes attributes) throws FormatterException {
		
		// 테두리 style 속성
		// 속성값은 BorderStyle 값의 문자열이어야 함
		String styleAttr = attributes.getValue("style");
		BorderStyle style = null;
		if(styleAttr == null || styleAttr.trim().equals("") == true) {
			throw new FormatterException(this, "cell border style is not set");
		}
		
		style = BorderStyle.valueOf(styleAttr);
		
		// 테두리 위치 속성
		// ALL(전체), TOP(상단), BOTTOM(하단), LEFT(왼쪽), RIGHT(오른쪽)
		// 여러개의 위치 동시 지정 가능
		// ex) 상단과 하단 지정시, "TOP, BOTTOM"으로 설정
		String sideAttr = attributes.getValue("side");
		if(style == null || sideAttr == null || sideAttr.trim().equals("") == true) {
			throw new FormatterException(this, "cell border side is not set");
		}
		
		// 테두리 위치별로 테두리 style을 지정
		String[] sideNames = sideAttr.split("[ \t]*,[ \\t]*");
		for(int index = 0; index < sideNames.length; index++) {
			switch(sideNames[index]) {
			case "TOP":
				this.setStyleTop(style);
				break;
			case "BOTTOM":
				this.setStyleBottom(style);
				break;
			case "LEFT":
				this.setStyleLeft(style);
				break;
			case "RIGHT":
				this.setStyleRight(style);
				break;
			case "ALL":
				this.setStyleTop(style);
				this.setStyleBottom(style);
				this.setStyleLeft(style);
				this.setStyleRight(style);
				break;
			default:
				throw new FormatterException(this, "Unknown cell border side name");
			}
		}
	}

	@Override
	protected void formatCellStyle(XSSFCellStyle copy, Charset charset, ValueContainer values) throws FormatterException {
		
		// 상단 테두리 및 색 지겅
		if(this.getStyleTop() != null) {
			copy.setBorderTop(this.getStyleTop());
			if(this.getColor() != null) {
				copy.setTopBorderColor(this.getColor());
			}
		}
		
		// 하단 테두리 및 색 지겅
		if(this.getStyleBottom() != null) {
			copy.setBorderBottom(this.getStyleBottom());
			if(this.getColor() != null) {
				copy.setBottomBorderColor(this.getColor());
			}
		}
		
		// 왼쪽 테두리 및 색 지겅
		if(this.getStyleLeft() != null) {
			copy.setBorderLeft(this.getStyleLeft());
			if(this.getColor() != null) {
				copy.setLeftBorderColor(this.getColor());
			}
		}
		
		// 오른쪽 테두리 및 색 지겅
		if(this.getStyleRight() != null) {
			copy.setBorderRight(this.getStyleRight());
			if(this.getColor() != null) {
				copy.setRightBorderColor(this.getColor());
			}
		}
	}

}
