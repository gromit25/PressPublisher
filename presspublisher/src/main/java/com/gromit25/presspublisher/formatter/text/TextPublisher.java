package com.gromit25.presspublisher.formatter.text;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.Publisher;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterXmlHandler;

/**
 * 텍스트 publisher 클래스
 * 
 * @author jmsohn
 */
public class TextPublisher extends Publisher {

	@Override
	protected FormatterXmlHandler createXmlHandler() throws Exception {
		return new TextFormatterXmlHandler();
	}

	@Override
	public void publish(OutputStream out, Charset charset, ValueContainer values) throws Exception {
		
		try (TextFormatOutputStream textFormatOut = new TextFormatOutputStream(out)) {
			this.getRootFormatter().format(textFormatOut, charset, values);
		}
	}

}
