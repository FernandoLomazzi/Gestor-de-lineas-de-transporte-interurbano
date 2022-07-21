package models.busline;

public class CheapLine extends BusLine {
	private Integer standingCapacity; // derived
	private Double standingCapacityPercentage;
	private static final Double ticketPercentagePerUse = 0.02;
	private static final Double maxStandingCapacityPercentage = 0.4;
}
