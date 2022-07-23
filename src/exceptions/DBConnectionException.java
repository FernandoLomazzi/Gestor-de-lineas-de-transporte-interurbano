package exceptions;

public class DBConnectionException extends Exception {
	public DBConnectionException(String errorMessage) {
		super(errorMessage);
	}
}
