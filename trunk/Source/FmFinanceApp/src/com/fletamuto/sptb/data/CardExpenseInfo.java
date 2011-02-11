package com.fletamuto.sptb.data;

import java.io.Serializable;
import java.util.Calendar;



public class CardExpenseInfo implements Serializable{
	private static final long serialVersionUID = -161154125226855497L;
	private CardItem mCard;
	private Calendar mSettlementDate = Calendar.getInstance();
	private long mTotalExpenseAmount = 0L;
	private long mBillingExpenseAmount = 0L;
	private long mNextBillingExpenseAmount = 0L;
	
	public CardExpenseInfo(CardItem card) {
		mCard = card;
	}
	
	public CardItem getCard() {
		return mCard;
	}
	
	public void setCard(CardItem card) {
		mCard = card;
	}

	public void setTotalExpenseAmount(long totalExpenseAmount) {
		this.mTotalExpenseAmount = totalExpenseAmount;
	}

	public long getTotalExpenseAmount() {
		return mTotalExpenseAmount;
	}

	public void setBillingExpenseAmount(long billingExpenseAmount) {
		this.mBillingExpenseAmount = billingExpenseAmount;
	}

	public long getBillingExpenseAmount() {
		return mBillingExpenseAmount;
	}

	public void setNextBillingExpenseAmount(long nextBillingExpenseAmount) {
		this.mNextBillingExpenseAmount = nextBillingExpenseAmount;
	}

	public long getNextBillingExpenseAmount() {
		return mNextBillingExpenseAmount;
	}

	public void setSettlementDate(Calendar settlementDate) {
		this.mSettlementDate = settlementDate;
	}

	public Calendar getSettlementDate() {
		return mSettlementDate;
	}

}
