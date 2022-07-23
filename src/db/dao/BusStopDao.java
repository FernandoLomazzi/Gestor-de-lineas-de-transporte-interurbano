package db.dao;

import java.sql.SQLException;
import java.util.List;

import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import models.BusStop;

public interface BusStopDao extends Dao<BusStop> {
	public BusStop getBusStop(Integer busStopNumber) throws DBConnectionException;
	public Boolean isEnabled(BusStop busStop) throws DBConnectionException;
	public List<BusStop> getStopMap() throws DBConnectionException;
}
