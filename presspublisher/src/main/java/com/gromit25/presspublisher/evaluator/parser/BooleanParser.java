package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.EvalCmd;
import com.gromit25.presspublisher.evaluator.Opcode;

/**
 * Boolean식 파싱하여 스택 명령어 생성
 * 
 * @author jmsohn
 */
class BooleanParser {
	
	/**
	 * 불린 수식 파싱을 위한 상태기계 값
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * 
	 * @author jmsohn
	 */
	enum BooleanStatus {
		START
		, EQUALITY_EXPRESSION_END
		, OR_OPERATOR
		, AND_OPERATOR
		, OPERATOR_END
		, FAIL;
	}

	/**
	 * 불린 수식을 파싱하여, 스택 명령어 형태로 변환 후 스택명령어 목록에 추가함 
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
		BooleanStatus status = BooleanStatus.START;
		
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
				EqualityParser.compile(reader, cmds);
				status = BooleanStatus.EQUALITY_EXPRESSION_END; 
				
				break;
			case EQUALITY_EXPRESSION_END:
				
				if(ch == '|') {
					status = BooleanStatus.OR_OPERATOR;
				} else if(ch == '&') {
					status = BooleanStatus.AND_OPERATOR;
				} else if(ParserUtil.isSpaceChar(ch) == true) {
					status = BooleanStatus.EQUALITY_EXPRESSION_END;
				} else {
					ParserUtil.unread(reader, read);
					return;
				}
				
				break;
			case OR_OPERATOR:
				
				if(ch == '|') {
					opcode = Opcode.OR;
					status = BooleanStatus.OPERATOR_END;
				} else {
					status = BooleanStatus.FAIL;
				}
				
				break;
			case AND_OPERATOR:
				
				if(ch == '&') {
					opcode = Opcode.AND;
					status = BooleanStatus.OPERATOR_END;
				} else {
					status = BooleanStatus.FAIL;
				}
				
				break;
			case OPERATOR_END:
				
				ParserUtil.unread(reader, read);
				BooleanParser.compile(reader, cmds);
				cmds.add(new EvalCmd(opcode, 2));
				return;
				
			default:
			}
			
			if(status == BooleanStatus.FAIL) {
				throw new Exception("Unexpected Char at " + BooleanParser.class + ":" + read);
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
