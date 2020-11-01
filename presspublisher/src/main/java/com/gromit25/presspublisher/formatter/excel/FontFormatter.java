package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Hashtable;

import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * font formatter
 * cellstyle에 사용할 글꼴을 정의
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="font")
public class FontFormatter extends AbstractWorkbookComponentFormatter {
	
	static String FONT_BUNDLE_NAME = "_FONT_BUNDLE_";

	/** value container의 글꼴목록에 설정할 이름 */
	@Getter
	@Setter
	@FormatterAttr(name="name", mandatory=true)
	private String name;
	
	/** 글꼴 명 */
	@Getter
	@Setter
	@FormatterAttr(name="fontname", mandatory=false)
	private String fontName;
	
	/** 글꼴 색, ex) #FFFF00*/
	@Getter
	@Setter
	@FormatterAttr(name="color", mandatory=false)
	private XSSFColor color;
	
	/** 글꼴 크기 */
	@Getter
	@Setter
	@FormatterAttr(name="height", mandatory=false)
	private int height = -1;
	
	/** 이탤릭체 여부 */
	@Getter
	@Setter
	@FormatterAttr(name="italic", mandatory=false)
	private boolean italic = false;
	
	/** 굵게표시 여부 */
	@Getter
	@Setter
	@FormatterAttr(name="bold", mandatory=false)
	private boolean bold = false;
	
	@Override
	protected void formatExcel(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		// excel workbook에서 글꼴 생성
		XSSFFont font = this.getParent(WorkbookFormatter.class)
							.getWorkbook().createFont(); 
		
		// 글꼴명 설정
		if(this.getFontName() != null) {
			font.setFontName(this.getFontName());
		}
		
		// 글꼴 색 설정
		if(this.getColor() != null) {
			try {
				font.setColor(this.getColor());
			} catch(Exception ex) {
				throw new FormatterException(this, ex);
			}
		}
		
		// 글꼴 크기 설정
		if(this.getHeight() > 0) { 
			font.setFontHeight(this.getHeight());
		}
		
		// 이탤릭체 여부 설정
		font.setItalic(this.isItalic());
		
		// 굵게표시 여부 설정
		font.setBold(this.isBold());
		
		// cellstyle 목록에 생성된 style 추가
		//
		@SuppressWarnings("unchecked")
		Hashtable<String, XSSFFont> fontMap = values.get(FONT_BUNDLE_NAME, Hashtable.class);
		if(fontMap == null) {
			fontMap = new Hashtable<String, XSSFFont>();
			try {
				values.put(FONT_BUNDLE_NAME, fontMap);
			} catch(Exception ex) {
				throw new FormatterException(this, ex);
			}
		}
		
		fontMap.put(this.getName(), font);
		
		// 하위 formatter는 존재하지 않으므로
		// 수행하지 않고 메소드 종료함
	}

}
