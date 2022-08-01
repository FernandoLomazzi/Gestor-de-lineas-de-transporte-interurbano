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
			busLine.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));
		}
		mapView = new SmartGraphPanel<>(map,new SmartCircularSortedPlacementStrategy());
	}
	public void addLine(BusLine busLine) {
		busLine.getRoutes().forEach(r -> map.insertEdge(r.getSourceStop(),r.getDestinationStop(),r));
		mapView.updateAndWait();
		busLine.getRoutes().forEach(r -> this.setEdgeStyle(r, getRouteStyle(busLine.getColorStyle())));	
		this.updateMapView();
	}
	public void chargeLine(BusLine busLine) {
        busLine.getRoutes().forEach(r -> this.setEdgeStyle(r, getRouteStyle(busLine.getColorStyle())));    
        this.updateMapView();
    }
	public void removeLine(BusLine busLine) {
        map.edges().forEach(edge -> {
            if(busLine.getRoutes().contains(edge.element()))
                map.removeEdge(edge);
        });
        this.updateMapView();
    }
	public void initView() {
		System.out.println(
				mapView.getHeight()+" - "+
				mapView.getWidth());
		super.initView();
		mapView.updateAndWait();
		for(BusLine busLine: busLines) {
			busLine.getRoutes().forEach(r -> this.setEdgeStyle(r, getRouteStyle(busLine.getColorStyle())));	
		}
		this.updateMapView();
	}
	public List<BusLine> getAllBusLines() {
		return busLines;
	}
}
