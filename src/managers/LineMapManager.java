package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.BusLineRoute;
import models.BusStop;
import models.busline.BusLine;
import models.utils.MyHeap;
import src.com.brunomnsilva.smartgraph.graph.Digraph;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

public class LineMapManager {
	private static MapManager instance;
	private Digraph<BusStop, BusLineRoute> map;
	private SmartGraphPanel<BusStop, BusLineRoute> mapView;
	private Set<BusLine> busLines;
	
	private LineMapManager() {
		map = new DigraphEdgeList<>();
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
	}
	
	public List<BusLineRoute> fastestPath(BusStop sourceStop,BusStop destinationStop) {
		List<BusLineRoute> routeMin = new ArrayList<>();
		Map<BusStop,Integer> minTimeToStop = new HashMap<>();
		MyHeap<BusStop> stopHeap = new MyHeap<BusStop>((BusStop b1,BusStop b2) -> minTimeToStop.get(b1).compareTo(minTimeToStop.get(b2)));
		Set<BusStop> visited = new HashSet();
		minTimeToStop.put(sourceStop,0);
		stopHeap.push(sourceStop);
		while(!stopHeap.empty()) {
			BusStop actualStop = stopHeap.top();
			stopHeap.pop();
			if(!visited.contains(actualStop)) {
				visited.add(actualStop);
				for(BusLineRoute route: map.outboundEdges(actualStop){
					Integer minTimeToActualStop = minTimeToStop.get(actualStop); 
					if(minTimeToActualStop+route.getEstimatedTime() <= ) {
						
					}
				}
			}
		}
		return routeMin;
	}
	public static void main(String[] arg) {
		Map<BusStop,Integer> minTimeStop = new HashMap<>();
		MyHeap<BusStop> stopHeap = new MyHeap<BusStop>((BusStop b1,BusStop b2) -> minTimeStop.get(b1).compareTo(minTimeStop.get(b2)));
		BusStop b1 = new BusStop(1,"",1,true);
		BusStop b2 = new BusStop(2,"",2,true);
		BusStop b3 = new BusStop(3,"",3,true);
		BusStop b4 = new BusStop(4,"",4,true);
		minTimeStop.put(b1,5);
		minTimeStop.put(b2,0);
		minTimeStop.put(b3,-41);
		minTimeStop.put(b4,100);
		stopHeap.push(b1);
		stopHeap.push(b2);
		stopHeap.push(b3);
		stopHeap.push(b4);
		while(!stopHeap.empty()) {
			System.out.println(stopHeap.top());
			stopHeap.pop();
		}
	}
	
}
