package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.dao.BusStopDao;
import db.dao.DBConnection;
import db.dao.RouteDao;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import exceptions.busStop.BusStopNotFoundException;
import models.BusStop;
import models.Route;

public class RouteDaoPG implements RouteDao{
	private static final String INSERT_SQL = "INSERT INTO Route (source_stop_number,destination_stop_number,distance_in_km) VALUES (?,?,?);";
	private static final String UPDATE_SQL = "UPDATE Route SET distance_in_km=? WHERE source_stop_number=? AND destination_stop_number=?;";
	private static final String DELETE_SQL = "DELETE FROM Route WHERE source_stop_number=? AND destination_stop_number=?;";
	private static final String SELECT_ALL_SQL = "SELECT source_stop_number,destination_stop_number,distance_in_km FROM Route;";
	
	@Override
	public void addData(Route route) throws AddFailException,DBConnectionException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setInt(1, route.getSourceStopNumber());
				ps.setInt(2, route.getDestinationStopNumber());
				ps.setDouble(3, route.getDistanceInKM());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			switch(e.getSQLState()) {
			case "23505": //unique_violation
				throw new AddFailException("Ya existe una calle entre la parada "+route.getDestinationStop()+" y la parada "+route.getSourceStop());
			default:
				// no deberia pasar
			}
			e.printStackTrace();
		}
	}

	@Override
	public void modifyData(Route route) throws DBConnectionException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
				ps.setDouble(1, route.getDistanceInKM());
				ps.setInt(2, route.getSourceStopNumber());
				ps.setInt(3, route.getDestinationStopNumber());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteData(Route route) throws DeleteFailException,DBConnectionException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(DELETE_SQL)){
				ps.setInt(1, route.getSourceStop().getStopNumber());
				ps.setInt(2, route.getDestinationStop().getStopNumber());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Habría que hacer transacciones pero bueno xd
	@Override
	public List<Route> getRouteMap() throws DBConnectionException {
		List<Route> ret = new ArrayList<>();
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL)){
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Integer sourceStopNumber = rs.getInt(1);
					Integer destinationStopNumber = rs.getInt(2);
					Double distanceInKM = rs.getDouble(3);
					BusStopDao busStopDao = new BusStopDaoPG();
					BusStop sourceStop;
					try {
						sourceStop = busStopDao.getBusStop(sourceStopNumber);
					} catch (BusStopNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(); return null;
					} catch (DBConnectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(); return null;
					}
					BusStop destinationStop;
					try {
						destinationStop = busStopDao.getBusStop(destinationStopNumber);
					} catch (BusStopNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(); return null;
					} catch (DBConnectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(); return null;
					}
					Route route = new Route(sourceStop,destinationStop,distanceInKM);
					ret.add(route);					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//throw new SQLException("Excepcion");
		}
		return ret;
	}
}
