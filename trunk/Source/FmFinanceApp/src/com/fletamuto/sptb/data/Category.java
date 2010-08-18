package com.fletamuto.sptb.data;

public class Category {
	private String mName;
	private long mID;
	
	public Category(long id, String name) {
		this.mID = id;
		this.mName = name;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public String getName() {
		return mName;
	}
	public void setId(long id) {
		this.mID = id;
	}
	public long getId() {
		return mID;
	}
	
	public void set(long id, String name) {
		this.mID = id;
		this.mName = name;
	}
	
}
