package com.fletamuto.sptb.data;

import java.util.Calendar;

public class Purpose {
	private long mAmount;
	private Calendar mStartDate = Calendar.getInstance();
	private Calendar mEndDate = Calendar.getInstance();
	
	public void setAmount(long mAmount) {
		this.mAmount = mAmount;
	}
	public long getAmount() {
		return mAmount;
	}
	public void setStartDate(Calendar mStartDate) {
		this.mStartDate = mStartDate;
	}
	public Calendar getStartDate() {
		return mStartDate;
	}
	public void setEndDate(Calendar mEndDate) {
		this.mEndDate = mEndDate;
	}
	public Calendar getEndDate() {
		return mEndDate;
	}
	
	
}
