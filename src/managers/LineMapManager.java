package managers;

import java.util.List;

import db.dao.BusLineDao;
import db.dao.impl.BusLineDaoPG;
import models.busline.BusLine;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

public class LineMapManager extends MapManager{
	private List<BusLine> busLines;
	
	public LineMapManager() {
		map = new DigraphEdgeList<>();
		CityMapManager cityMapManager = CityMapManager.getInstance();
		cityMapManager.getBusStops().forEach(b -> map.insertVertex(b));
		cityMapManager.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));
		BusLineDao busLineDao = new BusLineDaoPG();
		busLines = busLineDao.getAllBusLines();
		System.out.println(busLines);
		for(BusLine busLine: busLines) {
			busLine.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));	
		}
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
	}
	public void initView() {
		super.initView();
		mapView.updateAndWait();
		for(BusLine busLine: busLines) {
			String styleEdge = "-fx-stroke: #"+busLine.getColorStyle()+";";
			busLine.getRoutes().forEach(r -> this.setEdgeStyle(r, styleEdge));	
		}
		this.updateMapView();
	}
	
}
