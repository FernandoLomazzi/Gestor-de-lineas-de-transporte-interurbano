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
import models.BusStop;
import models.Route;

public class RouteDaoPG implements RouteDao{
	private static final String INSERT_SQL = "INSERT INTO Route (source_stop_number,destination_stop_number,distance_in_km,active) VALUES (?,?,?,?);";
	private static final String UPDATE_SQL = "UPDATE Route SET distance_in_km=?, active=? WHERE source_stop_number=? AND destination_stop_number=?;";
	private static final String UPDATE_DISTANCE_SQL = "UPDATE Route SET distance_in_km=? WHERE source_stop_number=? AND destination_stop_number=?;";
	private static final String DELETE_SQL = "DELETE FROM Route WHERE source_stop_number=? AND destination_stop_number=?;";
	private static final String SELECT_ID_SQL = "SELECT id FROM Route WHERE source_stop_number=? AND destination_stop_number=?;";
	private static final String SELECT_ALL_SQL = "SELECT source_stop_number,destination_stop_number,distance_in_km,active FROM Route;";
	
	@Override
	public Boolean addData(Route route) throws SQLException {
		Integer rowsChanged;
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setInt(1, route.getSourceStopNumber());
				ps.setInt(2, route.getDestinationStopNumber());
				ps.setDouble(3, route.getDistanceInKM());
				ps.setBoolean(4, route.getActive());
				rowsChanged = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error: Base de datos no disponible");
		}
		return rowsChanged>0;
	}

	@Override
	public Boolean modifyData(Route route) {
		Integer rowsChanged;
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
				ps.setDouble(1, route.getDistanceInKM());
				ps.setBoolean(2, route.getActive());
				ps.setInt(3, route.getSourceStopNumber());
				ps.setInt(4, route.getDestinationStopNumber());
				rowsChanged = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return rowsChanged>0;
	}

	@Override
	public Boolean deleteData(Route route) {
		return deleteRoute(route.getSourceStop(),route.getDestinationStop());
	}

	@Override
	public Boolean deleteRoute(BusStop sourceStop, BusStop destinationStop) {
		Integer rowsChanged;
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(DELETE_SQL)){
				ps.setInt(1, sourceStop.getStopNumber());
				ps.setInt(2, destinationStop.getStopNumber());
				rowsChanged = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return rowsChanged>0;
	}
	//Posible borrado
	@Override
	public Integer getRouteID(BusStop sourceStop, BusStop destinationStop) {
		Integer ret;
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(SELECT_ID_SQL)){
				ps.setInt(1, sourceStop.getStopNumber());
				ps.setInt(2, destinationStop.getStopNumber());
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					ret = rs.getInt(0);
				}
				else {
					//No hubo
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}
	//Habría que hacer transacciones pero bueno xd
	@Override
	public List<Route> getRouteMap() {
		List<Route> ret = new ArrayList<>();
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL)){
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Integer sourceStopNumber = rs.getInt(1);
					Integer destinationStopNumber = rs.getInt(2);
					Double distanceInKM = rs.getDouble(3);
					Boolean active = rs.getBoolean(4);
					BusStopDao busStopDao = new BusStopDaoPG();
					BusStop sourceStop = busStopDao.getBusStop(sourceStopNumber);
					BusStop destinationStop = busStopDao.getBusStop(destinationStopNumber);
					Route route = new Route(sourceStop,destinationStop,distanceInKM,active);
					ret.add(route);					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//throw new SQLException("Excepcion");
		}
		return ret;
	}
	public Boolean modRouteDistance(Route route) {
		Integer rowsChanged;
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_DISTANCE_SQL)){
				ps.setDouble(1, route.getDistanceInKM());
				ps.setInt(2, route.getSourceStopNumber());
				ps.setInt(3, route.getDestinationStopNumber());
				rowsChanged = ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return rowsChanged>0;
	}
}
