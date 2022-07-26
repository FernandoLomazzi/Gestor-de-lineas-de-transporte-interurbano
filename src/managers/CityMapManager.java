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
	
	public static final String enabledStopStyle = "-fx-stroke: #61B5F1;-fx-fill: #B1DFF7;";
	public static final String disabledStopStyle = "-fx-fill: #C3D3DB;-fx-stroke: #A8C5D9;";
	public static final String enabledRouteStyle = "-fx-stroke: #FF6D66;";
	public static final String disabledRouteStyle = "-fx-stroke: #FFA19D;";

	private CityMapManager() {
		map = new DigraphEdgeList<>();
		BusStopDao busStopDao = new BusStopDaoPG();
		RouteDao routeDao = new RouteDaoPG();
		List<BusStop> busStops;
		List<Route> routes;
		try {
			busStops = busStopDao.getStopMap();
			routes = routeDao.getRouteMap();
		} catch (DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", e.getMessage());
			return;
		}
		busStops.forEach(b -> map.insertVertex(b));
		routes.forEach(r -> map.insertEdge(r.getSourceStop(), r.getDestinationStop(), r));
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
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
			mapView.getStylableEdge(route).setStyle(disabledRouteStyle);
		}
		this.updateMapView();
	}
	public void enableStyleStop(BusStop busStop) {
		setStyleStopMap(busStop,enabledStopStyle,enabledRouteStyle);
	}
	public void disableStyleStop(BusStop busStop) {
		setStyleStopMap(busStop,disabledStopStyle,disabledRouteStyle);
	}
	public void initStyleMap() {
        map.vertices().forEach((Vertex<BusStop> b) -> {
        	if(!b.element().isEnabled()) {
        		this.setStyleStopMap(b.element(),disabledStopStyle,disabledRouteStyle);
        	}
        });
	}
	private void setStyleStopMap(BusStop busStop,String stopStlye,String routeStyle) {
		mapView.getStylableVertex(busStop).setStyle(stopStlye);
		map.incidentEdges(busStop).forEach((Edge<Route, BusStop> ed) -> {
			mapView.getStylableEdge(ed).setStyle(routeStyle);
		});
		map.outboundEdges(busStop).forEach((Edge<Route, BusStop> ed) -> {
			mapView.getStylableEdge(ed).setStyle(routeStyle);
		});
		this.updateMapView();
	}
}
