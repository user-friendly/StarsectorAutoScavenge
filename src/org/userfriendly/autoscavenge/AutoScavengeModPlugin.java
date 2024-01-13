package org.userfriendly.autoscavenge;

import org.apache.log4j.Logger;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemSpecAPI;

public class AutoScavengeModPlugin extends BaseModPlugin {
	private static final Logger log = Global.getLogger(AutoScavengeModPlugin.class);
	
	@Override
	public void onApplicationLoad() throws Exception {
		// Debug purposes.
		for (SpecialItemSpecAPI spec : Global.getSettings().getAllSpecialItemSpecs()) {
			log.debug("spec item: " + spec.getId() + ", `" + spec.getName() + "`");
		}
	}

	@Override
	public void onGameLoad(boolean newGame) {
		String msg = new String("Automatic salvage pickup enabled.");
		
		log.debug(msg);
		Utility.postConsoleMessage("Automatic salvage pickup enabled.");
		
		log.debug("Is vsync enabled?: " + Global.getSettings().getString("vsync"));
		
		Global.getSector().addTransientListener(new CampaignEventListener());
		Global.getSector().getListenerManager().addListener(new ShowLootListener(), true);
	}
}
