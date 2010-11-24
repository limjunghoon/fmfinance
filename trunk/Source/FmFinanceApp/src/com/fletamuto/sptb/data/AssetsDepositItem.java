package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * �ڻ� �� ����
 * @author yongbban
 *
 */
public class AssetsDepositItem extends AssetsExtendItem {
	public final static int EXEND_TYPE = ItemDef.ExtendAssets.DEPOSIT;
	
	/**
	 * ���� DB���̺� ���̵�
	 */
	private int mDepositID = -1;
	
	/**
	 * ���� ���� ��¥
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * ����
	 */
	private AccountItem mAccount  = new AccountItem();
	

	/**
	 * ���� ���̵� ����
	 * @param mDepositID ���� ���̵�
	 */
	public void setDepositID(int depositID) {
		this.mDepositID = depositID;
	}

	/**
	 * ���� ���̵� ��´�.
	 * @return ���� ���̵�
	 */
	public int getDepositID() {
		return mDepositID;
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
	
	
}
