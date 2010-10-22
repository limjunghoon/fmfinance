package com.fletamuto.sptb.data;

import java.util.Calendar;

public class ExpenseSMS {
	private int mID = -1;
	private Calendar mCreateDate = Calendar.getInstance();
	private CardItem mCard;
	private String mMessage;
	private boolean mDone = false;
	private String mReceiveNmuber;
	public void setID(int mID) {
		this.mID = mID;
	}
	public int getID() {
		return mID;
	}
	public void setCreateDate(Calendar createDate) {
		this.mCreateDate = createDate;
	}
	public Calendar getCreateDate() {
		return mCreateDate;
	}
	public void setMessage(String message) {
		this.mMessage = message;
	}
	public String getMessage() {
		return mMessage;
	}
	public void setDone(boolean done) {
		this.mDone = done;
	}
	public boolean isDone() {
		return mDone;
	}
	public void setReceiveNmuber(String mReceiveNmuber) {
		this.mReceiveNmuber = mReceiveNmuber;
	}
	public String getReceiveNmuber() {
		return mReceiveNmuber;
	}
	public void setCard(CardItem card) {
		this.mCard = card;
	}
	public CardItem getCard() {
		return mCard;
	}
	
	
}
