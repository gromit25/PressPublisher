package com.gromit25.presspublisher.formatter.excel.chart;

import java.lang.reflect.Method;

import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;

import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttrSetter;
import com.gromit25.presspublisher.formatter.FormatterAttrSetterClass;

@FormatterAttrSetterClass
public class ExcelChartAttrSetter {
	
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
