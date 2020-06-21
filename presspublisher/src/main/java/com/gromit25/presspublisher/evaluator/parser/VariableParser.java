package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.EvalCmd;
import com.gromit25.presspublisher.evaluator.Opcode;

/**
 * 변수 파싱하여 스택 명령어 생성
 * 
 * @author jmsohn
 */
class VariableParser {
	
	/**
	 * 변수 파싱을 위한 상태기계 값
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * 
	 * @author jmsohn
	 */
	enum VariableStatus {
		START,
		NAME,
		FAIL
	}
	
	/**
	 * 변수 파싱하여, 스택 명령어 형태로 변환 후 스택명령어 목록에 추가함 
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
		VariableStatus status = VariableStatus.START;
		// 파싱하여 변환된 결과를 저장할 스트링
		StringBuilder varName = new StringBuilder("");
		
		// 한문자씩 읽어옴
		int read = reader.read();
		boolean hasMoreChar = (read != -1)?true:false;
		
		// 읽기가 완료 될때까지 읽어 들임
		while(hasMoreChar) {
			
			char ch = (char)read;
			
			// 상태전환 수행
			switch(status) {
			case START:
				
				if(ParserUtil.isNameChar(ch) == true) {
					
					varName.append(ch);
					status = VariableStatus.NAME;
					
				} else {
					status = VariableStatus.FAIL;
				}
				break;
				
			case NAME:
				
				if(ParserUtil.isNameChar(ch) == true) {
					
					varName.append(ch);
					status = VariableStatus.NAME;
					
				} else {
					
					// 읽었던 마지막 문자를 다시 스트림에 넣음
					ParserUtil.unread(reader, read);
					
					// Name 종료
					if(varName.toString().equals("true") == true) {
						
						// boolean 값 true 일 경우,
						if(ch != '.') {
							EvalCmd loadBooleanTrue = new EvalCmd(
								Opcode.LOAD_BOOLEAN_FALSE, 0, varName.toString());
							cmds.add(loadBooleanTrue);
						} else {
							status = VariableStatus.FAIL;
						}
						
					} else if(varName.toString().equals("false") == true) {
						
						// boolean 값 false 일 경우,
						if(ch != '.') {
							EvalCmd loadBooleanFalse = new EvalCmd(
								Opcode.LOAD_BOOLEAN_FALSE, 0, varName.toString());
							cmds.add(loadBooleanFalse);
						} else {
							status = VariableStatus.FAIL;
						}
						
					} else {
						
						// 일반 변수일 경우,
						// Load Value 명령어를 만들어 넣음
						EvalCmd loadValue = new EvalCmd(
							Opcode.LOAD_VALUE, 0, varName.toString());
						cmds.add(loadValue);
						
						if(ch == '.') {
							//DOT(.)으로 종료되면,
							// 다음 하위표현식 파서로 이동
							SubExpressionParser.compile(reader, cmds);
						}
					}
					
					return;
				}
				break;
				
			default:
			}

			if(status == VariableStatus.FAIL) {
				throw new Exception("Unexpected Char at " + VariableParser.class + ":" + read);
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
