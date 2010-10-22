package com.fletamuto.sptb.data;

public class Budget {
	private int mID = -1;
	private int mTotalAmount = 0;
	private int mYear = 0;
	private int mMonth = 0;
	
	private BudgetCategory mMainCategory;
	private BudgetCategory mSubCategory;
	
	public void setID(int mID) {
		this.mID = mID;
	}

	public int getID() {
		return mID;
	}

	public void setTotalAmount(int totalAmount) {
		this.mTotalAmount = totalAmount;
	}

	public int getAmount() {
		return mTotalAmount;
	}

	public void setYear(int mYear) {
		this.mYear = mYear;
	}

	public int getYear() {
		return mYear;
	}

	public void setMonth(int month) {
		this.mMonth = month;
	}

	public int getMonth() {
		return mMonth;
	}

	public void setMainCategory(BudgetCategory mainCategory) {
		this.mMainCategory = mainCategory;
	}

	public BudgetCategory getMainCategory() {
		return mMainCategory;
	}

	public void setSubCategory(BudgetCategory subCategory) {
		this.mSubCategory = subCategory;
	}

	public BudgetCategory getSubCategory() {
		return mSubCategory;
	}

}
