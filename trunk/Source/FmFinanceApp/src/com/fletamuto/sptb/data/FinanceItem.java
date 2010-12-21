package com.fletamuto.sptb.data;

import java.util.Calendar;
import java.util.Date;

import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * 수입, 지출, 자산, 부채의 기본 클래스
 * @author yongbban
 * @version 1.0.0.1
 */
public abstract class FinanceItem extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -829539455398652012L;

	/** 제목 */
	private String mTitle;
	
	/** 메모 */
	private String mMemo;
	
	/** 만든날짜 */
	private Calendar mCreateDate = Calendar.getInstance();
	
	/** 만든시간 */
	private Calendar mCreateTime = Calendar.getInstance();
	
	/** 금액 */
	private long mAmount = 0L;
	
	/** 상위 분류 */
	private final Category mCategory = new Category(-1, "");
	
	/** 하위분류 */
	private final Category mSubCategory = new Category(-1, "");
	
	/** 반복 */
	private Repeat mRepeat = new Repeat();
	
	/** 확장아이디 */
	private int mExtendID = -1;
	
	/**
	 * 수입, 지출, 자산, 부채 타입을 얻는다.
	 * @return int 수입, 지출, 자산, 부채
	 */
	public abstract int getType();
	

	/**
	 * 제목을 설정
	 * @param title 설정할 제목
	 */
	public void setTitle(String title) {
		this.mTitle = title;
	}
	
	/**
	 * 제목을 얻는다.
	 * @return 제목
	 */
	public String getTitle() {
		return mTitle;
	}
	
	/**
	 * 메모를 설정
	 * @param memo 설정할 메모
	 */
	public void setMemo(String memo) {
		this.mMemo = memo;
	}
	
	/**
	 * 메모를 얻는다.
	 * @return 메모
	 */
	public String getMemo() {
		return mMemo;
	}
	
	/**
	 * 상위 분류를 설정한다.
	 * @param id 설정할 상위 분류 아이디
	 * @param name 분류 이름
	 */
	public void setCategory(int id, String name) {
		mCategory.set(id, name);
	}
	
	public void setCategory(int id, String name, int prioritize, int imageIndex, int extendType, int UIType) {
		mCategory.set(id, name, prioritize, imageIndex, extendType, UIType);
	}
	
	/**
	 * 상위 분류객체를 얻는다.
	 * @return 상위 분류
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
	 * 분류가 유효한지 확인
	 * @return 유효할 경우 true 
	 */
	public boolean isVaildCatetory() {
		if (mCategory.getID() == -1 || mCategory.getName() == "") {
			return false;
		}
		return true;
	}

	/**
	 * 만든 날짜를 설정
	 * @param createDate 만든날짜
	 */
	public void setCreateDate(Calendar createDate) {
		this.mCreateDate = createDate;
	}
	
	/**
	 * 만든 날자를 설정
	 * @param date 만든날짜
	 */
	public void setCreateDate(Date date) {
		this.mCreateDate.setTime(date);
	}

	/**
	 * 만든 날짜를 얻는다.
	 * @return 만든 날짜
	 */
	public Calendar getCreateDate() {
		return mCreateDate;
	}

	/**
	 * 금액을 설정
	 * @param amount 설정한 금액
	 */
	public void setAmount(long amount) {
		this.mAmount = amount;
	}

	/**
	 * 금액을 얻는다.
	 * @return 금액
	 */
	public long getAmount() {
		return mAmount;
	}
	
	/**
	 * 날짜를 문자열 포멧으로 얻는다.
	 * @return 날짜
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
	 * 만든 시간를 설정
	 * @param createDate 만든시간
	 */
	public void setCreateTime(Calendar createTime) {
		this.mCreateTime = createTime;
	}
	
	/**
	 * 만든시간를 설정
	 * @param date 만든시간
	 */
	public void setCreateTime(Date date) {
		this.mCreateTime.setTime(date);
	}

	/**
	 * 만든 시간를 얻는다.
	 * @return 만든 시간
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
