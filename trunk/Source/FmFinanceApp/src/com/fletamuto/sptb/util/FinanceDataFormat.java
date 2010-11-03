package com.fletamuto.sptb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FinanceDataFormat {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm");
	public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static final SimpleDateFormat NUMBER_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	public static String getDateFormat(Date date) {
		return DATE_FORMAT.format(date);
	}
	
	public static String getTimeFormat(Date date) {
		return TIME_FORMAT.format(date);
	}
	
	public static String getDateTimeFormat(Date date) {
		return DATETIME_FORMAT.format(date);
	}
	
	public static String getNumverDateFormat(Date date) {
		return NUMBER_DATE_FORMAT.format(date);
	}
}
