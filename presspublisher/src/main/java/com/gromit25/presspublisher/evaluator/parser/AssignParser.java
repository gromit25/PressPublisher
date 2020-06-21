package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.nio.CharBuffer;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.EvalCmd;
import com.gromit25.presspublisher.evaluator.Opcode;

/**
 * 할당 연산자를 파싱하여 스택 명령어 생성
 * 
 * @author jmsohn
 */
public class AssignParser {
	
	/**
	 * 할당 연산자 파싱을 위한 상태기계 값
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * 
	 * @author jmsohn
	 */
	enum AssignStatus {
		START
		, VAR_NAME
		, ASSIGN_OPERATOR
		, ASSIGN_OPERATOR_END
		, NOT_ASSIGN_OPERATOR
		, FAIL;
	}
	
	/**
	 * 할당 연산자를 파싱하여, 스택 명령어 형태로 변환 후 스택명령어 목록에 추가함 
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
		AssignStatus status = AssignStatus.START;
		// 만일 할당연산자가 아닐 경우,
		// unread를 수행할 char buffer
		CharBuffer buffer = CharBuffer.allocate(Parser.BUFFER_SIZE);
		// 스택의 결과를 할당할 변수명
		StringBuilder varName = new StringBuilder("");
		
		// 한문자씩 읽어옴
		int read = reader.read();
		boolean hasMoreChar = (read != -1)?true:false;
		
		// 읽기가 완료 될때까지 읽어 들임
		while(hasMoreChar == true) {
			
			char ch = (char)read;
			if(read != -1) {
				buffer.append(ch);
			}
			
			// 상태전환 수행
			switch(status) {
			case START:
				
				if(ParserUtil.isNameChar(ch) == true) {
					
					varName.append(ch);
					status = AssignStatus.VAR_NAME;
					
				} else {

					status = AssignStatus.NOT_ASSIGN_OPERATOR;
				}
				break;
				
			case VAR_NAME:
				
				if(ParserUtil.isNameChar(ch) == true) {
					
					varName.append(ch);
					
				} else {
					
					if(read == -1) {
						
						status = AssignStatus.NOT_ASSIGN_OPERATOR;
						
					} else {
						// 읽었던 마지막 문자를 다시 스트림에 넣음
						ParserUtil.unread(reader, read);
						buffer.position(buffer.position() - 1);
						
						///////////////////////
						// Name 종료
						if(varName.toString().equals("true") == true
							|| varName.toString().equals("false") == true) {
							
							status = AssignStatus.FAIL;
						} else {
							status = AssignStatus.ASSIGN_OPERATOR;
						}
					}
					
				}
				break;
				
			case ASSIGN_OPERATOR:
				
				if(ParserUtil.isSpaceChar(ch) == false) {
					if(ch == '=') {
						status = AssignStatus.ASSIGN_OPERATOR_END;
					} else {
						status = AssignStatus.NOT_ASSIGN_OPERATOR;
					}
				}
				
				break;
			case ASSIGN_OPERATOR_END:
				
				if(ch == '=') {

					status = AssignStatus.NOT_ASSIGN_OPERATOR;
				
				} else {
					
					// boolean(true, false) 값에는 할당할 수 없음 -> 오류 발생 시킴
					// boolean 값이 아닌 정상 변수명일 때, 할당 연산 추가함
					if(varName.toString().equals("true") == true
						|| varName.toString().equals("false") == true) {
						
						status = AssignStatus.FAIL;
						
					} else {
						
						ParserUtil.unread(reader, read);
						
						BooleanParser.compile(reader, cmds);
						cmds.add(new EvalCmd(Opcode.ASSIGN_TO, 1, varName.toString()));
						
						return;
						
					}
				}
				
				break;
				
			default:
			}

			// 할당 연산이 아님,
			// unread 후 boolean parser 호출함
			if(status == AssignStatus.NOT_ASSIGN_OPERATOR) {
				reader.unread(buffer.array(), 0, buffer.position());
				BooleanParser.compile(reader, cmds);
				return;
			}
			
			if(status == AssignStatus.FAIL) {
				throw new Exception("Unexpected Char at " + AssignParser.class + ":" + read);
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
