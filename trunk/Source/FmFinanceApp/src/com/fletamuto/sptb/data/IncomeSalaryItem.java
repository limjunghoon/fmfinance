package com.fletamuto.sptb.data;

public class IncomeSalaryItem extends IncomeItem {
//	public final static int EXEND_TYPE = ItemDef.ExtendIncome.SALARY;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3207895498624452158L;

	private int mSalaryID = -1;
	
	/** ���� */
	private ExpenseItem mExpensePension;
	
	/** ����*/
	private ExpenseItem mExpenseTax;
	
	/** ����*/
	private ExpenseItem mExpenseAssurance;
	
	/** ��Ÿ*/
	private ExpenseItem mExpenseEtc;


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

	public void setExpenseTax(ExpenseItem expenseTax) {
		this.mExpenseTax = expenseTax;
	}

	public ExpenseItem getExpenseTax() {
		return mExpenseTax;
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
