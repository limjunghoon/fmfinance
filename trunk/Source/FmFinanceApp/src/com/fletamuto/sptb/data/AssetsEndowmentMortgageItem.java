package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

/**
 * �ڻ� �� ���强 ����
 * @author yongbban
 *
 */
public class AssetsEndowmentMortgageItem extends AssetsExtendItem {
	
	/**
	 * ���强 ���� DB���̺� ���̵�
	 */
	private int mEndowmentMortgageID = -1;
	/**
	 * ���强 ���� ���� ��¥
	 */
	private Calendar mStartDate = Calendar.getInstance();
	
	/**
	 * ���强 ���� ���� ��¥
	 */
	private Calendar mEndDate = Calendar.getInstance();
	
	/**
	 * ���Ա�
	 */
	private long mPayment = 0;
	
	private FinancialCompany mCompany;
	

	/**
	 * ���强 ���� ���̵� ����
	 * @param mEndowmentMortgageID ���强 ���� ���̵�
	 */
	public void setEndowmentMortgageID(int endowmentMortgageID) {
		this.mEndowmentMortgageID = endowmentMortgageID;
	}

	/**
	 * ���强 ���� ���̵� ��´�.
	 * @return ���强 ���� ���̵�
	 */
	public int getEndowmentMortgageID() {
		return mEndowmentMortgageID;
	}

	
	/**
	 * ���强 ���� ���۳�¥�� ����
	 * @param createDate ���强 ���� ���۵Ǵ� ��¥
	 */
	public void setStartDate(Calendar startDate) {
		this.mStartDate = startDate;
	}
	
	/**
	 * ���强 ���� ���۳�¥�� ����
	 * @param date ���强 ���� ���۵Ǵ� ��¥
	 */
	public void setStartDate(Date date) {
		this.mStartDate.setTime(date);
	}

	/**
	 * ���强 ���� ���۳�¥�� ��´�.
	 * @return ���强 ���� ���۵Ǵ� ��¥
	 */
	public Calendar getStartDate() {
		return mStartDate;
	}
	
	/**
	 * ���强 ���� ���⳯¥�� ����
	 * @param createDate ���强 ���� ���۵Ǵ� ��¥
	 */
	public void setEndDate(Calendar endDate) {
		this.mEndDate = endDate;
	}
	
	/**
	 * ���强 ���� ���⳯¥�� ����
	 * @param date ���强 ���� ���۵Ǵ� ��¥
	 */
	public void setEndDate(Date date) {
		this.mEndDate.setTime(date);
	}

	/**
	 * ���强 ���� ���⳯¥�� ��´�.
	 * @return ���强 ���� ���۵Ǵ� ��¥
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

	/**
	 * ����ȸ�� ����
	 * @param mCompany
	 */
	public void setCompany(FinancialCompany mCompany) {
		this.mCompany = mCompany;
	}

	
	/**
	 * ����ȸ�縦 ��´�.
	 * @return
	 */
	public FinancialCompany getCompany() {
		return mCompany;
	}
	
}
