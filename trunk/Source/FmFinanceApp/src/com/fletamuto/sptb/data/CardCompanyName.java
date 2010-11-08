package com.fletamuto.sptb.data;

public class CardCompanyName extends UISelectItem{
	private String mName;
	private int mCompanyID = -1;
	
	public void setName(String name) {
		this.mName = name;
	}
	public String getName() {
		return mName;
	}
	public void setCompanyID(int companyID) {
		this.mCompanyID = companyID;
	}
	public int getCompanyID() {
		return mCompanyID;
	}
}
