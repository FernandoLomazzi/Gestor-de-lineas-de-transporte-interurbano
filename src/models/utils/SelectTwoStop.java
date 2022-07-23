package models.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.BusStop;

//Usar con cuidado obviamente no está muy bien esta clase
public class SelectTwoStop {
	private static List<BusStop> stops = new ArrayList<>();
	public static void addStop(BusStop busStop) {
		if(stops.size()>0 && stops.get(0).equals(busStop)) {
			return;
		}
		stops.add(busStop);
	}
	public static Boolean full() {
		return stops.size()==2;
	}
	public static BusStop getSourceStop() {
		return stops.get(0);
	}
	public static BusStop getDestinationStop() {
		return stops.get(1);
	}
	public static void reset() {
		stops.clear();
	}
}
