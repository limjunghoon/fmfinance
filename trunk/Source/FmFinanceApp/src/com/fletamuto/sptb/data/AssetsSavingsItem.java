package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

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
	private Calendar mStartDate = Calendar.getInstance();
	
	/**
	 * ���� ���� ��¥
	 */
	private Calendar mEndDate = Calendar.getInstance();
	
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
