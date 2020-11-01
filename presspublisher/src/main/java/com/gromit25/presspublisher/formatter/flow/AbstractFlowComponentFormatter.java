package com.gromit25.presspublisher.formatter.flow;

import java.nio.charset.Charset;

import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterException;

import lombok.Getter;
import lombok.Setter;

/**
 * 하위 flow를 가지는 formatter
 * ex) foreach, alt, for, while, if, case, default 등 하위 플로우를 가지는 경우
 * 
 * @author jmsohn
 */
public abstract class AbstractSubFlowFormatter extends AbstractFlowFormatter {
	
	/**
	 * formatter의 하위 기본 flow
	 */
	@Getter
	@Setter
	private BasicFlowFormatter basicFlowFormatter;

	@Override
	public void addText(String text) throws FormatterException {
		this.getBasicFlowFormatter().addText(text);
	}

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		this.getBasicFlowFormatter().addChildFormatter(formatter);
	}

	@Override
	public void format(Object copyObj, Charset charset, ValueContainer values) throws FormatterException {
		
		// 입력값 검증
		if(copyObj == null) {
			throw new FormatterException(this, "Copy Object is null");
		}

		if(values == null) {
			throw new FormatterException(this, "Value Container is null");
		}

		this.getBasicFlowFormatter().format(copyObj, charset, values);
	}

}
