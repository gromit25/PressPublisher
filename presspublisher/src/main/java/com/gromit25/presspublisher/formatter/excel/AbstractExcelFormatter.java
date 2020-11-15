package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.flow.BasicFlowFormatter;

/**
 * Excel의 Cell 출력용 Formatter의 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class AbstractExcelFormatter extends BasicFlowFormatter {

	/**
	 * excel에 출력작업 수행
	 * @param out 출력 스트림
	 * @param charset 출력시 사용할 character set
	 * @param values value container
	 */
	protected abstract void formatExcel(OutputStream out, Charset charset, ValueContainer values) throws FormatterException;
	
	@Override
	protected void execFormat(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {

		try {
			this.formatExcel(out, charset, values);
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
	}
	
}
