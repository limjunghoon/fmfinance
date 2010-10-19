package com.fletamuto.sptb.data;

import java.util.Calendar;

public class AccountItem {
	public final static int ORDINARY_DEPOSIT = 0;
	
	private int	mID = -1;
	private String mNumber;
	private long mBalance = 0L;
	private FinancialCompany mInstitution;
	private Calendar mCreateDate = Calendar.getInstance();
	private Calendar mExpiryDate = Calendar.getInstance();
	private String mMemo;
	private String mName;
	private int mType = ORDINARY_DEPOSIT;
	
	public void setID(int mID) {
		this.mID = mID;
	}
	
	public int getID() {
		return mID;
	}
	
	public void setNumber(String number) {
		this.mNumber = number;
	}
	
	public String getNumber() {
		return mNumber;
	}
	
	public void setInstitution(FinancialCompany institution) {
		this.mInstitution = institution;
	}
	
	public FinancialCompany getInstitution() {
		return mInstitution;
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
	
	public Calendar getCreateDate() {
		return mCreateDate;
	}
	
	public void setExpiryDate(Calendar expiryDate) {
		this.mExpiryDate = expiryDate;
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
}
