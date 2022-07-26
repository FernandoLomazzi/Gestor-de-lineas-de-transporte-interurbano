package models.busline;

import java.util.Set;

public class PremiumLine extends BusLine {
	public enum PremiumLineService{
		WIFI,AIR_CONDITIONING
	}
	private Set<PremiumLineService> services;
	private static final Double ticketPercentagePerUse = 0.1;
	private static final Double ticketPercentagePerService = 0.05;
	protected PremiumLine(String name,String color) {
		super(name,color);
	}
    public Set<PremiumLineService> getServices() {
        return services;
    }
}
