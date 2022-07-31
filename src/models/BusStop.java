package models;

import java.util.Objects;
import src.com.brunomnsilva.smartgraph.graphview.SmartLabelSource;

public class BusStop {
	private Integer stopNumber;
	private String stopStreetName;
	private Integer stopStreetNumber;
	private Boolean enabled;
	
	private static String defaultStyle = "-fx-stroke: #61B5F1;-fx-fill: #B1DFF7;";
	private static String disabledStyle = "-fx-fill: #C3D3DB;-fx-stroke: #A8C5D9;";  
	
	public BusStop() {
		this.enabled = true;
	}
	public BusStop(Integer stopNumber, String stopStreetName, Integer stopStreetNumber, Boolean enabled) {
		this.stopNumber = stopNumber;
		this.stopStreetName = stopStreetName;
		this.stopStreetNumber = stopStreetNumber;
		this.enabled = enabled;
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
	public static String getDefaultStyle() {
		return BusStop.defaultStyle;
	}
	public static String getDisabledStyle() {
		return BusStop.disabledStyle;
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
	public Boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public String toString() {
		return stopStreetName+" "+stopStreetNumber;
	}
	
}
