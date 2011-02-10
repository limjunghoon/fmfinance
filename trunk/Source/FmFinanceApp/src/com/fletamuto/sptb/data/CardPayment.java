package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class CardPayment extends BaseItem {

	private static final long serialVersionUID = 6774647452205496776L;
	
	public static final int DONE = 0;
	public static final int NOT_DONE = 1;
	public static final int REMAIN = 2;
	
	private Calendar mPaymentDate = Calendar.getInstance();
	private int mCardId;
	private long mPaymentAmount;
	private long mRemainAmount;
	private	int mState;
	
	public void setPaymentDate(Calendar PaymentDate) {
		this.mPaymentDate = PaymentDate;
	}
	public Calendar getPaymentDate() {
		return mPaymentDate;
	}
	
	/**
	 * 날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
	 */
	public String getPaymentDateString() {
		return FinanceDataFormat.getDateFormat(mPaymentDate.getTime());
	}
	/**
	 * 만든시간를 설정
	 * @param date 만든시간
	 */
	public void setPaymentDate(Date date) {
		this.mPaymentDate.setTime(date);
	}
	public void setCardId(int cardId) {
		this.mCardId = cardId;
	}
	public int getCardId() {
		return mCardId;
	}
	public void setPaymentAmount(long paymentAmount) {
		this.mPaymentAmount = paymentAmount;
	}
	public long getPaymentAmount() {
		return mPaymentAmount;
	}
	public void setRemainAmount(long remainAmount) {
		this.mRemainAmount = remainAmount;
	}
	public long getRemainAmount() {
		return mRemainAmount;
	}
	public void setState(int state) {
		this.mState = state;
	}
	public int getState() {
		return mState;
	}
	
}
