package models.busline;

import java.util.List;

import models.BusLineRoute;
import models.BusLineStop;

public class CheapLine extends BusLine {
	private Integer standingCapacity; // derived
	private Double standingCapacityPercentage;
	private static final Double ticketPercentagePerUse = 0.02;
	private static final Double maxStandingCapacityPercentage = 0.4;
	
	public CheapLine() {
		super();
	}
	public CheapLine(String name,String color) {
		super(name,color);
	}
	public CheapLine(List<BusLineStop> busStops,List<BusLineRoute> routes) {
		super(busStops,routes);
	}
	public Double getStandingCapacityPercentage() {
		return standingCapacityPercentage;
	}

	public void setStandingCapacityPercentage(Double standingCapacityPercentage) {
		this.standingCapacityPercentage = standingCapacityPercentage;
	}
	
	public static Double getMaxStandingCapacityPercentage() {
		return maxStandingCapacityPercentage;
	}
}
