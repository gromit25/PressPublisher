package com.gromit25.presspublisher.formatter;

import lombok.Data;

/**
 * 
 * @author jmsohn
 */
@Data
public class Loc implements Comparable<Loc>{
	
	private int lineNum;
	private int columnNum;
	
	public Loc(int lineNum, int columnNum) {
		this.setLineNum(lineNum);
		this.setColumnNum(columnNum);
	}

	@Override
	public int compareTo(Loc o) {
		
		if(null == o) {
			return 1;
		}
		
		if(this.getLineNum() > o.getLineNum()) {
			return 1;
		}
		
		if(this.getLineNum() == o.getLineNum()) {
			if(this.getColumnNum() > o.getColumnNum()) {
				return 1;
			} else if(this.getColumnNum() == o.getColumnNum()) {
				return 0;
			}
		}
		
		return -1;
	}
}