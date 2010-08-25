package com.fletamuto.sptb.data;

public class FinancialInstitution {
	public static final int BANKING = 1;
	public static final int SECURITIES_COMPANY = 2;
	public static final int INSURANCE_COMPANY = 3;
	
	private String name;
	private int	group;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getGroup() {
		return group;
	}
}
