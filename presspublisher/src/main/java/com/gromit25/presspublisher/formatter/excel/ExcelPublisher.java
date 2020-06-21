package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gromit25.presspublisher.Publisher;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterXmlHandler;

/**
 * excel publisher 클래스
 * 
 * @author jmsohn
 */
public class ExcelPublisher extends Publisher {

	@Override
	protected FormatterXmlHandler createXmlHandler() throws Exception {
		return new ExcelFormatterXmlHandler();
	}

	@Override
	public void publish(OutputStream out, Charset charset, ValueContainer values) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		this.getRootFormatter().format(workbook, charset, values);
		workbook.write(out);
	}

}
