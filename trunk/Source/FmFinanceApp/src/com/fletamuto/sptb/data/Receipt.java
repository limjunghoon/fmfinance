package com.fletamuto.sptb.data;

import java.util.Calendar;

/**
 * 영수증을 관리하는 클래스
 * @author yongbban
 * @version 1.0.0.0
 */
public class Receipt {
	/**
	 * 생성날짜
	 */
	private Calendar mCreateDate = Calendar.getInstance();
	/**
	 * 상위 지출 분류
	 */
	private Category mExpenseMainCategory;
	/**
	 * 하위 지출 분류
	 */
	private Category mExpenseSubCategory;
	/**
	 * 처리 여부
	 */
	private boolean mDone = false;
	/**
	 * 메모
	 */
	private String mMemo;
	/**
	 * 이미지 정보
	 */
	private int mPictureInfo = -1;
	
	public void setCreateDate(Calendar createDate) {
		this.mCreateDate = createDate;
	}
	public Calendar getCreateDate() {
		return mCreateDate;
	}
	public void setExpenseMainCategory(Category expenseMainCategory) {
		this.mExpenseMainCategory = expenseMainCategory;
	}
	public Category getExpenseMainCategory() {
		return mExpenseMainCategory;
	}
	public void setExpenseSubCategory(Category expenseSubCategory) {
		this.mExpenseSubCategory = expenseSubCategory;
	}
	public Category getExpenseSubCategory() {
		return mExpenseSubCategory;
	}
	public void setDone(boolean mDone) {
		this.mDone = mDone;
	}
	public boolean isDone() {
		return mDone;
	}
	public void setMemo(String memo) {
		this.mMemo = memo;
	}
	public String getMemo() {
		return mMemo;
	}
	public void setPictureInfo(int pictureInfo) {
		this.mPictureInfo = pictureInfo;
	}
	public int getPictureInfo() {
		return mPictureInfo;
	}
	
	
}
