package com.fletamuto.sptb.data;

import java.util.Calendar;

public class Receipt {
	private Calendar mCreateDate = Calendar.getInstance();
	private Category mExpenseMainCategory;
	private Category mExpenseSubCategory;
	private boolean mDone = false;
	private String mMemo;
	private int mPictureInfo = -1;
	
	public void setCreateDate(Calendar createDate) {
		this.mCreateDate = createDate;
	}
	public Calendar getCreateDate() {
		return mCreateDate;
	}
	public void setExpenseMainCategory(Category expenseMainCategory) {
		this.mExpenseMainCategory = expenseMainCategory;
	}
	public Category getExpenseMainCategory() {
		return mExpenseMainCategory;
	}
	public void setExpenseSubCategory(Category expenseSubCategory) {
		this.mExpenseSubCategory = expenseSubCategory;
	}
	public Category getExpenseSubCategory() {
		return mExpenseSubCategory;
	}
	public void setDone(boolean mDone) {
		this.mDone = mDone;
	}
	public boolean isDone() {
		return mDone;
	}
	public void setMemo(String memo) {
		this.mMemo = memo;
	}
	public String getMemo() {
		return mMemo;
	}
	public void setPictureInfo(int pictureInfo) {
		this.mPictureInfo = pictureInfo;
	}
	public int getPictureInfo() {
		return mPictureInfo;
	}
	
	
}
