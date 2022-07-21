package db.dao;

import java.sql.SQLException;
import java.util.List;

import models.BusStop;

public interface BusStopDao extends Dao<BusStop> {
	public BusStop getBusStop(Integer busStopNumber) throws SQLException ;
	public Boolean deleteBusStop(Integer busStopNumber);
	public List<BusStop> getStopMap();
}
