package com.gromit25.presspublisher;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterException;

public class CustomShowMessages extends Formatter{

	@Override
	public void addText(String text) throws FormatterException {
		// do nothing
	}

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		// do nothing
	}

	@Override
	public void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		System.out.println("TEST");
	}
}
