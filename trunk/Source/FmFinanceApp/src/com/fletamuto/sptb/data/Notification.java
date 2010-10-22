package com.fletamuto.sptb.data;

public class Notification {
	private int mID = -1;
	private int mType = -1;
	private int mItemID = -1;
	private int mMessageInfo = -1;
	public void setID(int mID) {
		this.mID = mID;
	}
	public int getID() {
		return mID;
	}
	public void setType(int type) {
		this.mType = type;
	}
	public int getType() {
		return mType;
	}
	public void setItemID(int itemID) {
		this.mItemID = itemID;
	}
	public int getItemID() {
		return mItemID;
	}
	public void setMessageInfo(int messageInfo) {
		this.mMessageInfo = messageInfo;
	}
	public int getMessageInfo() {
		return mMessageInfo;
	}
	
}
