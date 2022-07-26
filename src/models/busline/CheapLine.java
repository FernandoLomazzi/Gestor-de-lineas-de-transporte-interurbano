package models.busline;

public class CheapLine extends BusLine {
	private Integer standingCapacity; // derived
	private Double standingCapacityPercentage;
	private static final Double ticketPercentagePerUse = 0.02;
	private static final Double maxStandingCapacityPercentage = 0.4;
	
	public double getStandingCapacityPercentage() {
		return standingCapacityPercentage;
	}

	public void setStandingCapacityPercentage(Double standingCapacityPercentage) {
		this.standingCapacityPercentage = standingCapacityPercentage;
	}
}
