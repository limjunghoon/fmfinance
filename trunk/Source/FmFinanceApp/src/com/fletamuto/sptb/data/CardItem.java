package com.fletamuto.sptb.data;

import java.util.Calendar;

public class CardItem extends BaseItem{

	private static final long serialVersionUID = -1671898251206337042L;
	public static final int BILLING = 0;
	public static final int NEXT_BILLING = 1;
	
	public final static int CREDIT_CARD = 1;
	public final static int CHECK_CARD = 2;
	public final static int PREPAID_CARD = 3;
	public final static int CASH_CARD = 4;
	
	private CardCompanyName mCompenyName = new CardCompanyName();
	private String mName;
	private String mNumber;
	private AccountItem mAccount = new AccountItem();
	private String mMemo;
	private int mType = CREDIT_CARD;
	private long mBalance;
	private int mSettlementDay = 1;
	private int mBillingPeriodDay = 1;
	private int mBillingPeriodMonth = 0;
	
	public CardItem(int type) {
		mType = type;
	}

	public void setCompenyName(CardCompanyName compenyName) {
		this.mCompenyName = compenyName;
	}

	public CardCompanyName getCompenyName() {
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

	public int getBillingPeriodMonth() {
		return mBillingPeriodMonth;
	}
	
	public Calendar getStartBillingPeriod(Calendar currentDate) {
		Calendar startBillingPeriod = Calendar.getInstance();
		startBillingPeriod.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), mBillingPeriodDay);
		startBillingPeriod.add(Calendar.MONTH, -1);
		return startBillingPeriod;
	}
	
	public Calendar getEndBillingPeriod(Calendar currentDate) {
		Calendar endBillingPeriod = Calendar.getInstance();
		endBillingPeriod.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), mBillingPeriodDay);
		endBillingPeriod.add(Calendar.DAY_OF_MONTH, -1);
		return endBillingPeriod;
	}
	
	public Calendar getNextStartBillingPeriod(Calendar currentDate) {
		Calendar startBillingPeriod = getStartBillingPeriod(currentDate);
		startBillingPeriod.add(Calendar.MONTH, 1);
		return startBillingPeriod;
	}
	
	public Calendar getNextEndBillingPeriod(Calendar currentDate) {
		Calendar endBillingPeriod = getEndBillingPeriod(currentDate);
		endBillingPeriod.add(Calendar.MONTH, 1);
		return endBillingPeriod;
	}
}
