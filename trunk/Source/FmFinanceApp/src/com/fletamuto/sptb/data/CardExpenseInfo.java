package com.fletamuto.sptb.data;


public class CardExpenseInfo{
	CardItem mCard;
	private long mTotalExpenseAmount = 0L;
	private long mExpectedExpenseAmount = 0L;
	
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

	public void setExpectedExpenseAmount(long expectedExpenseAmount) {
		this.mExpectedExpenseAmount = expectedExpenseAmount;
	}

	public long getExpectedExpenseAmount() {
		return mExpectedExpenseAmount;
	}

}
