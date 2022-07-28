package db.dao;

import java.util.List;

import exceptions.DBConnectionException;
import models.busline.CheapLine;

public interface CheapLineDao extends Dao<CheapLine>{
	public List<CheapLine> getAllCheapLines() throws DBConnectionException;
}
