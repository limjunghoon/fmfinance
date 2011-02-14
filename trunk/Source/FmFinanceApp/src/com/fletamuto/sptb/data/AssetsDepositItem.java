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

	private static final long serialVersionUID = 5991940214076046151L;

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
	
	private int mRate;
	
	
	public AssetsDepositItem() {
		mExpiryDate.add(Calendar.YEAR, 1);
	}

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

	public void setRate(int rate) {
		this.mRate = rate;
	}

	public int getRate() {
		return mRate;
	}
	
	public int getExtendType() {
		return EXEND_TYPE;
	}
	
	public int getMonthPeriodTerm() {
		int monthTerm = 0;
		int yearTerm = mExpiryDate.get(Calendar.YEAR) - getCreateDate().get(Calendar.YEAR);
		if (yearTerm > 0) {
			monthTerm = yearTerm * 12;
		}
		
		return  monthTerm  + (mExpiryDate.get(Calendar.MONTH) - getCreateDate().get(Calendar.MONTH));
	}
	
	public int getMonthProcessCount() {
		int monthTerm = 0;
		int yearTerm = Calendar.getInstance().get(Calendar.YEAR) - getCreateDate().get(Calendar.YEAR);
		if (yearTerm > 0) {
			monthTerm = yearTerm * 12;
		}
		
		return  monthTerm  + (mExpiryDate.get(Calendar.MONTH) - getCreateDate().get(Calendar.MONTH)) + 1;
	}
	
	public boolean isOverExpirationDate() {
		return (Calendar.getInstance().before(getExpiryDate()));
	}
	
	public long getExpectAmount() {
		return (getAmount() * mRate) / 100;
	}
}
