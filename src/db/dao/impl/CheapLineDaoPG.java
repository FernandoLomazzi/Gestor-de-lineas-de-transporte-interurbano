package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.dao.CheapLineDao;
import db.dao.DBConnection;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import javafx.scene.paint.Color;
import models.busline.CheapLine;

public class CheapLineDaoPG implements CheapLineDao{
	private static final String INSERT_SQL =
			"INSERT INTO CheapLine " + 
			"(name,standing_capacity_percentage,standing_capacity) " +
			"VALUES (?,?,?);";
	private static final String UPDATE_SQL = 
			"UPDATE CheapLine SET " + 
			"standing_capacity_percentage=?,standing_capacity=? " +
			"WHERE name=?;";
	private static final String DELETE_SQL = 
			"DELETE FROM CheapLine " +
			"WHERE name=?;";
	private static final String SELECT_SQL_CHEAP_LINES =
			"SELECT Busline.name, color, seating_capacity, standing_capacity_percentage, standing_capacity " +
			"FROM BusLine, CheapLine " +
			"WHERE BusLine.name = CheapLine.name;";
	
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
			throw new AddFailException("Error inesperado. Contacte con el administrador.");
		}
	}
	
	@Override
	public void modifyData(CheapLine cheapLine) throws ModifyFailException, DBConnectionException{
		BusLineDaoPG busLineDaoPG = new BusLineDaoPG();
		busLineDaoPG.modifyData(cheapLine);
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){
				ps.setDouble(1, cheapLine.getStandingCapacityPercentage());
				ps.setInt(2, cheapLine.getSeatingCapacity());
				ps.setString(3, cheapLine.getName());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new ModifyFailException("Error inesperado. Contacte con el administrador.");
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
			throw new DeleteFailException("Error inesperado. Contacte con el administrador.");
		}
	}
	
	public List<CheapLine> getAllCheapLines() throws DBConnectionException {
		ArrayList<CheapLine> ret = new ArrayList<>();
		try(Connection connection = DBConnection.getConnection()) {
			try(PreparedStatement ps = connection.prepareStatement(SELECT_SQL_CHEAP_LINES)){
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					String name = rs.getString(1);
					Color color = Color.valueOf(rs.getString(2));
					Integer seating_capacity = rs.getInt(3);
					Double standing_capacity_porcentage = rs.getDouble(4);
					CheapLine cheapLine = new CheapLine(name,color,seating_capacity, standing_capacity_porcentage);
					ret.add(cheapLine);
				}
			}
		}
		catch(SQLException | DBConnectionException  e) {
			throw new DBConnectionException("Error inesperado. Contacte con el administrador.");
		}
		return ret;
	}
}









