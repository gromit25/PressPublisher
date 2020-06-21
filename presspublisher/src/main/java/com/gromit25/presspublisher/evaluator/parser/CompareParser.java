package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.EvalCmd;
import com.gromit25.presspublisher.evaluator.Opcode;

/**
 * 크기 비교 수식 파싱하여 스택 명령어 생성
 * 
 * @author jmsohn
 */
class CompareParser {
	
	/**
	 * 크기 비교 수식 파싱을 위한 상태기계 값
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * 
	 * @author jmsohn
	 */
	enum CompareStatus {
		START
		, ARITHMETIC_EXPRESSION_END
		, GTLT_OPERATOR
		, OPERATOR_END
		, FAIL;
	}
	
	/**
	 * 크기 비교 수식을 파싱하여, 스택 명령어 형태로 변환 후 스택명령어 목록에 추가함 
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
		CompareStatus status = CompareStatus.START;
		
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
				ArithmeticParser.compile(reader, cmds);
				status = CompareStatus.ARITHMETIC_EXPRESSION_END; 
				break;
				
			case ARITHMETIC_EXPRESSION_END:
				
				if(ch == '<' || ch == '>') {
					preCmp = ch;
					status = CompareStatus.GTLT_OPERATOR;
				} else if(ParserUtil.isSpaceChar(ch) == true) {
					status = CompareStatus.ARITHMETIC_EXPRESSION_END;
				} else {
					ParserUtil.unread(reader, read);
					return;
				}
				break;
				
			case GTLT_OPERATOR:
				
				if(ch == '=') {
					
					if(preCmp == '>') {
						// ">="
						opcode = Opcode.CMP_GE;
					} else if(preCmp == '<') {
						// "<="
						opcode = Opcode.CMP_LE;
					}
					status = CompareStatus.OPERATOR_END;
					
				} else {
					
					//
					if(preCmp == '>') {
						// ">"
						opcode = Opcode.CMP_GT;
					} else if(preCmp == '<') {
						// "<"
						opcode = Opcode.CMP_LT;
					} 
					
					//
					if(ParserUtil.isSpaceChar(ch) == true) {
						status = CompareStatus.OPERATOR_END;
					} else {
						ParserUtil.unread(reader, read);
						ArithmeticParser.compile(reader, cmds);
						cmds.add(new EvalCmd(opcode, 2));
						return;
					}
				}
				break;
				
			case OPERATOR_END:
				
				if(ParserUtil.isSpaceChar(ch) == true) {
					status = CompareStatus.OPERATOR_END;
				} else {
					ParserUtil.unread(reader, read);
					ArithmeticParser.compile(reader, cmds);
					cmds.add(new EvalCmd(opcode, 2));
					return;
				}
				
			default:
			}
			
			if(status == CompareStatus.FAIL) {
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
