package org.userfriendly.test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

public class Misc {
	protected PrintStream out;
	
	public static void main(String[] args) {
		Misc m = new Misc(System.out);
		
		m.testMathOps();
		m.testCollectionString();
		
		Properties props = System.getProperties();
		for (String key : props.stringPropertyNames()) {
			if (null != props.getProperty(key, null)) {
				System.out.println(String.format("[%s]: %s", key, props.getProperty(key)));
			}
		}
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
