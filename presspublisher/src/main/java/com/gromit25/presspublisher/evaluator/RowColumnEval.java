package com.gromit25.presspublisher.evaluator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

/**
 * row, column 표현식 클래스
 *  -> row, column 형식으로 표현식을 사용할 경우(ex. 엑셀)
 *  
 * @author jmsohn
 */
@Data
public class RowColumnEval {
	
	/** row의 evaluator */
	private Evaluator rowEval;
	/** column의 evaluator */
	private Evaluator columnEval;
	
	/**
	 * 생성자
	 * compile 메소드를 통해서만 생성할 수 있도록 private으로 선언 
	 */
	private RowColumnEval() {
	}
	
	/**
	 * rowColumn 표현식을 compile함 
	 * @param exp rowColumn 표현식
	 * @param rowDefaultExp row의 default 표현식
	 * @param columnDefaultExp column의 default 표현식
	 * @return rowColumn 표현식 compile 결과
	 */
	public static RowColumnEval compile(String exp, String rowDefaultExp, String columnDefaultExp) throws Exception {
		
		// 표현식이 null이거나 공백일 경우,
		// default 0, 0으로 지정
		if(exp == null || exp.trim().equals("") == true) {
			exp = ":";
		}
		
		// "rowExp:columnExp" 형식인지 검사
		Pattern expP = Pattern.compile("[ \\t]*(?<row>[^\\:]+)?[ \\t]*\\:[ \\t]*(?<column>[^\\:]+)?[ \\t]*");
		Matcher expM = expP.matcher(exp);
		
		if(expM.matches() == false) {
			throw new Exception("cell attribute is not valid form:" + exp);
		}
		
		// 반환할 rowColumn evaluator 생성
		RowColumnEval eval = new RowColumnEval();
		
		// rowExp compile 수행
		{
			String rowExp = expM.group("row");
			
			if(rowExp == null || rowExp.trim().equals("") == true) {
				rowExp = rowDefaultExp;
			}
			
			eval.setRowEval(Evaluator.compile(rowExp));
		}

		// columnExp compile 수행
		{
			String columnExp = expM.group("column");
			
			if(columnExp == null || columnExp.trim().equals("") == true) {
				columnExp = columnDefaultExp;
			}
			
			eval.setColumnEval(Evaluator.compile(columnExp));
		}
		
		return eval;
	}
	
	/**
	 * row evaluator를 수행하여 row 값을 계산하여 반환
	 * 
	 * @param values value container
	 * @return row position
	 */
	public int evalRowValue(ValueContainer values) throws Exception {
		
		// 설정된 row script의 값을 계산함
		// 0보다 작으면, 0으로 설정
		int rowValue = this.getRowEval().eval(values, Double.class).intValue();
		if(rowValue < 0) {
			rowValue = 0;
		}
		
		return rowValue;
	}
	
	/**
	 * column evaluator를 수행하여 column 값을 계산하여 반환
	 * 
	 * @param values value container
	 * @return column position
	 */
	public int evalColumnValue(ValueContainer values) throws Exception {

		// 설정된 column script의 값을 계산함
		// 0보다 작으면, 0으로 설정
		int columnValue = this.getColumnEval().eval(values, Double.class).intValue();
		if(columnValue < 0) {
			columnValue = 0;
		}
		
		return columnValue;
	}

}
