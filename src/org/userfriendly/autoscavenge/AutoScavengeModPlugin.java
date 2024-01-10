package org.userfriendly.autoscavenge;

import org.apache.log4j.Logger;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class AutoScavengeModPlugin extends BaseModPlugin {
	private static final Logger log = Global.getLogger(AutoScavengeModPlugin.class);
	
	@Override
	public void onGameLoad(boolean newGame) {
		String msg = new String("Automatic salvage pickup enabled.");
		log.debug(msg);
		
		Utility.postConsoleMessage("Automatic salvage pickup enabled.");
	}
}
