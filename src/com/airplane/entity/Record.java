package com.airplane.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.fhpotsdam.unfolding.geo.Location;

public class Record {
	private Date recordTime;
	private Location loc;
	private float height;
	private Flight flight;
	
	public Record() {
		
	}
	
	public Record(Flight flight, Location loc, Date recordTime, float height) {
		this.flight = flight;
		this.loc = loc;
		this.recordTime = recordTime;
		this.height = height;
	}
	
	/**
	 * @return the recordTime
	 */
	public Date getRecordTime() {
		return recordTime;
	}
	/**
	 * @param recordTime the recordTime to set
	 */
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	/**
	 * @return the loc
	 */
	public Location getLoc() {
		return loc;
	}
	/**
	 * @param loc the loc to set
	 */
	public void setLoc(Location loc) {
		this.loc = loc;
	}
	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	/**
	 * @return the flight
	 */
	public Flight getFlight() {
		return flight;
	}
	/**
	 * @param flight the flight to set
	 */
	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return "Record [recordTime=" + sdf.format(recordTime) + ", loc=" + loc
				+ ", height=" + height + ", flight=" + flight + "]";
	}
	
	
}
