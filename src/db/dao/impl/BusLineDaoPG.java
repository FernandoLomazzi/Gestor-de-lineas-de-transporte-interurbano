package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import db.dao.BusLineDao;
import db.dao.DBConnection;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import models.busline.BusLine;
import models.busline.CheapLine;
import models.busline.PremiumLine;
import models.busline.PremiumLine.PremiumLineService;

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
			"SELECT Busline.name, color, seating_capacity, standing_capacity_percentage, standing_capacity " +
			"FROM BusLine, CheapLine " +
			"WHERE BusLine.name = CheapLine.name;";
	private static final String SELECT_SQL_PREMIUM_LINES_NO_SERVICES =
			"SELECT BusLine.name, color, seating_capacity " +
			"FROM BusLine, PremiumLine " +
			"WHERE BusLine.name = PremiumLine.name;";
	private static final String SELECT_SQL_PREMIUM_LINE_SERVICES = 
			"SELECT name_service " +
			"FROM PremiumLineServices " +
			"WHERE name_line = ?;";
					
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
			}
			System.out.println("Premium");
			//Para lineas premium
			try(PreparedStatement ps = connection.prepareStatement(SELECT_SQL_PREMIUM_LINES_NO_SERVICES)){
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					//Primero se obtienen las lineas que son premium
					String name = rs.getString(1);
					String color = rs.getString(2);
					Integer seating_capacity = rs.getInt(3);
					
					//Segundo se obtienen los servicios para cada linea premium
					HashSet<PremiumLineService> services = new HashSet<PremiumLineService>();
					try(PreparedStatement ps2 = connection.prepareStatement(SELECT_SQL_PREMIUM_LINE_SERVICES)) {
						ps2.setString(1, name);
						ResultSet rs2 = ps2.executeQuery();
						while(rs2.next()) {
							String service = rs2.getString(1);
							if (service.equals(PremiumLineService.WIFI.toString())) {
								services.add(PremiumLineService.WIFI);
							}
							else {
								services.add(PremiumLineService.AIR_CONDITIONING);
							}
						}
					}
					PremiumLine premiumLine = new PremiumLine(name, color, seating_capacity, services);
					ret.add(premiumLine);
				}
			}
		}
		catch(SQLException | DBConnectionException  e) {
			e.printStackTrace();
			return null;
		}
		System.out.println(ret.size());
		return ret;
	}
}
