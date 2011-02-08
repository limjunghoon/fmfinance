package com.fletamuto.sptb.data;

import java.util.Calendar;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class TransferItem extends BaseItem {

	private static final long serialVersionUID = 6774647452205496776L;
	
	private Calendar mOccurrentceDate = Calendar.getInstance();
	private AccountItem mToAccount = new AccountItem();
	private AccountItem mFromAccount = new AccountItem();
	private long mAmount;
	private String mMemo;
	
	public void setOccurrentceDate(Calendar occurrentceDate) {
		this.mOccurrentceDate = occurrentceDate;
	}
	public Calendar getOccurrentceDate() {
		return mOccurrentceDate;
	}
	public void setToAccount(AccountItem toAccount) {
		this.mToAccount = toAccount;
	}
	public AccountItem getToAccount() {
		return mToAccount;
	}
	public void setFromAccount(AccountItem fromAccount) {
		this.mFromAccount = fromAccount;
	}
	public AccountItem getFromAccount() {
		return mFromAccount;
	}
	public void setAmount(long amount) {
		this.mAmount = amount;
	}
	public long getAmount() {
		return mAmount;
	}
	public void setMemo(String memo) {
		this.mMemo = memo;
	}
	public String getMemo() {
		return mMemo;
	}
	/**
	 * 날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
	 */
	public String getOccurrentceDateString() {
		return FinanceDataFormat.getDateFormat(mOccurrentceDate.getTime());
	}
	
}
