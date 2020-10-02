package com.gromit25.presspublisher.formatter.excel;

import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFChartAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;

/**
 * 
 * @author jmsohn
 */
public enum AxisTypes {
	CATEGORY {
		@Override
		public XDDFChartAxis createAxis(XSSFChart chart, AxisPosition position) {
			return chart.createCategoryAxis(position);
		}
	},
	VALUE {
		@Override
		public XDDFChartAxis createAxis(XSSFChart chart, AxisPosition position) {
			return chart.createValueAxis(position);
		}
	},
	DATE {
		@Override
		public XDDFChartAxis createAxis(XSSFChart chart, AxisPosition position) {
			return chart.createDateAxis(position);
		}
	};
	
	/**
	 * 
	 * @param chart
	 * @param position
	 * @return
	 */
	public abstract XDDFChartAxis createAxis(XSSFChart chart, AxisPosition position);
}
