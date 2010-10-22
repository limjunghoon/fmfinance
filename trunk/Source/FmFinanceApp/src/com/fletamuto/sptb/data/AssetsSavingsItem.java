package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

/**
 * 자산중 적금
 * @author yongbban
 *
 */
public class AssetsSavingsItem extends AssetsExtendItem {
	
	/**
	 * 적금 DB테이블 아이디
	 */
	private int mSavingsID = -1;
	
	/**
	 * 예금 시작 날짜
	 */
	private Calendar mStartDate = Calendar.getInstance();
	
	/**
	 * 적금 만기 날짜
	 */
	private Calendar mEndDate = Calendar.getInstance();
	
	/**
	 * 납입금
	 */
	private long mPayment = 0;
	
	/**
	 * 적금 아이디 설정
	 * @param mDepositID 적금 아이디
	 */
	public void setSavingsID(int savingsID) {
		this.mSavingsID = savingsID;
	}

	/**
	 * 적금 아이디를 얻는다.
	 * @return 적금 아이디
	 */
	public int getSavingsID() {
		return mSavingsID;
	}
	

	/**
	 * 적금 시작날짜를 설정
	 * @param createDate 적금 시작되는 날짜
	 */
	public void setStartDate(Calendar startDate) {
		this.mStartDate = startDate;
	}
	
	/**
	 * 적금 시작날짜를 설정
	 * @param date 적금 시작되는 날짜
	 */
	public void setStartDate(Date date) {
		this.mStartDate.setTime(date);
	}

	/**
	 * 적금 시작날짜를 얻는다.
	 * @return 적금 시작되는 날짜
	 */
	public Calendar getStartDate() {
		return mStartDate;
	}
	
	/**
	 * 적금 만기날짜를 설정
	 * @param createDate 적금 시작되는 날짜
	 */
	public void setEndDate(Calendar endDate) {
		this.mEndDate = endDate;
	}
	
	/**
	 * 적금 만기날짜를 설정
	 * @param date 적금 시작되는 날짜
	 */
	public void setEndDate(Date date) {
		this.mEndDate.setTime(date);
	}

	/**
	 * 적금 만기날짜를 얻는다.
	 * @return 적금 시작되는 날짜
	 */
	public Calendar getEndDate() {
		return mEndDate;
	}

	/**
	 * 납입금 설정
	 * @param payment 납입금
	 */
	public void setPayment(long payment) {
		this.mPayment = payment;
	}

	/**
	 * 납입금을 얻는다.
	 * @return 납입금
	 */
	public long getPayment() {
		return mPayment;
	}
}
