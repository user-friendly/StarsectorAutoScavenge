package org.userfriendly.autoscavenge.script;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.userfriendly.autoscavenge.AutoScavengeModPlugin;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;

public class ConsoleMessageFrameScript implements com.fs.starfarer.api.EveryFrameScript {
	private static final Logger log = Global.getLogger(ConsoleMessageFrameScript.class);
	
	protected boolean isDone = false;
	protected float wait = 0.5f;
	protected float elapsed = 0f;
	
	protected CampaignUIAPI campUi = Global.getSector().getCampaignUI();
	protected String message;
	protected Color color = Color.LIGHT_GRAY;
	
	public ConsoleMessageFrameScript(String message) {
		super();
		this.message = message;
	}

	public ConsoleMessageFrameScript(String message, Color color) {
		super();
		this.message = message;
		this.color = color;
	}
	
	public ConsoleMessageFrameScript(String message, float timeout) {
		super();
		this.wait = timeout;
		this.message = message;
	}

	public ConsoleMessageFrameScript(String message, Color color, float timeout) {
		super();
		this.message = message;
		this.color = color;
		this.wait = timeout;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public boolean runWhilePaused() {
		return true;
	}

	@Override
	public void advance(float amount) {
		// Count towards elapsed time only when the main campaign screen
		// is active and there is no other UI showing.
		if (null != campUi.getCurrentCoreTab()
			|| campUi.isShowingDialog()
			|| campUi.isShowingMenu()
		) {
			return;
		}
		
		if (elapsed < wait) {
			elapsed += amount;
			return;
		}
		
		log.debug("Console message: `" + message + "` in color: " + color.toString());
		campUi.addMessage(message, color);
        isDone = true;
	}

}
