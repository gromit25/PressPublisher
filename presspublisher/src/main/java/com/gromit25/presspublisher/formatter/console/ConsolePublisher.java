package com.gromit25.presspublisher.formatter.console;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.fusesource.jansi.AnsiConsole;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.FormatterXmlHandler;
import com.gromit25.presspublisher.formatter.text.TextPublisher;

/**
 * console 출력을 위한 publisher 클래스
 * 
 * @author jmsohn
 */
public class ConsolePublisher extends TextPublisher {

	@Override
	protected FormatterXmlHandler createXmlHandler() throws Exception {
		return new ConsoleFormatterXmlHandler();
	}

	@Override
	public void publish(OutputStream out, Charset charset, ValueContainer values) throws Exception {
		
		try(ByteArrayOutputStream outMessage = new ByteArrayOutputStream()) {

			//
			super.publish(outMessage, charset, values);
			
			// console 출력
			// Windows console창에서는 ansi 표준 출력을 지원하지 않기 때문에
			// jansi library를 사용하여 ansi 출력 수행
			// Linux 상에서 정상 동작 확인
			// 출력할 메시지가 없으면 스킵
			if(outMessage.size() != 0) {
				AnsiConsole.systemInstall();
				System.out.println(ansi().render(outMessage.toString()));
				AnsiConsole.systemUninstall();
			}
		}
	}

}
