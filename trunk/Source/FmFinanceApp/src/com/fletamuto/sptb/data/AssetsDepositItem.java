package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

/**
 * 자산 중 예금
 * @author yongbban
 *
 */
public class AssetsDepositItem extends AssetsExtendItem {
	
	/**
	 * 예금 DB테이블 아이디
	 */
	private int mDepositID = -1;
	/**
	 * 예금 시작 날짜
	 */
	private Calendar mStartDate = Calendar.getInstance();
	
	/**
	 * 예금 만기 날짜
	 */
	private Calendar mEndDate = Calendar.getInstance();
	
	/**
	 * 계좌
	 */
	private AccountItem mAccount;
	

	/**
	 * 예금 아이디 설정
	 * @param mDepositID 예금 아이디
	 */
	public void setDepositID(int depositID) {
		this.mDepositID = depositID;
	}

	/**
	 * 예금 아이디를 얻는다.
	 * @return 예금 아이디
	 */
	public int getDepositID() {
		return mDepositID;
	}

	
	/**
	 * 예금 시작날짜를 설정
	 * @param createDate 예금 시작되는 날짜
	 */
	public void setStartDate(Calendar startDate) {
		this.mStartDate = startDate;
	}
	
	/**
	 * 예금 시작날짜를 설정
	 * @param date 예금 시작되는 날짜
	 */
	public void setStartDate(Date date) {
		this.mStartDate.setTime(date);
	}

	/**
	 * 예금 시작날짜를 얻는다.
	 * @return 예금 시작되는 날짜
	 */
	public Calendar getStartDate() {
		return mStartDate;
	}
	
	/**
	 * 예금 만기날짜를 설정
	 * @param createDate 예금 시작되는 날짜
	 */
	public void setEndDate(Calendar endDate) {
		this.mEndDate = endDate;
	}
	
	/**
	 * 예금 만기날짜를 설정
	 * @param date 예금 시작되는 날짜
	 */
	public void setEndDate(Date date) {
		this.mEndDate.setTime(date);
	}

	/**
	 * 예금 만기날짜를 얻는다.
	 * @return 예금 시작되는 날짜
	 */
	public Calendar getEndDate() {
		return mEndDate;
	}

	/**
	 * 예금 계좌 설정
	 * @param account 예금 계좌
	 */
	public void setAccount(AccountItem account) {
		this.mAccount = account;
	}

	/**
	 * 예금 계좌를 얻는다.
	 * @return
	 */
	public AccountItem getAccount() {
		return mAccount;
	}

	
	
}
