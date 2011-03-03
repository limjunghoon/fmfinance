package com.fletamuto.sptb.file;

import java.util.ArrayList;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.db.DBMgr;

public class AccountData {	//보통예금
	public static final String STRING_NAME = "보통예금";
	public static final String STRING_FINANCIAL = "금융기관";
	public static final String STRING_ACCOUNT_NUMBER = "계좌번호";
	public static final String STRING_TYPE = "종류";
	public static final String STRING_BALANCE = "잔액";
	
	private String financial;
	private String accountNumber;
	private String type;
	private long balance;
	
	public String getFinancial() {
		return financial;
	}
	public void setFinancial(String financial) {
		this.financial = financial;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}

	public ArrayList<AccountData> getAccountDatas() {
		return new GetAccountDatas().getAccountDatas();
	}
}

class GetAccountDatas {
	protected ArrayList<AccountData> getAccountDatas() {
		ArrayList<AccountItem> accountItems = DBMgr.getAccountAllItems();
		ArrayList<AccountData> accountDatas = new ArrayList<AccountData>();
		
		for(int i = 0, size = accountItems.size(); i < size; i++) {
			AccountData accountData = new AccountData();
			
			accountData.setFinancial(accountItems.get(i).getCompany().getName());
			accountData.setAccountNumber(accountItems.get(i).getNumber());
			accountData.setType(accountItems.get(i).getTypeName());
			accountData.setBalance(accountItems.get(i).getBalance());
			
			accountDatas.add(accountData);
		}
		return accountDatas;
	}
}