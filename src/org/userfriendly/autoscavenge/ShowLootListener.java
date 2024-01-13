package org.userfriendly.autoscavenge;

import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignTerrainAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.SpecialItemSpecAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldSource;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.campaign.CargoStackAPI;

public class ShowLootListener implements
	com.fs.starfarer.api.campaign.listeners.ShowLootListener,
	com.fs.starfarer.api.campaign.listeners.CargoScreenListener,
	com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener,
	com.fs.starfarer.api.campaign.listeners.CampaignInputListener
{
	protected static final Logger log = Global.getLogger(ShowLootListener.class);
	
	/**
	 * Debug helper for cargo stacks.
	 * 
	 * @param cargo
	 * @return
	 */
	protected String debugCargo(CargoAPI cargo) {
		String debugMsg = new String();
		
		for (CargoStackAPI stack : cargo.getStacksCopy()) {
			debugMsg += "\n\t[" + stack.getSize() + "] " + stack.getDisplayName();
			if (stack.isSpecialStack()) {
				SpecialItemData data = stack.getSpecialDataIfSpecial();
				SpecialItemSpecAPI spec = stack.getSpecialItemSpecIfSpecial();

				debugMsg += "\n\t\tspecial stack data: " + stack.getData().toString();
				
				debugMsg += "\n\t\tspecial stack data id: " + data.getId();
				debugMsg += "\n\t\tspecial stack data data: " + data.getData();
				
				debugMsg += "\n\t\tspecial stack spec id: " + spec.getId();
				debugMsg += "\n\t\tspecial stack spec name: " + spec.getName();
				debugMsg += "\n\t\tspecial stack spec params: " + spec.getParams();
			}
		}
		
		return debugMsg;
	}
	
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
			debugMsg += "\n\tBought: " + debugCargo(transaction.getBought());
		}
		if (!transaction.getSold().isEmpty()) {
			debugMsg += "\n\tSold: " + debugCargo(transaction.getSold());
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
	
	@Override
	public void reportAboutToShowLootToPlayer(CargoAPI loot, InteractionDialogAPI dialog) {
		String debugMsg = "About to show loot to player";
		
		SectorEntityToken target = dialog.getInteractionTarget();
		if (null != target) {
			debugMsg += "\n\tEntity target: ";
			debugMsg += Utility.debugEntity(target);
			
			if (target instanceof CampaignTerrainAPI) {
				log.debug("Entity errain plugin class: " + ((CampaignTerrainAPI) target).getPlugin().getClass().getCanonicalName());
			}
		}
		
		// CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
		
		String dialogClass = dialog.getClass().getCanonicalName();
		
		String cargoClass = loot.getClass().getCanonicalName();
		
		float credits = loot.getCredits().get();
		float supplies = loot.getSupplies();
		float fuel = loot.getFuel();
		float heavy = loot.getCommodityQuantity(Commodities.HEAVY_MACHINERY);
		int crew = loot.getCrew();
		int marines = loot.getMarines();
		
		debugMsg += "\n\tDialog class: " + dialogClass;
		
		debugMsg += "\n\tCargo class: " + cargoClass;
		debugMsg += String.format("\n\tBasic contents:"
				+ "\n\t\tcredits: %.2f"
				+ "\n\t\tsupplies: %.2f"
				+ "\n\t\tcrew: %d"
				+ "\n\t\tmarines: %d"
				+ "\n\t\tfuel: %.2f"
				+ "\n\t\theavy machinery: %.2f"
				, credits, supplies, crew, marines, fuel, heavy);
		
		debugMsg += "\n\tLoot stack dump: ";
		for (CargoStackAPI stack : loot.getStacksCopy()) {
			debugMsg += "\n\t\t" + stack.getDisplayName() + " [" + stack.getSize() + "]";
		}
		
		log.debug(debugMsg);
	}

	@Override
	public void reportCargoScreenOpened() {
		String debugMsg = "Player opened cargo, dump it: ";
		
		CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
		
		debugMsg += debugCargo(playerCargo);
		
		log.debug(debugMsg);
	}

	@Override
	public void reportPlayerLeftCargoPods(SectorEntityToken entity) {
		log.debug("Player dropped loot. Entity class: " + entity.getClass().getCanonicalName());
	}

	@Override
	public void reportPlayerNonMarketTransaction(PlayerMarketTransaction transaction, InteractionDialogAPI dialog) {
		String debugMsg = "Player non-market transaction";
		if (null != dialog) {
			debugMsg += " [dialog: " + dialog.toString() + "] ";
		}
		log.debug(debugMsg + debugMarketTransaction(transaction));
	}

	@Override
	public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {
		String debugMsg = "Player market transaction";
		log.debug(debugMsg + debugMarketTransaction(transaction));
	}

	@Override
	public void reportSubmarketOpened(SubmarketAPI submarket) {
		log.debug("Player opened submarket: " + submarket.getNameOneLine());
	}

	@Override
	public void reportPlayerOpenedMarket(MarketAPI market) {
		log.debug("Player opened market: " + market.getName());
	}

	@Override
	public void reportPlayerOpenedMarketAndCargoUpdated(MarketAPI market) {
		log.debug("Player opened market [" + market.getName() + "] and cargo updated.");
	}

	@Override
	public void reportPlayerClosedMarket(MarketAPI market) {
		log.debug("Player closed market: " + market.getName());
	}

	@Override
	public int getListenerInputPriority() {
		// Pretty much, does not matter. Higher number, gets to process input first.
		return 0;
	}
	
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
