package com.fletamuto.sptb.data;

public class ExpenseTag {
	public static final int NONE_ID = 1; 
	private int mID = NONE_ID;
	private String mName;
	private int mPrioritize = -1;
	private int mImageIndex = -1;
	
	public void setID(int mID) {
		this.mID = mID;
	}
	
	public int getID() {
		return mID;
	}
	
	public void setName(String mName) {
		this.mName = mName;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setPrioritize(int mPrioritize) {
		this.mPrioritize = mPrioritize;
	}
	
	public int getPrioritize() {
		return mPrioritize;
	}
	
	public void setImageIndex(int mImageIndex) {
		this.mImageIndex = mImageIndex;
	}
	
	public int getImageIndex() {
		return mImageIndex;
	}
}
