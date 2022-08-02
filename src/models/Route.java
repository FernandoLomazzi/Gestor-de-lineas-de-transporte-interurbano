package models;

import java.util.Objects;
import src.com.brunomnsilva.smartgraph.graphview.SmartLabelSource;

public class Route {
	private BusStop sourceStop,destinationStop;
	private Double distanceInKM;

	private static String defaultStyle = "-fx-stroke: #26b1fc; -fx-opacity: 0.8;";
	private static String disabledStyle = "-fx-stroke: #26b1fc; -fx-opacity: 0.4;";
	
	public enum distanceUnits{
		Kilómetros,Metros,Millas
	}
	public Route() {
		
	}
	public Route(BusStop sourceStop, BusStop destinationStop, Double distanceInKM) {
		super();
		this.sourceStop = sourceStop;
		this.destinationStop = destinationStop;
		this.distanceInKM = distanceInKM;
	}
	public static String getDefaultStyle() {
		return Route.defaultStyle;
	}
	public static String getDisabledStyle() {
		return Route.disabledStyle;
	}
	@SmartLabelSource
	public String routeLabel() {
		return String.format("%.1f [km]", distanceInKM);
	}

	@Override
	public String toString() {
		return "desde "+sourceStop+" hacia "+destinationStop;
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
		if (!(obj instanceof Route) || obj instanceof BusLineRoute) {
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
	public Boolean isEnabled() {
		return this.sourceStop.isEnabled() && this.destinationStop.isEnabled();
	}
}
