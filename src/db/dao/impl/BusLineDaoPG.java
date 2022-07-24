package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.dao.BusLineDao;
import db.dao.DBConnection;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import models.BusStop;
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
}
