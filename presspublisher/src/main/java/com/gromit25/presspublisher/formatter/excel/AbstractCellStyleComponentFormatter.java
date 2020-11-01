package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.gromit25.presspublisher.common.PublisherUtil;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.flow.BasicFlowFormatter;

/**
 * celltype 이하의 설정용 Formatter의 추상 클래스
 * ex) BorderFormatter, BackgroundFormatter, AlignmentFormatter 등
 * 
 * @author jmsohn
 */
public abstract class AbstractSubCellStyleFormatter  extends BasicFlowFormatter {
	
	/**
	 * cellstyle에 설정작업 수행
	 * @param copy 설정할 cellstyle
	 * @param charset 출력시 사용할 character set
	 * @param values value container
	 */
	protected abstract void formatCellStyle(XSSFCellStyle copy, Charset charset, ValueContainer values) throws FormatterException;

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		
		if(formatter == null) {
			throw new FormatterException(this, "formatter is null");
		}
		
		if((formatter instanceof AbstractSubCellStyleFormatter) == false) {
			throw new FormatterException(this, "unexpected formatter type(not AbstractCellStyleFormatter):" + formatter.getClass().getName());
		}
		
		super.addChildFormatter(formatter);
	}
	
	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		try {
			XSSFCellStyle copy = PublisherUtil.cast(copyObj, XSSFCellStyle.class);
			this.formatCellStyle(copy, charset, values);
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
	}
}
