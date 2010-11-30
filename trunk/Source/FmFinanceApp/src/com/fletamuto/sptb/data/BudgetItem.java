package com.fletamuto.sptb.data;

public class BudgetItem extends BaseItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6998418680471054316L;
	static final int MAIN_CATEGORY_TITLE = 0;
	static final int MAIN_CATEGORY_ITEM = 1;
	static final int SUB_CATEGORY_TITLE = 2;
	static final int SUB_CATEGORY_ITEM = 3;
	
	private long mExpenseAmountMonth = 0;
	private long mAmount = 0;
	private int mYear = 0;
	private int mMonth = 0;
	private int mType = MAIN_CATEGORY_TITLE;
	
	private Category mExpenseCategory;
	
	public BudgetItem(int year, int month) {
		mYear = year;
		mMonth = month;
	}

	public void setAmount(long amount) {
		this.mAmount = amount;
	}

	public long getAmount() {
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

	public void setType(int type) {
		this.mType = type;
	}

	public int getType() {
		return mType;
	}

	public void setExpenseAmountMonth(long expenseAmountMonth) {
		this.mExpenseAmountMonth = expenseAmountMonth;
	}

	public long getExpenseAmountMonth() {
		return mExpenseAmountMonth;
	} 
}
