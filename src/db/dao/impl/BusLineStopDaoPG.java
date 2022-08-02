package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import db.dao.BusLineStopDao;
import db.dao.DBConnection;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import models.BusLineStop;

public class BusLineStopDaoPG implements BusLineStopDao{
	private static final String INSERT_SQL =
			"INSERT INTO BusLineStop " + 
			"(bus_line_name, stop_number, stops) " +
			"VALUES (?, ?, ?);";

	@Override
	public void addData(BusLineStop t) throws DBConnectionException, AddFailException {
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setString(1, t.getBusLine().getName());
				ps.setInt(2, t.getBusStop().getStopNumber());
				ps.setBoolean(3, t.stops());
				ps.executeUpdate();
			} 
		}
		catch (Exception e) {
			throw new DBConnectionException("Error inesperado. Contacte con el administrador.");
		}
	}

	@Override
	public void modifyData(BusLineStop t) throws DBConnectionException, ModifyFailException {
		//No está permitido
		throw new ModifyFailException("Error inesperado. Contacte con el administrador.");
	}

	@Override
	public void deleteData(BusLineStop t) throws DBConnectionException, DeleteFailException {
		//No está permitido
		throw new DeleteFailException("Error inesperado. Contacte con el administrador.");
	}

}
