package com.gromit25.presspublisher.formatter.excel;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * 
 * @author jmsohn
 */
public enum DataSourceTypes {
	
	NUMBER {
		@Override
		public XDDFDataSource<?> createDataSource(XSSFSheet worksheet, CellRangeAddress range) {
			return XDDFDataSourcesFactory.fromNumericCellRange(worksheet, range);
		}
	},
	STRING {
		@Override
		public XDDFDataSource<?> createDataSource(XSSFSheet worksheet, CellRangeAddress range) {
			return XDDFDataSourcesFactory.fromStringCellRange(worksheet, range);
		}
	};
	
	/**
	 * 
	 * @param worksheet
	 * @param range
	 * @return
	 */
	public abstract XDDFDataSource<?> createDataSource(XSSFSheet worksheet, CellRangeAddress range);
}
