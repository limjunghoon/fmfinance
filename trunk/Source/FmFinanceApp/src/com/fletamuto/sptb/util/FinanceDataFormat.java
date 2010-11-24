package com.fletamuto.sptb.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FinanceDataFormat {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm");
	public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static final SimpleDateFormat NUMBER_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat TEXT_DATE_FORMAT = new SimpleDateFormat("yyyy³â MM¿ù ddÀÏ");
	public static final SimpleDateFormat DOT_DATE_FORMAT = new SimpleDateFormat("yyyy. MM. dd");
	
	public static String getDateFormat(Date date) {
		return DATE_FORMAT.format(date);
	}
	
	public static String getTimeFormat(Date date) {
		return TIME_FORMAT.format(date);
	}
	
	public static String getDateTimeFormat(Date date) {
		return DATETIME_FORMAT.format(date);
	}
	
	public static String getNumberDateFormat(Date date) {
		return NUMBER_DATE_FORMAT.format(date);
	}
	
	public static String getFullDateFormat(Date date) {
		return SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL).format(date); 
	}
	
	public static String getShortDateFormat(Date date) {
		return SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(date); 
	}
	
	public static String getTextDateFormat(Date date) {
		return TEXT_DATE_FORMAT.format(date);
	}
	
	public static String getDotDateFormat(Date date) {
		return DOT_DATE_FORMAT.format(date);
	}
}
