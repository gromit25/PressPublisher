package com.gromit25.presspublisher.formatter.excel;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Excel 관련 Utility 클래스 
 * 
 * @author jmsohn
 */
class ExcelUtil {
	
	/**
	 * column길이 pixel 단위를 width 단위(정확한 단위명은 확인 못함)로 변경
	 * @param pixels 변경할 pixel단위 
	 * @return width단위
	 */
	static short pixel2WidthUnits(int pixels) {
		short widthUnits = (short)(pixels*256/Units.DEFAULT_CHARACTER_WIDTH);		
		return widthUnits;
	}
	
	/**
	 * format xml의 color 속성 문자열(#FFFFFF)를
	 * excel에서 사용하는 XSSFColor 객체로 변환
	 * @param colorCode color 속성 문자열
	 * @return 변환된 XSSFColor 객체
	 */
	static XSSFColor getColor(String colorCode) throws Exception {
		
		Pattern colorP = Pattern.compile("\\#(?<color>[0-9A-Fa-f]{6})");
		Matcher colorM = colorP.matcher(colorCode);
		
		if(colorM.matches() == false) {
			throw new Exception("color attribute is invalid type:" + colorCode);
		}
		
		XSSFColor color = new XSSFColor(
				new Color(Integer.parseInt(colorM.group("color"), 16))
				, new DefaultIndexedColorMap()
			);

		return color;
	}
	
	/**
	 * worksheet에서 row를 반환
	 * 생성된 row객체가 없으면 생성하여 반환
	 * @param sheet 대상 worksheet
	 * @param rowPosition 요청 row 위치
	 * @return rowPosition의 row 객체
	 */
	static XSSFRow getRow(XSSFSheet sheet, int rowPosition) {
		
		XSSFRow row = sheet.getRow(rowPosition);
		if(row == null) {
			row = sheet.createRow(rowPosition);
		}
		
		return row;
	}
	
	/**
	 * worksheet에서 cell을 반환
	 * 생성된 cell 객체가 없으면 생성하여 반환
	 * @param sheet 대상 worksheet
	 * @param cellAddr cell 위치 주소
	 * @return cell 위치 주소의 cell 객체
	 */
	static XSSFCell getCell(XSSFSheet sheet, CellAddress cellAddr) throws Exception {
		return getCell(sheet, cellAddr.getRow(), cellAddr.getColumn());
	}

	/**
	 * worksheet에서 cell을 반환
	 * 생성된 cell 객체가 없으면 생성하여 반환
	 * @param sheet 대상 worksheet
	 * @param rowPosition cell의 row 위치
	 * @param columnPosition cell의 column 위치
	 * @return 요청한 row, column 위치의 cell 객체
	 */
	static XSSFCell getCell(XSSFSheet sheet, int rowPosition, int columnPosition) throws Exception {
		
		XSSFRow row = getRow(sheet, rowPosition);
		
		XSSFCell cell = row.getCell(columnPosition);
		if(cell == null) {
			cell = row.createCell(columnPosition);
		}
		
		return cell;
	}

}
