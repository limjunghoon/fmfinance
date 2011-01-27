package com.fletamuto.sptb.data;


public class CategoryAmount {
	private int mCategoryID;
	private long mTotalAmount;
	private String mName;
	private int mCount = 1;
	private int mType = -1;
	
	public CategoryAmount(int type) {
		mType = type;
	}
	
	public int getType() {
		return mType;
	}
	
	public int getCategoryID() {
		return mCategoryID;
	}

	public long getTotalAmount() {
		return mTotalAmount;
	}
	public String getName() {
		return mName;
	}
	
	public int getCount() {
		return mCount;
	}
	
	public void setCount(int count) {
		mCount = count;
	}
	
	public void addAmount(long amount) {
		mTotalAmount += amount;
		mCount++;
	}
	
	public void set(int id, String name, long amount) {
		mCategoryID = id;
		mName = name;
		mTotalAmount = amount;
	}
}
