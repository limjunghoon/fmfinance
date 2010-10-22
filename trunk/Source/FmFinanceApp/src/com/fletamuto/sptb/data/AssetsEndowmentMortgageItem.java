package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

/**
 * 자산 중 보장성 보험
 * @author yongbban
 *
 */
public class AssetsEndowmentMortgageItem extends AssetsExtendItem {
	
	/**
	 * 보장성 보험 DB테이블 아이디
	 */
	private int mEndowmentMortgageID = -1;
	/**
	 * 보장성 보험 시작 날짜
	 */
	private Calendar mStartDate = Calendar.getInstance();
	
	/**
	 * 보장성 보험 만기 날짜
	 */
	private Calendar mEndDate = Calendar.getInstance();
	
	/**
	 * 납입금
	 */
	private long mPayment = 0;
	
	private FinancialCompany mCompany;
	

	/**
	 * 보장성 보험 아이디 설정
	 * @param mEndowmentMortgageID 보장성 보험 아이디
	 */
	public void setEndowmentMortgageID(int endowmentMortgageID) {
		this.mEndowmentMortgageID = endowmentMortgageID;
	}

	/**
	 * 보장성 보험 아이디를 얻는다.
	 * @return 보장성 보험 아이디
	 */
	public int getEndowmentMortgageID() {
		return mEndowmentMortgageID;
	}

	
	/**
	 * 보장성 보험 시작날짜를 설정
	 * @param createDate 보장성 보험 시작되는 날짜
	 */
	public void setStartDate(Calendar startDate) {
		this.mStartDate = startDate;
	}
	
	/**
	 * 보장성 보험 시작날짜를 설정
	 * @param date 보장성 보험 시작되는 날짜
	 */
	public void setStartDate(Date date) {
		this.mStartDate.setTime(date);
	}

	/**
	 * 보장성 보험 시작날짜를 얻는다.
	 * @return 보장성 보험 시작되는 날짜
	 */
	public Calendar getStartDate() {
		return mStartDate;
	}
	
	/**
	 * 보장성 보험 만기날짜를 설정
	 * @param createDate 보장성 보험 시작되는 날짜
	 */
	public void setEndDate(Calendar endDate) {
		this.mEndDate = endDate;
	}
	
	/**
	 * 보장성 보험 만기날짜를 설정
	 * @param date 보장성 보험 시작되는 날짜
	 */
	public void setEndDate(Date date) {
		this.mEndDate.setTime(date);
	}

	/**
	 * 보장성 보험 만기날짜를 얻는다.
	 * @return 보장성 보험 시작되는 날짜
	 */
	public Calendar getEndDate() {
		return mEndDate;
	}

	/**
	 * 납입금 설정
	 * @param payment 납입금
	 */
	public void setPayment(long payment) {
		this.mPayment = payment;
	}

	/**
	 * 납입금을 얻는다.
	 * @return 납입금
	 */
	public long getPayment() {
		return mPayment;
	}

	/**
	 * 보험회사 설정
	 * @param mCompany
	 */
	public void setCompany(FinancialCompany mCompany) {
		this.mCompany = mCompany;
	}

	
	/**
	 * 보험회사를 얻는다.
	 * @return
	 */
	public FinancialCompany getCompany() {
		return mCompany;
	}
	
}
