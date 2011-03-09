package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class ExpenseSMS extends BaseItem{
	private static final long serialVersionUID = -431858797456300474L;
	private Calendar mCreateDate = Calendar.getInstance();
	private String mMessage;
	private boolean mDone = false;
	private String mNmuber;

	public void setCreateDate(Calendar createDate) {
		this.mCreateDate = createDate;
	}
	
	public Calendar getCreateDate() {
		return mCreateDate;
	}
	
	/**
	 * ��¥�� ���ڿ� �������� ��´�.
	 * @return ��¥
	 */
	public String getCreateDateString() {
		return FinanceDataFormat.getDateFormat(mCreateDate.getTime());
	}
	
	/**
	 * ����ð��� ����
	 * @param date ����ð�
	 */
	public void setCreateDate(Date date) {
		this.mCreateDate.setTime(date);
	}
	
	public void setMessage(String message) {
		this.mMessage = message;
	}
	
	public String getMessage() {
		return mMessage;
	}
	
	public void setDone(boolean done) {
		this.mDone = done;
	}
	
	public boolean isDone() {
		return mDone;
	}
	
	public void setNmuber(String mNmuber) {
		this.mNmuber = mNmuber;
	}
	
	public String getNmuber() {
		return mNmuber;
	}
}
