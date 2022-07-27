package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.dao.BusLineDao;
import db.dao.DBConnection;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import exceptions.busStop.BusStopNotFoundException;
import models.Incident;
import models.busline.BusLine;
import models.busline.CheapLine;

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
	private static final String SELECT_SQL_CHEAP_LINES =
			"SELECT Busline.name, color, seating_capacity, standing_capacity_percentage, standing_capacity" +
			"FROM BusLine, CheapLine " +
			"WHERE BusLine.name = CheapLine.name";
	private static final String SELECT_SQL_PREMIUM_LINES_NO_SERVICES =
			"SELECT BusLine.name, color, seating_capacity " +
			"FROM BusLine, PremiumLine " +
			"WHERE BusLine.name = PremiumLine.name";
			
					
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
	
	@Override
	public List<BusLine> getAllBusLines() {
		List<BusLine> ret = new ArrayList<>();
		try(Connection connection = DBConnection.getConnection()){
			//Para lineas economicas
			try(PreparedStatement ps = connection.prepareStatement(SELECT_SQL_CHEAP_LINES)){
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					String name = rs.getString(1);
					String color = rs.getString(2);
					Integer seating_capacity = rs.getInt(3);
					Double standing_capacity_porcentage = rs.getDouble(4);
					CheapLine cheapLine = new CheapLine(name,color,seating_capacity, standing_capacity_porcentage);
					ret.add(cheapLine);
				}
			//Para lineas premim
			}
		}
		catch(SQLException e) {
			//No estoy seguro
			return null;
		}
		return ret;
	}
}
