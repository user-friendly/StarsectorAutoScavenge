package org.userfriendly.autoscavenge;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI.CargoItemType;

/**
 * Item spec for a given type.
 * 
 * A bag of properties relative to the filtering system.
 * 
 * Might expand this class further to include rules.
 */
public class FilterItemSpec {
	protected String id;
	/**
	 *	This is the special item data, usually an id.
	 */
	protected String data;
	CargoItemType type;
	protected float unitSpace;
	protected float unitValue;
	
	protected String name;
	
	public FilterItemSpec(CargoItemType type, String id) {
		this(type, id, "");
	}
	
	public FilterItemSpec(CargoItemType type, String id, String data) {
		this.type = type;
		this.id = id;
		this.data = data;
		
		init();
	}
	
	protected void init() {
		if (CargoItemType.RESOURCES == type) {
			unitSpace = Global.getSettings().getCommoditySpec(id).getCargoSpace();
			unitValue = Global.getSettings().getCommoditySpec(id).getBasePrice();
			name = Global.getSettings().getCommoditySpec(id).getName();
		}
	}
	
	public String getId() {
		return id;
	}
	
	public String getData() {
		return data;
	}
	
	public String getName() {
		return name;
	}
	
	public CargoItemType getType() {
		return type;
	}
	
	public float getUnitSpace() {
		return unitSpace;
	}
}
