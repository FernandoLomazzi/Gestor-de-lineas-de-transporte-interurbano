package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import db.dao.BusLineDao;
import db.dao.impl.BusLineDaoPG;
import exceptions.DBConnectionException;
import exceptions.NoPathException;
import javafx.scene.control.Alert.AlertType;
import models.BusLineRoute;
import models.BusLineStop;
import models.BusStop;
import models.Route;
import models.busline.BusLine;
import models.utils.MyHeap;
import models.utils.PathProperty;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

public class LinePathManager extends MapManager{
	List<BusLine> busLines;
	List<BusLineRoute> minPath;
	
	public LinePathManager(){
		minPath = new ArrayList<>();
		map = new DigraphEdgeList<>();
		CityMapManager cityMapManager = CityMapManager.getInstance();
		cityMapManager.getBusStops().forEach(b -> map.insertVertex(b));
		cityMapManager.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));
		BusLineDao busLineDao = new BusLineDaoPG();
		try {
			busLines = busLineDao.getAllBusLines();
		} catch (DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", e.getMessage());
		}
		for(BusLine busLine: busLines) {
			busLine.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));
		}
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
		mapView.setAutomaticLayout(true);
	}
	public void initView() {
		super.initView();
		mapView.updateAndWait();
		for(BusLine busLine: busLines) {
			busLine.getRoutes().forEach(r -> {
				this.setEdgeStyle(r, getRouteStyle(busLine.getColorStyle()));
				this.hideEdgeStyle(r,true);
			});	
		}
		this.updateMapView();
	}
	private void hideEdgeStyle(BusLineRoute route,Boolean hide) {
		SmartGraphEdge<BusLineRoute,BusStop> ed = (SmartGraphEdge<BusLineRoute,BusStop>) mapView.getStylableEdge(route);
		if(hide)
			this.setEdgeStyle(route, "visibility: hidden;");
		else
			this.setEdgeStyle(route, getRouteStyle(route.getBusLine().getColorStyle()));
	}
	private void clearPath() {
		this.minPath.forEach(r -> this.hideEdgeStyle(r,true));
		this.minPath.clear();
	}
	private PathProperty initNewPath() {
		Integer minTime = 0;
		Double minDist = 0.0,minCost = 0.0;
		for(BusLineRoute route: minPath) {
			minTime += route.getEstimatedTime();
			minDist += route.getDistanceInKM();
			minCost += route.getBusLine().calculateCost(route);
			this.hideEdgeStyle(route, false);
		}
		return new PathProperty(minCost,minDist,minTime);	
	}
	public PathProperty fastestPath(BusStop sourceStop,BusStop destinationStop) throws NoPathException{
		clearPath();
		this.minPath = dijkstraAlgorithm(sourceStop,destinationStop,(BusLineRoute r) -> r.getEstimatedTime().doubleValue());
		return initNewPath();
	}

	public PathProperty shortestPath(BusStop sourceStop,BusStop destinationStop) throws NoPathException{
		clearPath();
		minPath = dijkstraAlgorithm(sourceStop,destinationStop,(BusLineRoute r) -> r.getDistanceInKM());
		return initNewPath();
	}
	public PathProperty cheapestPath(BusStop sourceStop,BusStop destinationStop) throws NoPathException{
		clearPath();
		minPath = dijkstraAlgorithm(sourceStop,destinationStop,(BusLineRoute r) -> r.getBusLine().calculateCost(r));
		return initNewPath();
	}
	private List<BusLineRoute> dijkstraAlgorithm(BusStop sourceStop,BusStop destinationStop,Function<BusLineRoute,Double> costFunction) throws NoPathException{
		Map<BusLineStop,Double> minCostToStop = new HashMap<>();
		MyHeap<BusLineStop> stopHeap = new MyHeap<>((BusLineStop b1,BusLineStop b2) -> minCostToStop.get(b1).compareTo(minCostToStop.get(b2)));
		Set<BusLineStop> visited = new HashSet<>();
		Map<BusLineStop,BusLineStop> parent = new HashMap<>();
		List<BusLineRoute> ret = new ArrayList<>();
		for(BusLine busLine: busLines) {
			if(busLine.stops(sourceStop)) {
				BusLineStop busLineStop = new BusLineStop(busLine,sourceStop,true);
				minCostToStop.put(busLineStop, 0.0);
				stopHeap.push(busLineStop);
			}
		}
		while(!stopHeap.empty()) {
			BusLineStop actualLineStop = stopHeap.top();
			stopHeap.pop();
			if(actualLineStop.getBusStop().equals(destinationStop)) {
				while(!actualLineStop.getBusStop().equals(sourceStop)) {
					BusLineStop prevLineStop = parent.get(actualLineStop);
					BusLine busLine = actualLineStop.getBusLine();
					ret.add(busLine.getLineRoute(prevLineStop.getBusStop(),actualLineStop.getBusStop()));
					actualLineStop = prevLineStop;
				}
				break;
			}
			if(!visited.contains(actualLineStop)) {
				visited.add(actualLineStop);
				Double minToActualStop = minCostToStop.get(actualLineStop);
				BusLine actualLine = actualLineStop.getBusLine();
				List<BusLineRoute> routes;
				//Si para puedo cambiar de linea
				if(actualLineStop.stops()) {
					routes = map.outboundEdges(actualLineStop.getBusStop())
							.stream()
							.filter(e -> e.element() instanceof BusLineRoute)
							.map(e -> (BusLineRoute) e.element())
							.filter(route -> actualLine.equals(route.getBusLine()) || route.getSourceLineStop().stops())
							.toList();
				}
				//Si no para no puedo cambiar de linea
				else
					routes = actualLine.outboundEdges(actualLineStop);
				for(BusLineRoute route: routes) {
					BusLineStop destinationLineStop = route.getDestinationLineStop();
					if(destinationLineStop.getBusStop().equals(destinationStop) && !destinationLineStop.stops())
						continue;
					Double costOfRoute = costFunction.apply(route);
					Double minToDestinationStop = (Optional.ofNullable(minCostToStop.get(destinationLineStop))).orElse(Double.MAX_VALUE);
					if(minToActualStop+costOfRoute < minToDestinationStop) {
						minCostToStop.put(destinationLineStop, minToActualStop+costOfRoute);
						stopHeap.push(destinationLineStop);
						parent.put(destinationLineStop, actualLineStop);
					}
				}
			}
		}
		if(ret.isEmpty())
			throw new NoPathException("No existe camino entre la parada origen de calle "+sourceStop+" y la parada destino de calle "+destinationStop+".");
		return ret;
	}
}
