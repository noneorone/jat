package org.sunnysolong.pattern.strategy;

/** 
 * Title: base<br> 
 * Description: Just for the testing.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 22, 2011 8:10:57 PM <br> 
 * @author wangmeng
 */
public class Commission {
	
	public static void main(String[] args){
		
		/**To Build two executor of the strategy with the specified implementation class of the interface.*/
		StrategyExecutor executorOne = new StrategyExecutor(new NumericalControlRobort());
		StrategyExecutor executorTwo = new StrategyExecutor(new ProgramControlledRobort());
		
		/**Just do the action.*/
		executorOne.operate();
		executorTwo.operate();
		
	}
}
