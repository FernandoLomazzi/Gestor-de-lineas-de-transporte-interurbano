package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

import db.dao.BusStopDao;
import db.dao.DBConnection;
import db.dao.IncidentDao;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import exceptions.busStop.BusStopNotFoundException;
import models.BusStop;
import models.Incident;

public class IncidentDaoPG implements IncidentDao{
	private static final String INSERT_SQL = "INSERT INTO Incident (bus_stop_disabled_number,begin_date,end_date,description,concluded) VALUES (?,?,?,?,?);";
	private static final String UPDATE_SQL = "UPDATE Incident SET end_date=?, description=?, concluded=? WHERE bus_stop_disabled_number=? AND begin_date=?;";
	private static final String DELETE_SQL = "DELETE FROM Incident WHERE bus_stop_disabled_number=? AND begin_date=?;";
	private static final String SELECT_ALL_INCONCLUDED_SQL = "SELECT bus_stop_disabled_number,begin_date,end_date,description FROM Incident WHERE concluded=false;";
	
	@Override
	public void addData(Incident incident) throws AddFailException,DBConnectionException {
		if(!incident.areDatesCorrect()) {
			throw new AddFailException("ERROR: La fecha de finalización debe ser posterior o igual a la de inicio de la incidencia.");
		}
		else if(!incident.isConcludedCorrect()) {
			throw new AddFailException("ERROR: Debe establecer la fecha de finalización de la incidencia.");
		}
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setInt(1, incident.getBusStopDisabledNumber());
				ps.setObject(2, incident.getBeginDate());
				ps.setObject(3, incident.getEndDate());
				ps.setString(4, incident.getDescription());
				ps.setBoolean(5, incident.getConcluded());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			switch(e.getSQLState()) {
				case "23505": //unique_violation
					throw new AddFailException("ERROR: Ya existe una incidencia iniciada el "+incident.getBeginDate()+" para la parada "+incident.getBusStopDisabled()+".");
				default:
					throw new AddFailException("ERROR: Ocurrió un error al agregar la incidencia al sistema.");
			}
		}
	}
	@Override
	public void modifyData(Incident incident) throws ModifyFailException,DBConnectionException {
		if(!incident.areDatesCorrect()) {
			throw new ModifyFailException("ERROR: La fecha de finalización debe ser posterior o igual a la de inicio de la incidencia.");
		}
		else if(!incident.isConcludedCorrect()) {
			throw new ModifyFailException("ERROR: Debe establecer la fecha de finalización de la incidencia.");
		}
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
				ps.setObject(1,incident.getEndDate());
				ps.setString(2,incident.getDescription());
				ps.setBoolean(3, incident.getConcluded());
				ps.setInt(4, incident.getBusStopDisabledNumber());
				ps.setObject(5, incident.getBeginDate());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // no deberia ocurrir
		}
	}
	//No se usa para nada
	@Override
	public void deleteData(Incident incident) throws DeleteFailException,DBConnectionException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(DELETE_SQL)){
				ps.setInt(1, incident.getBusStopDisabledNumber());
				ps.setObject(2, incident.getBeginDate());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // no deberia ocurrir
		}
	}
	@Override
	public List<Incident> getAllInconcludedIncident() throws DBConnectionException {
		List<Incident> ret = new ArrayList<>();
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(SELECT_ALL_INCONCLUDED_SQL)){
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Integer busNumber = rs.getInt(1);
					LocalDate bDate = rs.getDate(2).toLocalDate();
					LocalDate eDate = rs.getDate(3)==null?null:rs.getDate(3).toLocalDate();
					String descr = rs.getString(4);
					BusStopDao bsDao = new BusStopDaoPG();
					BusStop busStop;
					try {
						busStop = bsDao.getBusStop(busNumber);
					} catch (BusStopNotFoundException e) {
						return null; // no deberia ocurrir
					}
					Incident incid = new Incident(busStop,bDate,eDate,descr,false);
					ret.add(incid);					
				}
			}
		} catch (SQLException e) {
			return null; // no deberia ocurrir
		}
		return ret;
	}
}
