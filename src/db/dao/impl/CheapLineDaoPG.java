package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.dao.CheapLineDao;
import db.dao.DBConnection;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import models.busline.CheapLine;

public class CheapLineDaoPG implements CheapLineDao{
	private static final String INSERT_SQL =
			"INSERT INTO CheapLine " + 
			"(name,standing_capacity_percentage,standing_capacity) " +
			"VALUES (?,?,?);";
	private static final String UPDATE_SQL = 
			"UPDATE CheapLine SET " + 
			"name=?,standing_capacity_percentage=?,standing_capacity=? " +
			"WHERE name=?;";
	private static final String DELETE_SQL = 
			"DELETE FROM CheapLine " +
			"WHERE name=?;";
	
	@Override
	public void addData(CheapLine cheapLine) throws AddFailException, DBConnectionException {
		BusLineDaoPG busLineDaoPG = new BusLineDaoPG();
		busLineDaoPG.addData(cheapLine);
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setString(1, cheapLine.getName());
				ps.setDouble(2, cheapLine.getStandingCapacityPercentage());
				ps.setInt(3, cheapLine.getSeatingCapacity());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//Se supone que es él padre quien produce el error.
		}
	}
	
	@Override
	public void modifyData(CheapLine cheapLine) throws ModifyFailException, DBConnectionException{
		BusLineDaoPG busLineDaoPG = new BusLineDaoPG();
		busLineDaoPG.modifyData(cheapLine);
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
				ps.setString(1, cheapLine.getName());
				ps.setDouble(2, cheapLine.getStandingCapacityPercentage());
				ps.setInt(3, cheapLine.getSeatingCapacity());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//Se supone que es él padre quien produce el error.
		}
	}
	
	@Override
	public void deleteData(CheapLine cheapLine) throws DeleteFailException, DBConnectionException {
		BusLineDaoPG busLineDaoPG = new BusLineDaoPG();
		busLineDaoPG.deleteData(cheapLine);
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(DELETE_SQL)){
				ps.setString(1, cheapLine.getName());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//Se supone que es él padre quien produce el error.
		}
	}
}









