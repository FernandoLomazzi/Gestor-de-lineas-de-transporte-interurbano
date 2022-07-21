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
	
}
