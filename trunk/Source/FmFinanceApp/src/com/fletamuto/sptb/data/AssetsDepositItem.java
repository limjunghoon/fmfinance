package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * 자산 중 예금
 * @author yongbban
 *
 */
public class AssetsDepositItem extends AssetsExtendItem {
	public final static int EXEND_TYPE = ItemDef.ExtendAssets.DEPOSIT;
	
	/**
	 * 예금 DB테이블 아이디
	 */
	private int mDepositID = -1;
	
	/**
	 * 예금 만기 날짜
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * 계좌
	 */
	private AccountItem mAccount  = new AccountItem();
	

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
	
	
}
