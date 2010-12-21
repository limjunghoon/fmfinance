package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * ����, ����, �ڻ�, ��ä�� �⺻ Ŭ����
 * @author yongbban
 * @version 1.0.0.1
 */
public abstract class FinanceItem extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -829539455398652012L;

	/** ���� */
	private String mTitle;
	
	/** �޸� */
	private String mMemo;
	
	/** ���糯¥ */
	private Calendar mCreateDate = Calendar.getInstance();
	
	/** ����ð� */
	private Calendar mCreateTime = Calendar.getInstance();
	
	/** �ݾ� */
	private long mAmount = 0L;
	
	/** ���� �з� */
	private final Category mCategory = new Category(-1, "");
	
	/** �����з� */
	private final Category mSubCategory = new Category(-1, "");
	
	/** �ݺ� */
	private Repeat mRepeat = new Repeat();
	
	/** Ȯ����̵� */
	private int mExtendID = -1;
	
	/**
	 * ����, ����, �ڻ�, ��ä Ÿ���� ��´�.
	 * @return int ����, ����, �ڻ�, ��ä
	 */
	public abstract int getType();
	

	/**
	 * ������ ����
	 * @param title ������ ����
	 */
	public void setTitle(String title) {
		this.mTitle = title;
	}
	
	/**
	 * ������ ��´�.
	 * @return ����
	 */
	public String getTitle() {
		return mTitle;
	}
	
	/**
	 * �޸� ����
	 * @param memo ������ �޸�
	 */
	public void setMemo(String memo) {
		this.mMemo = memo;
	}
	
	/**
	 * �޸� ��´�.
	 * @return �޸�
	 */
	public String getMemo() {
		return mMemo;
	}
	
	/**
	 * ���� �з��� �����Ѵ�.
	 * @param id ������ ���� �з� ���̵�
	 * @param name �з� �̸�
	 */
	public void setCategory(int id, String name) {
		mCategory.set(id, name);
	}
	
	public void setCategory(int id, String name, int prioritize, int imageIndex, int extendType, int UIType) {
		mCategory.set(id, name, prioritize, imageIndex, extendType, UIType);
	}
	
	/**
	 * ���� �з���ü�� ��´�.
	 * @return ���� �з�
	 */
	public Category getCategory() {
		return mCategory;
	}
	
	public void setSubCategory(int id, String name) {
		this.mSubCategory.set(id, name);
	}
	
	public void setSubCategory(int id, String name, int prioritize, int imageIndex, int extendType, int UIType) {
		mSubCategory.set(id, name, prioritize, imageIndex, extendType, UIType);
	}
	public Category getSubCategory() {
		return mSubCategory;
	}
	
	/**
	 * �з��� ��ȿ���� Ȯ��
	 * @return ��ȿ�� ��� true 
	 */
	public boolean isVaildCatetory() {
		if (mCategory.getID() == -1 || mCategory.getName() == "") {
			return false;
		}
		return true;
	}

	/**
	 * ���� ��¥�� ����
	 * @param createDate ���糯¥
	 */
	public void setCreateDate(Calendar createDate) {
		this.mCreateDate = createDate;
	}
	
	/**
	 * ���� ���ڸ� ����
	 * @param date ���糯¥
	 */
	public void setCreateDate(Date date) {
		this.mCreateDate.setTime(date);
	}

	/**
	 * ���� ��¥�� ��´�.
	 * @return ���� ��¥
	 */
	public Calendar getCreateDate() {
		return mCreateDate;
	}

	/**
	 * �ݾ��� ����
	 * @param amount ������ �ݾ�
	 */
	public void setAmount(long amount) {
		this.mAmount = amount;
	}

	/**
	 * �ݾ��� ��´�.
	 * @return �ݾ�
	 */
	public long getAmount() {
		return mAmount;
	}
	
	/**
	 * ��¥�� ���ڿ� �������� ��´�.
	 * @return ��¥
	 */
	public String getCreateDateString() {
		return FinanceDataFormat.getDateFormat(mCreateDate.getTime());
	}
	
	public String getCreateDateTimeString() {
		return FinanceDataFormat.getDateTimeFormat(mCreateDate.getTime());
	}

	public Repeat getRepeat() {
		return mRepeat;
	}
	
	public void setRepeat(Repeat repeat) {
		mRepeat = repeat;
	}
	
	public void setRepeatWeekly(int weekly) {
		mRepeat.setWeeklyRepeat(weekly);
	}


	/**
	 * ���� �ð��� ����
	 * @param createDate ����ð�
	 */
	public void setCreateTime(Calendar createTime) {
		this.mCreateTime = createTime;
	}
	
	/**
	 * ����ð��� ����
	 * @param date ����ð�
	 */
	public void setCreateTime(Date date) {
		this.mCreateTime.setTime(date);
	}

	/**
	 * ���� �ð��� ��´�.
	 * @return ���� �ð�
	 */
	public Calendar getCreateTime() {
		return mCreateDate;
	}
	
	public void setRepeatMonthly(int day) {
		mRepeat.setMonthlyRepeat(day);
	}


	public void setExtendID(int extendID) {
		this.mExtendID = extendID;
	}


	public int getExtendID() {
		return mExtendID;
	}
	
	public int getExtendType() {
		return ItemDef.EXTEND_NONE;
	}
}
