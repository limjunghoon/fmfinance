package com.fletamuto.sptb.data;

public class BudgetCategory {
	private int mID = -1;
	private int mAmount = 0;
	private int mYear = 0;
	private int mMonth = 0;
	
	private Category mExpenseCategory;

	public void setID(int mID) {
		this.mID = mID;
	}

	public int getID() {
		return mID;
	}

	public void setAmount(int amount) {
		this.mAmount = amount;
	}

	public int getAmount() {
		return mAmount;
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

	public void setExpenseCategory(Category expenseCategory) {
		this.mExpenseCategory = expenseCategory;
	}

	public Category getExpenseCategory() {
		return mExpenseCategory;
	} 
}
