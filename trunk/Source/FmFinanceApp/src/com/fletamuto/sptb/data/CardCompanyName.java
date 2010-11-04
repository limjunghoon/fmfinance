package com.fletamuto.sptb.data;

public class CardCompanyName extends UISelectItem{
	private int mID = -1;
	private String mName;
	private int mCompanyID = -1;
	
	public void setID(int id) {
		this.mID = id;
	}
	public int getID() {
		return mID;
	}
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
