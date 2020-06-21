package com.gromit25.presspublisher.evaluator;

import java.io.Serializable;
import java.util.Hashtable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * value container
 * formatter와 evaluator에서 사용하는
 * 변수 저장용 container
 * 일종의 jvm heap memory 역할
 * (key, value 객체) 형식으로 저장
 * 
 * @author jmsohn
 */
public class ValueContainer implements Serializable {
	private static final long serialVersionUID = -900213143586427109L;
	
	@Getter(value=AccessLevel.PRIVATE)
	@Setter(value=AccessLevel.PRIVATE)
	private Hashtable<String, Value> values = new Hashtable<String, Value>();

	/**
	 * key의 value 객체를 요청타입으로 casting하여 반환
	 * 
	 * @param key value에 맵핑된 이름
	 * @param type castring할 class
	 * @return casting된 value 객체
	 */
	public <T> T get(String key, Class<T> type) {
		
		if(key == null || type==null) {
			return null;
		}
		
		Object returnObj = this.getValues().get(key);
		
		if(Value.class.isAssignableFrom(type) == true) {
			
			if(returnObj != null && type.isInstance(returnObj) == true) {
				return type.cast(returnObj);
			}
			
		} else {
			
			if(returnObj instanceof ValueInstance) {
				
				Object value = ((ValueInstance)returnObj).getValue();
				
				if(EvalUtil.isPrimitiveType(value, type) == true) {
					
					@SuppressWarnings("unchecked")
					T primitiveValue = (T)value;
					return primitiveValue;
					
				} else {
					
					if(value != null && type.isInstance(value)) {
						return type.cast(value);
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Value Container에 value 객체 추가
	 *  
	 * @param key 추가할 key 값
	 * @param type value 객체의 type(class)
	 * @param value 추가할 value 객체
	 */
	public void put(String key, Class<?> type, Object value) throws Exception {
		
		if(key == null) {
			throw new Exception("The key value to put into the value container is null.");
		}
		
		// key 값이 없거나,
		// 입력된 값이 ValueInstance(Instance Type) 이면,
		// value 객체를 추가함
		if(this.getValues().containsKey(key) == false || this.getValues().get(key) instanceof ValueInstance) {
			this.getValues().put(key, new ValueInstance(type, value));
		} else {
			throw new Exception("A value(" + key + ") that is not of type ValueInstance already exists in the Value Container.");
		}
	}
	
	/**
	 * Value Container에 value 객체 추가
	 * 
	 * @param key 추가할 key 값
	 * @param value 추가할 value 객체 (만일, value 값이 null이면, type(class)는 Object class 설정함)
	 */
	public void put(String key, Object value) throws Exception {
		
		if(value != null) {
			this.put(key, value.getClass(), value);
		} else {
			this.put(key, Object.class, value);
		}
	}
	
	/**
	 * Value Container에 static 클래스를 추가함
	 * 
	 * @param alias expression에서 사용할 별칭(alias)
	 * @param type 추가할 static 클래스
	 */
	public void importStaticClass(String alias, Class<?> type) throws Exception {
		
		if(alias == null) {
			throw new Exception("The alias(key) value to put into the value container is null.");
		}
		
		if(this.getValues().containsKey(alias) == true) {
			throw new Exception("The alias(key, " + alias + ") value is already exists.");
		}
		
		if(type == null) {
			throw new Exception("The type value to put into the value container is null.");
		}
		
		this.getValues().put(alias, new ValueStaticClass(type));
	}
	
	/**
	 * Value Container에 static 클래스를 추가함
	 * 표현식에서 사용할 alias는 static 클래스의 이름
	 * 
	 * @param type 추가할 static 클래스
	 */
	public void importStaticClass(Class<?> type) throws Exception {
		this.importStaticClass(type.getSimpleName(), type);
	}
	
	/**
	 * 
	 * @param key
	 */
	public void remove(String key) {
		this.getValues().remove(key);
	}
}
