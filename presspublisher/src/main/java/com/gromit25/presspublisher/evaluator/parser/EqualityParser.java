package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.EvalCmd;
import com.gromit25.presspublisher.evaluator.Opcode;

/**
 * 동등 비교 파싱하여 스택 명령어 생성
 * 
 * @author jmsohn
 */
class EqualityParser {
	
	/**
	 * 동등 비교 수식 파싱을 위한 상태기계 값
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * 
	 * @author jmsohn
	 */
	enum EqualityStatus {
		START
		, COMPARE_EXPRESSION_END
		, EQUALITY_OPERATOR
		, OPERATOR_END
		, FAIL;
	}
	
	/**
	 * 동등 비교 수식을 파싱하여, 스택 명령어 형태로 변환 후 스택명령어 목록에 추가함 
	 * @param reader 입력 문자열
	 * @param cmds 스택 명령어 목록
	 */
	static void compile(PushbackReader reader, ArrayList<EvalCmd> cmds) throws Exception {
		
		// 입력값 검사
		if(reader == null) {
			throw new Exception("reader is null");
		}
		
		if(cmds == null) {
			throw new Exception("commands is null");
		}
		
		// 파서 상태 초기화
		EqualityStatus status = EqualityStatus.START;
		
		// 한문자씩 읽어옴
		int read = reader.read();
		boolean hasMoreChar = (read != -1)?true:false;
		
		Opcode opcode = null;
		char preCmp = (char)-1;

		// 읽기가 완료 될때까지 읽어 들임
		while(hasMoreChar == true) {
			
			char ch = (char)read;
			
			switch(status) {
			case START:
				
				ParserUtil.unread(reader, read);
				CompareParser.compile(reader, cmds);
				status = EqualityStatus.COMPARE_EXPRESSION_END; 
				break;
				
			case COMPARE_EXPRESSION_END:
				
				if(ch == '=' || ch == '!') {
					preCmp = ch;
					status = EqualityStatus.EQUALITY_OPERATOR;
				} else {
					ParserUtil.unread(reader, read);
					return;
				}
				break;
				
			case EQUALITY_OPERATOR:
				
				if(ch == '=') {
					
					if(preCmp == '=') {
						// "=="
						opcode = Opcode.CMP_EQ;
					} else if(preCmp == '!') {
						// "!="
						opcode = Opcode.CMP_NE;
					}
					
					status = EqualityStatus.OPERATOR_END;
				} else {
					status = EqualityStatus.FAIL;
				}
				break;
				
			case OPERATOR_END:
				
				if(ParserUtil.isSpaceChar(ch) == true) {
					status = EqualityStatus.OPERATOR_END;
				} else {
					ParserUtil.unread(reader, read);
					EqualityParser.compile(reader, cmds);
					cmds.add(new EvalCmd(opcode, 2));
					return;
				}
				
			default:
			}
			
			if(status == EqualityStatus.FAIL) {
				throw new Exception("Unexpected Char at " + CompareParser.class + ":" + read);
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
		}// End of while
		
	}

}
