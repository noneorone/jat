package org.noneorone.test;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Test2 {

	public static void main(String[] args) {

		try {
			int a = 0;
			int b = 1;
			int c = b / a;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println("begin---\n" + sw.toString() + "\nend---");
		}

	}

}
