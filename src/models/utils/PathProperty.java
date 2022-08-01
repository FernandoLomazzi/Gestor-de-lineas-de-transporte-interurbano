package models.utils;

public class PathProperty {
	private Double pathCost;
	private Double pathDistanceInKM;
	private Integer pathEstimatedTimeInSeconds;
	public PathProperty(Double pathCost, Double pathDistanceInKM, Integer pathEstimatedTimeInSeconds) {
		this.pathCost = pathCost;
		this.pathDistanceInKM = pathDistanceInKM;
		this.pathEstimatedTimeInSeconds = pathEstimatedTimeInSeconds;
	}
	public String costToString() {
		return String.format("$%.2f", pathCost);
	}
	public String distanceToString() {
		return String.format("%.2f [km]", pathDistanceInKM);
	}
	public String estimatedTimeToString() { 
		return String.format("%sh%sm%ss", this.hours(),this.minutes(),this.seconds());
	}
	private Integer hours() {
		return pathEstimatedTimeInSeconds/3600;
	}
	private Integer minutes() {
		return (pathEstimatedTimeInSeconds%3600)/60;
	}
	private Integer seconds() {
		return pathEstimatedTimeInSeconds%60;
	}
}
