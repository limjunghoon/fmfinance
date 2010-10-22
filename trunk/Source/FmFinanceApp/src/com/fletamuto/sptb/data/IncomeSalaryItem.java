package com.fletamuto.sptb.data;

public class IncomeSalaryItem extends IncomeItem {
	public final static int EXEND_TYPE = ItemDef.ExtendIncome.SALARY;
	
	private int mExtendID = -1;
	private int mSalaryID = -1;
	
	private ExpenseItem mExpensePension;
	private ExpenseItem mExpenseTex;
	private ExpenseItem mExpenseAssurance;
	private ExpenseItem mExpenseEtc;
	
	public void setExtendID(int extendID) {
		this.mExtendID = extendID;
	}
	
	public int getExtendID() {
		return mExtendID;
	}

	public void setSalaryID(int salaryID) {
		this.mSalaryID = salaryID;
	}

	public int getSalaryID() {
		return mSalaryID;
	}

	public void setExpensePension(ExpenseItem expensePension) {
		this.mExpensePension = expensePension;
	}

	public ExpenseItem getExpensePension() {
		return mExpensePension;
	}

	public void setExpenseTex(ExpenseItem expenseTex) {
		this.mExpenseTex = expenseTex;
	}

	public ExpenseItem getExpenseTex() {
		return mExpenseTex;
	}

	public void setExpenseAssurance(ExpenseItem mExpenseAssurance) {
		this.mExpenseAssurance = mExpenseAssurance;
	}

	public ExpenseItem getExpenseAssurance() {
		return mExpenseAssurance;
	}

	public void setExpenseEtc(ExpenseItem expenseEtc) {
		this.mExpenseEtc = expenseEtc;
	}

	public ExpenseItem getExpenseEtc() {
		return mExpenseEtc;
	}

}
