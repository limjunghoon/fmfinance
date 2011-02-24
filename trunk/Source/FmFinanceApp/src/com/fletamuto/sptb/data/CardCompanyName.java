package com.fletamuto.sptb.data;

public class CardCompanyName extends UISelectItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6961199997270233118L;
	private String mName;
	private int mCompanyID = -1;
	private int mImageIndex = -1;
	
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
	
	public void setImageIndex(int imageIndex) {
		this.mImageIndex = imageIndex;
	}
	
	public int getImageIndex() {
		return mImageIndex;
	}
}
