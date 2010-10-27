package com.fletamuto.sptb.data;

public class PaymentAccountMethod extends PaymentMethod {
	private AccountItem mAccount;
	
	PaymentAccountMethod() {
		setType(ACCOUNT);
	}

	public void setAccount(AccountItem account) {
		this.mAccount = account;
	}

	public AccountItem getAccount() {
		return mAccount;
	}

	@Override
	public String getText() {
		if (mAccount == null) return "°èÁÂ";
		
		return mAccount.getCompany().getName() +  " : " + mAccount.getNumber();
	}
}
