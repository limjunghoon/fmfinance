package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class AccountItem extends BaseItem {
	private static final long serialVersionUID = 5366848481035789230L;

	/** 내 주머니 */
	public final static int MY_POCKET = 0;
	
	/** 보통 예금 */
	public final static int ORDINARY_DEPOSIT = 1;
	
	/** 정기 예금 */
	public final static int TIME_DEPOSIT = 2;
	
	/** 적금 */
	public final static int SAVINGS = 3;
	
	/** 계좌번호*/
	private String mNumber;
	
	/** 잔액*/
	private long mBalance = 0L;
	
	/** 금융기관*/
	private FinancialCompany mCompany = new FinancialCompany();
	
	/** 생성날짜*/
	private Calendar mCreateDate = Calendar.getInstance();
	
	/** 만료날짜*/
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/** 마지막 수정날짜*/
	private Calendar mLastModifyDate = Calendar.getInstance();
	
	/** 메모*/
	private String mMemo;
	
	/** 상품명*/
	private String mName;
	
	/** 종류*/
	private int mType = ORDINARY_DEPOSIT;
	
	private static String mTypeNames[] = {"내 주머니", "보통예금", "정기예금", "적금"};

	
	public void setNumber(String number) {
		this.mNumber = number;
	}
	
	public String getNumber() {
		return mNumber;
	}
	
	public void setCompany(FinancialCompany Company) {
		this.mCompany = Company;
	}
	
	public FinancialCompany getCompany() {
		return mCompany;
	}
	
	public void setBalance(long balance) {
		this.mBalance = balance;
	}
	
	public long getBalance() {
		return mBalance;
	}
	
	public void setCreateDate(Calendar CreateDate) {
		this.mCreateDate = CreateDate;
	}
	
	/**
	 * 만든시간를 설정
	 * @param date 만든시간
	 */
	public void setCreateDate(Date date) {
		this.mCreateDate.setTime(date);
	}
	
	public Calendar getCreateDate() {
		return mCreateDate;
	}
	
	public void setExpiryDate(Calendar expiryDate) {
		this.mExpiryDate = expiryDate;
	}
	
	
	public void setExpiryDate(Date date) {
		this.mExpiryDate.setTime(date);
	}
	
	public Calendar getExpiryDate() {
		return mExpiryDate;
	}
	
	public void setLastModifyDate(Calendar lastModifyDate) {
		this.mLastModifyDate = lastModifyDate;
	}
	
	
	public void setLastModifyDate(Date date) {
		this.mLastModifyDate.setTime(date);
	}
	
	public Calendar getLastModifyDate() {
		return mLastModifyDate;
	}
	
	public void setMemo(String memo) {
		this.mMemo = memo;
	}
	
	public String getMemo() {
		return mMemo;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
	
	public String getName() {
		return mName;
	}

	public void setType(int type) {
		this.mType = type;
	}

	public int getType() {
		return mType;
	}
	
	/**
	 * 날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
	 */
	public String getCreateDateString() {
		return FinanceDataFormat.getDateFormat(mCreateDate.getTime());
	}
	
	/**
	 * 만료날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
	 */
	public String getExpiryDateString() {
		return FinanceDataFormat.getDateFormat(mExpiryDate.getTime());
	}
	
	/**
	 * 마지막 수정날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
	 */
	public String getLastModifyDateString() {
		return FinanceDataFormat.getDateFormat(mLastModifyDate.getTime());
	}
	
	public static String getTypeName(int type) {
		if (type >= mTypeNames.length) return "";  
		return mTypeNames[type];
	}
	
	public String getTypeName() {
		return mTypeNames[mType];
	}
}
