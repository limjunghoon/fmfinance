package com.fletamuto.sptb.data;

public class Repeat {
	/**
	 * 주간 반복
	 */
	public static final int WEEKLY = 0;
	/**
	 * 날짜 반복
	 */
	public static final int MONTHLY = 1;
	/**
	 * 주간/월간 반복 설정
	 */
	int mType = WEEKLY;
	
	/**
	 * 주간 반복 설정
	 */
	int mWeeklyRepeat = 0;
	/**
	 * 월간 반복 날짜 지정
	 */
	int mMonthRepeat = 1;
	
	/**
	 * 타입을 얻는다.
	 * @return 타입
	 */
	public int getType() {
		return mType;
	}
	
	/**
	 * 타입을 설정
	 * @param type WEEKLY, MONTHLY
	 */
	public void setType(int type) {
		mType = type;
	}
	
	/**
	 * 주간 반복 값을 얻는다.
	 * @return 주간 반복값
	 */
	public int getWeeklyRepeat() {
		return mWeeklyRepeat;
	}
	
	/**
	 * 주간반복값을 설정
	 * @param weekly 설정된 주간 반복값
	 */
	public void setWeeklyRepeat(int weekly) {
		mType = WEEKLY;
		mWeeklyRepeat = weekly;
	}
	
	public void setDailyRepeat(int day) {
		mType = MONTHLY;
		mMonthRepeat = day;
	}
	
}
