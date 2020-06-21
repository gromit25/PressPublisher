package com.gromit25.presspublisher.formatter.excel;

import java.lang.reflect.Method;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttrSetter;
import com.gromit25.presspublisher.formatter.FormatterAttrSetterClass;
import com.gromit25.presspublisher.formatter.FormatterException;

@FormatterAttrSetterClass
public class ExcelAttrSetter {
	
	@FormatterAttrSetter(String.class)
	public static void setString(Formatter formatter, Method setMethod, String attrValue) throws FormatterException {
		try {
			setMethod.invoke(formatter, attrValue);
		} catch(Exception ex) {
			throw new FormatterException(formatter, ex);
		}
	}
	
	@FormatterAttrSetter(HorizontalAlignment.class)
	public static void setHorizontalAlignment(Formatter formatter, Method setMethod, String attrValue) throws FormatterException {
		try {
			setMethod.invoke(formatter, HorizontalAlignment.valueOf(attrValue));
		} catch(Exception ex) {
			throw new FormatterException(formatter, ex);
		}
	}
	
	@FormatterAttrSetter(VerticalAlignment.class)
	public static void setVerticalAlignment(Formatter formatter, Method setMethod, String attrValue) throws FormatterException {
		try {
			setMethod.invoke(formatter, VerticalAlignment.valueOf(attrValue));
		} catch(Exception ex) {
			throw new FormatterException(formatter, ex);
		}
	}
	
	@FormatterAttrSetter(XSSFColor.class)
	public static void setXSSFColor(Formatter formatter, Method setMethod, String attrValue) throws FormatterException {
		try {
			setMethod.invoke(formatter, ExcelUtil.getColor(attrValue));
		} catch(Exception ex) {
			throw new FormatterException(formatter, ex);
		}
	}

}
