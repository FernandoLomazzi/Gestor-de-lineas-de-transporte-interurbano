package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.dao.BusStopDao;
import db.dao.DBConnection;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import models.BusStop;

public class BusStopDaoPG implements BusStopDao{
	private static final String INSERT_SQL = "INSERT INTO BusStop (stop_number,stop_street_name,stop_street_number,enabled) VALUES (?,?,?,?);";
	private static final String UPDATE_SQL = "UPDATE BusStop SET stop_street_name=?, stop_street_number=?, enabled=? WHERE stop_number=?;";
	private static final String SELECT_BY_ID_SQL = "SELECT stop_number,stop_street_name,stop_street_number,enabled FROM BusStop WHERE stop_number=?";
	private static final String SELECT_ALL_SQL = "SELECT stop_number,stop_street_name,stop_street_number,enabled FROM BusStop";
	private static final String DELETE_SQL = "DELETE FROM BusStop WHERE stop_number=?";
	private static final String IS_ENABLED_SQL = "SELECT * FROM Incident WHERE Incident.concluded=false AND Incident.bus_stop_disabled_number=?;";
	
	@Override
	public void addData(BusStop busStop) throws AddFailException,DBConnectionException{
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setInt(1, busStop.getStopNumber());
				ps.setString(2, busStop.getStopStreetName());
				ps.setInt(3, busStop.getStopStreetNumber());
				ps.setBoolean(4, busStop.isEnabled());
				ps.executeUpdate();
			} 
		} catch (SQLException e) {
			throw new AddFailException("La parada "+busStop.getStopNumber()+" ya se encuentra en el sistema.");
		}
	}

	@Override
	public void modifyData(BusStop busStop) throws DBConnectionException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
				ps.setString(1, busStop.getStopStreetName());
				ps.setInt(2, busStop.getStopStreetNumber());
				ps.setBoolean(3, busStop.isEnabled());
				ps.setInt(4, busStop.getStopNumber());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace(); //No debería ocurrir.
		}
	}

	@Override
	public void deleteData(BusStop busStop) throws DeleteFailException,DBConnectionException{
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(DELETE_SQL)){
				ps.setInt(1, busStop.getStopNumber());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public BusStop getBusStop(Integer busStopNumber) throws DBConnectionException {
		BusStop ret = new BusStop();
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL)){
				ps.setInt(1, busStopNumber);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					ret.setStopNumber(rs.getInt("stop_number"));
					ret.setStopStreetName(rs.getString("stop_street_name"));
					ret.setStopStreetNumber(rs.getInt("stop_street_number"));
					ret.setEnabled(rs.getBoolean("enabled"));
				}
				else {
					//No hubo
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//throw new SQLException("Excepcion");
		}
		return ret;
	}

	@Override
	public List<BusStop> getStopMap() throws DBConnectionException {
		List<BusStop> ret = new ArrayList<>();
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL)){
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Integer stopNumber = rs.getInt(1);
					String stopStreetName = rs.getString(2);
					Integer stopStreetNumber = rs.getInt(3);
					Boolean enabled = rs.getBoolean(4);
					BusStop busStop = new BusStop(stopNumber,stopStreetName,stopStreetNumber,enabled);
					ret.add(busStop);					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//throw new SQLException("Excepcion");
		}
		return ret;
	}

	@Override
	public Boolean isEnabled(BusStop busStop) throws DBConnectionException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(IS_ENABLED_SQL)){
				ps.setInt(1, busStop.getStopNumber());
				ResultSet rs = ps.executeQuery();
				return !rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
