package com.gromit25.presspublisher.formatter;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author jmsohn
 */
class Reflections {
	
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	Vector<Class<?>> clazzes;
	
	/**
	 * 
	 * @param packName
	 */
	@SuppressWarnings("unchecked")
	Reflections(String packName) throws Exception {
		
		// ClassLoader의 classes 멤버 변수에 저장되어 있는  
		// class 목록을 가져옴
		// 일반적인 방법으로 접근이 불가하기 때문에,
		// reflection을 이용하여 접근함
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?> loaderClazz = loader.getClass();
        while (loaderClazz != java.lang.ClassLoader.class) {
        	loaderClazz = loaderClazz.getSuperclass();
        }
        
        // package 이하의 모든 클래스들을 로딩함
		Enumeration<URL> resources = loader.getResources(packName.replace('.', '/'));
		
		while(true == resources.hasMoreElements()) {
			
			URL resource = resources.nextElement();
			File path = new File(resource.getFile());
			
			loadAllClasses(loader, packName, path);
		}

        // ClassLoader의 classes 멤버변수를
        // 접근 가능한 상태로 변경함
        Field clazzesField = loaderClazz.getDeclaredField("classes");
        clazzesField.setAccessible(true);
        
        // ClassLoader의 classes 멤버변수에 접근하여
        // Class 목록에서 Package의 Class를 추출하여 설정함
        Vector<Class<?>> packClazzes = new Vector<Class<?>>();
        
        // class 목록을 clone으로 복제함
        // -> class loader에 의해 class 목록 변경이 발생하면,
        //    동시성 오류가 발생함
        Vector<Class<?>> allClazzes = (Vector<Class<?>>)((Vector<Class<?>>)clazzesField.get(loader)).clone();
        for(Class<?> clazz: allClazzes) {
        	
        	String clazzName = clazz.getName();
        	if(null != clazzName && true == clazzName.startsWith(packName)) {
        		packClazzes.add(clazz);
        	}
        }
                
		this.setClazzes(packClazzes);
	}
	
	/**
	 * 특정 패키지 이하의 모든 클래스들을 강제로 로딩 시킴
	 * @param loader 클래스 로딩시 사용할 class loader
	 * @param packName 로드할 패키지의 이름
	 * @param path 로드할 클래스의 파일
	 */
	private static void loadAllClasses(ClassLoader loader, String packName, File path) throws Exception {
		
		if(null == packName) {
			throw new Exception("packName param is null.");
		}
		
		if(null == path) {
			throw new Exception("path param is null.");
		}
		
		if(false == path.canRead()) {
			throw new Exception(path.getAbsolutePath() + " is not readable.");
		}
		
		if(true == path.isDirectory()) {
			
			// 현재 파일이 디렉토리이면, 
			// 파일 목록을 가져옴
			// 파일 목록은 디렉토리와 파일명에 "$"가 포함되지 않은 .class 파일 목록임
			// $가 포함된 경우, 다른 클래스 파일에 포함된 경우이기 때문에
			// 포함하는 클래스를 로딩하면 자동로딩 되기 때문에 필요없음
			File[] files = path.listFiles(new FileFilter() {
				@Override
				public boolean accept(File path) {
					if(true == path.isDirectory()) {
						return true;
					}
					if(true == path.getName().endsWith(".class")
						&& false == path.getName().contains("$")) {
						return true;
					}
					return false;
				}
			});
			
			// 로딩할 packName을 새로 만들어,
			// loadAllClasses를 재귀호출함
			for(File file: files) {
				String newPackName = packName + "." + file.getName().replaceAll("\\.class$", "");
				loadAllClasses(loader, newPackName, file);
			}
			
		} else if(true == path.isFile()){
			
			// 파일일 경우
			// packName으로 클래스를 로딩함
			loader.loadClass(packName);
			
		} else {
			
			// 파일도 디렉토리도 아닌 그 무엇?
			throw new Exception(path.getAbsolutePath() + " is not File or Directory");
		}
	}
	
	/**
	 * 
	 * @param annotationClazz
	 * @return
	 */
	Stream<Class<?>> getTypesAnnotatedWith(Class<?> annotationClazz) throws Exception {
		
		ArrayList<Class<?>> clazzAnnotated = new ArrayList<Class<?>>();
		
		for(Class<?> clazz: this.getClazzes()) {
			
			Annotation[] annotations = clazz.getDeclaredAnnotations();
			
			for(Annotation annotation: annotations) {
				if(annotation.annotationType() == annotationClazz) {
					clazzAnnotated.add(clazz);
					break;
				}
			}
		}
		
		return clazzAnnotated.stream();
	}

}
