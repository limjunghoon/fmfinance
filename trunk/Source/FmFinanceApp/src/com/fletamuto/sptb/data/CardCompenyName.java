package com.fletamuto.sptb.data;

public class CardCompenyName extends UISelectItem{
	private int mID = -1;
	private String mName;
	private int mInstituionID = -1;
	
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
	public void setInstituionID(int instituionID) {
		this.mInstituionID = instituionID;
	}
	public int getInstituionID() {
		return mInstituionID;
	}
}
