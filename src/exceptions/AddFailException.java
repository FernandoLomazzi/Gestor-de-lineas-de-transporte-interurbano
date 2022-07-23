package exceptions;

import java.sql.SQLException;

public class AddFailException extends SQLException {
    public AddFailException(String errorMessage) {
        super(errorMessage);
    }
}
