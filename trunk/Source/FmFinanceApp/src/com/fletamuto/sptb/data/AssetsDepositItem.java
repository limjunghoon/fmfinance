package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

/**
 * �ڻ� �� ����
 * @author yongbban
 *
 */
public class AssetsDepositItem extends AssetsExtendItem {
	
	/**
	 * ���� DB���̺� ���̵�
	 */
	private int mDepositID = -1;
	/**
	 * ���� ���� ��¥
	 */
	private Calendar mStartDate = Calendar.getInstance();
	
	/**
	 * ���� ���� ��¥
	 */
	private Calendar mEndDate = Calendar.getInstance();
	
	/**
	 * ����
	 */
	private AccountItem mAccount;
	

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
	 * ���� ���۳�¥�� ����
	 * @param createDate ���� ���۵Ǵ� ��¥
	 */
	public void setStartDate(Calendar startDate) {
		this.mStartDate = startDate;
	}
	
	/**
	 * ���� ���۳�¥�� ����
	 * @param date ���� ���۵Ǵ� ��¥
	 */
	public void setStartDate(Date date) {
		this.mStartDate.setTime(date);
	}

	/**
	 * ���� ���۳�¥�� ��´�.
	 * @return ���� ���۵Ǵ� ��¥
	 */
	public Calendar getStartDate() {
		return mStartDate;
	}
	
	/**
	 * ���� ���⳯¥�� ����
	 * @param createDate ���� ���۵Ǵ� ��¥
	 */
	public void setEndDate(Calendar endDate) {
		this.mEndDate = endDate;
	}
	
	/**
	 * ���� ���⳯¥�� ����
	 * @param date ���� ���۵Ǵ� ��¥
	 */
	public void setEndDate(Date date) {
		this.mEndDate.setTime(date);
	}

	/**
	 * ���� ���⳯¥�� ��´�.
	 * @return ���� ���۵Ǵ� ��¥
	 */
	public Calendar getEndDate() {
		return mEndDate;
	}

	/**
	 * ���� ���� ����
	 * @param account ���� ����
	 */
	public void setAccount(AccountItem account) {
		this.mAccount = account;
	}

	/**
	 * ���� ���¸� ��´�.
	 * @return
	 */
	public AccountItem getAccount() {
		return mAccount;
	}

	
	
}
