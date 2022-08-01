package models.busline;

import java.util.List;

import javafx.scene.paint.Color;
import models.BusLineRoute;
import models.BusLineStop;

public class CheapLine extends BusLine {
	private Integer standingCapacity; // derived
	private Double standingCapacityPercentage;
	private static final Double ticketPercentagePerUse = 0.02;
	private static final Double maxStandingCapacityPercentage = 0.4;
	
	public String getType() {
		return "Económica";
	}
	public CheapLine(String name, Color color, Integer seatingCapacity, Double standingCapacityPercentage) {
		super(name, color, seatingCapacity);
		this.standingCapacityPercentage = standingCapacityPercentage;
		this.standingCapacity = (int) Math.floor(standingCapacityPercentage*this.seatingCapacity);
	}
	public CheapLine() {
		super();
	}
	public CheapLine(String name,Color color) {
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
	@Override
	public Double calculateCost(BusLineRoute route) {
		return route.getDistanceInKM()*super.ticketCostPerKM*(1+ticketPercentagePerUse);
	}
}
