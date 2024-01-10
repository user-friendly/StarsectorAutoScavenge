package org.userfriendly.autoscavenge;

import java.util.Map;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;

public class DebrisInteractionDialogPlugin implements InteractionDialogPlugin {

	@Override
	public void init(InteractionDialogAPI dialog) {
		
	}

	@Override
	public void optionSelected(String optionText, Object optionData) {
	}

	@Override
	public void optionMousedOver(String optionText, Object optionData) {
	}

	@Override
	public void advance(float amount) {

	}

	@Override
	public void backFromEngagement(EngagementResultAPI battleResult) {
	}

	@Override
	public Object getContext() {
		return null;
	}

	@Override
	public Map<String, MemoryAPI> getMemoryMap() {
		return null;
	}

}
