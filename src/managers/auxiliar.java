package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import models.BusLineRoute;
import models.BusLineStop;
import models.BusStop;
import models.Route;
import models.busline.BusLine;
import models.busline.CheapLine;
import models.utils.MyHeap;
import src.com.brunomnsilva.smartgraph.graph.Digraph;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import src.com.brunomnsilva.smartgraph.graph.Edge;

public class auxiliar extends MapManager{
	private static auxiliar instance;
	private Set<BusLine> busLines;
	
	private auxiliar() {
		busLines = new HashSet<>();
		CityMapManager cityMapManager = CityMapManager.getInstance();
		cityMapManager.getBusStops().forEach(b -> map.insertVertex(b));
		cityMapManager.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));
		
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
	}
	public static auxiliar getInstance() {
		if(instance==null) {
			instance = new auxiliar();
		}
		return instance;
	}
	/*
	public Integer fastestPath(BusStop sourceStop,BusStop destinationStop) {
		Function<BusLineRoute,Double> getCostRoute = route -> {
			return (Double) route.getEstimatedTime().doubleValue();
		};
		Function3<Map<BusStop,Double>,BusLineRoute,Boolean> condition = 
		(minToStop,route) -> {
			BusLine actualBusLine = route.getBusLine();
			if()
		};
	}
	private Double dijkstraOnMap(BusStop sourceStop,BusStop destinationStop,Function<BusLineRoute,Double> getCostRoute,Function3<Map<BusStop,Double>,BusLineRoute,Boolean> condition) {
		Map<BusStop,Double> minToStop = new HashMap<>();
		MyHeap<BusStop> stopHeap = new MyHeap<BusStop>((BusStop b1,BusStop b2) -> minToStop.get(b1).compareTo(minToStop.get(b2)));
		Set<BusStop> visited = new HashSet<>();
		minToStop.put(sourceStop,0.0);
		stopHeap.push(sourceStop);
		while(!stopHeap.empty()) {
			BusStop actualStop = stopHeap.top();
			stopHeap.pop();
			if(!visited.contains(actualStop)) {
				visited.add(actualStop);
				for(Edge<BusLineRoute, BusStop> r: map.outboundEdges(actualStop)) {
					BusLineRoute route = r.element();
					Double minToActualStop = minToStop.get(actualStop);
					Double costOfRoute = getCostRoute.apply(route);
					if(condition.apply(minToStop,route)) {
						minToStop.put(route.getDestinationStop(), minToActualStop+costOfRoute);
						stopHeap.push(route.getDestinationStop());
					}
				}
			}
		}
		return minToStop.get(destinationStop);
	}*/
	public Integer fastestPath(BusStop sourceStop,BusStop destinationStop) {
		Map<BusLineStop,Integer> minTimeToStop = new HashMap<>();
		MyHeap<BusLineStop> stopHeap = new MyHeap<>((BusLineStop b1,BusLineStop b2) -> minTimeToStop.get(b1).compareTo(minTimeToStop.get(b2)));
		Set<BusLineStop> visited = new HashSet<>();
		
		Map<BusLineStop,BusLineStop> parent = new HashMap<>();
		
		for(BusLine busLine: busLines) {
			if(busLine.stops(sourceStop)) {
				BusLineStop busLineStop = new BusLineStop(busLine,sourceStop,true);
				minTimeToStop.put(busLineStop, 0);
				stopHeap.push(busLineStop);
			}
		}
		while(!stopHeap.empty()) {
			BusLineStop actualLineStop = stopHeap.top();
			stopHeap.pop();
			if(!visited.contains(actualLineStop)) {
				visited.add(actualLineStop);
				Integer minToActualStop = minTimeToStop.get(actualLineStop);
				List<BusLineRoute> routes;
				//Si para puedo cambiar de linea
				if(actualLineStop.stops())
					routes = map.outboundEdges(actualLineStop.getBusStop()).stream().map(e -> (BusLineRoute) e.element()).toList();
				//Si no para no puedo cambiar de linea
				else 
					routes = actualLineStop.getBusLine().outboundEdges(actualLineStop);
					
				
				for(BusLineRoute route: routes) {
					BusLineStop destinationLineStop = route.getDestinationLineStop();
					Integer costOfRoute = route.getEstimatedTime();
					Integer minToDestinationStop = (Optional.ofNullable(minTimeToStop.get(destinationLineStop))).orElse(Integer.MAX_VALUE);
					if(minToActualStop+costOfRoute < minToDestinationStop) {
						minTimeToStop.put(destinationLineStop, minToActualStop+costOfRoute);
						stopHeap.push(destinationLineStop);
						parent.put(destinationLineStop, actualLineStop);
					}
				}
			}
		}
		Integer ret = Integer.MAX_VALUE;
		BusLineStop minima = new BusLineStop();
		for(BusLine busLine: busLines) {
			if(busLine.stops(destinationStop)) {
				Integer minTimeFinishingInLine = minTimeToStop.get(new BusLineStop(busLine,destinationStop,true));
				if(ret>minTimeFinishingInLine) {
					ret = minTimeFinishingInLine;
					minima = new BusLineStop(busLine,destinationStop,true);
				}
			}
		}
		System.out.println(minTimeToStop);
		while(!minima.getBusStop().equals(sourceStop)) {
			System.out.println("Stop: "+minima.getBusStop()+" - Linea"+minima.getBusLine().getColor());
			minima = parent.get(minima);
		}
		System.out.println("Stop: "+minima.getBusStop()+" - Linea"+minima.getBusLine().getColor());
		return ret;		
	}
	public static void main(String[] arg) {
		BusStop b1 = new BusStop(1,"",1,true);
		BusStop b2 = new BusStop(2,"",2,true);
		BusStop b3 = new BusStop(3,"",3,true);
		BusStop b4 = new BusStop(4,"",4,true);
		BusStop b5 = new BusStop(5,"",5,true);
		BusStop b6 = new BusStop(6,"",6,true);
		Route r1 = new Route(b1,b2,0.0);
		Route r2 = new Route(b1,b3,0.0);
		Route r3 = new Route(b3,b2,0.0);
		Route r4 = new Route(b3,b5,0.0);
		Route r5 = new Route(b2,b5,0.0);
		Route r6 = new Route(b2,b4,0.0);
		Route r7 = new Route(b4,b6,0.0);
		Route r8 = new Route(b5,b6,0.0);
		BusLine bl1 = new CheapLine("l1","rojo");
		BusLineRoute blrRojo = new BusLineRoute(bl1,r2,2);
		BusLineRoute blrRojo2 = new BusLineRoute(bl1,r3,1);
		BusLineStop blsRojo = new BusLineStop(bl1,b1,true);
		BusLineStop blsRojo2 = new BusLineStop(bl1,b3,true);
		BusLineStop blsRojo3 = new BusLineStop(bl1,b2,true);
		bl1.getBusStops().add(blsRojo);
		bl1.getBusStops().add(blsRojo2);
		bl1.getBusStops().add(blsRojo3);
		bl1.getRoutes().add(blrRojo);
		bl1.getRoutes().add(blrRojo2);
		
		BusLine bl2 = new CheapLine("l2","naranja");
		BusLineRoute blrNar = new BusLineRoute(bl2,r1,4);
		BusLineRoute blrNar2 = new BusLineRoute(bl2,r6,2);
		BusLineRoute blrNar3 = new BusLineRoute(bl2,r7,4);
		BusLineStop blsNar = new BusLineStop(bl2,b1,true);
		BusLineStop blsNar2 = new BusLineStop(bl2,b2,true);
		BusLineStop blsNar3 = new BusLineStop(bl2,b4,true);
		BusLineStop blsNar4 = new BusLineStop(bl2,b6,true);
		bl2.getBusStops().add(blsNar);
		bl2.getBusStops().add(blsNar2);
		bl2.getBusStops().add(blsNar3);
		bl2.getBusStops().add(blsNar4);
		bl2.getRoutes().add(blrNar);
		bl2.getRoutes().add(blrNar2);
		bl2.getRoutes().add(blrNar3);		
		
		BusLine bl3 = new CheapLine("l3","azul");
		BusLineRoute blrAzul = new BusLineRoute(bl3,r2,4);
		BusLineRoute blrAzul2 = new BusLineRoute(bl3,r4,10);
		BusLineRoute blrAzul3 = new BusLineRoute(bl3,r8,3);
		BusLineStop blsAzul = new BusLineStop(bl3,b1,true);
		BusLineStop blsAzul2 = new BusLineStop(bl3,b3,true);
		BusLineStop blsAzul3 = new BusLineStop(bl3,b5,true);
		BusLineStop blsAzul4 = new BusLineStop(bl3,b6,true);
		bl3.getBusStops().add(blsAzul);
		bl3.getBusStops().add(blsAzul2);
		bl3.getBusStops().add(blsAzul3);
		bl3.getBusStops().add(blsAzul4);
		bl3.getRoutes().add(blrAzul);
		bl3.getRoutes().add(blrAzul2);
		bl3.getRoutes().add(blrAzul3);
		
		BusLine bl4 = new CheapLine("l4","verde");
		BusLineRoute blrVerde = new BusLineRoute(bl4,r1,10);
		BusLineRoute blrVerde2 = new BusLineRoute(bl4,r5,1);
		BusLineStop blsVerde = new BusLineStop(bl4,b1,true);
		BusLineStop blsVerde2 = new BusLineStop(bl4,b2,true);
		BusLineStop blsVerde3 = new BusLineStop(bl4,b5,true);
		bl4.getBusStops().add(blsVerde);
		bl4.getBusStops().add(blsVerde2);
		bl4.getBusStops().add(blsVerde3);
		bl4.getRoutes().add(blrVerde);
		bl4.getRoutes().add(blrVerde2);
		
		auxiliar lmm = auxiliar.getInstance();
		lmm.map.insertVertex(b1);
		lmm.map.insertVertex(b2);
		lmm.map.insertVertex(b3);
		lmm.map.insertVertex(b4);
		lmm.map.insertVertex(b5);
		lmm.map.insertVertex(b6);
		lmm.map.insertEdge(b1, b3, blrRojo);
		lmm.map.insertEdge(b3, b2, blrRojo2);
		lmm.map.insertEdge(b1, b2, blrNar);
		lmm.map.insertEdge(b2, b4, blrNar2);
		lmm.map.insertEdge(b4, b6, blrNar3);
		lmm.map.insertEdge(b1, b3, blrAzul);
		lmm.map.insertEdge(b3, b5, blrAzul2);
		lmm.map.insertEdge(b5, b6, blrAzul3);
		lmm.map.insertEdge(b1, b2, blrVerde);
		lmm.map.insertEdge(b2, b5, blrVerde2);
		
		lmm.busLines.add(bl1);
		lmm.busLines.add(bl2);
		lmm.busLines.add(bl3);
		lmm.busLines.add(bl4);
		
		System.out.println("ANS: "+lmm.fastestPath(b1, b6));
		
	}
	
}
