package com.fletamuto.sptb.data;

public class ReceiveMethod extends BaseItem {

	private static final long serialVersionUID = -726005593593505670L;
	public final static int CASH = 0;
	public final static int ACCOUNT = 2;
	
	private int mType = CASH;
	private String mMessage;
	private AccountItem mAccount;
	/** Ä«µå³ª °èÁÂ ¾ÆÀÌµð*/
	private int mMethodItemID = -1;

	
	public void setType(int type) {
		this.mType = type;
	}
	
	public void setAccount(AccountItem account) {
		mAccount = account;
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
	
	public String getText() {
		if (mType == CASH) 
			return "Çö±Ý";
		else {
			if (mAccount == null) return "°èÁÂ";
			if (mAccount.getID() == -1) return "°èÁÂ";
			return mAccount.getCompany().getName() +  " : " + mAccount.getNumber();
		}
	}

	public String getName() {
		if (mAccount == null ||  mAccount.getID() == -1) return "Çö±Ý";
		return "°èÁÂ :" + mAccount.getCompany().getName() +  "(" + mAccount.getNumber() + ")";
	}
	
	 

}
