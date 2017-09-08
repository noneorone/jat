package org.noneorone.pattern.strategy;

/** 
 * Title: base<br> 
 * Description: The Executor of the Strategy.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 22, 2011 8:13:21 PM <br> 
 * @author wangmeng
 */
public class StrategyExecutor {

	private IRobort robort;
	
	public StrategyExecutor(){}
	
	/**
	 * Constructor's rebuilding.
	 * @param robort -- The implementation class of the specified interface.
	 */
	public StrategyExecutor(IRobort robort){
		this.robort = robort;
	}
	
	/**
	 * The action of the executor.
	 */
	public void operate(){
		robort.performTrust();
	}
	
}
