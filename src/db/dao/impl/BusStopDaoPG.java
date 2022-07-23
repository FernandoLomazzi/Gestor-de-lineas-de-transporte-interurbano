package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.dao.BusStopDao;
import db.dao.DBConnection;
import models.BusStop;
import models.Route;

public class BusStopDaoPG implements BusStopDao{
	private static final String INSERT_SQL = "INSERT INTO BusStop (stop_number,stop_street_name,stop_street_number,enabled) VALUES (?,?,?,?);";
	private static final String UPDATE_SQL = "UPDATE BusStop SET stop_street_name=?, stop_street_number=?, enabled=? WHERE stop_number=?;";
	private static final String SELECT_BY_ID_SQL = "SELECT stop_number,stop_street_name,stop_street_number,enabled FROM BusStop WHERE stop_number=?";
	private static final String SELECT_ALL_SQL = "SELECT stop_number,stop_street_name,stop_street_number,enabled FROM BusStop";
	private static final String DELETE_SQL = "DELETE FROM BusStop WHERE stop_number=?";
	private static final String IS_ENABLED_SQL = "SELECT * FROM Incident WHERE Incident.concluded=false AND Incident.bus_stop_disabled_number=?;";
	
	@Override
	public Boolean addData(BusStop busStop) throws SQLException{
		Integer rowsChanged;
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setInt(1, busStop.getStopNumber());
				ps.setString(2, busStop.getStopStreetName());
				ps.setInt(3, busStop.getStopStreetNumber());
				ps.setBoolean(4, busStop.isEnabled());
				rowsChanged = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error: Base de datos no disponible");
		}
		return rowsChanged>0;
	}

	@Override
	public Boolean modifyData(BusStop busStop) {
		Integer rowsChanged;
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
				ps.setString(1, busStop.getStopStreetName());
				ps.setInt(2, busStop.getStopStreetNumber());
				ps.setBoolean(3, busStop.isEnabled());
				ps.setInt(4, busStop.getStopNumber());
				rowsChanged = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return rowsChanged>0;
	}

	@Override
	public Boolean deleteData(BusStop busStop) {
		return this.deleteBusStop(busStop.getStopNumber());
	}
	@Override
	public Boolean deleteBusStop(Integer busStopNumber)  {
		Integer rowsChanged;
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(DELETE_SQL)){
				ps.setInt(1, busStopNumber);
				rowsChanged = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return rowsChanged>0;
	}
	@Override
	public BusStop getBusStop(Integer busStopNumber) throws SQLException {
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
			throw new SQLException("Excepcion");
		}
		return ret;
	}

	@Override
	public List<BusStop> getStopMap() {
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
	public Boolean isEnabled(BusStop busStop) {
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
