package exceptions;

import java.sql.SQLException;

public class ModifyFailException extends SQLException {
	public ModifyFailException(String errorMessage) {
		super(errorMessage);
	}
}
