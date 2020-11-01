package com.gromit25.presspublisher.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;

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
		
		if(false == (this.getRootFormatter() instanceof WorkbookFormatter)) {
			throw new Exception("root formatter is not WorkbookFormatter:" + this.getRootFormatter().getClass());
		}
		
		WorkbookFormatter root = (WorkbookFormatter)this.getRootFormatter();
		root.format(out, charset, values);
		root.getWorkbook().write((OutputStream)out);
	}

}
