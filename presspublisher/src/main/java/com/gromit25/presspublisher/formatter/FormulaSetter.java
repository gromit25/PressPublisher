package com.gromit25.presspublisher.formatter;

import java.lang.reflect.Method;

import com.gromit25.presspublisher.evaluator.Evaluator;
import com.gromit25.presspublisher.evaluator.ValueContainer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author jmsohn
 */
/*package*/ class FormulaSetter {
	
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private Evaluator formula;
	
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private Method setter;
	
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private Method writer;
	
	/*package*/ FormulaSetter(Evaluator formula, Method setter, Method writer) {
		this.setFormula(formula);
		this.setSetter(setter);
		this.setWriter(writer);
	}
	
	/*package*/ FormulaSetter(String formulaScript, Method setter, Method writer) throws Exception {
		this(Evaluator.compile(formulaScript), setter, writer);
	}
	
	/*package*/ void setData(Formatter formatter, ValueContainer values) throws Exception {
		Object retValue = this.getFormula().eval(values, Object.class);
		this.getSetter().invoke(null, formatter, this.getWriter(), retValue.toString());
	}

}
