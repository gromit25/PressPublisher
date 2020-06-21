package com.gromit25.presspublisher.formatter.text;

import java.io.OutputStream;
import java.nio.charset.Charset;

import com.gromit25.presspublisher.common.PublisherUtil;
import com.gromit25.presspublisher.evaluator.Evaluator;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * print formatter
 * 화면상에 표현식(exp) 수행결과를 출력
 *  
 * ex)
 * <print exp="info.getName()" length="20"/>
 * 
 * @author jmsohn
 */
@FormatterSpec(group="text", tag="print")
public class PrintFormatter extends AbstractTextFormatter {
	
	/** 수행할 표현식 */
	@Getter
	@Setter
	@FormatterAttr(name="exp")
	private String exp;
	
	/** 표현식(exp)의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="exp")
	private Evaluator expEval;
	
	/**
	 * 출력 길이
	 * 출력 길이(옵션) 설정되어 있지 않으면 수행 결과 스트링의 길이 만큼만 출력
	 */
	@Getter
	@Setter
	@FormatterAttr(name="length", mandatory=false)
	private int length = -1;
	
	@Override
	public void addText(String text) throws FormatterException {
		// do nothing
	}

	@Override
	public void addChildFormatter(Formatter fomatter) throws FormatterException {
		// do nothing
	}

	@Override
	public void formatText(OutputStream copy, Charset charset, ValueContainer values) throws FormatterException {
		
		// 입력값 검증
		if(copy == null) {
			throw new FormatterException(this, "Copy Object is null");
		}
		
		if(charset == null) {
			throw new FormatterException(this, "Charset is null");
		}
		
		if(values == null) {
			throw new FormatterException(this, "Value Container is null");
		}
		
		if(this.getExp() == null) {
			throw new FormatterException(this, "expression(exp) is null");
		}
		
		// 표현식(exp)을 수행한 후
		// 수행 결과 값을 반환함
		try {
			
			Object result = this.getExpEval().eval(values, Object.class);
			
			// 수행 결과를 메세지에 설정함
			if(result != null) {
				copy.write(
						PublisherUtil.makeString(result.toString(), this.getLength())
						// 개행만 있고 '|' 이 없을경우,
						// 개행 이후 부분을 최종적으로 잘라내기 때문에
						// '|'으로 변경해 줌
						.replaceAll("\n", "\n|")
						.getBytes(charset));
			} else {
				copy.write(PublisherUtil.makeString("N/A", this.getLength()).getBytes(charset));
			}
			
			copy.flush();
			
		} catch(Exception ex) {
			
			// 예외 발생시
			throw new FormatterException(this, ex);
		}
		
	}

}
