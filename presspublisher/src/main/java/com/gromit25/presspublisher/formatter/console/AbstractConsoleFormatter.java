package com.gromit25.presspublisher.formatter.console;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.flow.AbstractFlowFormatter;
import com.gromit25.presspublisher.formatter.text.AbstractTextFormatter;

/**
 * console 출력용 Formatter의 추상화된 클래스
 * 
 * @author jmsohn
 */
public abstract class AbstractConsoleFormatter extends AbstractFlowFormatter {
	
	/**
	 * console에 출력작업 수행
	 * @param out 출력 스트림
	 * @param charset 출력시 사용할 character set
	 * @param values value container
	 */
	protected abstract void formatConsole(OutputStream out, Charset charset, ValueContainer values) throws FormatterException;
	
	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		
		if(formatter == null) {
			throw new FormatterException(this, "formatter is null");
		}
		
		// 추가되는 Formatter가 TextFormatter나 FlowFormatter가 아닐 경우, 예외 발생
		if((formatter instanceof AbstractTextFormatter) == false
			&& (formatter instanceof AbstractFlowFormatter) == false) {
			throw new FormatterException(this, "unexpected formatter type(not AbstractTextFormatter or AbstractFlowFormatter):" + formatter.getClass().getName());
		}
		
		super.addChildFormatter(formatter);
	}
	
	@Override
	public void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		try {
			this.formatConsole(out, charset, values);
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
	}

}
