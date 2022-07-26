package managers;

import java.util.List;
import java.util.function.Consumer;

import models.BusStop;
import models.Route;
import src.com.brunomnsilva.smartgraph.graph.Digraph;
import src.com.brunomnsilva.smartgraph.graph.Edge;
import src.com.brunomnsilva.smartgraph.graph.Vertex;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;

public abstract class MapManager {
	protected Digraph<BusStop, Route> map;
	protected SmartGraphPanel<BusStop, Route> mapView;
	
	public List<BusStop> getBusStops(){
		return map.vertices().stream().map(v -> v.element()).toList();
	}
	public List<Route> getRoutes(){
		return map.edges().stream().map(e -> e.element()).toList();
	}
	//Sacar
	public SmartGraphPanel<BusStop, Route> getMapView() {
		return mapView;
	}
	//Sacar
	public Digraph<BusStop, Route> getMap() {
		return map;
	}
	public void addStopMap(BusStop busStop) {
		map.insertVertex(busStop);
		this.updateMapView();
	}
	public void deleteStopMap(Vertex<BusStop> busStop) {
		map.removeVertex(busStop);
		this.updateMapView();
	}
	public void addRouteMap(Route route) {
		map.insertEdge(route.getSourceStop(),route.getDestinationStop(),route);
		this.updateMapView();
	}
	public void deleteRouteMap(Edge<Route,BusStop> route) {
		map.removeEdge(route);
		this.updateMapView();
	}
	public void updateMapView() {
		mapView.update();
	}
	public void setVertexDoubleClickAction(Consumer<SmartGraphVertex<BusStop>> c) {
		mapView.setVertexDoubleClickAction(c);
	}
	public void setEdgeDoubleClickAction(Consumer<SmartGraphEdge<Route,BusStop>> c) {
		mapView.setEdgeDoubleClickAction(c);
	}
	public void initView() {
		mapView.init();
	}
}
