package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * �ڻ� �� ���强 ����
 * @author yongbban
 *
 */
public class AssetsInsuranceItem extends AssetsExtendItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2224085382368286553L;

	/**
	 * ���强 ���� DB���̺� ���̵�
	 */
	private int mInsuranceID = -1;
	
	/**
	 * ���� ���� ��¥
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * ���Ա�
	 */
	private long mPayment = 0;
	
	private String mCompany;
	

	/**
	 * ���强 ���� ���̵� ����
	 * @param mEndowmentMortgageID ���强 ���� ���̵�
	 */
	public void setInsuranceID(int InsuranceID) {
		this.mInsuranceID = InsuranceID;
	}

	/**
	 * ���强 ���� ���̵� ��´�.
	 * @return ���强 ���� ���̵�
	 */
	public int getInsuranceID() {
		return mInsuranceID;
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
	public void setCompany(String company) {
		this.mCompany = company;
	}

	
	/**
	 * ����ȸ�縦 ��´�.
	 * @return
	 */
	public String getCompany() {
		return mCompany;
	}
	
}
