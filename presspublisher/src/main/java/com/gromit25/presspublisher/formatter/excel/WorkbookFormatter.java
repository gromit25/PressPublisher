package com.gromit25.presspublisher.formatter.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;
import com.gromit25.presspublisher.formatter.flow.BasicFlowFormatter;

import lombok.Getter;
import lombok.Setter;

/**
 * workbook formatter
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="workbook")
public class WorkbookFormatter extends BasicFlowFormatter {
	
	/**
	 * 현재 workbook
	 * 하위 formatter에서 사용함
	 */
	@Getter
	@Setter
	@FormatterAttr(name="template", mandatory=false)
	private XSSFWorkbook workbook;
	
	/**
	 * 암호화된 엑셀 파일을 만들때 사용하는 패스워드
	 */
	@Getter
	@Setter
	@FormatterAttr(name="password", mandatory=false)
	private String password;
	
	@Override
	protected void execFormat(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		try {
			
			// 작업 workbook 설정
			if(null == this.getWorkbook()) {
				this.setWorkbook(new XSSFWorkbook());
			}
		
			// child formatter 호출 
			this.execChildFormatters(out, charset, values);
			
			// 출력 완료 후 모든 수식 강제 재계산
			this.getWorkbook().getCreationHelper().createFormulaEvaluator().evaluateAll();
			
			// password가 설정되어 있을 경우,
			// 엑셀 암호화 수행
			if(null != this.getPassword()) {
				
				// 주의! workbook에서 getPackage()를 사용하여 OPCPackage를 가져와
				// 사용하게 되면, 파일 write는 정상이나 실제로 열어보면 오류가 발생함 
				// -> 이유는 모름
				// 일단 파일에 출력하는 것처럼 tempWorkbook에 출력한 후
				// 이것을 파일에서 읽어 들이는 것 처럼 tempInputStream으로 읽어 들여
				// 암호화 시키면 정상적으로 암호화가 수행됨
				
				byte[] tempWorkbook = null;
				try(ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream()) {
					this.getWorkbook().write(tempOutputStream);
					tempWorkbook = tempOutputStream.toByteArray();
				}
				
				// 암호화 스펙 및 패스워드 설정
				EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
				
				Encryptor enc = info.getEncryptor();
				enc.confirmPassword(this.getPassword());
				
				// 암호화 수행 및 출력
				try(POIFSFileSystem fs = new POIFSFileSystem()) {
					
				    try(InputStream tempInputStream = new ByteArrayInputStream(tempWorkbook);
				    	OPCPackage opc = OPCPackage.open(tempInputStream);
					    OutputStream os = enc.getDataStream(fs)) {
				    	
				    	opc.save(os);
				    }
			        
			    	fs.writeFilesystem(out);
			    }
				
			}
			// password가 설정되어 있지 않은 경우,
			// 일반 파일로 출력함
			else {
				this.getWorkbook().write(out);
			}
			
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
		//
	}
}
