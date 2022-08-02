package managers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import db.dao.BusStopDao;
import db.dao.RouteDao;
import db.dao.impl.BusStopDaoPG;
import db.dao.impl.RouteDaoPG;
import exceptions.DBConnectionException;
import javafx.scene.control.Alert.AlertType;

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

public class CityMapManager extends MapManager{
	private static CityMapManager instance;	
	private Boolean initialized;
	
	private CityMapManager() {
		this.initialized = false;
		map = new DigraphEdgeList<>();
		BusStopDao busStopDao = new BusStopDaoPG();
		RouteDao routeDao = new RouteDaoPG();
		List<BusStop> busStops;
		List<Route> routes;
		try {
			busStops = busStopDao.getStopMap();
			routes = routeDao.getRouteMap();
		} catch (DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", e.getMessage()).showAndWait();
			return;
		}
		busStops.forEach(b -> map.insertVertex(b));
		routes.forEach(r -> map.insertEdge(r.getSourceStop(), r.getDestinationStop(), r));
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
		mapView.setAutomaticLayout(true);
	}
	
	public static CityMapManager getInstance() {
		if(instance==null) {
			instance = new CityMapManager();
		}
		return instance;
	}
	public void addRouteMap(Route route) {
		map.insertEdge(route.getSourceStop(),route.getDestinationStop(),route);
		if(!route.isEnabled()) {
			mapView.updateAndWait();
			mapView.getStylableEdge(route).setStyle(Route.getDisabledStyle());
		}
		this.updateMapView();
	}
	public void enableStyleStop(BusStop busStop) {
		setStyleStopMap(busStop,BusStop.getDefaultStyle(),Route.getDefaultStyle());
	}
	public void disableStyleStop(BusStop busStop) {
		setStyleStopMap(busStop,BusStop.getDisabledStyle(),Route.getDisabledStyle());
	}
	@Override
	public void initView() {
		if(initialized) 
			return;
		mapView.init();
		initStyleMap();
		initialized = true;
	}
	private void initStyleMap() {
        map.vertices().forEach((Vertex<BusStop> b) -> {
        	if(!b.element().isEnabled()) {
        		this.setStyleStopMap(b.element(),BusStop.getDisabledStyle(),Route.getDisabledStyle());
        	}
        });
	}
	private void setStyleStopMap(BusStop busStop,String stopStyle,String routeStyle) {
		this.setVertexStyle(busStop, stopStyle);
		map.incidentEdges(busStop).forEach((Edge<Route, BusStop> ed) -> {
			this.setEdgeStyle(ed.element(), routeStyle);
		});
		map.outboundEdges(busStop).forEach((Edge<Route, BusStop> ed) -> {
			this.setEdgeStyle(ed.element(), routeStyle);
		});
		this.updateMapView();
	}
	@Override
	protected void setEdgeStyle(Route route,String style) {
		SmartGraphEdge<Route,BusStop> ed = (SmartGraphEdge<Route,BusStop>) mapView.getStylableEdge(route);
		ed.setStyle(style);
		ed.getStylableArrow().setStyle(style);
	}
}
