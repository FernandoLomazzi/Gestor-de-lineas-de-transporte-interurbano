package managers;

import java.util.List;

import db.dao.BusLineDao;
import db.dao.impl.BusLineDaoPG;
import exceptions.DBConnectionException;
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
		try {
			busLines = busLineDao.getAllBusLines();
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(busLines);
		for(BusLine busLine: busLines) {
			System.out.println(busLine.getName());
			System.out.println(busLine.getRoutes());
			System.out.println(busLine.getBusStops());
			busLine.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));	
		}
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
	}
	public void initView() {
		super.initView();
		mapView.updateAndWait();
		for(BusLine busLine: busLines) {
			String styleEdge = "-fx-stroke: #"+busLine.getColor().substring(2)+";";
			busLine.getRoutes().forEach(r -> this.setEdgeStyle(r, styleEdge));	
		}
		this.updateMapView();
	}
	
}
