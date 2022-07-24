package db.dao.impl;

import db.dao.PremiumLineDao;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;
import models.busline.PremiumLine;

public class PremiumLineDaoPG implements PremiumLineDao {
	private class PremiumLineServiceDaoPG {
		//Le voy a dar una PremiumLine y me va hacer solo esta parte, lo hago asi para tener mejor organizacion
		private static final String INSERT_SQL =
				"INSERT INTO PremiumLineServices " + 
				"(name_line, name_service) " +
				"VALUES (?, ?);";
		private static final String UPDATE_SQL = 
				"UPDATE PremiumLineServices SET " + 
				"name_line=?,name_service=? " +
				"WHERE name_line=?;";
		private static final String DELETE_SQL = 
				"DELETE FROM PremiumLineServies " +
				"WHERE name_line=?;";
		
		public void AddData()
	}
	private static final String INSERT_SQL_PREMIUM_LINE_SERVICE =
			"INSERT INTO PremiumLine " + 
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
	public void addData(PremiumLine premiumLine) throws DBConnectionException, AddFailException {
		
	}

	@Override
	public void modifyData(PremiumLine premiumLine) throws DBConnectionException, ModifyFailException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteData(PremiumLine premiumLine) throws DBConnectionException, DeleteFailException {
		// TODO Auto-generated method stub
		
	}
	
}
