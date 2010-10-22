package com.fletamuto.sptb.data;


public class ExpenseItem extends FinanceItem {
	public final static int TYPE = ItemDef.FinanceDef.EXPENSE;
	
	private boolean mWaste = false;
	private PaymentMethod mPaymentMethod;;
	private ExpenseTag mTag = new ExpenseTag();

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return ExpenseItem.TYPE;
	}

	public void setWaste(boolean waste) {
		this.mWaste = waste;
	}


	public boolean isWaste() {
		return mWaste;
	}


	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.mPaymentMethod = paymentMethod;
	}


	public PaymentMethod getPaymentMethod() {
		return mPaymentMethod;
	}


	public void setTag(ExpenseTag tag) {
		this.mTag = tag;
	}


	public ExpenseTag getTag() {
		return mTag;
	}
	
	
	
}
