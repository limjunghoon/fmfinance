package com.fletamuto.sptb.data;

public class FinancialCompany extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1454745898095007207L;
	public static final int GROUP_NONE = 0;
	public static final int BANKING = 1;
	public static final int SECURITIES_COMPANY = 2;
	public static final int INSURANCE_COMPANY = 3;
	
	private String mName;
	private int	mGroup = GROUP_NONE;
	
	public FinancialCompany() {
	}
	
	public FinancialCompany(int group, String name) {
		this.mName = name;
		this.mGroup = group;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setGroup(int group) {
		this.mGroup = group;
	}

	public int getGroup() {
		return mGroup;
	}

}
