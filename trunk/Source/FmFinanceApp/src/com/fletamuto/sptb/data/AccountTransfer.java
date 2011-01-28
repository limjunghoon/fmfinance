package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class AccountTransfer extends BaseItem {
	private static final long serialVersionUID = 4296498035300419596L;
	
	private Calendar mOccurrenceDate = Calendar.getInstance();
	private int mToAccountId = -1;
	private int mFromAccountId = -1;
	private long mAmount = 0L;
	
	public void setOccurrenceDate(Calendar occurrenceDate) {
		this.mOccurrenceDate = occurrenceDate;
	}
	public Calendar getOccurrenceDate() {
		return mOccurrenceDate;
	}
	public void setToAccountId(int toAccountId) {
		this.mToAccountId = toAccountId;
	}
	public int getToAccountId() {
		return mToAccountId;
	}
	public void setFromAccountId(int fromAccountId) {
		this.mFromAccountId = fromAccountId;
	}
	public int getFromAccountId() {
		return mFromAccountId;
	}
	public void setAmount(long amount) {
		this.mAmount = amount;
	}
	public long getAmount() {
		return mAmount;
	}
	
	/**
	 * ��¥�� ���ڿ� �������� ��´�.
	 * @return ��¥
	 */
	public String getOccurrenceDateString() {
		return FinanceDataFormat.getDateFormat(mOccurrenceDate.getTime());
	}
	
	/**
	 * ����ð��� ����
	 * @param date ����ð�
	 */
	public void setOccurrenceDate(Date date) {
		this.mOccurrenceDate.setTime(date);
	}
	
	
}
