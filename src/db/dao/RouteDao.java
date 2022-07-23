package db.dao;


import java.util.List;

import models.BusStop;
import models.Route;

public interface RouteDao extends Dao<Route>{
	public Boolean deleteRoute(BusStop sourceStop,BusStop destinationStop);
	public Integer getRouteID(BusStop sourceStop,BusStop destinationStop);
	public List<Route> getRouteMap();
}
