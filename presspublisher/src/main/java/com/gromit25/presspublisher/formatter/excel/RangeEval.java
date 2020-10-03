package com.gromit25.presspublisher.formatter.excel;

import com.gromit25.presspublisher.evaluator.ValueContainer;

import lombok.Getter;
import lombok.Setter;

/**
 * 셀의 범위 지정용 객체
 * 시작셀과 종료셀의 위치를 지정함
 * @author jmsohn
 */
public class RangeEval {
	
	/**
	 * 셀의 범위 중 시작셀의 위치
	 */
	@Getter
	@Setter
	private RowColumnEval start;
	
	/**
	 * 셀의 범위 중 종료셀의 위치
	 */
	@Getter
	@Setter
	private RowColumnEval end;
	
	/**
	 * 생성자
	 * create 메소드를 통해서만 생성 가능하도록 private 으로 선언
	 * 외부에서 생성하지 못하도록함
	 */
	private RangeEval() {
	}
	
	/**
	 * range 객체를 생성 
	 * @param range range 객체의 범위 문자열(ex. 0:0~2:3)
	 * @return 생성된 range 객체
	 */
	public static RangeEval create(String range) throws Exception {
		
		// 입력값 검증
		if(null == range || true == range.trim().equals("")) {
			throw new Exception("range value is null or blank");
		}
		
		// 범위의 형식을 시작셀 위치와 종료셀 위치를 분리함
		// ex) 0:0~2:3 -> 시작셀 위치 0:0, 종료셀 위치 2:3
		String[] splitedRange = range.split("~");
		if(2 != splitedRange.length) {
			throw new Exception("range value is invalid:" + range);
		}
		
		// 시작셀의 위치와 종료셀의 위치를 지정함
		RowColumnEval start = RowColumnEval.compile(splitedRange[0], "0", "0");
		RowColumnEval end = RowColumnEval.compile(splitedRange[1], "0", "0");
		
		return RangeEval.create(start, end);
	}
	
	/**
	 * range 객체를 생성
	 * @param start 시작 셀의 위치
	 * @param end 종료 셀의 위치
	 * @return 생성된 range 객체
	 */
	public static RangeEval create(RowColumnEval start, RowColumnEval end) throws Exception {
		
		// 입력값 검증
		if(null == start) {
			throw new Exception("start is null.");
		}
		
		if(null == end) {
			throw new Exception("end is null.");
		}
		
		// range 객체를 생성하여 시작 셀(start)과 종료 셀(end)을 설정
		RangeEval range = new RangeEval();
		range.setStart(start);
		range.setEnd(end);
		
		return range;
	}
	
	/**
	 * 시작 셀의 row 값을 계산 
	 * @param values
	 * @return 시작 셀의 row 값
	 */
	public int evalStartRow(ValueContainer values) throws Exception {
		return this.getStart().evalRowValue(values);
	}

	/**
	 * 시작 셀의 column 값을 계산
	 * @param values
	 * @return 시작 셀의 column 값
	 */
	public int evalStartColumn(ValueContainer values) throws Exception {
		return this.getStart().evalColumnValue(values);
		
	}
	
	/**
	 * 종료 셀의 row 값을 계산
	 * @param values
	 * @return 종료 셀의 row 값
	 */
	public int evalEndRow(ValueContainer values) throws Exception {
		return this.getEnd().evalRowValue(values);
	}
	
	/**
	 * 종료 셀의 column 값을 계산
	 * @param values
	 * @return 종료 셀의 column 값
	 */
	public int evalEndColumn(ValueContainer values) throws Exception {
		return this.getEnd().evalColumnValue(values);
	}
}
