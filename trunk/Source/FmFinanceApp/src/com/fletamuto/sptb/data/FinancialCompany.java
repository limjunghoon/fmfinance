package com.fletamuto.sptb.data;

public class FinancialCompany {
	public static final int GROUP_NONE = 0;
	public static final int BANKING = 1;
	public static final int SECURITIES_COMPANY = 2;
	public static final int INSURANCE_COMPANY = 3;
	
	private int mID = -1;
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

	public void setID(int id) {
		this.mID = id;
	}

	public int getID() {
		return mID;
	}
}
