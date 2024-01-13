package org.userfriendly.test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

public class Misc {
	protected PrintStream out;
	
	public static void main(String[] args) {
		Misc m = new Misc(System.out);
		m.testMathOps();
		m.testCollectionString();
	}
	
	public Misc(PrintStream out) {
		super();
		this.out = out;
	}
	
	protected void testMathOps() {
		out.println("Arithmetic operator precedents: ");
		out.println("8f / 2f * (2f + 2f): " + (8f / 2f * (2f + 2f)));
	}

	protected void testCollectionString() {
		Collection<String> strs = new ArrayList<String>();
		
		strs.add("cargo");
		strs.add("floating");
		strs.add("inSpace");
		
		out.println(strs.toString());
	}
}
