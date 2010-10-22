package com.fletamuto.sptb.data;

public class Repeat {
	/**
	 * �ְ� �ݺ�
	 */
	public static final int WEEKLY = 0;
	/**
	 * ��¥ �ݺ�
	 */
	public static final int MONTHLY = 1;
	/**
	 * �ְ�/���� �ݺ� ����
	 */
	int mType = WEEKLY;
	
	/**
	 * �ְ� �ݺ� ����
	 */
	int mWeeklyRepeat = 0;
	/**
	 * ���� �ݺ� ��¥ ����
	 */
	int mMonthRepeat = 1;
	
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
	
	public void setDailyRepeat(int day) {
		mType = MONTHLY;
		mMonthRepeat = day;
	}
	
}
