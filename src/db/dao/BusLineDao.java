package db.dao;

import java.util.List;

import models.busline.BusLine;

public interface BusLineDao extends Dao<BusLine>{
	public List<BusLine> getAllBusLines();
}
