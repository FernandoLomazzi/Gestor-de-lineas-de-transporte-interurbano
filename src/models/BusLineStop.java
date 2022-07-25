package models;

import java.util.Objects;

import models.busline.BusLine;

public class BusLineStop {
	private BusLine busLine;
	private BusStop busStop;
	private Boolean stops;
	public BusLineStop() {
		
	}
	public BusLineStop(BusLine busLine, BusStop busStop, Boolean stops) {
		this.busLine = busLine;
		this.busStop = busStop;
		this.stops = stops;
	}
	
	public BusLine getBusLine() {
		return busLine;
	}

	public BusStop getBusStop() {
		return busStop;
	}

	public Boolean stops() {
		return stops;
	}

	@Override
	public int hashCode() {
		return Objects.hash(busLine, busStop);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BusLineStop)) {
			return false;
		}
		BusLineStop other = (BusLineStop) obj;
		return Objects.equals(busLine, other.busLine) && Objects.equals(busStop, other.busStop);
	}
	
}
