package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.util.ArrayList;

import org.apache.commons.text.StringEscapeUtils;

import com.gromit25.presspublisher.evaluator.EvalCmd;
import com.gromit25.presspublisher.evaluator.Opcode;

/**
 * 문자열 파싱하여 스택 명령어 생성
 * 
 * @author jmsohn
 */
class StringParser {
	
	/**
	 * 문자열 파싱을 위한 상태기계 값
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * 
	 * @author jmsohn
	 */
	enum StringStatus {
		STRING_START
		, IN_STRING
		, ESCAPE
		, FAIL
	}
	
	/**
	 * 문자열형식 파싱하여 수행 명령어 생성
	 * @param reader 입력 스트림 Reader
	 * @param cmds 파싱된 스택명령어를 추가할 명령어 목록 
	 */
	static void compile(PushbackReader reader, ArrayList<EvalCmd> cmds) throws Exception {
		
		// 입력값 검사
		if(reader == null) {
			throw new Exception("reader is null");
		}
		
		if(cmds == null) {
			throw new Exception("commands is null");
		}
		
		// 파서 상태
		StringStatus status = StringStatus.STRING_START;
		// 파싱하여 변환된 결과를 저장할 스트링
		StringBuilder parsedString = new StringBuilder("");
		
		// 한문자씩 읽어옴
		int read = reader.read();
		boolean hasMoreChar = (read != -1)?true:false;
		
		// 읽기가 완료 될때까지 읽어 들임
		while(hasMoreChar) {
			
			char ch = (char)read;
			
			// 상태전환 수행
			switch(status) {
			case STRING_START:
				if(ch == '\'') {
					status = StringStatus.IN_STRING;
				} else {
					status = StringStatus.FAIL;
				}
				break;
				
			case IN_STRING:
				if(ch == '\'') {
					// String이 종료되면,
					// stack에 parsing된 문자열을 넣는다.
					// 정상수행시 여기서 리턴됨
					String unescapedString = StringEscapeUtils.unescapeJava(parsedString.toString());
					EvalCmd loadConst = new EvalCmd(
							Opcode.LOAD_CONST, 0, unescapedString);

					cmds.add(loadConst);
					return;
				} else if(ch == '\\') {
					parsedString.append(ch);
					status = StringStatus.ESCAPE;
				} else if(read == -1){
					status = StringStatus.FAIL;
				} else {
					parsedString.append(ch);
				}
				break;
				
			case ESCAPE:
				if(read == -1) {
					status = StringStatus.FAIL;
				} else {
					parsedString.append(ch);
					status = StringStatus.IN_STRING;
				}
				break;
				
			default:
			}
			
			if(status == StringStatus.FAIL) {
				throw new Exception("Unexpected Char at " + StringParser.class + ":" + read);
			}

			// 한문자씩 읽음
			// 마지막 종료시, 종료문자(-1)까지 
			// 상태변환에서 사용하기 위해 종료 문자까지 읽어서 넘기고 종료함
			if(read == -1) {
				// 읽기 완료시 종료함
				hasMoreChar = false;
			} else {
				read = reader.read();
				hasMoreChar = true;
			}
		}
	}
}
