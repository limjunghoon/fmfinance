package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import android.content.res.Resources.Theme;
import android.util.Log;

import com.fletamuto.sptb.LogTag;
import com.fletamuto.sptb.util.FinanceDataFormat;

public class Repeat {

	
	public static final int ONCE = 0;
	/** �ְ� �ݺ� */
	public static final int WEEKLY = 1;
	
	/** ��¥ �ݺ� */
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
	
	/** �ְ�/���� �ݺ� ���� */
	private int mType = ONCE;
	
	/** �ְ� �ݺ� ���� */
	private int mWeeklyRepeat = 0;
	
	/** ���� �ݺ� ��¥ ���� */
	private int mDayofMonth = 1;
	
	private int mID = -1;
	
	private Calendar mLastApplyDay = Calendar.getInstance();
	
	/**
	 * Ÿ���� ��´�.
	 * @return Ÿ��
	 */
	public int getType() {
		return mType;
	}
	
	/**
	 * Ÿ���� ����
	 * @param type WEEKLY, MONTHLY
	 */
	public void setType(int type) {
		mType = type;
	}
	
	/**
	 * �ְ� �ݺ� ���� ��´�.
	 * @return �ְ� �ݺ���
	 */
	public int getWeeklyRepeat() {
		return mWeeklyRepeat;
	}
	
	/**
	 * �ְ��ݺ����� ����
	 * @param weekly ������ �ְ� �ݺ���
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

	public void setID(int id) {
		this.mID = id;
	}

	public int getID() {
		return mID;
	}

	/**
	 * ��¥�� ���ڿ� �������� ��´�.
	 * @return ��¥
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
	 * ���� ���ڸ� ����
	 * @param date ���糯¥
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
}
