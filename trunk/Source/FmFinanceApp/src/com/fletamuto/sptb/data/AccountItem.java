package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class AccountItem extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5366848481035789230L;

	public final static int ORDINARY_DEPOSIT = 0;
	
	private String mNumber;
	private long mBalance = 0L;
	private FinancialCompany mCompany = new FinancialCompany();
	private Calendar mCreateDate = Calendar.getInstance();
	private Calendar mExpiryDate = Calendar.getInstance();
	private String mMemo;
	private String mName;
	private int mType = ORDINARY_DEPOSIT;

	
	public void setNumber(String number) {
		this.mNumber = number;
	}
	
	public String getNumber() {
		return mNumber;
	}
	
	public void setCompany(FinancialCompany Company) {
		this.mCompany = Company;
	}
	
	public FinancialCompany getCompany() {
		return mCompany;
	}
	
	public void setBalance(long balance) {
		this.mBalance = balance;
	}
	
	public long getBalance() {
		return mBalance;
	}
	
	public void setCreateDate(Calendar CreateDate) {
		this.mCreateDate = CreateDate;
	}
	
	/**
	 * 만든시간를 설정
	 * @param date 만든시간
	 */
	public void setCreateDate(Date date) {
		this.mCreateDate.setTime(date);
	}
	
	public Calendar getCreateDate() {
		return mCreateDate;
	}
	
	public void setExpiryDate(Calendar expiryDate) {
		this.mExpiryDate = expiryDate;
	}
	
	
	public void setExpiryDate(Date date) {
		this.mExpiryDate.setTime(date);
	}
	
	public Calendar getExpiryDate() {
		return mExpiryDate;
	}
	
	public void setMemo(String memo) {
		this.mMemo = memo;
	}
	
	public String getMemo() {
		return mMemo;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
	
	public String getName() {
		return mName;
	}

	public void setType(int type) {
		this.mType = type;
	}

	public int getType() {
		return mType;
	}
	
	/**
	 * 날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
	 */
	public String getCreateDateString() {
		return FinanceDataFormat.getDateFormat(mCreateDate.getTime());
	}
	
	/**
	 * 만료날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
	 */
	public String getExpiryDateString() {
		return FinanceDataFormat.getDateFormat(mCreateDate.getTime());
	}
}
