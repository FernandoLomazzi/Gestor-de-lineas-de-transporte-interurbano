package trash;

import java.util.List;

import db.dao.Dao;
import models.BusStop;
import models.Route;

public interface MapDao extends Dao<Route>{
	public Boolean deleteRoute(BusStop sourceStop,BusStop destinationStop);
	public List<Route> getMap();
}
