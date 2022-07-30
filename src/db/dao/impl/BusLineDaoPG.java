package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.dao.BusLineDao;
import db.dao.BusStopDao;
import db.dao.DBConnection;
import db.dao.RouteDao;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import exceptions.busStop.BusStopNotFoundException;
import models.BusLineRoute;
import models.BusLineStop;
import models.BusStop;
import models.Route;
import models.busline.BusLine;

public class BusLineDaoPG implements BusLineDao{
	private static final String INSERT_SQL =
			"INSERT INTO BusLine " + 
			"(name,color,seating_capacity) " +
			"VALUES (?,?,?);";
	private static final String UPDATE_SQL = 
			"UPDATE BusLine SET " + 
			"name=?,color=?,seating_capacity=? " +
			"WHERE name=?;";
	private static final String DELETE_SQL = 
			"DELETE FROM BusLine " +
			"WHERE name=?;";
	private static final String SELECT_SQL_ROUTES = "SELECT bus_line_name, source_stop_number, destination_stop_number, estimated_time FROM BusLineRoute WHERE bus_line_name = ?;";
	private static final String SELECT_SQL_STOPS = "SELECT bus_line_name, stop_number, stops FROM BusLineStop WHERE bus_line_name = ?;";

	@Override
	public void addData(BusLine busLine) throws DBConnectionException, AddFailException{
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setString(1, busLine.getName());
				ps.setString(2, busLine.getColor());
				ps.setInt(3, busLine.getSeatingCapacity());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new AddFailException("La linea " + busLine.getName() + " ya se encuentra en el sistema");
		}
		BusLineStopDaoPG busLineStopDaoPG = new BusLineStopDaoPG();
		busLineStopDaoPG.addData(busLine);
		BusLineRouteDaoPG busLineRouteDaoPG = new BusLineRouteDaoPG();
		busLineRouteDaoPG.addData(busLine);
	}
	
	@Override
	public void modifyData(BusLine busLine) throws DBConnectionException, ModifyFailException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
				ps.setString(1, busLine.getName());
				ps.setString(2, busLine.getColor());
				ps.setInt(3, busLine.getSeatingCapacity());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteData(BusLine busLine) throws DBConnectionException, DeleteFailException{
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(DELETE_SQL)){
				ps.setString(1, busLine.getName());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<BusLine> getAllBusLines() throws DBConnectionException{
		ArrayList<BusLine> ret = new ArrayList<>();

		PremiumLineDaoPG premiumLineDaoPG = new PremiumLineDaoPG();
		CheapLineDaoPG cheapLineDaoPG = new CheapLineDaoPG();
		try {
			ret.addAll(premiumLineDaoPG.getAllPremiumLines());
			ret.addAll(cheapLineDaoPG.getAllCheapLines());
		}
		catch (DBConnectionException e) {
			throw e;
		}

		for(BusLine busLine : ret) {
			try(Connection connection = DBConnection.getConnection()){

				ArrayList<BusLineRoute> routes = new ArrayList<>();
				RouteDao routeDao = new RouteDaoPG();
				try(PreparedStatement ps = connection.prepareStatement(SELECT_SQL_ROUTES)){
					System.out.println(busLine.getName());
					ps.setString(1, busLine.getName());
					ResultSet rs = ps.executeQuery();
					
					while(rs.next()) {
						Route route = routeDao.getRoute(rs.getInt(2), rs.getInt(3));
						BusLineRoute busLineRoute = new BusLineRoute(busLine, route, rs.getInt(4));
						routes.add(busLineRoute);
					}
				}
				busLine.setRoutes(routes);
				
				ArrayList<BusLineStop> busStops = new ArrayList<>();
				BusStopDao busStopDao = new BusStopDaoPG(); 
				try(PreparedStatement ps = connection.prepareStatement(SELECT_SQL_STOPS)){
					ps.setString(1, busLine.getName());
					ResultSet rs = ps.executeQuery();
					while(rs.next()) {
						BusStop busStop = busStopDao.getBusStop(rs.getInt(2));
						BusLineStop busLineStop = new BusLineStop(busLine, busStop, rs.getBoolean(3));
						busStops.add(busLineStop);
					}
				}
				busLine.setBusStops(busStops);
			}
			catch(SQLException | DBConnectionException | BusStopNotFoundException e) {
				throw new DBConnectionException("Error inesperado");
			}
		}
		return ret;
	}
	
	private class BusLineRouteDaoPG {
		private static final String INSERT_SQL = 
				"INSERT INTO BusLineRoute " +
				"(bus_line_name, source_stop_number, destination_stop_number, estimated_time) " +
				"VALUES (?, ?, ?, ?)";
		

		public void addData(BusLine t) throws DBConnectionException {
			for(BusLineRoute busLineRoute : t.getRoutes()) {
				try(Connection connection = DBConnection.getConnection()){
					try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
						ps.setString(1, busLineRoute.getBusLine().getName());
						ps.setInt(2, busLineRoute.getRoute().getSourceStopNumber());
						ps.setInt(3, busLineRoute.getRoute().getDestinationStopNumber());
						ps.setInt(4, busLineRoute.getEstimatedTime());
						ps.executeUpdate();
					} 
				}
				catch (Exception e) {
					throw new DBConnectionException("Error inesperado");
				}
			}
		}
	}
	
	private class BusLineStopDaoPG {
		private static final String INSERT_SQL =
				"INSERT INTO BusLineStop " + 
				"(bus_line_name, stop_number, stops) " +
				"VALUES (?, ?, ?);";

		public void addData(BusLine t) throws DBConnectionException {
			for (BusLineStop busLineStop : t.getBusStops())
			try(Connection connection = DBConnection.getConnection()){
				try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
					ps.setString(1, busLineStop.getBusLine().getName());
					ps.setInt(2, busLineStop.getBusStop().getStopNumber());
					ps.setBoolean(3, busLineStop.stops());
					ps.executeUpdate();
				} 
			}
			catch (Exception e) {
				throw new DBConnectionException("Error inesperado");
			}
		}
	}
}
