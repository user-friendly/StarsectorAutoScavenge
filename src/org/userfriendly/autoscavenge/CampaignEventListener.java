package org.userfriendly.autoscavenge;

import org.apache.log4j.Logger;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;

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
		
		log.debug(debugMsg);
	}
}
