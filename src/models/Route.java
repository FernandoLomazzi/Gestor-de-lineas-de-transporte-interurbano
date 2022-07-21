package models;

import java.util.Objects;

public class Route {
	private BusStop sourceStop,destinationStop;
	private Double distanceInKM;
	private Boolean active;
	
	public enum distanceUnits{
		Kilómetros,Metros,Millas
	}
	public Route(BusStop sourceStop, BusStop destinationStop, Double distanceInKM, Boolean active) {
		super();
		this.sourceStop = sourceStop;
		this.destinationStop = destinationStop;
		this.distanceInKM = distanceInKM;
		this.active = active;
	}
	
	@Override
	public String toString() {
		return distanceInKM+" [km]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(destinationStop, sourceStop);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Route)) {
			return false;
		}
		Route other = (Route) obj;
		return Objects.equals(destinationStop, other.destinationStop) && Objects.equals(sourceStop, other.sourceStop);
	}

	public Integer getSourceStopNumber() {
		return getSourceStop().getStopNumber();
	}
	public BusStop getSourceStop() {
		return sourceStop;
	}
	public void setSourceStop(BusStop sourceStop) {
		this.sourceStop = sourceStop;
	}
	public Integer getDestinationStopNumber() {
		return getDestinationStop().getStopNumber();
	}
	public BusStop getDestinationStop() {
		return destinationStop;
	}
	public void setDestinationStop(BusStop destinationStop) {
		this.destinationStop = destinationStop;
	}
	public Double getDistanceInKM() {
		return distanceInKM;
	}
	public void setDistanceInKM(Double distanceInKM) {
		this.distanceInKM = distanceInKM;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
