package org.noneorone.servlet.jsp.tag;

import java.io.IOException;
import java.util.Random;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class RandomTag extends TagSupport {

	private int min = 0;
	private int max = Integer.MAX_VALUE;

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public int doStartTag() throws JspException {
		Random random = new Random();
		int result = random.nextInt(max-min);
		try {
			this.pageContext.getOut().write(String.valueOf(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
