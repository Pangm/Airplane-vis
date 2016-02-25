package com.airplane.entity;

import de.fhpotsdam.unfolding.geo.Location;

public class Flight {
	private String flightID;
	private Location loc;
//	private float height; // Ó¢³ß
	private String flightNo;
	private Airport source;
	private Airport destination;
	
	public Flight() {
	
	}
	
	public Flight(String flightID, Location loc, String flightNo, Airport source, Airport destination) {
		this.flightID = flightID;
		this.loc = loc;
		this.flightNo = flightNo;
		this.source = source;
		this.destination = destination;
	}

	public String getID() {
		return flightID;
	}

	public void setID(String flightID) {
		this.flightID = flightID;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public Airport getSource() {
		return source;
	}

	public void setSource(Airport source) {
		this.source = source;
	}

	public Airport getDestination() {
		return destination;
	}

	public void setDestination(Airport destination) {
		this.destination = destination;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flightID == null) ? 0 : flightID.hashCode());
		result = prime * result
				+ ((flightNo == null) ? 0 : flightNo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Flight)) {
			return false;
		}
		Flight other = (Flight) obj;
		if (flightID == null) {
			if (other.flightID != null) {
				return false;
			}
		} else if (!flightID.equals(other.flightID)) {
			return false;
		}
		if (flightNo == null) {
			if (other.flightNo != null) {
				return false;
			}
		} else if (!flightNo.equals(other.flightNo)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Flight [flightID=" + flightID + ", flightNo=" + flightNo + "]";
	}
	
	
	
}
