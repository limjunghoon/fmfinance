package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class LiabilityItem extends FinanceItem {

	private static final long serialVersionUID = 504474056076380394L;
	public final static int TYPE = ItemDef.FinanceDef.LIABILITY;
	
	/**
	 * 잔금
	 */
	private long mOrignAmount = 0L;
	
	/**
	 * 대출 만기 날짜
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * 대출 납입 시작일
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
	 *  만기날짜를 설정
	 * @param createDate 예금 시작되는 날짜
	 */
	public void setExpiryDate(Calendar endDate) {
		this.mExpiryDate = endDate;
	}
	
	/**
	 *  만기날짜를 설정
	 * @param date 예금 시작되는 날짜
	 */
	public void setExpiryDate(Date date) {
		this.mExpiryDate.setTime(date);
	}

	/**
	 *  만기날짜를 얻는다.
	 * @return 예금 시작되는 날짜
	 */
	public Calendar getExpiryDate() {
		return mExpiryDate;
	}
	
	public String getExpiryDateString() {
		return FinanceDataFormat.getDateFormat(mExpiryDate.getTime());
	}
	
	/**
	 *  납입날짜를 설정
	 * @param createDate 예금 시작되는 날짜
	 */
	public void setPaymentDate(Calendar paymentDate) {
		this.mPaymentDate = paymentDate;
	}
	
	/**
	 *  납입날짜를 설정
	 * @param date 예금 시작되는 날짜
	 */
	public void setPaymentDate(Date date) {
		if (mPaymentDate == null) {
			mPaymentDate = Calendar.getInstance();
		}
		this.mPaymentDate.setTime(date);
	}

	/**
	 *  납입날짜를 얻는다.
	 * @return 예금 시작되는 날짜
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
