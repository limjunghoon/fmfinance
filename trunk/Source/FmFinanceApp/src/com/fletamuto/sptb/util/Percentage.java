package com.fletamuto.sptb.util;

import java.text.DecimalFormat;

public class Percentage {
	public static String getString(double part, double whole) {
		if (part == 0.0 || whole == 0.0) {
			return "";
		}
		
		DecimalFormat decimalFormat = new DecimalFormat("##0.0%");
		return decimalFormat.format((part / whole));
	}
}
