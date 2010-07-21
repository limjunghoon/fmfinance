package com.fletamuto.sptb.data;

import java.util.Calendar;

public abstract class InfoFinance {
	private String title;
	private String memo;
	private Calendar createDate = Calendar.getInstance();
	private long amount;
	
	private FinanceCategory category;
	
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
	
	public void setCategory(FinanceCategory category) {
		this.category = category;
	}
	
	public FinanceCategory getCategory() {
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
	
	
}
