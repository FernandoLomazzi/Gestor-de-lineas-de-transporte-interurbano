package db.dao;

import java.util.List;

import models.Incident;

public interface IncidentDao extends Dao<Incident>{
	public List<Incident> getAllInconcludedIncident();
}
