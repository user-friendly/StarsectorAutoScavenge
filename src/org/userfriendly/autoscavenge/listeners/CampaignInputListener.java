package org.userfriendly.autoscavenge.listeners;

import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.userfriendly.autoscavenge.Utility;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignTerrainAPI;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldSource;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;

//FIXME This is a debug class - remove it, if not used.
public class CampaignInputListener implements com.fs.starfarer.api.campaign.listeners.CampaignInputListener {
	protected static final Logger log = Global.getLogger(CampaignInputListener.class);
	
	protected String debugInputEvent(InputEventAPI event) {
		return "\n\tvalue: " + event.getEventValue()
			+ "\n\tchar: " + event.getEventChar()
			+ "\n\ttype: " + event.getEventType().name()
			+ "\n\tisKeyboard: " + (event.isKeyboardEvent() ? "yes" : "no")
			+ "\n\tisKeyDown: " + (event.isKeyDownEvent() ? "yes" : "no")
			+ "\n\tisKeyUp: " + (event.isKeyUpEvent() ? "yes" : "no")
			+ "\n\tisMouse: " + (event.isMouseEvent() ? "yes" : "no")
			+ "\n\tisMouseMove: " + (event.isMouseMoveEvent() ? "yes" : "no")
		;
	}
	
	protected void debugInputEvents(List<InputEventAPI> events, String prefix) {
		if (events.size() <= 0) {
			return;
		}
		
		String debugMsg = new String();
		
		if (null == prefix) {
			prefix = "input";
		}
		
		for (InputEventAPI event : events) {
			// Avoid mouse move spam.
			if (event.isMouseMoveEvent()) {
				continue;
			}
			// Ignore consumed events.
			if (event.isConsumed()) {
				continue;
			}
			
			debugMsg += "\n" + prefix + " event: " + debugInputEvent(event);
		}
		
		if (debugMsg.isEmpty()) {
			return;
		}
		
		log.debug(debugMsg);
	}

	@Override
	public int getListenerInputPriority() {
		// Pretty much, does not matter. Higher number, gets to process input first.
		return 0;
	}

	@Override
	public void processCampaignInputPreCore(List<InputEventAPI> events) {
		//debugInputEvents(events, "inputPreCore");
	}

	@Override
	public void processCampaignInputPreFleetControl(List<InputEventAPI> events) {
		//debugInputEvents(events, "inputPreFleetControl");
		for (InputEventAPI event : events) {
			if (event.isKeyboardEvent() && event.isKeyDownEvent() && event.isCtrlDown()) {
				if (event.getEventValue() == Keyboard.KEY_O) {
					if (Global.getSector().getPlayerFleet().isInHyperspace()) {
						return;
					}
					
					Utility.postConsoleMessage("Automatic salvage pickup: spawn random debris", 0f);
					
					// Basically copy-paste from the CoreScript class.
					
					DebrisFieldParams params = new DebrisFieldParams(
							600f, // field radius - should not go above 1000 for performance reasons
							4f, // density, visual - affects number of debris pieces
							256f, // duration in days 
							128f); // days the field will keep generating glowing pieces
					params.source = DebrisFieldSource.GEN;
					params.baseSalvageXP = (long) 1000; // Some made up test value.
					
					CampaignTerrainAPI debris = (CampaignTerrainAPI) Misc.addDebrisField(
							Global.getSector().getPlayerFleet().getContainingLocation(),
							params, null);
					
					debris.getLocation().set(Global.getSector().getPlayerFleet().getLocation());
					
					log.debug("Debris terrain plugin class: " + debris.getPlugin().getClass().getCanonicalName());
					
					event.consume();
				}
			}
		}
	}

	@Override
	public void processCampaignInputPostCore(List<InputEventAPI> events) {
		//debugInputEvents(events, "inputPostCore");
	}
}
