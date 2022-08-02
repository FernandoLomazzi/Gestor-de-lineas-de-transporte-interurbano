package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.dao.BusLineRouteDao;
import db.dao.DBConnection;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import models.BusLineRoute;

public class BusLineRouteDaoPG implements BusLineRouteDao{
	private static final String INSERT_SQL = 
			"INSERT INTO BusLineRoute " +
			"(bus_line_name, source_stop_number, destination_stop_number, estimated_time) " +
			"VALUES (?, ?, ?, ?)";

	@Override
	public void addData(BusLineRoute t) throws DBConnectionException, AddFailException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setString(1, t.getBusLine().getName());
				ps.setInt(2, t.getRoute().getSourceStopNumber());
				ps.setInt(3, t.getRoute().getDestinationStopNumber());
				ps.setInt(4, t.getEstimatedTime());
				ps.executeUpdate();
			} 
		}
		catch(SQLException e) {
			throw new DBConnectionException("Error inesperado. Contacte con el administrador.");
		}
	}
		
	@Override
	public void modifyData(BusLineRoute t) throws DBConnectionException, ModifyFailException {
		//No está permitido
		throw new ModifyFailException("Error inesperado. Contacte con el administrador.");
	}

	@Override
	public void deleteData(BusLineRoute t) throws DBConnectionException, DeleteFailException {
		//No está permitido
		throw new DeleteFailException("Error inesperado. Contacte con el administrador.");
	}

}
