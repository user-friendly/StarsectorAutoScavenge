package org.userfriendly.autoscavenge;

import java.awt.Color;

import org.userfriendly.autoscavenge.script.ConsoleMessageFrameScript;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;

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
	
	/**
	 * Get a debug message string for an entity.
	 * 
	 * @param entity
	 * @return
	 */
	public static String debugEntity(SectorEntityToken entity) {	
		if (null == entity) {
			return "\n\ttarget is null";
		}
		
		String debugMsg = new String();
		
		debugMsg += "\n\tclass: " + entity.getClass().getCanonicalName()
				+ "\n\tid: " + entity.getId()
				+ "\n\tce type: " + entity.getCustomEntityType()
				+ "\n\tname: " + entity.getName()
				+ "\n\tfull name: " + entity.getFullName()
				+ "\n\tfaction: " + (null != entity.getFaction() ? entity.getFaction().getDisplayName() : "NULL");

		if (entity.getTags().size() > 0) {
			debugMsg += "\n\ttags: " + entity.getTags().toString();
		}
		
		if (entity.getScripts().size() > 0) {
			debugMsg += "\n\tframe scripts: " + entity.getScripts().toString();
		}
		
		return debugMsg;
	}
}
