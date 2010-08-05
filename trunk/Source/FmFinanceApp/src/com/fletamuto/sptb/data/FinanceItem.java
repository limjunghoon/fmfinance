package com.fletamuto.sptb.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class FinanceItem {
	private String title;
	private String memo;
	private Calendar createDate = Calendar.getInstance();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private long amount = 0L;
	private int	id = -1;
	
	private Category category = null;
	
	public abstract int getType();
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getMemo() {
		return memo;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	public Calendar getCreateDate() {
		return createDate;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}
	
	public String getDateString() {
		return dateFormat.format(createDate.getTime());
	}
	
	public void setCreateDate(int year, int month, int day) {
		createDate.set(year, month, day);
	}
	
	public int getCreateYear() {
		return createDate.get(Calendar.YEAR);
	}
	
	public int getCreateMonth() {
		return createDate.get(Calendar.MONTH);
	}
	
	public int getCreateDay() {
		return createDate.get(Calendar.DAY_OF_MONTH);
	}
}
