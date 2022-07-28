package db.dao;

import java.util.List;

import exceptions.DBConnectionException;
import models.busline.PremiumLine;

public interface PremiumLineDao extends Dao<PremiumLine> {
	public List<PremiumLine> getAllPremiumLines() throws DBConnectionException;
}
