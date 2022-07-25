package models;

import models.busline.BusLine;

public class BusLineRoute {
	private BusLine busLine;
	private Route route;
	private Integer estimatedTime; //seconds
	
	public BusLineRoute(BusLine busLine,Route route,Integer estimatedTime) {
		this.busLine = busLine;
		this.route = route;
		this.estimatedTime = estimatedTime;
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
