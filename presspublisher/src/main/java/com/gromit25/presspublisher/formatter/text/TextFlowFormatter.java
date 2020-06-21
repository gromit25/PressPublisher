 package com.gromit25.presspublisher.formatter.text;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

/**
 * Text Flow Formatter
 * 텍스트 처리를 위한 Formatter
 * 
 * @author jmsohn
 */
@FormatterSpec(group="text", tag="format")
public class TextFlowFormatter extends AbstractTextFormatter {
	
	@Override
	public void addText(String text) throws FormatterException {
		
		// 입력값 체크
		if(text == null) {
			throw new FormatterException(this, "text is null");
		}
		
		// TextFormatter를 만들어 문장 추가
		//
		// <format>텍스트 메시지</format> 은 
		// 개념상, <format><text>텍스트 메시지</text></format>로
		// 처리되는 것임
		// 상기와 같이 처리되기 떄문에 <text> 태그는 만들지 않음
		this.getChildFormatterList().add(new TextFormatter(text));
	}

	@Override
	protected void formatText(OutputStream copy, Charset charset, ValueContainer values) throws FormatterException {
		// 자식의 Formatter 수행
		this.execChildFormatters(copy, charset, values);
	}

}
