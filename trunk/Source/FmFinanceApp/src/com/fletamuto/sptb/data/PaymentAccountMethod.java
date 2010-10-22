package com.fletamuto.sptb.data;

public class PaymentAccountMethod extends PaymentMethod {
	private AccountItem mAccount;
	
	public AccountItem getAccount() {
		return mAccount;
	}
	
	public void setAccount(AccountItem account) {
		mAccount = account;
	}
	
	public int getAccountID() {
		return mAccount.getID();
	}
	
	public void setAccountID(int id) {
		mAccount.setID(id);
	}
}
