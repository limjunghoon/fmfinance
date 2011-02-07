package com.fletamuto.sptb.util;

import java.util.Calendar;
import java.util.Date;

public class FinanceCurrentDate {
	private static Calendar mCurrentDay = Calendar.getInstance();
//	private static FinanceCurrentDate mInstance;
/*	
	public static FinanceCurrentDate getInstance() {
		if (mInstance == null) {
			mInstance = new FinanceCurrentDate(); 
		}
		
		return mInstance;
	}
*/	
	public static Calendar getDate() {
		return mCurrentDay;
	}
	
	
	public static void moveCurrentDay(int dayValue) {
    	mCurrentDay.add(Calendar.DAY_OF_MONTH, dayValue);
    }
	
	public static Date getTime() {
		return mCurrentDay.getTime();
	}
	
	public static void setDate(Calendar day) {
		mCurrentDay = day;
	}
	


}
