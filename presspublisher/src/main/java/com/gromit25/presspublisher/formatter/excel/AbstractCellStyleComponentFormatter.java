package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

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
public abstract class AbstractCellStyleComponentFormatter  extends BasicFlowFormatter {
	
	/**
	 * cellstyle에 설정작업 수행
	 * @param out 출력 스트림
	 * @param parent 부모 CellStyleFormatter
	 * @param charset 출력시 사용할 character set
	 * @param values value container
	 */
	protected abstract void formatCellStyle(OutputStream out, Charset charset, ValueContainer values) throws FormatterException;

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		
		if(formatter == null) {
			throw new FormatterException(this, "formatter is null");
		}
		
		if((formatter instanceof AbstractCellStyleComponentFormatter) == false) {
			throw new FormatterException(this, "unexpected formatter type(not AbstractCellStyleFormatter):" + formatter.getClass().getName());
		}
		
		super.addChildFormatter(formatter);
	}
	
	@Override
	public void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		try {
			this.formatCellStyle(out, charset, values);
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
	}

	/**
	 * 
	 * @return
	 */
	protected XSSFCellStyle getParentStyle() throws FormatterException {
		CellStyleFormatter parent = this.getParent(CellStyleFormatter.class);
		return parent.getStyle();
	}
}
