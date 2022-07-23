package db.dao;

import java.lang.System.Logger.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.logging.Logger;

import exceptions.DBConnectionException;

public class DBConnection {
	private static final String URL = "jdbc:postgresql://localhost:5432/Prueba";
	private static final String USER = "postgres";
	private static final String PASS = "3435";
	private static final String DRIVER = "org.postgresql.Driver";
	private DBConnection() {
		;
	}
	public static Connection getConnection() throws DBConnectionException{
		Connection connection = null;
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			throw new DBConnectionException("Hubo un problema al intentar cargar los drivers de la base de datos.");
		} catch (SQLException  e) {
			throw new DBConnectionException("Hubo un problema al intentar establecer una conexión con la base de datos.");
		}
		return connection;
	}
}
