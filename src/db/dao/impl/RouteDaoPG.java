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
	private static final String SELECT_SQL_ROUTE = "SELECT distance_in_km FROM Route WHERE source_stop_number = ? AND destination_stop_number = ?;";
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
				throw new AddFailException("Ya existe una calle que conecta "+route.getDestinationStop()+" con "+route.getSourceStop()+".");
			default:
				throw new AddFailException("Error inesperado. Contacte con el administrador.");
			}
		}
	}

	@Override
	public void modifyData(Route route) throws DBConnectionException,ModifyFailException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
				ps.setDouble(1, route.getDistanceInKM());
				ps.setInt(2, route.getSourceStopNumber());
				ps.setInt(3, route.getDestinationStopNumber());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new ModifyFailException("Error inesperado. Contacte con el administrador.");
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
			throw new DeleteFailException("Error inesperado. Contacte con el administrador.");
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
					BusStop sourceStop = busStopDao.getBusStop(sourceStopNumber);
					BusStop destinationStop = busStopDao.getBusStop(destinationStopNumber);
					Route route = new Route(sourceStop,destinationStop,distanceInKM);
					ret.add(route);					
				}
			}
		} catch (SQLException|BusStopNotFoundException|DBConnectionException e) {
			throw new DBConnectionException("Error inesperado. Contacte con el administrador.");
		}
		return ret;
	}
	@Override
	public Route getRoute(Integer source_stop_number,  Integer destination_stop_number) throws DBConnectionException {
		BusStop source = null;
		BusStop destination = null;
		Double distance = null;
		try {
			BusStopDaoPG busStopDaoPG = new BusStopDaoPG();
			source = busStopDaoPG.getBusStop(source_stop_number);
			destination = busStopDaoPG.getBusStop(destination_stop_number);
		}
		catch(BusStopNotFoundException | DBConnectionException e) {
			throw new DBConnectionException("Error inesperado. Contacte con el administrador.");
		}
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(SELECT_SQL_ROUTE)){
				ps.setInt(1, source_stop_number);
				ps.setInt(2, destination_stop_number);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					distance = rs.getDouble(1);
				}
			}
		}
		catch (SQLException e) {
			throw new DBConnectionException("Error inesperado. Contacte con el administrador.");
		}
		return new Route(source, destination, distance);
	}
}















