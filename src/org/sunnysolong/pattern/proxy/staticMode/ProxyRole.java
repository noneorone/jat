package org.sunnysolong.pattern.proxy.staticMode;

/** 
 * Title: base<br> 
 * Description: It's a proxy role.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 24, 2011 6:30:00 PM <br> 
 * @author wangmeng
 */
public class ProxyRole extends AbstractRole{
	
	/**
	 * Members who need agent.
	 */
	private RealRole realRole;
	
	public ProxyRole(){}

	/**
	 * It's a action of proxy's own, just a member method.
	 */
	private void declare(){
		System.out.println("If you want build a family, just tell me, since I'm a matchmaker.");
	}
	
	@Override
	void response() {
		if(null == realRole){
			realRole = new  RealRole();
		}
		this.declare();
		//Deal the handler of the real role. 
		realRole.response();
	}
	
}
