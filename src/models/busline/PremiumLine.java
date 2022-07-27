package models.busline;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.BusLineRoute;
import models.BusLineStop;

public class PremiumLine extends BusLine {
	public enum PremiumLineService{
		WIFI,AIR_CONDITIONING
	}
	private Set<PremiumLineService> services;
	private static final Double ticketPercentagePerUse = 0.1;
	private static final Double ticketPercentagePerService = 0.05;
	
	public PremiumLine() {
		super();
		services = EnumSet.allOf(PremiumLineService.class);
	}
	protected PremiumLine(String name,String color) {
		super(name,color);
		services = new HashSet<>();
	}
	public PremiumLine(List<BusLineStop> busStops,List<BusLineRoute> routes) {
		super(busStops,routes);
		services = new HashSet<>();
	}
    public Set<PremiumLineService> getServices() {
        return services;
    }
}
