package com.gromit25.presspublisher.formatter.console;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;
import com.gromit25.presspublisher.formatter.text.TextFormatter;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * style formatter 
 * 출력할 텍스트의 style을 지정
 * type 속성: 색 또는 Underline 등을 지정
 * 
 * ex)
 * <style type="FG_WHITE_BRIGHT">메세지</style>
 * 
 * @author jmsohn
 */
@FormatterSpec(group="console", tag="style")
public class StyleFormatter extends AbstractConsoleFormatter {
	
	/** 설정된 style */
	@Getter(value=AccessLevel.PRIVATE)
	private ArrayList<ConsoleStyle> styleTypes = new ArrayList<ConsoleStyle>();
	
	@Override
	public void setAttributes(Attributes attributes) throws FormatterException {
		// 입력값 체크
		String styleTypeAttr = attributes.getValue("type");
		if(styleTypeAttr == null) {
			throw new FormatterException(this, "style type is null");
		}
		
		// FG_WHITE_BRIGHT, BG_BLUE_BRIGHT 형식으로
		// 입력된 style 속성을 분해하여
		// ArrayList 에 추가함
		String[] styleTypeNames = styleTypeAttr.split(",");
		for(String styleTypeName: styleTypeNames) {
			this.getStyleTypes().add(ConsoleStyle.valueOf(styleTypeName.trim()));
		}
	}
	
	@Override
	public void addText(String text) throws FormatterException {
		// 입력값 체크
		if(text == null) {
			throw new FormatterException(this, "text is null");
		}
		
		this.getChildFormatterList().add(new TextFormatter(text));
	}
	
	@Override
	public void formatConsole(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		// 입력값 검증
		if(out == null) {
			throw new FormatterException(this, "out param is null.");
		}
		
		if(charset == null) {
			throw new FormatterException(this, "Charset is null.");
		}
		
		if(values == null) {
			throw new FormatterException(this, "Value Container is null.");
		}
		
		try {
			// 현재 설정된 style을
			// 순서대로 모두 적용함 
			for(ConsoleStyle style: this.getStyleTypes()) {
				out.write(style.getCode().getBytes(charset));
			}
	
			// 자식 formatter들을 수행 
			this.execChildFormatters(out, charset, values);
			
			// 모든 text를 추가 완료하면,
			// 현재 style을 초기화 시킴
			out.write(ConsoleStyle.RESET.getCode().getBytes(charset));
			
			out.flush();
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}
}
