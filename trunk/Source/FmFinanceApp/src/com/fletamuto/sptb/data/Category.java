package com.fletamuto.sptb.data;

/**
 * �����з��� �����з�
 * @author yongbban
 *
 */
public class Category extends UISelectItem {
	private String mName;
	private int mID = -1; 
	
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
