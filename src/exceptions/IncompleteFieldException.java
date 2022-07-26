package exceptions;

public class IncompleteFieldException extends Exception {
	public IncompleteFieldException(String errorMessage) {
		super(errorMessage);
	}
}
