package com.gromit25.presspublisher.formatter;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="custom", tag="custom")
public class CustomFormatter extends Formatter {
	
	@FormatterAttr(name="class", mandatory=true)
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private Class<?> customClass;

	@Override
	public void addText(String text) throws FormatterException {
		// do nothing
	}

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		// CustomFormatter만 추가 가능
		if(formatter instanceof CustomFormatter) {
		}
	}

	@Override
	public void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		// TODO Auto-generated method stub

	}

}
