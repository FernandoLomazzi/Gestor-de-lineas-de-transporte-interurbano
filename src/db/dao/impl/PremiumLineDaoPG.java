package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.dao.DBConnection;
import db.dao.PremiumLineDao;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import models.busline.CheapLine;
import models.busline.PremiumLine;

public class PremiumLineDaoPG implements PremiumLineDao {
	private static final String INSERT_SQL =
			"INSERT INTO PremiumLine " + 
			"(name) " +
			"VALUES (?);";
	private static final String DELETE_SQL = 
			"DELETE FROM PremiumLine " +
			"WHERE name=?;";
	
	@Override
	public void addData(PremiumLine premiumLine) throws DBConnectionException, AddFailException {
		BusLineDaoPG busLineDaoPG = new BusLineDaoPG();
		busLineDaoPG.addData(premiumLine);
		try(Connection connection = DBConnection.getConnection()){
			try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)){
				ps.setString(1, premiumLine.getName());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//Se supone que es él padre quien produce el error.
		}
		PremiumLineServiceDaoPG premiumLineServiceDaoPG = new PremiumLineServiceDaoPG();
		premiumLineServiceDaoPG.AddData(premiumLine);
	}

	@Override
	//Los servicios son inmutables
	public void modifyData(PremiumLine premiumLine) throws DBConnectionException, ModifyFailException {
		BusLineDaoPG busLineDaoPG = new BusLineDaoPG();
		busLineDaoPG.modifyData(premiumLine);
		PremiumLineServiceDaoPG premiumLineServiceDaoPG = new PremiumLineServiceDaoPG();
		try {
			premiumLineServiceDaoPG.ModifyData(premiumLine);
		}
		catch (AddFailException e) {
			throw new ModifyFailException("Cambiar");
		}
		
	}

	@Override
	public void deleteData(PremiumLine premiumLine) throws DBConnectionException, DeleteFailException {
		BusLineDaoPG busLineDaoPG = new BusLineDaoPG();
		busLineDaoPG.deleteData(premiumLine);
		PremiumLineServiceDaoPG premiumLineServiceDaoPG = new PremiumLineServiceDaoPG();
		try {
			premiumLineServiceDaoPG.DeleteData(premiumLine);
		}
		catch (AddFailException e) {
			throw new DeleteFailException("Cambiar");
		}
	}
	
	private class PremiumLineServiceDaoPG {
		private static final String INSERT_SQL =
				"INSERT INTO PremiumLineServices " + 
				"(name_line, name_service) " +
				"VALUES (?, CAST(? as PremiumLineService));";
		private static final String UPDATE_SQL_DELETE = 
				"DELETE FROM PremiumLineServices "+
				"WHERE name_line=?;";
		private static final String UPDATE_SQL = 
				"UPDATE PremiumLineServices SET" +
				"name_line=? " + 
				"WHERE name_line=?";
		private static final String DELETE_SQL = 
				"DELETE FROM PremiumLineServies " +
				"WHERE name_line=?;";
		
		public void AddData(PremiumLine premiumLine) throws DBConnectionException, AddFailException{
			try(Connection connection = DBConnection.getConnection()) {
				for(PremiumLine.PremiumLineService premiumLineService : premiumLine.getServices()) {
					try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {
						ps.setString(1,premiumLine.getName());
						ps.setString(2, premiumLineService.toString());
						ps.executeUpdate();
					}
				}
			}
			catch (SQLException e) {
				//Esto nunca deberia fallar
				e.printStackTrace();
			}
		}

		public void ModifyData(PremiumLine premiumLine) throws DBConnectionException, AddFailException {
			try(Connection connection = DBConnection.getConnection()) {
				try(PreparedStatement ps = connection.prepareStatement(UPDATE_SQL_DELETE)) {
					ps.setString(1,premiumLine.getName());
					ps.executeUpdate();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			AddData(premiumLine);
		}

		public void DeleteData(PremiumLine premiumLine) throws DBConnectionException, AddFailException{
			try(Connection connection = DBConnection.getConnection()) {
				try(PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {
					ps.setString(1, premiumLine.getName());
					ps.executeUpdate();
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
