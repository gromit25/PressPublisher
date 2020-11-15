package com.gromit25.presspublisher.formatter.flow;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="flow", tag="custom")
public class CustomFormatter extends BasicFlowFormatter {
	
	@FormatterAttr(name="class", mandatory=true)
	@Getter
	@Setter
	private Class<?> customClass;

	@Override
	public void addText(String text) throws FormatterException {
		// do nothing
	}

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		// CustomFormatter만 추가 가능
		if(formatter instanceof CustomFormatter) {
			super.addChildFormatter(formatter);
		} else {
			throw new FormatterException(this, "Unexpected Formatter type(CustomFormatter):" + formatter.getClass());
		}
	}

	@Override
	protected void execFormat(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		try {
			
			if(false == Formatter.class.isAssignableFrom(this.getCustomClass())) {
				throw new FormatterException(this, "Unexpected class(Expect:Formatter):" + this.getCustomClass());
			}
			
			Formatter customInstance = Formatter.class.cast(this.getCustomClass().newInstance());
			this.copy(customInstance);
			customInstance.format(out, charset, values);
			
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
