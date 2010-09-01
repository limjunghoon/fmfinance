package com.fletamuto.sptb.data;

public class CardItem {
	public final static int CREDIT_CARD = 1;
	public final static int CASH_CARD = 2;
	public final static int PRIPAID_CARD = 3;
	
	private int mID;
	private CardCompenyName mCompenyName;
	private String mName;
	private String mNumber;
	private AccountItem mAccount;
	private String mMemo;
	private int mType = CREDIT_CARD;
	private Settlement mSettlement = new Settlement();
	
	public class Settlement {
		private int mDay;
		private int mStartDay;
		private int mStartMonth;
		private int mEndDay;
		private int mEndMonth;
	}
	
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

	public void setAccount(AccountItem account) {
		this.mAccount = account;
	}

	public AccountItem getAccount() {
		return mAccount;
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
	
	public void setSettlementDay(int day) {
		mSettlement.mDay = day;
	}
	
	public int getSettlementDay() {
		return mSettlement.mDay;
	}
	
	public void setStartSettlementDay(int startDay) {
		mSettlement.mStartDay = startDay;
	}
	
	public int getStartSettlementDay() {
		return mSettlement.mStartDay;
	}
	
	public void setStartSettlementMonth(int startMonth) {
		mSettlement.mStartMonth = startMonth;
	}
	
	public int getStartSettlementMonth() {
		return mSettlement.mStartMonth;
	}
	
	public void setEndSettlementDay(int endDay) {
		mSettlement.mEndDay = endDay;
	}
	
	public int getEndSettlementDay() {
		return mSettlement.mEndDay;
	}
	
	public void setEndSettlementMonth(int endMonth) {
		mSettlement.mEndMonth = endMonth;
	}
	
	public int getEndSettlementMonth() {
		return mSettlement.mEndMonth;
	}
}
