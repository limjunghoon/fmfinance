package com.fletamuto.sptb.data;

public class CardItem {
	public final static int CREDIT_CARD = 1;
	public final static int CHECK_CARD = 2;
	public final static int PREPAID_CARD = 3;
	public final static int CASH_CARD = 4;
	
	private int mID = -1;
	private CardCompenyName mCompenyName;
	private String mName;
	private String mNumber;
	private int mAccountID;
	private String mMemo;
	private int mType = CREDIT_CARD;
	private long mBalance;
	private int mSettlementDay = 1;
	private int mBillingPeriodDay = 1;
	private int mBillingPeriodMonth = 0;
	
	public CardItem(int type) {
		mType = type;
	}

	public void setID(int id) {
		this.mID = id;
	}

	public int getID() {
		return mID;
	}

	public void setCompenyName(CardCompenyName compenyName) {
		this.mCompenyName = compenyName;
	}

	public CardCompenyName getCompenyName() {
		return mCompenyName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setNumber(String number) {
		this.mNumber = number;
	}

	public String getNumber() {
		return mNumber;
	}

	public void setAccountID(int accountID) {
		this.mAccountID = accountID;
	}

	public int getAccountID() {
		return mAccountID;
	}

	public void setMemo(String memo) {
		this.mMemo = memo;
	}

	public String getMemo() {
		return mMemo;
	}

	public void setType(int type) {
		this.mType = type;
	}

	public int getType() {
		return mType;
	}

	public void setBalance(long balance) {
		this.mBalance = balance;
	}

	public long getBalance() {
		return mBalance;
	}

	public void setSettlementDay(int settlementDay) {
		this.mSettlementDay = settlementDay;
	}

	public int getSettlementDay() {
		return mSettlementDay;
	}

	public void setBillingPeriodDay(int billingPeriodDay) {
		this.mBillingPeriodDay = billingPeriodDay;
	}

	public int getBillingPeriodDay() {
		return mBillingPeriodDay;
	}

	public void setBillingPeriodMonth(int billingPeriodMonth) {
		this.mBillingPeriodMonth = billingPeriodMonth;
	}

	public int getmillingPeriodMonth() {
		return mBillingPeriodMonth;
	}
}
