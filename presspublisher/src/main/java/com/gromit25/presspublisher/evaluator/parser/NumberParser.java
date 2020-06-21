package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;
import java.util.ArrayList;

import com.gromit25.presspublisher.evaluator.EvalCmd;
import com.gromit25.presspublisher.evaluator.Opcode;

/**
 * 숫자 파싱하여 수행 명령어 생성
 * 
 * @author jmsohn
 */
class NumberParser {
	
	/**
	 * 숫자 파싱을 위한 상태기계 값
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * 
	 * @author jmsohn
	 */
	enum NumberStatus {
		START
		, NUMBER
		, DOT
		, FLOATING_NUMBER
		, FAIL
	}
	
	/**
	 * 숫자 파싱하여 스택 명령어 생성
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
		
		// 파서 상태 초기화
		NumberStatus status = NumberStatus.START;
		// 파싱하여 변환된 결과를 저장할 스트링
		StringBuilder parsedNumber = new StringBuilder("");
		
		// 한문자씩 읽어옴
		int read = reader.read();
		boolean hasMoreChar = (read != -1)?true:false;
		
		// 읽기가 완료 될때까지 읽어 들임
		while(hasMoreChar == true) {
			
			char ch = (char)read;
			
			// 상태전환 수행
			switch(status) {
			case START:
				
				if((ch >= '0' && ch <= '9')
					|| ch == '-') {
					parsedNumber.append(ch);
					status = NumberStatus.NUMBER;
				} else {
					status = NumberStatus.FAIL;
				}
				break;
				
			case NUMBER:
				
				if(ch >= '0' && ch <= '9') {
					parsedNumber.append(ch);
					status = NumberStatus.NUMBER;
				} else if(ch == '.') {
					parsedNumber.append(ch);
					status = NumberStatus.DOT;
				} else {
					// Number가 종료되면,
					// 읽었던 마지막 문자를 다시 스트림에 넣고
					// stack에 parsing된 값을 넣는다.
					// 정상수행시 여기서 리턴됨
					ParserUtil.unread(reader, read);
					EvalCmd loadConst = new EvalCmd(
						Opcode.LOAD_CONST, 0, Double.parseDouble(parsedNumber.toString()));					
					cmds.add(loadConst);
					
					return;
				}
				break;
				
			case DOT:
				
				if(ch >= '0' && ch <= '9') {
					parsedNumber.append(ch);
					status = NumberStatus.FLOATING_NUMBER;
				} else {
					status = NumberStatus.FAIL;
				}
				break;
				
			case FLOATING_NUMBER:
				
				if(ch >= '0' && ch <= '9') {
					parsedNumber.append(ch);
					status = NumberStatus.FLOATING_NUMBER;
				} else {
					// Number가 종료되면,
					// 읽었던 마지막 문자를 다시 스트림에 넣고
					// stack에 parsing된 값을 넣는다.
					// 정상수행시 여기서 리턴됨
					ParserUtil.unread(reader, read);
					EvalCmd loadConst = new EvalCmd(
							Opcode.LOAD_CONST, 0, Double.parseDouble(parsedNumber.toString()));
					cmds.add(loadConst);

					return;
				}
				break;
				
			default:
			}
			
			if(status == NumberStatus.FAIL) {
				throw new Exception("Unexpected Char at " + NumberParser.class + ":" + read);
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
