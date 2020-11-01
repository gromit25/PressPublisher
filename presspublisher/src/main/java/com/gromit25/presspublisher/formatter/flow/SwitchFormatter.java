package com.gromit25.presspublisher.formatter.flow;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;

import com.gromit25.presspublisher.evaluator.Evaluator;
import com.gromit25.presspublisher.evaluator.ValueContainer;
import com.gromit25.presspublisher.formatter.Formatter;
import com.gromit25.presspublisher.formatter.FormatterAttr;
import com.gromit25.presspublisher.formatter.FormatterException;
import com.gromit25.presspublisher.formatter.FormatterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * switch formatter
 * switch 분기 수행,
 * 자식 formatter로는 case formatter와 default formatter가 있음
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="switch")
public class SwitchFormatter extends AbstractFlowFormatter {

	/** switch 분기를 위한 값을 얻어오기 위한 스크립트 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="exp", mandatory=true)
	private Evaluator switchExp;
	
	/**
	 * 분기를 위한 case formatter 맵
	 * key : case 테그의 value 값(정규표현식)
	 * value : caseFormatter
	 */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private HashMap<String, CaseFormatter> caseFormatterMap = new HashMap<String, CaseFormatter>();

	/** default 분기를 위한 default formatter */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private DefaultFormatter defaultFormatter;

	@Override
	public void addText(String text) throws FormatterException {
		// do nothing
	}

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		
		// case formatter일 경우,
		// case formatter의 value값을 key로 하여,
		// case formatter 목록에 추가
		if((formatter instanceof CaseFormatter) == true ) {
			
			CaseFormatter caseFormatter = (CaseFormatter)formatter;
			this.getCaseFormatterMap().put(caseFormatter.getValue(), caseFormatter);
			
		} else if((formatter instanceof DefaultFormatter) == true) {
			
			this.setDefaultFormatter((DefaultFormatter)formatter);
			
		} else {
			
			throw new FormatterException(this, "unexpected formatter type(not CaseFormatter or DefaultFormatter):" + formatter.getClass().getName());
		}
	}

	@Override
	public void format(OutputStream out, Charset charset, ValueContainer values) throws FormatterException {
		
		// 입력값 체크
		if(out == null) {
			throw new FormatterException(this, "out param is null.");
		}
		
		if(values == null) {
			throw new FormatterException(this, "Value Container is null.");
		}
		
		// 1. switch 문의 내용을 수행하여, 수행결과를 가지고 옴
		Object condition = null;
		try {
			condition = this.getSwitchExp().eval(values, Object.class);
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
		// 2. switch 문 수행 결과에 해당하는 case formatter를 검색함
		CaseFormatter caseFormatter = null;
		Iterator<String> keyIter = this.getCaseFormatterMap().keySet().iterator();
		
		while(keyIter.hasNext() == true) {
			
			String key = keyIter.next();
			if(condition.toString().matches(key) == true) {
				caseFormatter = this.getCaseFormatterMap().get(key);
				break;
			}
		}
		
		// 3. switch 문 수행 결과에 해당하는 case formatter를 수행함
		//    만일 없을 경우 default formatter를 수행함
		if(caseFormatter != null) {
			caseFormatter.format(out, charset, values);
		} else if(this.getDefaultFormatter() != null){
			this.getDefaultFormatter().format(out, charset, values);
		}
	}

}
