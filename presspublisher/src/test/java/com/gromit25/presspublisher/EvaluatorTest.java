package com.gromit25.presspublisher;

import com.gromit25.presspublisher.evaluator.Evaluator;
import com.gromit25.presspublisher.evaluator.ValueContainer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Evaluator 단위 테스트 (Unit test)
 * @author jmsohn
 */
public class EvaluatorTest extends TestCase {
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EvaluatorTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(EvaluatorTest.class);
    }
    
    public void testArithmetic() {
    	
    	try {
    		
    		ValueContainer values = new ValueContainer();
    		
    		Evaluator eval_1 = Evaluator.compile("1+2");
    		Double rst_1 = eval_1.eval(values, Double.class);
    		
    		assertEquals(rst_1, 3.0);
    		
    		Evaluator eval_2 = Evaluator.compile("1+ 2* (3+4 ) ");
    		Double rst_2 = eval_2.eval(values, Double.class);
    		
    		assertEquals(rst_2, 15.0);
    		
    		Evaluator eval_3 = Evaluator.compile(" 1+2*3+4 ");
    		Double rst_3 = eval_3.eval(values, Double.class);
    		
    		assertEquals(rst_3, 11.0);
    		
    		Evaluator eval_4 = Evaluator.compile("(1 +2)*3+4 ");
    		Double rst_4 = eval_4.eval(values, Double.class);
    		
    		assertEquals(rst_4, 13.0);
    		
    		Evaluator eval_5 = Evaluator.compile("10  \t -300");
    		Double rst_5 = eval_5.eval(values, Double.class);
    		
    		assertEquals(rst_5, -290.0);
    		
    		Evaluator eval_6 = Evaluator.compile("10*  \r\n24");
    		Double rst_6 = eval_6.eval(values, Double.class);
    		
    		assertEquals(rst_6, 240.0);
    		
    		Evaluator eval_7 = Evaluator.compile("   1  /   2   ");
    		Double rst_7 = eval_7.eval(values, Double.class);
    		
    		assertEquals(rst_7, 0.5);
    		
    	} catch(Exception ex) {
    		
    		assertTrue(false);
    		ex.printStackTrace();
    		
    	}
    	
    }
    
    public void testAssign() {
    	try {
    		
	    	ValueContainer values = new ValueContainer();
	    	values.importStaticClass(Integer.class);
	    	values.put("a", "4");
	    	
	    	Evaluator
	    		.compile("testval1 =1+2")
	    		.eval(values, void.class);
	    	Double testval1 = values.get("testval1", Double.class);
	    	assertEquals(testval1, 3.0);
	    	
	    	Evaluator
	    		.compile("testval2 =    -5 + Integer.parseInt(a + '1')")
	    		.eval(values, void.class);
	    	Double testval2 = values.get("testval2", Double.class);
	    	assertEquals(testval2, 36.0);
	    	
	    	Evaluator
    			.compile("testval3 = 1 / 2")
    			.eval(values, void.class);
	    	double testval3 = values.get("testval3", double.class);
	    	assertEquals(testval3, 0.5);
	    	
		} catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
    }
    
    /**
     * 
     */
    public void testCallStaticMethod() {
    	
    	try {
    		
    		ValueContainer values = new ValueContainer();
    		values.importStaticClass("Integer", Integer.class);
    		values.importStaticClass(System.class);
    		values.importStaticClass(Double.class);
    		values.put("a", "20");
    		
    		Evaluator eval_1 = Evaluator.compile("Integer.parseInt('1')");
    		Double rst_1 = eval_1.eval(values, Double.class);
    		
    		assertEquals(rst_1, 1.0);
    		
    		Evaluator eval_2 = Evaluator.compile("Integer.parseInt('1') + 1");
    		Double rst_2 = eval_2.eval(values, Double.class);
    		
    		assertEquals(rst_2, 2.0);
    		
    		Evaluator eval_3 = Evaluator.compile("-5 + Integer.parseInt(a)");
    		Double rst_3 = eval_3.eval(values, Double.class);
    		
    		assertEquals(rst_3, 15.0);
    		
    		Evaluator eval_3_1 = Evaluator.compile("-5 + Integer.parseInt(a + '1')");
    		Double rst_3_1 = eval_3_1.eval(values, Double.class);
    		
    		assertEquals(rst_3_1, 196.0);

    		Evaluator eval_4 = Evaluator.compile("System.out.println('This is test-1.')");
    		eval_4.eval(values, void.class);
    		
    		assertTrue(true);

    		Evaluator eval_5 = Evaluator.compile("System.out.println('This is ' + 'test-2.')");
    		eval_5.eval(values, void.class);
    		
    		assertTrue(true);
    		
    		Evaluator eval_6 = Evaluator.compile("System.out.println('This is test-3:' + Integer.toString(1+2))");
    		eval_6.eval(values, void.class);
    		
    		assertTrue(true);
    		
    		Evaluator eval_7 = Evaluator.compile("System.out.println('This is test-4:' + (1+2))");
    		eval_7.eval(values, void.class);
    		
    		assertTrue(true);
    		
    		Evaluator eval_8 = Evaluator.compile("System.out.println('This is test-5:' + Double.toString(1+2))");
    		eval_8.eval(values, void.class);
    		
    		assertTrue(true);
    		
    		Evaluator eval_9 = Evaluator.compile("System.out.println('This is test-6:' + 3)");
    		eval_9.eval(values, void.class);
    		
    		assertTrue(true);

    	} catch(Exception ex) {
    		ex.printStackTrace();
    		assertTrue(false);
    	}
    }
    
    public void testCompare() {
    	try {
    		
    		ValueContainer values = new ValueContainer();
    		
    		Evaluator eval_1 = Evaluator.compile("1<  2");
    		Boolean rst_1 = eval_1.eval(values, Boolean.class);
    		
    		assertTrue(rst_1);
    		
    		Evaluator eval_2 = Evaluator.compile("1 >2");
    		Boolean rst_2 = eval_2.eval(values, Boolean.class);
    		
    		assertFalse(rst_2);
    		
    		Evaluator eval_3 = Evaluator.compile("1+ 1 < 12");
    		Boolean rst_3 = eval_3.eval(values, Boolean.class);
    		
    		assertTrue(rst_3);
    		
    		Evaluator eval_4 = Evaluator.compile("-1 <= 2");
    		Boolean rst_4 = eval_4.eval(values, Boolean.class);
    		
    		assertTrue(rst_4);
    		
    		Evaluator eval_5 = Evaluator.compile("2 <= 2");
    		Boolean rst_5 = eval_5.eval(values, Boolean.class);
    		
    		assertTrue(rst_5);
    		
    		Evaluator eval_6 = Evaluator.compile("100 <= 2");
    		Boolean rst_6 = eval_6.eval(values, Boolean.class);
    		
    		assertFalse(rst_6);
    		
    		Evaluator eval_7 = Evaluator.compile("-1/2 < 2/3");
    		Boolean rst_7 = eval_7.eval(values, Boolean.class);
    		
    		assertTrue(rst_7);

    		Evaluator eval_8 = Evaluator.compile("1 / 2 == 0.5");
    		Boolean rst_8 = eval_8.eval(values, Boolean.class);
    		
    		assertTrue(rst_8);

    	} catch(Exception ex) {
    		ex.printStackTrace();
    		assertTrue(false);
    	}
    }
    
}
