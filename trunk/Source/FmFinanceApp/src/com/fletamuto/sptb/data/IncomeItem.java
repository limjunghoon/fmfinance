package com.fletamuto.sptb.data;

public class IncomeItem extends FinanceItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 172725728587158778L;
	public final static int TYPE = ItemDef.FinanceDef.INCOME;
	
	private AccountItem mAccount = new AccountItem();
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return IncomeItem.TYPE;
	}

	public void setAccount(AccountItem account) {
		this.mAccount = account;
	}

	public AccountItem getAccount() {
		return mAccount;
	}
	
	public String getAccountText() {
		if (mAccount.getID() == -1) {
			return "Çö±Ý";
		}
		
		return "°èÁÂ";
	}
}
