package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.EvalCmd;
import com.gromit25.presspublisher.evaluator.Opcode;

/**
 * 수식 파싱하여 스택 명령어 생성
 * 
 * @author jmsohn
 */
class ArithmeticParser {
	
	/**
	 * 수식 파싱을 위한 상태기계 값
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * 
	 * @author jmsohn
	 */
	enum ArithmeticStatus {
		START
		, TERM_END
		, OPERATOR
		, FAIL;
	}

	/**
	 * 수식을 파싱하여, 스택 명령어 형태로 변환 후 스택명령어 목록에 추가함 
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
		ArithmeticStatus status = ArithmeticStatus.START;
		
		// 한문자씩 읽어옴
		int read = reader.read();
		boolean hasMoreChar = (read != -1)?true:false;
		
		Opcode opcode = null;

		// 읽기가 완료 될때까지 읽어 들임
		while(hasMoreChar == true) {
			
			char ch = (char)read;
			
			switch(status) {
			case START:
				
				ParserUtil.unread(reader, read);
				TermParser.compile(reader, cmds);
				status = ArithmeticStatus.TERM_END; 
				break;
				
			case TERM_END:
				
				if(ch == '+') {
					opcode = Opcode.ADD;
					status = ArithmeticStatus.OPERATOR;
				} else if(ch == '-') {
					opcode = Opcode.MINUS;
					status = ArithmeticStatus.OPERATOR;
				} else if(ParserUtil.isSpaceChar(ch) == true) {
					status = ArithmeticStatus.TERM_END;
				} else {
					ParserUtil.unread(reader, read);
					return;
				}
				break;
				
			case OPERATOR:
				
				if(ParserUtil.isSpaceChar(ch) == true) {
					status = ArithmeticStatus.OPERATOR;
				} else {
					ParserUtil.unread(reader, read);
					ArithmeticParser.compile(reader, cmds);
					cmds.add(new EvalCmd(opcode, 2));
					return;
				}
				break;
				
			default:
			}
			
			if(status == ArithmeticStatus.FAIL) {
				throw new Exception("Unexpected Char at " + ArithmeticParser.class + ":" + read);
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
