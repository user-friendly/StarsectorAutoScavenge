package org.userfriendly.autoscavenge;

import java.awt.Color;

import org.userfriendly.autoscavenge.script.ConsoleMessageFrameScript;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;

public class Utility {
	/**
	 * Display a message on the console.
	 * 
	 * The console is the right-hand side notification area on the campaign UI.
	 * 
	 * @param message
	 * @return
	 */
	public static EveryFrameScript postConsoleMessage(String message) {
		ConsoleMessageFrameScript listener = new ConsoleMessageFrameScript(message);
		Global.getSector().addTransientScript(listener);
		return listener;
	}
	
	/**
	 * Display a message on the console.
	 * 
	 * The console is the right-hand side notification area on the campaign UI.
	 * 
	 * @param message
	 * @param color
	 * @return
	 */
	public static EveryFrameScript postConsoleMessage(String message, Color color) {
		ConsoleMessageFrameScript listener = new ConsoleMessageFrameScript(message, color);
		Global.getSector().addTransientScript(listener);
		return listener;
	}
	
	/**
	 * Display a message on the console.
	 * 
	 * The console is the right-hand side notification area on the campaign UI.
	 * 
	 * @param message
	 * @param timeout
	 * @return
	 */
	public static EveryFrameScript postConsoleMessage(String message, float timeout) {
		ConsoleMessageFrameScript listener = new ConsoleMessageFrameScript(message, timeout);
		Global.getSector().addTransientScript(listener);
		return listener;
	}
	
	/**
	 * Display a message on the console.
	 * 
	 * The console is the right-hand side notification area on the campaign UI.
	 * 
	 * @param message
	 * @param color
	 * @param timeout
	 * @return
	 */
	public static EveryFrameScript postConsoleMessage(String message, Color color, float timeout) {
		ConsoleMessageFrameScript listener = new ConsoleMessageFrameScript(message, color, timeout);
		Global.getSector().addTransientScript(listener);
		return listener;
	}
}
