package models.busline;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.scene.paint.Color;
import models.BusLineRoute;
import models.BusLineStop;
import models.BusStop;
import models.Route;

public abstract class BusLine {
	protected String name;
	//Cambiar color
	protected Color color;
	protected Integer seatingCapacity;
	protected List<BusLineRoute> routes;
	protected List<BusLineStop> busStops;
	protected static final Double ticketCostPerKM = 5.5;
	
	protected BusLine() {
		routes = new ArrayList<>();
		busStops = new ArrayList<>(); 
	}
	protected BusLine(String name, Color color, Integer seatingCapacity) {
		this.name = name;
		this.color = color;
		this.seatingCapacity = seatingCapacity;
		this.routes = new ArrayList<>();
		this.busStops = new ArrayList<>();
	}
	protected BusLine(String name,Color color) {
		routes = new ArrayList<>();
		busStops = new ArrayList<>();
		this.name=name;
		this.color=color;
	}
	protected BusLine(List<BusLineStop> busStops,List<BusLineRoute> routes) {
		this.busStops=busStops;
		this.routes=routes;
	}
	public abstract String getType();
	public abstract Double calculateCost(BusLineRoute route);
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

	public void setColor(Color color) {
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
	public Color getColor() {
		return color;
	}
	public String getColorStyle() {
		return color.toString().substring(2);
	}
	public String getColorDisabledStyle() {
		return color.darker().toString().substring(2);
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
	private Integer inDegree(BusStop busStop) {
		return (int) routes.stream().filter(r -> r.getDestinationStop().equals(busStop)).count();
	}
	private Integer outDegree(BusStop busStop) {
		return (int) routes.stream().filter(r -> r.getSourceStop().equals(busStop)).count();
	}
	public void addRouteLine(BusLineRoute route) {
		routes.add(route);
	}
	public void addStopLine(BusLineStop lineStop) {
		busStops.add(lineStop);
	}
	public BusStop getBeginStop() {
		return busStops.stream()
				.map(b -> b.getBusStop())
				.filter(b -> this.inDegree(b)==0).findAny().get();
	}
	public BusStop getEndStop() {
		return busStops.stream()
				.map(b -> b.getBusStop())
				.filter(b -> this.outDegree(b)==0).findAny().get();
	}

	public BusLineRoute getLineRoute(BusStop sourceStop,BusStop destinationStop) {
		for(BusLineRoute route : routes) {
			if(route.getSourceStop().equals(sourceStop) && route.getDestinationStop().equals(destinationStop))
				return route;
		}
		return null;
	}
	
	public Boolean validateChanges(BusLine busLine) {
		return this.color.equals(busLine.getColor()) && this.seatingCapacity.equals(busLine.getSeatingCapacity());
	}
}
