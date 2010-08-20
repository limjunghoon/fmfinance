package com.fletamuto.sptb.data;

public class Category {
	private String mName;
	private int mID;
	
	public Category(int id, String name) {
		this.mID = id;
		this.mName = name;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public String getName() {
		return mName;
	}
	public void setId(int id) {
		this.mID = id;
	}
	public int getId() {
		return mID;
	}
	
	public void set(int id, String name) {
		this.mID = id;
		this.mName = name;
	}
	
}
