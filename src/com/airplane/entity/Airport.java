package com.airplane.entity;

import de.fhpotsdam.unfolding.geo.Location;

public class Airport extends Place {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8176826226321986652L;
	private Province province;
	
	public Airport() {
		super();
		province = null;
	}
	
	public Airport(String name, Location loc) {
		super(name, loc);
		province = null;
	}
	
	public Airport(String name, Location loc, Province province) {
		super(name, loc);
		this.province = province;
	}	

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}
}
