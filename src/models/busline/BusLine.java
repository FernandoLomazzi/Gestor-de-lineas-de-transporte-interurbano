package models.busline;

import java.util.List;

import models.BusLineRoute;
import models.BusLineStop;

public abstract class BusLine {
	protected String name;
	//Cambiar color
	protected String color;
	protected Integer seatingCapacity;
	protected List<BusLineRoute> routes;
	protected List<BusLineStop> busStops;
	
	protected static final Double ticketCostPerKM = 5.5;
	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Integer getSeatingCapacity() {
		return seatingCapacity;
	}

	public List<BusLineRoute> getRoutes() {
		return routes;
	}

	public List<BusLineStop> getBusStops() {
		return busStops;
	}
}
