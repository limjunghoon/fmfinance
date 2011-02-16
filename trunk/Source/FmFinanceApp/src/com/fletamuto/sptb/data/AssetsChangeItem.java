package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class AssetsChangeItem extends BaseItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6771971252352954357L;
	
	private Calendar mChangeDate = Calendar.getInstance();
	private int mAssetsID = -1;
	private long mAmount = 0L;
	private long mChangeAmount = 0L;
	private String mMemo;
	private int mCount = 0;
	private int mState;
	private String mStore;
	private int mPriceType;
	
	public AssetsChangeItem() {
		
	}
	
	public AssetsChangeItem(AssetsItem assets) {
		mAssetsID = assets.getID();
		mChangeDate = assets.getCreateDate();
		mAmount = assets.getAmount();
		mChangeAmount = assets.getAmount();
		mMemo = assets.getMemo();
		mCount = assets.getCount();
		mState = assets.getState();
		mStore = assets.getStore();
		mPriceType = assets.getPriceType();
	}
	
	public void setChangeDate(Calendar changeDate) {
		this.mChangeDate = changeDate;
	}
	public Calendar getChangeDate() {
		return mChangeDate;
	}
	/**
	 * 날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
	 */
	public String getChangeDateString() {
		return FinanceDataFormat.getDateFormat(mChangeDate.getTime());
	}
	/**
	 * 만든 날자를 설정
	 * @param date 만든날짜
	 */
	public void setChangeDate(Date date) {
		this.mChangeDate.setTime(date);
	}
	public void setAssetsID(int assetsID) {
		this.mAssetsID = assetsID;
	}
	public int getAssetsID() {
		return mAssetsID;
	}
	public void setChangeAmount(long changeAmount) {
		this.mChangeAmount = changeAmount;
	}
	public long getChangeAmount() {
		return mChangeAmount;
	}
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
	public void setCount(int count) {
		this.mCount = count;
	}
	public int getCount() {
		return mCount;
	}
	public void setState(int state) {
		this.mState = state;
	}
	public int getState() {
		return mState;
	}

	public void setStore(String store) {
		this.mStore = store;
	}

	public String getStore() {
		return mStore;
	}

	public void setPriceType(int priceType) {
		this.mPriceType = priceType;
	}

	public int getPriceType() {
		return mPriceType;
	}

}
