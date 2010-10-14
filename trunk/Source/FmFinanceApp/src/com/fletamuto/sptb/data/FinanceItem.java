package com.fletamuto.sptb.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class FinanceItem {
	private String mTitle;
	private String mMemo;
	private Calendar mCreateDate = Calendar.getInstance();
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private long mAmount = 0L;
	private int	mID = -1;
	private final Category mCategory = new Category(-1, "");
	
	public abstract int getType();
	
	public void setId(int id) {
		this.mID = id;
	}

	public int getId() {
		return mID;
	}
	
	public void setTitle(String title) {
		this.mTitle = title;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setMemo(String memo) {
		this.mMemo = memo;
	}
	
	public String getMemo() {
		return mMemo;
	}
	
	public void setCategory(int id, String name) {
		mCategory.set(id, name);
	}
	
	public Category getCategory() {
		return mCategory;
	}
	
	public boolean isVaildCatetory() {
		if (mCategory.getId() == -1 || mCategory.getName() == "") {
			return false;
		}
		return true;
	}

	public void setCreateDate(Calendar createDate) {
		this.mCreateDate = createDate;
	}

	public Calendar getCreateDate() {
		return mCreateDate;
	}

	public void setAmount(long amount) {
		this.mAmount = amount;
	}

	public long getAmount() {
		return mAmount;
	}
	
	public String getDateString() {
		return mDateFormat.format(mCreateDate.getTime());
	}
	
	public void setCreateDate(int year, int month, int day) {
		mCreateDate.set(year, month, day);
	}
	
	public int getCreateYear() {
		return mCreateDate.get(Calendar.YEAR);
	}
	
	public int getCreateMonth() {
		return mCreateDate.get(Calendar.MONTH);
	}
	
	public int getCreateDay() {
		return mCreateDate.get(Calendar.DAY_OF_MONTH);
	}
}
