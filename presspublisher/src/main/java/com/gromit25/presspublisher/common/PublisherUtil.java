package com.gromit25.presspublisher.common;

/**
 * Publisher Utility
 * 
 * @author jmsohn
 */
public class PublisherUtil {
	
	/**
	 * print tag에 설정된 형식으로 문자열을 만듦
	 * -> String.format의 %s를 사용하지 않는 이유는
	 *    한글의 경우 2byte이지만 1문자로 계산되어
	 *    실제 원하는 길이가 안맞는 문제 발생
	 *    ex) "한test", "1test" 문자열에 6칸으로 요청하면
	 *        -> "한test ","1test " 이렇게 출력되어 console 화면에 표현되는 길이가 맞지 않음
	 * -> length 가 음수(-)일 경우, 원래 스트링을 반환
	 * 
	 * @param target 길이를 맞출 문자열
	 * @param length 맞출 길이
	 * @return 길이(PrintFormatter.length)가 맞추어진 문자열
	 */
	public static String makeString(String target, int length) {
		
		// taget 문자열의 byte 길이를 가져옴
		int targetLength = 0;
		if(target != null) {
			targetLength = target.getBytes().length;
		}
		
		// 설정해야할 길이가 0 보다 작거나,
		// target 문자열이 더 길 경우,
		// target문자열을 리턴함
		if(length <= 0 || length <= targetLength) {
			return target;
		}
		
		// 추가해야할 space개수를 계산함
		int countOfSpace = length;
		if(target != null) {
			countOfSpace -= targetLength;
			return target + String.format("%" + countOfSpace + "s", "");
		} else {
			return String.format("%" + countOfSpace + "s", "");
		}
		
	}
	
	/**
	 * type casting method
	 * warning 발생없이 type casting 수행  
	 * @param obj casting할 object
	 * @param type casting할 타입
	 * @return castring된 객체
	 */
	public static <T> T cast(Object obj, Class<T> type) throws Exception {
		
		// 입력값 체크
		if(obj == null) {
			throw new Exception("obj is null");
		}
		
		if(type == null) {
			throw new Exception("type is null");
		}
		
		// object가 type 객체이면,
		// casting 하여 반환함
		if(type.isInstance(obj) == true) {
			return type.cast(obj);
		} else {
			throw new Exception("obj(" + obj.getClass().getName() + ") is not " + type.toString() + " type");
		}
	}

}
