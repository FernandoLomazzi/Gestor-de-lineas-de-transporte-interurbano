package models;

import java.util.Objects;

import models.busline.BusLine;
import src.com.brunomnsilva.smartgraph.graphview.SmartLabelSource;

public class BusLineRoute extends Route{
	private BusLine busLine;
	private Route route;
	private Integer estimatedTime; //seconds
	
	public BusLineRoute(BusLine busLine,Route route) {
		this.busLine=busLine;
		this.route = route;
		this.estimatedTime = Integer.MAX_VALUE;
	}
	public BusLineRoute(BusLine busLine,Route route,Integer estimatedTime) {
		this.busLine = busLine;
		this.route = route;
		this.estimatedTime = estimatedTime;
	}
	
	@SmartLabelSource
	public String routeLineLabel() { 
		return String.format("%sh%sm%ss", this.hours(),this.minutes(),this.seconds());
	}
	private Integer hours() {
		return estimatedTime/3600;
	}
	private Integer minutes() {
		return (estimatedTime%3600)/60;
	}
	private Integer seconds() {
		return estimatedTime%60;
	}
	@Override
	public int hashCode() {
		return Objects.hash(busLine, route);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BusLineRoute)) {
			return false;
		}
		BusLineRoute other = (BusLineRoute) obj;
		return Objects.equals(busLine, other.busLine) && Objects.equals(route, other.route);
	}
	@Override
	public String toString() {
		return route.toString();
	}

	public Double getDistanceInKM() {
		return route.getDistanceInKM();
	}
	public BusLineStop getSourceLineStop() {
		return busLine.getBusLineStop(this.getSourceStop());
	}
	public BusLineStop getDestinationLineStop() {
		return busLine.getBusLineStop(this.getDestinationStop());
	}
	public BusStop getSourceStop() {
		return route.getSourceStop();
	}
	public BusStop getDestinationStop() {
		return route.getDestinationStop();
	}
	
	public BusLine getBusLine() {
		return busLine;
	}
	public void setBusLine(BusLine busLine) {
		this.busLine = busLine;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public Integer getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(Integer estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
}
