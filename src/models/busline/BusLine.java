package models.busline;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import models.BusLineRoute;
import models.BusLineStop;
import models.BusStop;

public abstract class BusLine {
	protected String name;
	//Cambiar color
	protected String color;
	protected Integer seatingCapacity;
	protected List<BusLineRoute> routes;
	protected List<BusLineStop> busStops;
	
	protected static final Double ticketCostPerKM = 5.5;
	protected BusLine(String name,String color) {
		routes = new ArrayList<>();
		busStops = new ArrayList<>();
		this.name=name;
		this.color=color;
	}
	public List<BusLineRoute> outboundEdges(BusLineStop busStop){
		List<BusLineRoute> ret = new ArrayList<>();
		for(BusLineRoute route: routes) {
			if(route.getSourceStop().equals(busStop.getBusStop()))
				ret.add(route);
		}
		return ret;
	}
	public BusLineStop getBusLineStop(BusStop busStop) {
		for(BusLineStop stop: this.busStops) {
			if(stop.getBusStop().equals(busStop))
				return stop;
		}
		return null;
	}
	public Boolean stops(BusStop busStop) {
		for(BusLineStop stop: this.busStops) {
			if(stop.getBusStop().equals(busStop) && stop.stops())
				return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BusLine)) {
			return false;
		}
		BusLine other = (BusLine) obj;
		return Objects.equals(name, other.name);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setSeatingCapacity(Integer seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public void setRoutes(List<BusLineRoute> routes) {
		this.routes = routes;
	}

	public void setBusStops(List<BusLineStop> busStops) {
		this.busStops = busStops;
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
