package com.gromit25.presspublisher.formatter.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * 엑셀 image 추가용 formatter
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="image")
public class ImageFormatter extends AbstractExcelFormatter {

	/**
	 * 추가할 이미지의 경로
	 */
	@Getter
	@Setter
	@FormatterAttr(name="src", mandatory=true)
	private File srcFile;

	/**
	 * 이미지를 추가할 셀 위치
	 */
	@Getter
	@Setter
	@FormatterAttr(name="position", mandatory=false)
	private RowColumnEval position;

	/**
	 * 이미지의 x축 scale
	 * 주의!, scale은 MAX_VALUE를 넣으면, 원본 크기로 보여줌
	 *       scale은 cell 크기 비례함
	 *       즉 1.0으로 설정하면 cell의 x축 크기임
	 */
	@Getter
	@Setter
	@FormatterAttr(name="x-scale", mandatory=false)
	private double scaleX = Double.MAX_VALUE;

	/**
	 * 이미지의 y축 scale
	 * 주의!, scale은 MAX_VALUE를 넣으면, 원본 크기로 보여줌
	 *       scale은 cell 크기 비례함
	 *       즉 1.0으로 설정하면 cell의 y축 크기임
	 */
	@Getter
	@Setter
	@FormatterAttr(name="y-scale", mandatory=false)
	private double scaleY = Double.MAX_VALUE;

	@Override
	protected void formatExcel(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		if(false == this.getSrcFile().canRead()) {
			throw new FormatterException(this, "Can't read file:" + this.getSrcFile().getAbsolutePath());
		}
		
		try {
			
			// 현재 workbook과 worksheet 객체를 가져옴
			WorkbookFormatter wbFormatter = this.getParentInBranch(WorkbookFormatter.class);
			XSSFWorkbook workbook = wbFormatter.getWorkbook();
			
			WorksheetFormatter shFormatter = this.getParentInBranch(WorksheetFormatter.class);
			XSSFSheet sheet = shFormatter.getWorksheet();
			
			// 이미지를 생성 읽어 와서 workbook에 추가함
			// workbook에 추가만 된것이지, worksheet에 표시 위치를 지정한 것은 아님
			int imageIdx = -1;
			try(InputStream imageInputStream = new FileInputStream(this.getSrcFile())) {
				byte[] imageBytes = IOUtils.toByteArray(imageInputStream);
				imageIdx = workbook.addPicture(imageBytes, XSSFWorkbook.PICTURE_TYPE_JPEG);
			}
			
			//
			XSSFCreationHelper helper = workbook.getCreationHelper();
			XSSFDrawing drawing = sheet.createDrawingPatriarch();
			XSSFClientAnchor anchor = helper.createClientAnchor();
			
			// 이미지를 표시해야할 시작 컬럼/로우 위치를 가져와서,
			// 설정함
			int columnPosition = shFormatter.getCursorColumnPosition();
			int rowPosition = shFormatter.getCursorRowPosition();
			
			if(null != this.getPosition()) {
				columnPosition = this.getPosition().evalColumnValue(values);
				rowPosition = this.getPosition().evalRowValue(values);
			}
			
			anchor.setCol1(columnPosition);
			anchor.setRow1(rowPosition);
			
			// 이미지를 그리고,
			// 이미지의 scale을 설정함
			XSSFPicture image = drawing.createPicture(anchor, imageIdx);
			image.resize(this.getScaleX(), this.getScaleX());
			
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}

	}

}
