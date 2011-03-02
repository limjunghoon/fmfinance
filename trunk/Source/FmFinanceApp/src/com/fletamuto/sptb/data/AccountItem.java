package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

public class AccountItem extends BaseItem {
	private static final long serialVersionUID = 5366848481035789230L;

	/** �� �ָӴ� */
	public final static int MY_POCKET = 0;
	
	/** ���� ���� */
	public final static int ORDINARY_DEPOSIT = 1;
	
	/** ���� ���� */
	public final static int TIME_DEPOSIT = 2;
	
	/** ���� */
	public final static int SAVINGS = 3;
	
	/** ���¹�ȣ*/
	private String mNumber;
	
	/** �ܾ�*/
	private long mBalance = 0L;
	
	/** �������*/
	private FinancialCompany mCompany = new FinancialCompany();
	
	/** ������¥*/
	private Calendar mCreateDate = Calendar.getInstance();
	
	/** ���ᳯ¥*/
	private Calendar mExpiryDate = Calendar.getInstance();
	
	/** ������ ������¥*/
	private Calendar mLastModifyDate = Calendar.getInstance();
	
	/** �޸�*/
	private String mMemo;
	
	/** ��ǰ��*/
	private String mName;
	
	/** ����*/
	private int mType = ORDINARY_DEPOSIT;
	
	private static String mTypeNames[] = {"�� �ָӴ�", "���뿹��", "���⿹��", "����"};

	
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
	 * ����ð��� ����
	 * @param date ����ð�
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
	 * ��¥�� ���ڿ� �������� ��´�.
	 * @return ��¥
	 */
	public String getCreateDateString() {
		return FinanceDataFormat.getDateFormat(mCreateDate.getTime());
	}
	
	/**
	 * ���ᳯ¥�� ���ڿ� �������� ��´�.
	 * @return ��¥
	 */
	public String getExpiryDateString() {
		return FinanceDataFormat.getDateFormat(mExpiryDate.getTime());
	}
	
	/**
	 * ������ ������¥�� ���ڿ� �������� ��´�.
	 * @return ��¥
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
