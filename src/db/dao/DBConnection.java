package db.dao;

import java.lang.System.Logger.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBConnection {
	private static final String URL = "jdbc:postgresql://localhost:5432/Prueba";
	private static final String USER = "postgres";
	private static final String PASS = "3435";
	private static final String DRIVER = "org.postgresql.Driver";
	private DBConnection() {
		;
	}
	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			//Logger.getLogger(DBConnection.class.getName()).log(Level.Severe,null,ex);
			//throw new ConexionDBException();
		} catch (SQLException e) {
			e.printStackTrace();
			//Logger.getLogger(DBConnection.class.getName()).log(Level.Severe,null,ex);
			//throw new ConexionDBException();
		}
		return connection;
	}
}
