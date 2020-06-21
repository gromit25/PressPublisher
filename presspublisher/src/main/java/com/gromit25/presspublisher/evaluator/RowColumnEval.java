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

}
