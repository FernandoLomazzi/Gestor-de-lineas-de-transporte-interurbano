package exceptions.busStop;

public class BusStopNotFoundException extends Exception{
	public BusStopNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}