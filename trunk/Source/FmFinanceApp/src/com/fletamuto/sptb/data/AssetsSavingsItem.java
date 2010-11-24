package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * �ڻ��� ����
 * @author yongbban
 *
 */
public class AssetsSavingsItem extends AssetsExtendItem {
	/**
	 * ���� DB���̺� ���̵�
	 */
	private int mSavingsID = -1;
	
	/**
	 * ���� ���� ��¥
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * ����
	 */
	private AccountItem mAccount  = new AccountItem();
	
	/**
	 * ���Ա�
	 */
	private long mPayment = 0;
	
	/**
	 * ���� ���̵� ����
	 * @param mDepositID ���� ���̵�
	 */
	public void setSavingsID(int savingsID) {
		this.mSavingsID = savingsID;
	}

	/**
	 * ���� ���̵� ��´�.
	 * @return ���� ���̵�
	 */
	public int getSavingsID() {
		return mSavingsID;
	}

	
	/**
	 * ���� ���⳯¥�� ����
	 * @param createDate ���� ���۵Ǵ� ��¥
	 */
	public void setExpiryDate(Calendar endDate) {
		this.mExpiryDate = endDate;
	}
	
	/**
	 * ���� ���⳯¥�� ����
	 * @param date ���� ���۵Ǵ� ��¥
	 */
	public void setExpiryDate(Date date) {
		this.mExpiryDate.setTime(date);
	}

	/**
	 * ���� ���⳯¥�� ��´�.
	 * @return ���� ���۵Ǵ� ��¥
	 */
	public Calendar getExpiryDate() {
		return mExpiryDate;
	}
	
	public String getExpriyDateString() {
		return FinanceDataFormat.getDateFormat(mExpiryDate.getTime());
	}

	public void setAccount(AccountItem account) {
		this.mAccount = account;
	}

	public AccountItem getAccount() {
		return mAccount;
	}

	/**
	 * ���Ա� ����
	 * @param payment ���Ա�
	 */
	public void setPayment(long payment) {
		this.mPayment = payment;
	}

	/**
	 * ���Ա��� ��´�.
	 * @return ���Ա�
	 */
	public long getPayment() {
		return mPayment;
	}
}
