package com.gromit25.presspublisher.evaluator;

import java.lang.reflect.Method;

/**
 * Evaluator Utility
 * 
 * @author jmsohn
 */
class EvalUtil {
	
	/**
	 * java의 모든 숫자 형태를 double 형태로 반환
	 * Evaluator에서는 모두 double로 계산 수행
	 * 
	 * 일반적인 객체일 경우, NaN(Not a Number) 반환
	 * 
	 * @param value type 변환할 숫자
	 * @return double로 변환된 숫자
	 */
	static double getNumber(ValueInstance value) throws Exception {
		
		if(value == null || value.getValue() == null) {
			return Double.NaN;
		}
		
		Object obj = value.getValue();
		
		if(obj instanceof Float) {
			return ((Float)obj).doubleValue();
		} else if (obj instanceof Double) {
			return (double)obj;
		} else if (obj instanceof Integer) {
			return ((Integer)obj).doubleValue();
		} else if (obj instanceof Long) {
			return ((Long)obj).doubleValue();
		} else {
			return Double.NaN;
		}
	}
	
	/**
	 * 객체를 boolean 형태로 변환
	 * 만일, 객체가 boolean type이 아니면 예외 발생
	 * @param obj 변환할 객체명
	 * @return boolean 값
	 */
	static boolean getBoolean(ValueInstance value) throws Exception {
		
		if(value != null && value.getValue() != null) {
			throw new Exception("value is null.");
		}
		
		Object obj = value.getValue();
		
		if(obj.getClass() == Boolean.class || obj.getClass() == boolean.class) {
			return (boolean)obj;
		} else {
			throw new Exception("not Boolean type:" + obj.getClass().getName());
		}
	}
	
	/**
	 * Value 객체를 ValueInstance객체로 casting
	 * 
	 * @param value casting할 Value 객체
	 * @return ValueInstance로 casting된 Value 객체
	 */
	static ValueInstance castValueInstance(Value value) throws Exception {
		if(value instanceof ValueInstance == false) {
			throw new Exception("value is not ValueInstance type.");
		}
		
		return (ValueInstance)value;
	}
	
	/**
	 * 특정 클래스(type)에 메소드명(methodName)과 매개변수 목록(paramObjects)에
	 * 적합한 메소드를 검색하여 반환 
	 * 
	 * @param type 메소드를 찾을 클래스
	 * @param methodName 메소드 명
	 * @param paramObjects 메소드의 매개변수 객체 목록(매개변수가 없을 경우에도 빈배열로 입력해야함)
	 * @return 검색된 메소드
	 */
	static Method getMethod(Class<?> type, String methodName, Object[] paramObjects) throws Exception {
		
		// 입력값 검증
		if(type == null || methodName == null || paramObjects == null) {
			return null;
		}
		
		// 클래스에 모든 메소드를 가져옴 
		// 메소드 별로 입력될 매개변수의 적합여부를 확인하여,
		// 적합한 메소드가 발견되면 반환
		Method[] methods = type.getMethods();
		Method matchedMethod = null;
		
		for(Method method : methods) {
			
			// 메소드 명이 일치하는 경우만
			// 매개변수 검사하여 일치 여부 검증
			if(method.getName().equals(methodName) == true) {
				
				// 현재 메소드의 매개변수 목록을 가져옴
				Class<?>[] methodParamTypes = method.getParameterTypes();
				
				// 현 메소드 매개변수 타입개수와 찾으려는 매개변수 개수가 다르면 다음 메소드로 넘어감
				if(methodParamTypes.length != paramObjects.length) {
					continue;
				}
				
				// 매개변수 형식에 따라 변환된 매개변수 객체를 임시 저장
				// 매치된 메소드로 확인되면,
				// double 형식으로 입력되어 있는 매개변수를
				// 임시 저장된 객체로 치환 위한 용도
				// * 중간에 변경하면 매치되지 않은 메소드로 인해 값이 변경될 수 있어,
				//   마킹만 하고 있다가 최종 매치 확인이 완료되면 설정 작업을 함
				Object[] transformValues = new Object[paramObjects.length];
				
				// 각 매개변수의 종류 일치 여부 확인
				boolean isMatchedMethod = true;
				
				for(int index = 0; index < methodParamTypes.length; index++) {
					
					Class<?> methodParamType = methodParamTypes[index];
					Object paramObject = paramObjects[index];
					
					// 들어갈 매개변수가 null 인지 검사
					// 들어갈 매개변수가 null 이면,
					//    타입에 상관 없이 넣을 수 있기 때문에 다음으로 진행
					if(paramObject != null) {
						if(paramObject instanceof Double) {
							
							// 현재 파라미터가 숫자형식(Double)인 경우,
							// 메소드의 매개변수가 Float, Integer, Long, Double 중
							// 아무거나 하나 맞으면 해당 형식으로 변경하여 저장함
							Double paramDouble = (Double)paramObject;
							
							if(float.class.equals(methodParamType) == true
								|| methodParamType.isAssignableFrom(Float.class) == true) {
								transformValues[index] = paramDouble.floatValue();
							} else if(int.class.equals(methodParamType) == true
								|| methodParamType.isAssignableFrom(Integer.class) == true) {
								transformValues[index] = paramDouble.intValue();
							} else if(long.class.equals(methodParamType) == true
								|| methodParamType.isAssignableFrom(Long.class) == true) {
								transformValues[index] = paramDouble.longValue();
							} else if(double.class.equals(methodParamType) == true
								|| methodParamType.isAssignableFrom(Double.class) == true) {
								transformValues[index] = paramDouble;
							} else {
								// 메소드의 매개변수가 숫자타입이 아닐 경우
								// 다음 메소드로 넘어감
								isMatchedMethod = false;
								break;
							}

						} else {
							
							// 숫자이외의 매개변수가 오는 경우
							// 입력될 매개변수가 메소드의 매개변수 형식에 대입될 수 없으면,
							// 중단하고 다음 메소드로 넘어감
							isMatchedMethod = methodParamType.isAssignableFrom(paramObject.getClass());
							if(isMatchedMethod == true) {
								break;
							}
						}
					} // end of if(paramObject != null)
				} // end of for(methodParamTypes)
				
				// 매치된 메소드이면, 
				// 입력될 매개변수 치환 작업(숫자타입)을 수행 후,
				// 반환함
				if(isMatchedMethod == true) {
					
					// 입력될 매개변수 치환 작업
					for(int index = 0; index < paramObjects.length; index++) {
						if(transformValues[index] != null) {
							paramObjects[index] = transformValues[index];
						}
					}
					
					matchedMethod = method;
					break;
				}
			} // end of if(method.getName().equals(methodName) == true)
		} // end of for(methods)
		
		return matchedMethod;
	}
	
	/**
	 * 
	 * @param returnVal
	 * @param type
	 * @return
	 */
	public static boolean isPrimitiveType(Object returnVal, Class<?> type) {
		
		if(type == double.class && returnVal instanceof Double) {
			return true;
		} else if(type == float.class && returnVal instanceof Float) {
			return true;
		} else if(type == int.class && returnVal instanceof Integer) {
			return true;
		} else if(type == long.class && returnVal instanceof Long) {
			return true;
		} else if(type == short.class && returnVal instanceof Short) {
			return true;
		} else if(type == boolean.class && returnVal instanceof Boolean) {
			return true;
		} else if(type == byte.class && returnVal instanceof Byte) {
			return true;
		} else if(type == char.class && returnVal instanceof Character) {
			return true;
		}
		
		return false;
	}

}
