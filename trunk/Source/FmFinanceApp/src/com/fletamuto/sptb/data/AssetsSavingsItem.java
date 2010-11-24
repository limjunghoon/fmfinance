package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

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
	 * 예금 만기 날짜
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * 계좌
	 */
	private AccountItem mAccount  = new AccountItem();
	
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
	 * 예금 만기날짜를 설정
	 * @param createDate 예금 시작되는 날짜
	 */
	public void setExpiryDate(Calendar endDate) {
		this.mExpiryDate = endDate;
	}
	
	/**
	 * 예금 만기날짜를 설정
	 * @param date 예금 시작되는 날짜
	 */
	public void setExpiryDate(Date date) {
		this.mExpiryDate.setTime(date);
	}

	/**
	 * 예금 만기날짜를 얻는다.
	 * @return 예금 시작되는 날짜
	 */
	public Calendar getExpiryDate() {
		return mExpiryDate;
	}
	
	public String getExpriyDateString() {
		return FinanceDataFormat.getDateFormat(mExpiryDate.getTime());
	}

	public void setAccount(AccountItem account) {
		this.mAccount = account;
	}

	public AccountItem getAccount() {
		return mAccount;
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
