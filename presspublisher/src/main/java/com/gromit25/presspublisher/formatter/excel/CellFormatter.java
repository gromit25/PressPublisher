package com.gromit25.presspublisher.formatter.excel;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gromit25.presspublisher.evaluator.RowColumnEval;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;
import com.gromit25.presspublisher.formatter.text.TextFlowFormatter;
import com.gromit25.presspublisher.formatter.text.TextFormatOutputStream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * cell formatter
 * cell을 생성, cell 데이터 출력, cell style 적용 수행함
 * 
 * 속성에 공통으로 cell의 row와 column 관련 속성들을 지정하기 위해
 * rowColumn 표현식을 만듦
 * 형식) "rowExp:columnExp"
 * ex) "5:4" 6 row(0 부터 시작), 5 column("E" column, 0이 "A" column)
 *     ":4"  1 row, 5 column, "5:" 6 row, 0 column("A" column)
 *     "base.getRow() : base.getColumn() + 2" <- 스크립트 사용가능
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="cell")
public class CellFormatter extends AbstractExcelFormatter {

	/**
	 * cell의 위치
	 * rowColumn 표현 형식 사용, default값은 "0:0"
	 */
	@Getter
	@Setter
	@FormatterAttr(name="position", mandatory=false)
	private RowColumnEval positionExpEval;
	
	/**
	 * cell의 병합 범위
	 * default값은 "0:0", 0일 경우 병합하지 않음
	 */
	@Getter
	@Setter
	@FormatterAttr(name="span", mandatory=false)
	private RowColumnEval spanExpEval;
	
	/**
	 * cell의 크기 지정, pixel 단위임, rowColumn 표현 형식 사용
     * row pixel은 정확히 지정되나, column pixel은 근사치로 지정됨
     * default값은 “0:0”, 0일 경우 size 조정하지 않음
	 */
	@Getter
	@Setter
	@FormatterAttr(name="size", mandatory=false)
	private RowColumnEval sizeExpEval;
	
	/**
	 * 자동 크기 설정
	 * true/false 값 설정, cell 자동 size 조정 기능 설정
	 */
	@Getter
	@Setter
	@FormatterAttr(name="autoSizing", mandatory=false)
	private boolean autoSizing;
	
	/**
	 * cellstyle 설정
	 */
	@Getter
	@Setter
	@FormatterAttr(name="style", mandatory=false)
	private String style;

	/**
	 * cell에 표시될 텍스트를 생성하는 formatter
	 */
	@Getter(value=AccessLevel.PRIVATE)
	private TextFlowFormatter cellTextFormatter = new TextFlowFormatter();
	
	
	/**
	 * 생성자
	 */
	public CellFormatter() throws Exception {
		
		super();
		
		// default 값 설정
		this.setPositionExpEval(RowColumnEval.compile("0:0", "0", "0"));
		this.setSpanExpEval(RowColumnEval.compile("0:0", "0", "0"));
		this.setSizeExpEval(RowColumnEval.compile("0:0", "0", "0"));
		this.setAutoSizing(false);
	}
	
