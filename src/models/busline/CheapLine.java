package models.busline;

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
