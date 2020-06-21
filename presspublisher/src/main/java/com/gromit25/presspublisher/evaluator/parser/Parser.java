package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.EvalCmd;

/**
 * 표현식 파싱
 * @author jmsohn
 */
public class Parser {
	
	/** PushbackReader에서 되돌릴 수 있는 Buffer의 크기 */
	static int BUFFER_SIZE = 1024 * 1024;
	
	/**
	 * 표현식을 파싱하여 스택 명령어 목록에 명령어 추가 
	 * @param reader 파싱할 표현식
	 * @param cmds 스택 명령어 목록 - 파싱이 완료되면 이 객체에 스택 명령어를 추가함
	 */
	public static void compile(Reader reader, ArrayList<EvalCmd> cmds) throws Exception {
		
		PushbackReader pushbackReader = new PushbackReader(reader, BUFFER_SIZE);
		//BooleanParser.compile(pushbackReader, cmds);
		AssignParser.compile(pushbackReader, cmds);
		
	}

}