	@Override
	public void addText(String text) throws FormatterException {
		// Cell에 표시될 텍스트 추가
		this.getCellTextFormatter().addText(text);
	}

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		
		// Cell에 표시될 text/flow formatter 추가
		// text/flow formatter 여부는
		// TextFlowFormatter의 추상 클래스인 AbstractTextFormatter에서 체크함
		this.getCellTextFormatter().addChildFormatter(formatter);

	}

	@Override
	public void formatExcel(XSSFWorkbook copy, Charset charset, ValueContainer values) throws FormatterException {
		
		// 입력값 검증
		if(copy == null) {
			throw new FormatterException(this, "Copy Object is null");
		}
		
		if(charset == null) {
			throw new FormatterException(this, "Charset is null");
		}
		
		if(values == null) {
			throw new FormatterException(this, "Value Container is null");
		}

		try {
			
			////////////////////////////////////////////////////////////////
			// 1. 사용할 cell의 객체 가져옴 
			
			// row와 column 위치 표현식 수행 결과 설정
			// 표현식 수행 결과는 항상 Double이기 때문에,
			// Double로 캐스팅 후 다시 Integer 값을 가져옴
			int rowPosition = this.getPositionExpEval().getRowEval().eval(values, Double.class).intValue();
			int columnPosition = this.getPositionExpEval().getColumnEval().eval(values, Double.class).intValue();
			
			// worksheet에 cell의 위치를 가져옴
			// baseCell이라고 이름지은 이유는
			// cell 병합이 있을 경우, 여러 cell을 다루기 때문에 기준점 cell이라는 의미임
			XSSFSheet sheet = copy.getSheetAt(copy.getActiveSheetIndex());
			XSSFCell baseCell = ExcelUtil.getCell(sheet, rowPosition, columnPosition);
			
			////////////////////////////////////////////////////////////////
			// 2. cell 표시될 메시지 생성 및 설정
			
			String message = "";
			
			// 설정된 text formatter를 수행하여,
			// cell에 표시될 text를 설정함
			try(TextFormatOutputStream messageOut = new TextFormatOutputStream(new ByteArrayOutputStream())) {
				this.getCellTextFormatter().format(messageOut, charset, values);
				message = messageOut.toString();
			}
			baseCell.setCellValue(message);
	
			////////////////////////////////////////////////////////////////
			// 3. cell 병합 작업 수행
			
			// 병합 cell 주소
			// default는 baseCell 만 설정
			CellRangeAddress spanCellAddr = new CellRangeAddress(rowPosition, rowPosition, columnPosition, columnPosition);
			
			// row와 column 병합 범위 표현식 수행 결과 설정
			int rowSpan = this.getSpanExpEval().getRowEval().eval(values, Double.class).intValue();;
			int columnSpan = this.getSpanExpEval().getColumnEval().eval(values, Double.class).intValue();
			
			// 수행 결과가 0 이상일때, 병합 수행
			if(rowSpan > 0 || columnSpan > 0) {
				spanCellAddr = new CellRangeAddress(rowPosition, rowPosition + rowSpan, columnPosition, columnPosition + columnSpan);
				sheet.addMergedRegion(spanCellAddr);
			}
			
			////////////////////////////////////////////////////////////////
			// 4. cell 크기 설정
			//    병합된 cell 전체의 크기로 설정함
			//    ex) span이 4:5 에 size가 100:110 라면,
			//        row는 100/(4+1(baseCell)) = 20 픽셀씩 설정됨
			//        column은 110/(5+1) = 18 픽셀씩 설정되고,
			//        최종 column은 18 + (110%(4+1)) 즉, 18+2 = 20 픽셀이 설정됨
			
			// row 크기 계산하여 설정, 단위는 pixel
			int rowSize = this.getSizeExpEval().getRowEval().eval(values, Double.class).intValue();
			if(rowSize > 0 && rowSpan >= 0) {
				
				int shareSize = rowSize / (rowSpan + 1);
				int restSize = rowSize % (rowSpan + 1);
				
				if(shareSize > 0) {
					for(int index = 0; index < rowSpan + 1; index++) {
						
						int rowPixel = shareSize;
						if(index == rowSpan) { 
							rowPixel += restSize;
						}
						
						XSSFRow row  = ExcelUtil.getRow(sheet, rowPosition + index);
						row.setHeightInPoints((float)Units.pixelToPoints(rowPixel));
					}
				}
			}
			
			// column 크기 계산하여 설정, 단위는 pixel
			// ※ 상기 row pixel은 정확히 지정되나, column pixel은 근사치로 지정됨
			int columnSize = this.getSizeExpEval().getColumnEval().eval(values, Double.class).intValue();
			if(columnSize > 0 && columnSpan >= 0) {
				
				int shareSize = columnSize / (columnSpan + 1);
				int restSize = columnSize % (columnSpan + 1);
				
				if(shareSize > 0) {
					for(int index = 0; index < columnSpan + 1; index++) {
						
						int widthPixel = shareSize;
						if(index == columnSpan) { 
							widthPixel += restSize;
						}
						
						sheet.setColumnWidth(columnPosition + index, ExcelUtil.pixel2WidthUnits(widthPixel));
					}
				}
			}
			
			////////////////////////////////////////////////////////////////
			// 5. cell 자동 크기 설정
			//    병합된 모든 cell에 대해 각각 적용
			//
			//    ※ autoSize option은 처리시간이 많이 걸리는 
			//      항목으로 사용 자제
			if(this.isAutoSizing() == true) {
				
				Iterator<CellAddress> spanCellAddrIter = spanCellAddr.iterator();
				while(spanCellAddrIter.hasNext() == true) {
					
					CellAddress cellAddr = spanCellAddrIter.next();
					sheet.autoSizeColumn(cellAddr.getColumn());
				}
			}
	
			////////////////////////////////////////////////////////////////
			// 6. cell style 설정
			//    cell style 이름은 cell style 목록(CellStyleFormatter.CELLSTYLE_BUNDLE_NAME)에 있어야함
			//    병합된 모든 cell에 대해 각각 적용
			@SuppressWarnings("unchecked")
			Hashtable<String, XSSFCellStyle> styles = values.get(CellStyleFormatter.CELLSTYLE_BUNDLE_NAME, Hashtable.class);
			if(styles != null && this.getStyle() != null && styles.containsKey(this.getStyle())) {
				
				Iterator<CellAddress> spanCellAddrIter = spanCellAddr.iterator();
				while(spanCellAddrIter.hasNext() == true) {
					
					CellAddress cellAddr = spanCellAddrIter.next();
					
					XSSFCell spanCell = ExcelUtil.getCell(sheet, cellAddr);
					spanCell.setCellStyle(styles.get(this.getStyle()));
					
				}
			}
			
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
		// cellTextFormatter에 자식 Formatter들이
		// 등록 되어 있기 때문에 CellFormatter의 자식 Formatter를 수행할 
		// 필요가 없다. 
		// cellTextFormatter는 formatExcel 메소드 진입부에서 이미 수행함
		//this.execChildFormatters(copy, charset, values);
	}

}
