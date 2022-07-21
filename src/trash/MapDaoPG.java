package trash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import db.dao.BusStopDao;
import db.dao.DBConnection;
import db.dao.RouteDao;
import db.dao.impl.BusStopDaoPG;
import db.dao.impl.RouteDaoPG;
import models.BusStop;
import models.Incident;
import models.Route;

public class MapDaoPG implements MapDao{
	private static final String INSERT_ROUTE_SQL = "INSERT INTO Route (source_stop_number,destination_stop_number,distance_in_km,active) VALUES (?,?,?,?);";
	private static final String INSERT_MAP_SQL = "INSERT INTO Map (source_stop_number,destination_stop_number) VALUES (?,?);";
	private static final String DELETE_ROUTE_SQL = "DELETE FROM Route WHERE source_stop_number=? AND destination_stop_number=?;";
	private static final String DELETE_MAP_SQL = "DELETE FROM Map WHERE source_stop_number=? AND destination_stop_number=?;";
	private static final String SELECT_ALL_SQL = "SELECT source_stop_number,destination_stop_number,distance_in_km,active FROM Route,Map WHERE Route.source_stop_number=Map.source_stop_number AND Route.destination_stop_number=Map.destination_top_number;";
	
	
	@Override
	public Boolean addData(Route route) throws SQLException {
		Integer rowsChanged=0;
		try(Connection connection = DBConnection.getConnection()){
			connection.setAutoCommit(false);
			try{
				PreparedStatement psRoute = connection.prepareStatement(INSERT_ROUTE_SQL);
				PreparedStatement psMap = connection.prepareStatement(INSERT_MAP_SQL);
				psRoute.setInt(1, route.getSourceStopNumber());
				psRoute.setInt(2, route.getDestinationStopNumber());
				psRoute.setDouble(3, route.getDistanceInKM());
				psRoute.setBoolean(4, route.getActive());
				rowsChanged += psRoute.executeUpdate();
				psMap.setInt(1, route.getSourceStopNumber());
				psMap.setInt(2, route.getDestinationStopNumber());
				rowsChanged += psRoute.executeUpdate();
				connection.commit();
			}catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
				throw new SQLException("Error: Base de datos no disponible");
			}
			finally {
				connection.setAutoCommit(true);
			}
		}
		return rowsChanged>0;
	}

	@Override
	public Boolean modifyData(Route route) {
		RouteDao routeDao = new RouteDaoPG();
		return routeDao.modifyData(route);
	}

	@Override
	public Boolean deleteData(Route route) {
		return this.deleteRoute(route.getSourceStop(),route.getDestinationStop());
	}

	@Override
	public List<Route> getMap() {
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

	@Override
	public Boolean deleteRoute(BusStop sourceStop, BusStop destinationStop) {
		Integer rowsChanged=0;
		try(Connection connection = DBConnection.getConnection()){
			connection.setAutoCommit(false);
			try{
				PreparedStatement psRoute = connection.prepareStatement(DELETE_ROUTE_SQL);
				PreparedStatement psMap = connection.prepareStatement(DELETE_MAP_SQL);
				psRoute.setInt(1, sourceStop.getStopNumber());
				psRoute.setInt(2, destinationStop.getStopNumber());
				rowsChanged += psRoute.executeUpdate();
				psMap.setInt(1, sourceStop.getStopNumber());
				psMap.setInt(2, destinationStop.getStopNumber());
				rowsChanged += psRoute.executeUpdate();
				connection.commit();
			}catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
				return false;
			}
			finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		return rowsChanged>0;
	}

}

