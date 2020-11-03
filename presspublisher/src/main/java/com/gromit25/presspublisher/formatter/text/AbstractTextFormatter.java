package com.gromit25.presspublisher.formatter.text;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.BasicFormatter;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.flow.AbstractFlowFormatter;

/**
 * 텍스트 처리용 Formatter의 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class AbstractTextFormatter extends BasicFormatter {
	
	/**
	 * OutputStream에 텍스트 출력작업 수행
	 * @param out 출력 스트림
	 * @param charset 출력시 사용할 character set
	 * @param values value container
	 */
	protected abstract void formatText(OutputStream copy, Charset charset, ValueContainer values) throws Exception;
	
	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		
		// 입력값 검증
		if(formatter == null) {
			throw new FormatterException(this, "formatter is null");
		}
		
		// Text 또는 Flow Formatter 가 아니면 오류 발생
		if((formatter instanceof AbstractTextFormatter) == false
			&& (formatter instanceof AbstractFlowFormatter) == false) {
			throw new FormatterException(this, "unexpected formatter type(not AbstractTextFormatter or AbstractFlowFormatter):" + formatter.getClass().getName());
		}
		
		super.addChildFormatter(formatter);
	}
	
	@Override
	public void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		try {
			this.formatText(out, charset, values);
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
	}
}
