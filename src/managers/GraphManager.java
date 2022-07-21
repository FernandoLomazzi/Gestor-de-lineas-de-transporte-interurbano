package managers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import db.dao.BusStopDao;
import db.dao.RouteDao;
import db.dao.impl.BusStopDaoPG;
import db.dao.impl.RouteDaoPG;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import models.BusStop;
import models.Route;
import src.com.brunomnsilva.smartgraph.graph.Digraph;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graph.Edge;
import src.com.brunomnsilva.smartgraph.graph.Vertex;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import trash.MapDao;
import trash.MapDaoPG;

public class GraphManager {
	private static GraphManager instance;
	private Digraph<BusStop, Route> map;
	private SmartGraphPanel<BusStop, Route> mapView;
	
	private GraphManager() {
		map = new DigraphEdgeList<>();
		chargeMap();
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
	}
	private void chargeMap() {
		BusStopDao busStopDao = new BusStopDaoPG();
		RouteDao routeDao = new RouteDaoPG();
		List<BusStop> busStops = busStopDao.getStopMap();
		List<Route> routes = routeDao.getRouteMap();
		busStops.forEach(b -> map.insertVertex(b));
		routes.forEach(r -> map.insertEdge(r.getSourceStop(), r.getDestinationStop(), r));
	}
	public static GraphManager getInstance() {
		if(instance==null) {
			instance = new GraphManager();
		}
		return instance;
	}
	public SmartGraphPanel<BusStop, Route> getMapView() {
		return mapView;
	}
	public Digraph<BusStop, Route> getMap() {
		return map;
	}
	public void addStopMap(BusStop busStop) {
		map.insertVertex(busStop);
		this.updateMap();
	}
	public void deleteStopMap(Vertex<BusStop> busStop) {
		map.removeVertex(busStop);
		this.updateMap();
	}
	public void updateMap() {
		mapView.update();
	}
	public void addRouteMap(Route route) {
		map.insertEdge(route.getSourceStop(),route.getDestinationStop(),route);
		this.updateMap();
	}

	public void setVertexDoubleClickAction(Consumer<SmartGraphVertex<BusStop>> c) {
		mapView.setVertexDoubleClickAction(c);
	}
	public void setEdgeDoubleClickAction(Consumer<SmartGraphEdge<Route,BusStop>> c) {
		mapView.setEdgeDoubleClickAction(c);
	}
}
