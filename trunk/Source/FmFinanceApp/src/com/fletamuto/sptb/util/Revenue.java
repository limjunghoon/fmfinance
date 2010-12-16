package com.fletamuto.sptb.util;

import java.text.DecimalFormat;


public class Revenue {
	public static String getString(double purchasePrice, double currentPrice) {
		double percent = 1.0 - (currentPrice / (double)purchasePrice);
		
		if (purchasePrice == 0.0 || currentPrice == 0.0) {
			return "";
		}
		
		DecimalFormat decimalFormat = new DecimalFormat("##0.0%");
		
		if (currentPrice >= purchasePrice) {
			if (percent != 0.0) percent = -percent;
			return decimalFormat.format(percent);
		}
		else {
			percent = -percent;
			return decimalFormat.format(percent);
		}
	}
}
