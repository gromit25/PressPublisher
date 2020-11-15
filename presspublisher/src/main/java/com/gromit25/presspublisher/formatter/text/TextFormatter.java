package com.gromit25.presspublisher.formatter.text;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Text Formatter
 * 텍스트를 출력하는 Formatter
 * Tag 없이 다른 Formatter에서 생성하여 사용하기 때문에,
 * FormatterSpec을 따로 지정하지 않음
 * ex) StyleFormatter, TextFlowFormatter 등
 * 
 * @author jmsohn
 */
public class TextFormatter extends AbstractTextFormatter {
	
	/** 출력할 텍스트 메시지 */
	@Getter
	@Setter(value=AccessLevel.PACKAGE)
	private String message;
	
	/**
	 * 생성자
	 * TextFormatterXmlHandler에 의해 생성되지 않고,
	 * TextFlowFormatter에셔 addText 메소드 수행시 생성됨
	 * 
	 * @param message 출력할 텍스트 메시지
	 */
	public TextFormatter(String message) {
		this.setMessage(message);
	}

	@Override
	public void addText(String text) throws FormatterException {
		// do nothing
	}

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		// do nothing
	}

	@Override
	public void formatText(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		try {
			// Output stream에 출력 수행
			out.write(this.getMessage().getBytes(charset));
			out.flush();
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
