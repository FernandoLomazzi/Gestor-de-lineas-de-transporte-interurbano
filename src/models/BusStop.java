package models;

import java.util.Objects;

public class BusStop {
	private Integer stopNumber;
	private String stopStreetName;
	private Integer stopStreetNumber;
	private Boolean active;
	
	public BusStop() {
		this.active = true;
	}
	
	public BusStop(Integer stopNumber, String stopStreetName, Integer stopStreetNumber, Boolean active) {
		this.stopNumber = stopNumber;
		this.stopStreetName = stopStreetName;
		this.stopStreetNumber = stopStreetNumber;
		this.active = active;
	}

	@Override
	public int hashCode() {
		return Objects.hash(stopNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BusStop)) {
			return false;
		}
		BusStop other = (BusStop) obj;
		return Objects.equals(stopNumber, other.stopNumber) && Objects.equals(stopStreetName, other.stopStreetName)
				&& Objects.equals(stopStreetNumber, other.stopStreetNumber);
	}

	public Integer getStopNumber() {
		return stopNumber;
	}
	public void setStopNumber(Integer stopNumber) {
		this.stopNumber = stopNumber;
	}
	public String getStopStreetName() {
		return stopStreetName;
	}
	public void setStopStreetName(String stopStreetName) {
		this.stopStreetName = stopStreetName;
	}
	public Integer getStopStreetNumber() {
		return stopStreetNumber;
	}
	public void setStopStreetNumber(Integer stopStreetNumber) {
		this.stopStreetNumber = stopStreetNumber;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return stopStreetName+" "+stopStreetNumber;
	}
	
}
