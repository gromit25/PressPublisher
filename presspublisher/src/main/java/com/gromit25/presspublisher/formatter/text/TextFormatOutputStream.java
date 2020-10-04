package com.gromit25.presspublisher.formatter.text;

import java.io.IOException;
import java.io.OutputStream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 텍스트 출력 스트림 클래스
 * 출력 텍스트에서 문장시작('|')이 있는 라인만 출력하도록 함 
 * 
 * @author jmsohn
 */
public class TextFormatOutputStream extends OutputStream {
	
	/** Publisher의 출력 스트림 */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private OutputStream out;

	/** 텍스트 출력시, 파싱할 상태 값*/
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private FormatStatus status;

	/**
	 * 이전 출력 라인이 있었는지 여부
	 * 예)
	 *   | 메시지1
	 *   | 메시지2
	 * 일 경우,
	 * 메시지1 파싱 시작시에는 이전 출력라인이 없으며
	 * 메시지2의 경우에는 이전 출력라인이 있음으로 하여 개행을 먼저 추가한 후 메시지2를 출력함
	 * 
	 * 이렇게 하는 이유는 메시지 종료시, 일괄적으로 개행을 하면
	 * 메시지2 출력시에도 개행이 발생하여 항상 한줄이 추가되는 문제가 있음
	 */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private boolean hasPreLine;
	
	/**
	 * 출력 텍스트 파싱을 위한 상태 목록 클래스
	 * 
	 * 상태변환내용 참조(docs/ACellLogViewer Inspector 개요.pptx 참조)
	 * @author jmsohn
	 */
	enum FormatStatus {
		START
		, IN_PRINT
		, NOT_PRINT
		, END;
	}
	
	/**
	 * 생성자
	 * @param out Publisher의 출력 스트림
	 */
	public TextFormatOutputStream(OutputStream out) {
		this.setOut(out);
		this.setStatus(FormatStatus.START);
		this.setHasPreLine(false);
	}

	@Override
	public void write(int b) throws IOException {
		
		char ch = (char)b;
		
		// 입력값과 현재 파싱 상태에 따라
		// 파싱 상태 변환 수행
		switch(this.getStatus()) {
		case START:
			if(ch == ' ' || ch =='\t' || ch == '\n') {
				this.setStatus(FormatStatus.START);
			} else if(ch == '|') {
				if(this.isHasPreLine() == true) {
					this.getOut().write('\r');
					this.getOut().write('\n');
					this.setHasPreLine(false);
				}
				this.setStatus(FormatStatus.IN_PRINT);
			} else if(b == -1) {
				this.setStatus(FormatStatus.END);
			} else {
				this.setStatus(FormatStatus.NOT_PRINT);
			}
			
			break;
			
		case IN_PRINT:
			if(ch == '\n') {
				this.setHasPreLine(true);
				this.setStatus(FormatStatus.START);
			} else if(b == -1) {
				this.setStatus(FormatStatus.END);
			} else {
				this.getOut().write(b);
				this.setStatus(FormatStatus.IN_PRINT);
			}
			
			break;
			
		case NOT_PRINT:
			if(ch == '\n') {
				this.setStatus(FormatStatus.START);
			} else if(b == -1) {
				this.setStatus(FormatStatus.END);
			} else {
				this.setStatus(FormatStatus.NOT_PRINT);
			}
			
			break;
			
		case END:
		default:
			// END 상태와 나머지 상태에서는
			// 아무것도 하지 않음
		}
	}
	
	@Override
	public void flush() throws IOException {
		this.getOut().flush();
	}
	
	@Override
	public void close() throws IOException {
		this.getOut().close();
	}
	
	@Override
	public String toString() {
		return this.getOut().toString();
	}

}
