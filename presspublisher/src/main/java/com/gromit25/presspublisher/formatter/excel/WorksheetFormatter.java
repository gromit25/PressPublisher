package com.gromit25.presspublisher.formatter.excel;

import java.nio.charset.Charset;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * worksheet formatter
 * 신규 worksheet 생성
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="worksheet")
public class WorksheetFormatter extends AbstractExcelFormatter {
	
	/** worksheet 명(필수) */
	@Getter
	@Setter
	@FormatterAttr(name="name")
	private String name;

	@Override
	public void formatExcel(XSSFWorkbook copy, Charset charset, ValueContainer values) throws FormatterException {
		
		// 입력값 검증
		if(copy == null) {
			throw new FormatterException(this, "Copy Object is null");
		}
		
		// 설정된 worksheet명으로 worksheet 생성 및 활성화 sheet(active sheet)로 설정
		XSSFSheet sheet = copy.getSheet(this.getName());
		if(sheet == null) {
			sheet = copy.createSheet(this.getName());
		}
		
		copy.setActiveSheet(copy.getSheetIndex(this.getName()));
		
		// worksheet의 자식 formatter 수행
		this.execChildFormatters(copy, charset, values);
		
		// 첫 worksheet로 active worksheet를 변경
		copy.setActiveSheet(0);
	}

}
