package models;

import java.util.HashMap;
import java.util.Map;

public class text {
	public BusStop bs;
	public static void main(String[] arg) {
		Map<BusStop,text> map = new HashMap<>();
		BusStop bs = new BusStop(1,"aDelgado",1952,true);
		text t = new text();
		t.bs=bs;
		map.put(bs,t);
		System.out.println(map);
		bs.setStopStreetName("Peru");
		System.out.println(map);
	}
	public String toString() {
		return bs.toString();
	}
}
