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
		
		// 엑셀 출력 수행
		WorkbookFormatter root = (WorkbookFormatter)this.getRootFormatter();
		root.format(out, charset, values);
		
		// 출력 완료 후 모든 수식 강제 재계산
		root.getWorkbook().getCreationHelper().createFormulaEvaluator().evaluateAll();
		
		// 파일에 출력 
		root.getWorkbook().write((OutputStream)out);
	}

}
