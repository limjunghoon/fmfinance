package com.fletamuto.sptb.data;

public class PaymentAccountMethod extends PaymentMethod {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6893851018348918474L;
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

	@Override
	public String getName() {
		if (mAccount == null) return "°èÁÂ";
		return "°èÁÂ :" + mAccount.getCompany().getName() +  "(" + mAccount.getNumber() + ")";
	}
}
