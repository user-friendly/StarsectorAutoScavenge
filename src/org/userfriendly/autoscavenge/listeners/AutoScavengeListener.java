package org.userfriendly.autoscavenge.listeners;

import org.apache.log4j.Logger;
import org.userfriendly.autoscavenge.Filter;
import org.userfriendly.autoscavenge.Utility;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignTerrainAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;

public class AutoScavengeListener implements
	com.fs.starfarer.api.campaign.listeners.ShowLootListener,
	com.fs.starfarer.api.campaign.listeners.CargoScreenListener
{
	protected static final Logger log = Global.getLogger(AutoScavengeListener.class);
	
	/**
	 * 
	 * 
	 * @param transaction
	 * @return
	 */
	protected String debugMarketTransaction(PlayerMarketTransaction transaction) {
		String debugMsg = new String();
		
		if (null != transaction.getMarket()) {
			debugMsg += "\n\tMarket: " + transaction.getMarket().getName();
		}
		else {
			debugMsg += "\n\tNo market";
		}
		if (null != transaction.getSubmarket()) {
			debugMsg += "\n\tSubmarket: " + transaction.getSubmarket().getNameOneLine();
		}
		else {
			debugMsg += "\n\tNo submarket";
		}

		if (!transaction.getBought().isEmpty()) {
			debugMsg += "\n\tBought: " + Utility.debugCargo(transaction.getBought());
		}
		if (!transaction.getSold().isEmpty()) {
			debugMsg += "\n\tSold: " + Utility.debugCargo(transaction.getSold());
		}
		if (transaction.getBought().isEmpty() && transaction.getSold().isEmpty()) {
			debugMsg += "\n\tNo item transactions";
		}
		
		if (0 < transaction.getShipsBought().size() + transaction.getShipsSold().size()) {
			debugMsg += String.format("\n\tShips bought %d, sold %d",
					transaction.getShipsBought().size(),
					transaction.getShipsSold().size()
			);
		}
		else {
			debugMsg += "\n\tNo ship transactions";
		}
		
		return debugMsg;
	}
	
	protected String debugShowLoot(CargoAPI loot, InteractionDialogAPI dialog) {
		String debugMsg = "About to show loot to player";
		SectorEntityToken target = dialog.getInteractionTarget();
		if (null != target) {
			debugMsg += "\n\tEntity target: ";
			debugMsg += Utility.debugEntity(target);
			
			if (target instanceof CampaignTerrainAPI) {
				log.debug("Entity errain plugin class: " + ((CampaignTerrainAPI) target).getPlugin().getClass().getCanonicalName());
			}
		}
		
		String dialogClass = dialog.getClass().getCanonicalName();
		String cargoClass = loot.getClass().getCanonicalName();		
		debugMsg += "\n\tDialog class: " + dialogClass;
		debugMsg += "\n\tCargo class: " + cargoClass;
		
		float credits = loot.getCredits().get();
		float supplies = loot.getSupplies();
		float fuel = loot.getFuel();
		float heavy = loot.getCommodityQuantity(Commodities.HEAVY_MACHINERY);
		int crew = loot.getCrew();
		int marines = loot.getMarines();
		
		debugMsg += String.format("\n\tBasic contents:"
				+ "\n\t\tcredits: %.2f"
				+ "\n\t\tsupplies: %.2f"
				+ "\n\t\tcrew: %d"
				+ "\n\t\tmarines: %d"
				+ "\n\t\tfuel: %.2f"
				+ "\n\t\theavy machinery: %.2f"
				, credits, supplies, crew, marines, fuel, heavy);
		
		debugMsg += "\n\tLoot stack dump: " + Utility.debugCargo(loot);
		return debugMsg;
	}
	
	@Override
	public void reportAboutToShowLootToPlayer(CargoAPI loot, InteractionDialogAPI dialog) {
		if (Global.getSettings().isDevMode()) {
			log.debug(debugShowLoot(loot, dialog));
		}
		
		// TODO Handle encounter generated loot.
		//		See FleetInteractionDialogPluginImpl::optionSelected()... I think.
		//		At some point, the VisualPanelAPI::showLoot() is called. Right before
		//		that, a ::reportEncounterLootGenerated() is raised. See/impl. the
		//		...autoscavenge.CampaignEventListener::reportEncounterLootGenerated()
		//		handler and somehow pass a flag to this guy here.
		
		SectorEntityToken target = dialog.getInteractionTarget();
		if (null != target && null != target.getCustomEntityType()
				&& target.getCustomEntityType().equals("debris_field_shared")
		) {
			// TODO Find out if this is the correct memory object to use.
			//		The SalvageEntity::performSalvage() does the "no dismiss dialog" check,
			//		but on what memory object? Find out how this memory system works and
			//		what exactly it is. If this flag is set on a dialog memory, that is
			//		local to that dialog then the flag check is no good.
			MemoryAPI memory = target.getMemoryWithoutUpdate();
			if (null != memory) {
				// TODO Search in the rules.csv file - it's some kind of ruins salvage thing.
				if (!memory.contains("$doNotDismissDialogAfterSalvage")) {
					log.debug("Automatic salvage pickup is going to do its thing...");
					
					Filter lootFilter = new Filter();
					lootFilter.apply(loot, Global.getSector().getPlayerFleet());
					
					// Given there is unused loot is not empty, remember it
					// so that the cargo screen opened handler can report it.
					if (!loot.isEmpty()) {
						memory.set("$autoSalvagePickupLoot", loot);
					}
				}
				else {
					// If the dialog is not finished, bad thing happens:
					// Tested closing the cargo UI when opening cargo pods and
					// the player is stuck on the initial dialog and the
					// main campaign gets unpaused. Pressing the numbers or escape,
					// keys does not close the dialog.
					log.debug("Cannot perform automatic salvage pickup - dialog not finished!");
				}
			}
			else {
				log.debug("Cannot perform automatic salvage pickup - no memory map!");
			}
		}
	}

	@Override
	public void reportCargoScreenOpened() {
//		String debugMsg = "Player opened cargo, dump it: ";
//		CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
//		debugMsg += debugCargo(playerCargo);
//		log.debug(debugMsg);
		
		// TODO Refactor code and check for entity type, same as in the loot report method.
		//		Or better yet, yank the similar code into a helper method.
		InteractionDialogAPI dialog = Global.getSector().getCampaignUI().getCurrentInteractionDialog();
		if (null != dialog && null != dialog.getInteractionTarget()) {
			MemoryAPI memory = dialog.getInteractionTarget().getMemory();
		    // Fact set during the "about to show loot" handler, only given
			// certain scavenge conditions (currently, debris fields).
			if (null != memory.get("$autoSalvagePickupLoot")
			) {
				// NOTE Possible switch here - open cargo UI anyway?
				//		Also, if there are any special items found, keep the
				//		cargo UI opened, but still auto can picked up.
				CargoAPI loot = (CargoAPI) memory.get("$autoSalvagePickupLoot");
				dialog.getVisualPanel().closeCoreUI();
				Global.getSector().reportPlayerDidNotTakeCargo(loot);
				memory.unset("$autoSalvagePickupLoot");
				
				log.debug("Drop unused loot from salvage ops.");
				
//				Object test = dialog.getInteractionTarget().getMemoryWithoutUpdate().get(MemFlags.SALVAGE_DEBRIS_FIELD);
//				if (test instanceof DebrisFieldTerrainPlugin) {
//					DebrisFieldTerrainPlugin debris = (DebrisFieldTerrainPlugin) test;
//					debris.setScavenged(false);
//				}
			}
		}
	}

	@Override
	public void reportPlayerLeftCargoPods(SectorEntityToken entity) {
		if (Global.getSettings().isDevMode()) {
			log.debug("Player dropped loot. Entity class: " + entity.getClass().getCanonicalName());
		}
	}

	@Override
	public void reportPlayerNonMarketTransaction(PlayerMarketTransaction transaction, InteractionDialogAPI dialog) {
		if (Global.getSettings().isDevMode()) {
			String debugMsg = "Player non-market transaction";
			if (null != dialog) {
				debugMsg += " [dialog: " + dialog.toString() + "] ";
			}
			log.debug(debugMsg + debugMarketTransaction(transaction));
		}
	}
	
	@Override
	public void reportSubmarketOpened(SubmarketAPI submarket) {
		if (Global.getSettings().isDevMode()) {
			log.debug("Player opened submarket: " + submarket.getNameOneLine());
		}
	}
}
