package exceptions;

import java.sql.SQLException;

public class DeleteFailException extends SQLException {
	public DeleteFailException(String errorMessage) {
		super(errorMessage);
	}
}
