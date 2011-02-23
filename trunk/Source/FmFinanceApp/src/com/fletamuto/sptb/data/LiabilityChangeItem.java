package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class LiabilityChangeItem extends BaseItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6771971252352954357L;
	
	private Calendar mChangeDate = Calendar.getInstance();
	private int mLiabilityID = -1;
	
	/** ���� �ݾ� */
	private long mAmount = 0L;
	
	/** ����*/
	private ExpenseItem mPrincipal = new ExpenseItem();
	
	/** ����*/
	private ExpenseItem mInterest = new ExpenseItem();
	
	/** �޸�*/
	private String mMemo;
	
//	private int mCount = 1;
//	private int mState;
//	private String mStore;
//	private int mPriceType;
	
	public LiabilityChangeItem() {
		
	}
	
	public LiabilityChangeItem(LiabilityItem liability) {
		mLiabilityID = liability.getID();
		mChangeDate = liability.getCreateDate();
		mAmount = liability.getAmount();
//		mChangeAmount = liability.getAmount();
		mMemo = liability.getMemo();
//		mCount = liability.getCount();
//		mState = liability.getState();
//		mStore = liability.getStore();
//		mPriceType = liability.getPriceType();
	}
	
	public void setChangeDate(Calendar changeDate) {
		this.mChangeDate = changeDate;
	}
	public Calendar getChangeDate() {
		return mChangeDate;
	}
	/**
	 * ��¥�� ���ڿ� �������� ��´�.
	 * @return ��¥
	 */
	public String getChangeDateString() {
		return FinanceDataFormat.getDateFormat(mChangeDate.getTime());
	}
	/**
	 * ���� ���ڸ� ����
	 * @param date ���糯¥
	 */
	public void setChangeDate(Date date) {
		this.mChangeDate.setTime(date);
	}
	public void setLiabilityID(int liabilityID) {
		this.mLiabilityID = liabilityID;
	}
	public int getLiabilityID() {
		return mLiabilityID;
	}
//	public void setChangeAmount(long changeAmount) {
//		this.mChangeAmount = changeAmount;
//	}
//	public long getChangeAmount() {
//		return mChangeAmount;
//	}
	public void setAmount(long amount) {
		this.mAmount = amount;
	}
	public long getAmount() {
		return mAmount;
	}
	public void setMemo(String memo) {
		this.mMemo = memo;
	}
	public String getMemo() {
		return mMemo;
	}
//	public void setCount(int count) {
//		this.mCount = count;
//	}
//	public int getCount() {
//		return mCount;
//	}
//	public void setState(int state) {
//		this.mState = state;
//	}
//	public int getState() {
//		return mState;
//	}

	public void setPrincipal(ExpenseItem principal) {
		this.mPrincipal = principal;
	}

	public ExpenseItem getPrincipal() {
		return mPrincipal;
	}

	public void setInterest(ExpenseItem interest) {
		this.mInterest = interest;
	}

	public ExpenseItem getInterest() {
		return mInterest;
	}

//	public void setStore(String store) {
//		this.mStore = store;
//	}
//
//	public String getStore() {
//		return mStore;
//	}
//
//	public void setPriceType(int priceType) {
//		this.mPriceType = priceType;
//	}
//
//	public int getPriceType() {
//		return mPriceType;
//	}

}
