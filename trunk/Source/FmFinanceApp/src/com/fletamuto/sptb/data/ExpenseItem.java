package com.fletamuto.sptb.data;

import android.util.Log;

import com.fletamuto.sptb.util.LogTag;


public class ExpenseItem extends FinanceItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4904314900215308453L;

	public final static int TYPE = ItemDef.FinanceDef.EXPENSE;
	
	private boolean mWaste = false;
	private PaymentMethod mPaymentMethod;
	private ExpenseTag mTag = new ExpenseTag();

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return ExpenseItem.TYPE;
	}

	public void setWaste(boolean waste) {
		this.mWaste = waste;
	}
	
	public void setCard(CardItem card) {
		if (mPaymentMethod == null || mPaymentMethod.getType() != PaymentMethod.CARD) {
			return;
		}
		
		((PaymentCardMethod)mPaymentMethod).setCard(card);
	}
	
	public CardItem getCard() {
		if (mPaymentMethod == null || mPaymentMethod.getType() != PaymentMethod.CARD) {
			return null;
		} 
		
		return ((PaymentCardMethod)mPaymentMethod).getCard();
	}


	public boolean isWaste() {
		return mWaste;
	}
	
	/**
	 * 瘤阂规过 按眉甫 积己茄促.
	 * @param paymentSelected 积己茄 摹阂规过
	 * @return PaymentCardMethod
	 */
	public PaymentMethod createPaymentMethod(int paymentSelected) {
		if ((mPaymentMethod != null) && (mPaymentMethod.getType() == paymentSelected)) {
			return null;
		}
		
		if (paymentSelected == PaymentMethod.CASH) {
			mPaymentMethod = new PaymentCashMethod();
		}
		else if (paymentSelected == PaymentMethod.ACCOUNT) {
			mPaymentMethod = new PaymentAccountMethod();
		}
		else if (paymentSelected == PaymentMethod.CARD) {
			mPaymentMethod = new PaymentCardMethod();
		}
		else {
			Log.e(LogTag.DATA, ":: invalid payment");
			return null;
		}
		mPaymentMethod.setType(paymentSelected);
		
		return mPaymentMethod;
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
	
	public void setTag(int id, String name) {
		mTag.setID(id);
		mTag.setName(name);
	}
	
	public int getSelectedPaymentMethodType() {
		if (mPaymentMethod == null) {
			return PaymentMethod.CASH;
		}
		return mPaymentMethod.getType();
	}


	public ExpenseTag getTag() {
		return mTag;
	}
	
	
	
}
