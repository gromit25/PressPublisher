package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.BasicFormatter;
import com.gromit25.presspublisher.formatter.FormatterException;

/**
 * Workbook의 하위 컴포넌트 Formatter의 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class AbstractWorkbookComponentFormatter extends BasicFormatter {
	
	/**
	 * excel의 workbook에 출력작업 수행
	 * @param out 출력 스트림
	 * @param charset 출력시 사용할 character set
	 * @param values value container
	 */
	protected abstract void formatExcel(OutputStream out, Charset charset, ValueContainer values) throws FormatterException;
	
	@Override
	public void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {

		try {
			this.formatExcel(out, charset, values);
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
	}
}
