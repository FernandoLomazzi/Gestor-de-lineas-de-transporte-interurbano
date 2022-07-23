package db.dao;


import java.util.List;

import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import models.BusStop;
import models.Route;

public interface RouteDao extends Dao<Route>{
	public List<Route> getRouteMap() throws DBConnectionException;
}
