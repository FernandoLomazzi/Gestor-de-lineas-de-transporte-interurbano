package trash;

import java.util.ArrayList;
import java.util.List;

import models.Route;

public class Map {
	private static Map map;
	private List<Route> routes;
	private Map() {
		routes = new ArrayList<>();
	}
	public Map getInstance() {
		if(map==null) {
			map = new Map();
		}
		return map;
	}
}
