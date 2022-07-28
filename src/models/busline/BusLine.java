package models.busline;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import models.BusLineRoute;
import models.BusLineStop;
import models.BusStop;
import models.Route;

public abstract class BusLine {
	protected String name;
	//Cambiar color
	protected String color;
	protected Integer seatingCapacity;
	protected List<BusLineRoute> routes;
	protected List<BusLineStop> busStops;
	
	public abstract String getType();
	protected static final Double ticketCostPerKM = 5.5;
	protected BusLine() {
		routes = new ArrayList<>();
		busStops = new ArrayList<>(); 
	}
	protected BusLine(String name, String color, Integer seatingCapacity) {
		this.name = name;
		this.color = color;
		this.seatingCapacity = seatingCapacity;
		this.routes = new ArrayList<>();
		this.busStops = new ArrayList<>();
	}
	protected BusLine(String name,String color) {
		routes = new ArrayList<>();
		busStops = new ArrayList<>();
		this.name=name;
		this.color=color;
	}
	protected BusLine(List<BusLineStop> busStops,List<BusLineRoute> routes) {
		this.busStops=busStops;
		this.routes=routes;
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
	public void addNewStopLine(BusStop busStop) {
		busStops.add(new BusLineStop(this,busStop,true));
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
	public static void main(String[] arg) {
		BusLine bl = new CheapLine("Hola","rojo");
		BusStop b1 = new BusStop(1,"b1",111,true);
		BusStop b2 = new BusStop(2,"b2",222,true);
		//BusStop b3 = new BusStop(3,"b3",333,true);
		bl.addStopLine(new BusLineStop(bl,b1,true));
		bl.addStopLine(new BusLineStop(bl,b2,true));
		//bl.insertStop(new BusLineStop(bl,b3,true));
		Route r1 = new Route(b1,b2,20.0);
		//Route r2 = new Route(b2,b3,20.0);
		bl.addRouteLine(new BusLineRoute(bl,r1));
		//bl.insertRoute(new BusLineRoute(bl,r2));
		System.out.println(bl.getBusStops());
		System.out.println(bl.getRoutes());
		System.out.println(bl.inDegree(b1));
		System.out.println(bl.inDegree(b2));
		System.out.println(bl.outDegree(b1));
		System.out.println(bl.outDegree(b2));
		System.out.println(bl.getBeginStop());
		System.out.println(bl.getEndStop());
		//System.out.println(bl.inDegree(b3));
		
	}
}
