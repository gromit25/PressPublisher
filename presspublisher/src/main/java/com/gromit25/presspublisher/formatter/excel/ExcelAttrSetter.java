package com.gromit25.presspublisher.formatter.excel;

import java.lang.reflect.Method;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttrSetter;
import com.gromit25.presspublisher.formatter.FormatterAttrSetterClass;

/**
 * 엑셀 객체에 대한 XML 속성값(스트링)을 파싱하여 설정하는 클래스
 * 
 * @author jmsohn
 */
@FormatterAttrSetterClass
public class ExcelAttrSetter {
	
	//
	
	/**
	 * RowColumnEval type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter(RowColumnEval.class)
	public static void setRowColumnEval(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, RowColumnEval.compile(attrValue, "0", "0"));
	}
	

	@FormatterAttrSetter(RangeEval.class)
	public static void setRangeEval(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		RangeEval range = RangeEval.create(attrValue);
		setMethod.invoke(formatter, range);
	}
	
	@FormatterAttrSetter(CellType.class)
	public static void setCellType(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, CellType.valueOf(attrValue));
	}
	
	@FormatterAttrSetter(HorizontalAlignment.class)
	public static void setHorizontalAlignment(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, HorizontalAlignment.valueOf(attrValue));
	}
	
	@FormatterAttrSetter(VerticalAlignment.class)
	public static void setVerticalAlignment(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, VerticalAlignment.valueOf(attrValue));
	}
	
	@FormatterAttrSetter(XSSFColor.class)
	public static void setXSSFColor(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, ExcelUtil.getColor(attrValue));
	}
	
	//
	
	@FormatterAttrSetter(ChartTypes.class)
	public static void setChartTypes(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, ChartTypes.valueOf(attrValue));
	}

	@FormatterAttrSetter(AxisPosition.class)
	public static void setAxisPosition(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, AxisPosition.valueOf(attrValue));
	}
	
	@FormatterAttrSetter(DataSourceTypes.class)
	public static void setDataSourceTypes(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, DataSourceTypes.valueOf(attrValue));
	}

	@FormatterAttrSetter(MarkerStyle.class)
	public static void setMarkerStyle(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, MarkerStyle.valueOf(attrValue));
	}
	
	@FormatterAttrSetter(BarDirection.class)
	public static void setBarDirection(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, BarDirection.valueOf(attrValue));
	}
	
	@FormatterAttrSetter(LegendPosition.class)
	public static void setLegendPosition(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, LegendPosition.valueOf(attrValue));
	}

}
