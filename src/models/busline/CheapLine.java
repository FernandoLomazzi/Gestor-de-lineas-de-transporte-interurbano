package models.busline;

public class CheapLine extends BusLine {
	private Integer standingCapacity; // derived
	private Double standingCapacityPercentage;
	private static final Double ticketPercentagePerUse = 0.02;
	private static final Double maxStandingCapacityPercentage = 0.4;
	
	public CheapLine(String name,String color) {
		super(name,color);
	}
	public double getStandingCapacityPercentage() {
		return standingCapacityPercentage;
	}
}
