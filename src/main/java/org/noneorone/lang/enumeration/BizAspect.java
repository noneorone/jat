package org.noneorone.lang.enumeration;

import java.io.Serializable;

/** 
 * Title: base<br> 
 * Description: Normal Bean<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 8, 2011 2:42:10 PM <br> 
 * @author wangmeng
 */
public class BizAspect implements Serializable{

	private static final long serialVersionUID = 6601146750320150389L;
	private ThinType thinType;
	private String highTechName;

	public ThinType getThinType() {
		return thinType;
	}

	public void setThinType(ThinType thinType) {
		this.thinType = thinType;
	}

	public String getHighTechName() {
		return highTechName;
	}

	public void setHighTechName(String highTechName) {
		this.highTechName = highTechName;
	}

}
