package com.fletamuto.sptb.data;

public abstract class PaymentMethod {
	public final static int CASH = 0;
	public final static int CARD = 1;
	public final static int ACCOUNT = 2;
	
	private int mID = -1;
	private int mType = CASH;
	private String mMessage;
	
	public abstract String getText();
	
	public void setId(int id) {
		this.mID = id;
	}
	
	public int getId() {
		return mID;
	}
	
	public void setType(int type) {
		this.mType = type;
	}
	
	public int getType() {
		return mType;
	}

	public void setDisplayMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public String getDisplayMessage() {
		return mMessage;
	}

}
