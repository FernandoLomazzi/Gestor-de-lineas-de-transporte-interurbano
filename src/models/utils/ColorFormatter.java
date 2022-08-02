package models.utils;

import javafx.scene.paint.Color;

public class ColorFormatter {
	private static String format(double val) {
		String in = Integer.toHexString((int) Math.round(val * 255));
		return in.length() == 1 ? "0" + in : in;
    }
	public static final String toHexString(Color value) {
		return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
				.toUpperCase();
	}
}
