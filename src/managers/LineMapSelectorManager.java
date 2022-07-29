package managers;

import models.BusLineRoute;
import models.BusLineStop;
import models.BusStop;
import models.busline.BusLine;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

public class LineMapSelectorManager extends MapManager{
	private BusLine busLine;
	private String routeLineStyle = "";
	private String stopLineStyle = "";
	private String stopLineDisabledStyle = "";
	
	public LineMapSelectorManager() {
		map = new DigraphEdgeList<>();
		CityMapManager cityMapManager = CityMapManager.getInstance();
		cityMapManager.getBusStops().forEach(b -> map.insertVertex(b));
		cityMapManager.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
	}
	public void initView() {
		super.initView();
		mapView.updateAndWait();
		busLine.getBusStops().forEach(s -> this.setStopLineStyle(s));
		busLine.getRoutes().forEach(r -> this.setEdgeStyle(r, routeLineStyle));
		this.updateMapView();
	}
	public void addStopLine(BusStop busStop) {
		BusLineStop stopLine = new BusLineStop(busLine,busStop,true);
		busLine.addStopLine(stopLine);
		this.setStopLineStyle(stopLine);
	}
	public void setStopLineStyle(BusLineStop stopLine) {
		if(stopLine.stops()) {
			this.setVertexStyle(stopLine.getBusStop(), stopLineStyle);
		}
		else {
			this.setVertexStyle(stopLine.getBusStop(), stopLineDisabledStyle);
		}
	}
	public void addRouteLine(BusLineRoute route) {
		map.insertEdge(route.getSourceStop(),route.getDestinationStop(),route);
		busLine.addRouteLine(route);
		mapView.updateAndWait();
		this.setEdgeStyle(route, routeLineStyle);
		mapView.updateAndWait();
		//this.updateMapView();
	}
	public BusLine getBusLine() {
		return busLine;
	}
	public void toggleStop(BusStop busStop) {
		BusLineStop lineStop = busLine.getBusLineStop(busStop);
		this.setStopLineStyle(lineStop);
		lineStop.toggleStops();
	}
	public void setBusline(BusLine busLine) {
		this.busLine=busLine;
		String busLineColor = busLine.getColorStyle();
		routeLineStyle = "-fx-stroke: #"+busLineColor+";";
		stopLineStyle = "-fx-stroke: #"+busLineColor+";-fx-fill: #"+busLineColor+";";
		stopLineDisabledStyle = "-fx-stroke: #"+busLine.getColorDisabledStyle()+";-fx-fill: #"+busLine.getColorDisabledStyle()+";";
		busLine.getRoutes().forEach(r -> {
			map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r);
		});
	}
	public Boolean contains(BusLineRoute busLineRoute) {
		return busLine.getRoutes().contains(busLineRoute);
	}
	public Boolean contains(BusStop busStop) {
		return busLine.getBusStops().stream().map(b -> b.getBusStop()).filter(b -> b.equals(busStop)).count()==1;
	}
	public void clear() {
		busLine.getBusStops().clear();
		busLine.getRoutes().clear();
		map.vertices().forEach(v -> {
			this.setVertexStyle(v.element(),BusStop.getDefaultStyle());
		});
		map.edges().forEach(e -> {
			if(e.element() instanceof BusLineRoute)
				map.removeEdge(e);
		});
		this.updateMapView();
	}
	
}
