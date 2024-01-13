package org.userfriendly.autoscavenge;

import org.apache.log4j.Logger;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;

public class CampaignEventListener extends BaseCampaignEventListener {
	protected static final Logger log = Global.getLogger(CampaignEventListener.class);

	public CampaignEventListener() {
		super(false);
	}
	
	public void reportShownInteractionDialog(InteractionDialogAPI dialog) {
		String debugMsg = "About to show dialog: ";
		
		debugMsg += "\n\tClass: " + dialog.getClass().getCanonicalName();
		debugMsg += "\n\tPlugin class: " + dialog.getPlugin().getClass().getCanonicalName();
		
		debugMsg += "\n\tInteration target: "
				+ Utility.debugEntity(dialog.getInteractionTarget());
		
		// OptionPanelAPI op = dialog.getOptionPanel();
		//dialog.getVisualPanel().showLoot(debugMsg, null, false, false, false, null);
		
		log.debug(debugMsg);
	}
}
