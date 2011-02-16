package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * 자산중 적금
 * @author yongbban
 *
 */
public class AssetsSavingsItem extends AssetsExtendItem {
	public final static int EXEND_TYPE = ItemDef.ExtendAssets.SAVINGS;

	private static final long serialVersionUID = -411684904811090956L;

	/**
	 * 적금 DB테이블 아이디
	 */
	private int mSavingsID = -1;
	
	/**
	 * 예금 만기 날짜
	 */
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/**
	 * 계좌
	 */
	private AccountItem mAccount  = new AccountItem();
	
	/**
	 * 납입금
	 */
	private long mPayment = 0;
	
	private int mRate;
	
	public AssetsSavingsItem() {
		mExpiryDate.add(Calendar.YEAR, 1);
	}
	
	/**
	 * 적금 아이디 설정
	 * @param mDepositID 적금 아이디
	 */
	public void setSavingsID(int savingsID) {
		this.mSavingsID = savingsID;
	}

	/**
	 * 적금 아이디를 얻는다.
	 * @return 적금 아이디
	 */
	public int getSavingsID() {
		return mSavingsID;
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

	public void setAccount(AccountItem account) {
		this.mAccount = account;
	}

	public AccountItem getAccount() {
		return mAccount;
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
