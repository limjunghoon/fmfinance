package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class LiabilityItem extends FinanceItem {

	private static final long serialVersionUID = 504474056076380394L;
	public final static int TYPE = ItemDef.FinanceDef.LIABILITY;
	
	/**
	 * �ܱ�
	 */
	private long mOrignAmount = 0L;
	
	/**
	 * ���� ���� ��¥
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * ���� ���� ������
	 */
	private Calendar mPaymentDate = null;
	
	public LiabilityItem() {
		mExpiryDate.add(Calendar.YEAR, 1);
	}
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return LiabilityItem.TYPE;
	}
	
	/**
	 *  ���⳯¥�� ����
	 * @param createDate ���� ���۵Ǵ� ��¥
	 */
	public void setExpiryDate(Calendar endDate) {
		this.mExpiryDate = endDate;
	}
	
	/**
	 *  ���⳯¥�� ����
	 * @param date ���� ���۵Ǵ� ��¥
	 */
	public void setExpiryDate(Date date) {
		this.mExpiryDate.setTime(date);
	}

	/**
	 *  ���⳯¥�� ��´�.
	 * @return ���� ���۵Ǵ� ��¥
	 */
	public Calendar getExpiryDate() {
		return mExpiryDate;
	}
	
	public String getExpiryDateString() {
		return FinanceDataFormat.getDateFormat(mExpiryDate.getTime());
	}
	
	/**
	 *  ���Գ�¥�� ����
	 * @param createDate ���� ���۵Ǵ� ��¥
	 */
	public void setPaymentDate(Calendar paymentDate) {
		this.mPaymentDate = paymentDate;
	}
	
	/**
	 *  ���Գ�¥�� ����
	 * @param date ���� ���۵Ǵ� ��¥
	 */
	public void setPaymentDate(Date date) {
		if (mPaymentDate == null) {
			mPaymentDate = Calendar.getInstance();
		}
		this.mPaymentDate.setTime(date);
	}

	/**
	 *  ���Գ�¥�� ��´�.
	 * @return ���� ���۵Ǵ� ��¥
	 */
	public Calendar getPaymentDate() {
		return mPaymentDate;
	}
	
	public String getPaymentDateString() {
		if (mPaymentDate == null) return null;
		return FinanceDataFormat.getDateFormat(mPaymentDate.getTime());
	}

	public void setOrignAmount(long OrignAmount) {
		this.mOrignAmount = OrignAmount;
	}

	public long getOrignAmount() {
		return mOrignAmount;
	}

}
