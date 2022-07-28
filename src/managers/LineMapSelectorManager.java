package managers;

import java.util.HashSet;

import models.BusLineRoute;
import models.BusLineStop;
import models.BusStop;
import models.Route;
import models.busline.BusLine;
import src.com.brunomnsilva.smartgraph.graph.Digraph;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graph.Edge;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

public class LineMapSelectorManager extends MapManager{
	private BusLine busLine;
	String styleEdge = "";
	
	public LineMapSelectorManager() {
		map = new DigraphEdgeList<>();
		CityMapManager cityMapManager = CityMapManager.getInstance();
		cityMapManager.getBusStops().forEach(b -> map.insertVertex(b));
		cityMapManager.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
		mapView.setAutomaticLayout(true);
	}
	public void initView() {
		super.initView();
		mapView.updateAndWait();
		busLine.getRoutes().forEach(r -> this.setEdgeStyle(r, styleEdge));
		this.updateMapView();
	}
	public void addRouteLine(BusLineRoute route) {
		map.insertEdge(route.getSourceStop(),route.getDestinationStop(),route);
		busLine.addRouteLine(route);
		mapView.updateAndWait();
		this.setEdgeStyle(route, styleEdge);
		this.updateMapView();
	}
	public BusLine getBusLine() {
		return busLine;
	}
	public void setBusline(BusLine busLine) {
		this.busLine=busLine;
		styleEdge = "-fx-stroke: #"+busLine.getColor().substring(2)+";";
		busLine.getRoutes().forEach(r -> {
			map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r);
		});
	}
	public Boolean contains(BusLineRoute busLineRoute) {
		return busLine.getRoutes().contains(busLineRoute);
	}
	public void clear() {
		busLine.getBusStops().clear();
		busLine.getRoutes().clear();
		map.edges().forEach(e -> {
			if(e.element() instanceof BusLineRoute)
				map.removeEdge(e);
		});
		this.updateMapView();
	}
	
}
