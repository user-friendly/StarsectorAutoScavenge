package org.userfriendly.autoscavenge;

import java.util.ArrayList;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoAPI.CargoItemType;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;

/**
 * Rules:
 * Think about rules per item - for example, supplies are always needed
 * so always pick them up (if enough cargo), fuel and crew are special
 * in terms of storage compartment. Fuel should always be picked up, if
 * enough free space. Crew on the other hand can be variable - % of
 * the max. Other commodities and special items (weapons & wings) can
 * have a priority list. First iteration can store these in code,
 * later ones in a config (JSON) file, per save?
 */

/**
 * Loot filter class.
 * 
 * Main class that orchestrates the filtering process and applies it.
 */
public class Filter {
	
	ArrayList<FilterItemSpec> specs = new ArrayList<FilterItemSpec>();
	
	public Filter() {
		 specs.add(new FilterItemSpec(CargoItemType.RESOURCES, Commodities.SUPPLIES));
		 specs.add(new FilterItemSpec(CargoItemType.RESOURCES, Commodities.CREW));
		 specs.add(new FilterItemSpec(CargoItemType.RESOURCES, Commodities.MARINES));
		 specs.add(new FilterItemSpec(CargoItemType.RESOURCES, Commodities.FUEL));
		 specs.add(new FilterItemSpec(CargoItemType.RESOURCES, Commodities.HEAVY_MACHINERY));
	}
	
	/**
	 * Do the actual automatic salvage pickup.
	 * 
	 * Function can (and probably will) modify both arguments.
	 * 
	 * @param salvage The salvaged (from the debris field).
	 * @param fleet The recipient fleet.
	 */
	public void apply(CargoAPI salvage, CampaignFleetAPI fleet) {
		CargoAPI recipient = fleet.getCargo();
		
		// TODO Do the actual cargo filtering and operations here.
		
		// float suppliesUnitSize = Global.getSettings().getCommoditySpec(Commodities.SUPPLIES).getCargoSpace();
		
		for (FilterItemSpec spec : specs) {
			doTransfer(spec, salvage, recipient);
		}
		
		// TODO Make special item check here maybe?
		
		salvage.removeEmptyStacks();
	}
	
	// TODO Call filter rules in here.
	protected float getTransferAmount(FilterItemSpec spec, CargoAPI from, CargoAPI to) {
		float units = 0;
		float unitSize = 0;
		float spaceLeft = 0;
		float transfer = 0;
		
		if (spec.getType() == CargoItemType.RESOURCES) {
			units = from.getCommodityQuantity(spec.getId());
			unitSize = spec.getUnitSpace();
			spaceLeft = 0;
			
			if (spec.getId() == Commodities.CREW || spec.getId() == Commodities.MARINES) {
				spaceLeft = to.getFreeCrewSpace();
				unitSize = 1;
			}
			else if (spec.getId() == Commodities.FUEL) {
				spaceLeft = to.getFreeFuelSpace();
				unitSize = 1;
			}
			else {
				spaceLeft = to.getSpaceLeft();
			}
			
			if (units > 0 && spaceLeft >= unitSize) {
				transfer = spaceLeft / unitSize;
				if (units < transfer) {
					transfer = units;
				}
			}
		}
		
		return transfer;
	}
	
	protected boolean doTransfer(FilterItemSpec spec, CargoAPI from, CargoAPI to) {
		if (spec.getType() == CargoItemType.RESOURCES) {
			float transfer = getTransferAmount(spec, from, to);
			if (transfer > 0) {
				from.removeCommodity(spec.getId(), transfer);
				to.addCommodity(spec.getId(), transfer);
				
				Utility.postConsoleMessage(String.format("Salvaged %d %s.", (int) transfer, spec.getName()));
				
				return true;
			}
		}
		
		return false;
	}
}

























