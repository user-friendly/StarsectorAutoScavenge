package org.userfriendly.autoscavenge;

import org.apache.log4j.Logger;
import org.userfriendly.autoscavenge.listeners.CampaignInputListener;
import org.userfriendly.autoscavenge.listeners.AutoScavengeListener;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class AutoScavengeModPlugin extends BaseModPlugin {
	private static final Logger log = Global.getLogger(AutoScavengeModPlugin.class);

	@Override
	public void onGameLoad(boolean newGame) {
		String notice = new String("Automatic salvage pickup enabled.");
		
		log.debug(notice);
		Utility.postConsoleMessage(notice);
		
		if (Global.getSettings().isDevMode()) {
			Global.getSector().addTransientListener(new CampaignEventListener());
			Global.getSector().getListenerManager().addListener(new CampaignInputListener(), true);
		}
		
		Global.getSector().getListenerManager().addListener(new AutoScavengeListener(), true);
	}
}
