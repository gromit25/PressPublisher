package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.EvalCmd;
import com.gromit25.presspublisher.evaluator.Opcode;

/**
 * 하위표현식 파싱하여 스택 명령어 생성
 * -> 하위 표현식이란,
 *    a.method1() 일 경우
 *    a instance의 method1() 부분임
 * 
 * @author jmsohn
 */
class SubExpressionParser {
	
	/**
	 * 하위표현식 파싱을 위한 상태기계 값
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * 
	 * @author jmsohn
	 */
	enum SubExpressionStatus {
		START
		, NAME_START
		, NAME
		, PARAM_SEPARATOR
		, PARAM_END
		, METHOD_END
		, FAIL
	}
	
	/**
	 * 하위표현식을 파싱하여, 스택 명령어 형태로 변환 후 스택명령어 목록에 추가함 
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
		SubExpressionStatus status = SubExpressionStatus.START;
		// 파싱하여 변환된 결과를 저장할 스트링
		StringBuilder parsedName = new StringBuilder("");
		// 메소드 호출일 경우, 파라미터의 수
		int paramCnt = 0;
		
		// 한문자씩 읽어옴
		int read = reader.read();
		boolean hasMoreChar = (read != -1)?true:false;
		
		// 읽기가 완료 될때까지 읽어 들임
		while(hasMoreChar) {
			
			char ch = (char)read;
			
			// 상태전환 수행
			switch(status) {
			case START:
				
				if(ch == '.') {
					status = SubExpressionStatus.NAME_START;
				} else {
					status = SubExpressionStatus.FAIL;
				}
				break;
				
			case NAME_START:
				
				if(ParserUtil.isNameChar(ch) == true) {
					parsedName.append(ch);
					status = SubExpressionStatus.NAME;
				} else {
					status = SubExpressionStatus.FAIL;
				}
				break;
				
			case NAME:
				
				if(ParserUtil.isNameChar(ch) == true) {
					// name에 추가
					parsedName.append(ch);
				} else if(ch == '.') {
					// Name이 DOT(.)으로 종료되면,
					// Load Attribute 명령어를 만들어 넣음
					EvalCmd loadAttr = new EvalCmd(
						Opcode.LOAD_ATTR, 1, parsedName.toString());
					cmds.add(loadAttr);
					// 읽었던 마지막 문자를 다시 스트림에 넣고
					// 다음 하위표현식 파서로 이동
					ParserUtil.unread(reader, read);
					SubExpressionParser.compile(reader, cmds);
					// attribute name 정상수행시 여기서 리턴됨
					return;
				} else if(", \t)".indexOf(ch) >= 0) {
					// Name이 종료되면,
					// Load Attribute 명령어를 만들어 넣음
					EvalCmd loadAttr = new EvalCmd(
						Opcode.LOAD_ATTR, 1, parsedName.toString());
					cmds.add(loadAttr);
					// 읽었던 마지막 문자를 다시 스트림에 넣음
					ParserUtil.unread(reader, read);
					// attribute name 정상수행시 여기서 리턴됨
					return;
				} else if(ch == '(') {
					status = SubExpressionStatus.PARAM_SEPARATOR;
				} else {
					status = SubExpressionStatus.FAIL;
				}
				break;
				
			case PARAM_SEPARATOR:
				
				if(ch == ')') {
					// 메소드 종료시.
					// invoke virtual 명령어 추가 
					EvalCmd invokeVirtual = new EvalCmd(
							Opcode.INVOKE_METHOD, paramCnt + 1, parsedName.toString());
					cmds.add(invokeVirtual);
					status = SubExpressionStatus.METHOD_END;
				} else {
					// 변수
					reader.unread(ch);
					BooleanParser.compile(reader, cmds);
					status = SubExpressionStatus.PARAM_END;
				}
				break;
				
			case PARAM_END:
				
				// 파라미터 하나 종료시, count를 하나 올림
				paramCnt++;
				
				if(ch == ',') {
					status = SubExpressionStatus.PARAM_SEPARATOR;
				} else if(ch == ')') {
					// 메소드 종료시.
					// invoke virtual 명령어 추가 
					EvalCmd invokeVirtual = new EvalCmd(
							Opcode.INVOKE_METHOD, paramCnt + 1, parsedName.toString());
					cmds.add(invokeVirtual);
					status = SubExpressionStatus.METHOD_END;
				} else if(ParserUtil.isSpaceChar(ch) == true) {
					status = SubExpressionStatus.PARAM_END;
				} else {
					status = SubExpressionStatus.FAIL;
				}
				break;
			
			case METHOD_END:
				
				ParserUtil.unread(reader, read);
				if(ch == '.') {
					SubExpressionParser.compile(reader, cmds);
				}
				return;
				
			default:
			}
			
			// 파싱 실패시
			if(status == SubExpressionStatus.FAIL) {
				throw new Exception("Unexpected Char at " + SubExpressionParser.class + ":" + read);
			}

			// 다음 한문자씩 읽음
			// 마지막 종료시, 종료문자(-1)까지 읽음 
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
