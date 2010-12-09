package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;

import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

public class Repeat extends BaseItem {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2053858177051707684L;
	public static final int ONCE = 0;
	/** 주간 반복 */
	public static final int WEEKLY = 1;
	
	/** 날짜 반복 */
	public static final int MONTHLY = 2;
	
	public static final int SUNDAY 		= 0x00000001;
	public static final int MONDAY 		= 0x00000010;
	public static final int TUESDAY 	= 0x00000100;
	public static final int WEDNESDAY 	= 0x00001000;
	public static final int THURSDAY 	= 0x00010000;
	public static final int FRIDAY 		= 0x00100000;
	public static final int SATURDAY 	= 0x01000000;
	
	private int mItemType = -1;
	
	private int mItemID = -1;
	
	/** 주간/월간 반복 설정 */
	private int mType = ONCE;
	
	/** 주간 반복 설정 */
	private int mWeeklyRepeat = 0;
	
	/** 월간 반복 날짜 지정 */
	private int mDayofMonth = 1;
	
	
	private Calendar mLastApplyDay = Calendar.getInstance();
	
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
	
	public void setMonthlyRepeat(int day) {
		mType = MONTHLY;
		mDayofMonth = day;
	}
	
	public int getDayofMonth() {
		return mDayofMonth;
	}

	/**
	 * 날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
	 */
	public String getLastApplyDateString() {
		return FinanceDataFormat.getDateFormat(mLastApplyDay.getTime());
	}
	
	public Calendar getLastApplyDate() {
		return mLastApplyDay;
	}

	public void setLastApplyDay(Calendar lastApplyDay) {
		this.mLastApplyDay = lastApplyDay;
	}
	
	/**
	 * 만든 날자를 설정
	 * @param date 만든날짜
	 */
	public void setLastApplyDay(Date date) {
		this.mLastApplyDay.setTime(date);
	}

	public Calendar getLastApplyDay() {
		return mLastApplyDay;
	}

	public void setItemType(int itemType) {
		this.mItemType = itemType;
	}

	public int getItemType() {
		return mItemType;
	}

	public void setItemID(int itemID) {
		this.mItemID = itemID;
	}

	public int getItemID() {
		return mItemID;
	}

	/**
	 * 반복에 해당하는 날짜인지  확인한다.
	 * @param date 대상 날짜
	 * @return 반복에 지정된 날짜이면 True
	 */
	public boolean isRepeatDay(Calendar date) {
		if (mType == ONCE) {
			return false;
		}
		else if (mType == WEEKLY) {
			int weekly = date.get(Calendar.DAY_OF_WEEK);
			
			if (((mWeeklyRepeat & MONDAY) != 0) && (weekly == Calendar.MONDAY)) {
				return true;
			}
			if (((mWeeklyRepeat & TUESDAY) != 0) && (weekly == Calendar.TUESDAY)) {
				return true;	
			}
			if (((mWeeklyRepeat & WEDNESDAY) != 0) && (weekly == Calendar.WEDNESDAY)) {
				return true;
			}
			if (((mWeeklyRepeat & THURSDAY) != 0) && (weekly == Calendar.THURSDAY)) {
				return true;
			}
			if (((mWeeklyRepeat & FRIDAY) != 0) && (weekly == Calendar.FRIDAY)) {
				return true;
			}
			if (((mWeeklyRepeat & SATURDAY) != 0) && (weekly == Calendar.SATURDAY)) {
				return true;
			}
			if (((mWeeklyRepeat & SUNDAY) != 0) && (weekly == Calendar.SUNDAY)) {
				return true;
			}
			return false;
		}
		else if (mType == MONTHLY) {
			return (mDayofMonth == date.get(Calendar.DAY_OF_MONTH));
		}
		else {
			Log.e(LogTag.DATA, ":: invalid repeat type");
			return false;
		}
	}
	
	/**
	 * 요일관련 캘린더 타임을 Repeat타입으로 변환한다.
	 * @param weekOfDay
	 * @return Repeat 요일 타입
	 */
	public static int getCalendarWeekToRepeatWeek(int weekOfDay) {
		if (Calendar.SUNDAY == weekOfDay)			return SUNDAY;
		else if (Calendar.MONDAY == weekOfDay) 		return MONDAY;
		else if (Calendar.TUESDAY == weekOfDay)		return TUESDAY;
		else if (Calendar.WEDNESDAY == weekOfDay) 	return WEDNESDAY;
		else if (Calendar.THURSDAY == weekOfDay) 	return THURSDAY;
		else if (Calendar.FRIDAY == weekOfDay) 		return FRIDAY;
		else if (Calendar.SATURDAY == weekOfDay) 	return SATURDAY;
		else {
			return getCalendarWeekToRepeatWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		}
	}
	
	/**
	 * 반복이 설정된 상태를 매시지를 얻는다.
	 * @return 메시지
	 */
	public String getRepeatMessage() {
		String message = "반복이 설정되지 않았습니다.";
		
		if (mType == WEEKLY) {
			if (isEveryDayRepeat())			return "매일 반복";
			else if (isWeekEndRepeat()) 	return "주말 반복";
			else if (isNormalWeekRepeat()) 	return "평일 반복";
			else {
				message = "매주";
				if (((mWeeklyRepeat & MONDAY) 	!= 0))	message += " 월";
				if (((mWeeklyRepeat & TUESDAY) 	!= 0)) 	message += " 화";
				if (((mWeeklyRepeat & WEDNESDAY)!= 0)) 	message += " 수";
				if (((mWeeklyRepeat & THURSDAY) != 0)) 	message += " 목";
				if (((mWeeklyRepeat & FRIDAY) 	!= 0)) 	message += " 금";
				if (((mWeeklyRepeat & SATURDAY) != 0)) 	message += " 토";
				if (((mWeeklyRepeat & SUNDAY) 	!= 0)) 	message += " 일";
				message += " 요일반복";
			}
		}
		else if (mType == MONTHLY) {
			if (mDayofMonth >= 31) message = String.format("매월  말 반복");
			else message = String.format("매월 %d일 반복", mDayofMonth);
		}
		else {
			message = "반복이 설정되지 않았습니다.";
		}
		
		return message;
	}

	/**
	 * 매일 반복확인
	 * @return 매일 반복이면 true
	 */
	private boolean isEveryDayRepeat() {
		return (mWeeklyRepeat == (MONDAY | TUESDAY | WEDNESDAY | THURSDAY | FRIDAY | SATURDAY | SUNDAY));
	}

	/**
	 * 평일 반복확인
	 * @return 평일 반복이면 true
	 */
	private boolean isNormalWeekRepeat() {
		return (mWeeklyRepeat == (MONDAY | TUESDAY | WEDNESDAY | THURSDAY | FRIDAY));
	}

	/**
	 * 주말 반복확인
	 * @return 주말 반복이면 true
	 */
	private boolean isWeekEndRepeat() {
		return (mWeeklyRepeat == (SATURDAY | SUNDAY));
	}
}
