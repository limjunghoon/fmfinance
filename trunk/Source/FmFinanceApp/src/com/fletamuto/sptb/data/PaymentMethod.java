package com.fletamuto.sptb.data;

public abstract class PaymentMethod extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -726005593593505670L;
	public final static int CASH = 0;
	public final static int CARD = 1;
	public final static int ACCOUNT = 2;
	
	private int mType = CASH;
	private String mMessage;
	/** 카드나 계좌 아이디*/
	private int mMethodItemID = -1;
	
	public abstract String getText();
	
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

	public void setMethodItemID(int methodItemID) {
		this.mMethodItemID = methodItemID;
	}

	public int getMethodItemID() {
		return mMethodItemID;
	}

}
