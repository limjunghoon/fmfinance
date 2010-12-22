package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * 자산 중 보장성 보험
 * @author yongbban
 *
 */
public class AssetsInsuranceItem extends AssetsExtendItem {
	public final static int EXEND_TYPE = ItemDef.ExtendAssets.ENDOWMENT_MORTGAGE;
 
	private static final long serialVersionUID = -2224085382368286553L;

	/**
	 * 보장성 보험 DB테이블 아이디
	 */
	private int mInsuranceID = -1;
	
	/**
	 * 예금 만기 날짜
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * 납입금
	 */
	private long mPayment = 0;
	
	private String mCompany;
	
	public AssetsInsuranceItem() {
		mExpiryDate.add(Calendar.YEAR, 1);
	}
	

	/**
	 * 보장성 보험 아이디 설정
	 * @param mEndowmentMortgageID 보장성 보험 아이디
	 */
	public void setInsuranceID(int InsuranceID) {
		this.mInsuranceID = InsuranceID;
	}

	/**
	 * 보장성 보험 아이디를 얻는다.
	 * @return 보장성 보험 아이디
	 */
	public int getInsuranceID() {
		return mInsuranceID;
	}

	
	/**
	 * 예금 만기날짜를 설정
	 * @param createDate 예금 시작되는 날짜
	 */
	public void setExpiryDate(Calendar endDate) {
		this.mExpiryDate = endDate;
	}
	
	/**
	 * 예금 만기날짜를 설정
	 * @param date 예금 시작되는 날짜
	 */
	public void setExpiryDate(Date date) {
		this.mExpiryDate.setTime(date);
	}

	/**
	 * 예금 만기날짜를 얻는다.
	 * @return 예금 시작되는 날짜
	 */
	public Calendar getExpiryDate() {
		return mExpiryDate;
	}
	
	public String getExpriyDateString() {
		return FinanceDataFormat.getDateFormat(mExpiryDate.getTime());
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
	public void setCompany(String company) {
		this.mCompany = company;
	}

	
	/**
	 * 보험회사를 얻는다.
	 * @return
	 */
	public String getCompany() {
		return mCompany;
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
	
}
