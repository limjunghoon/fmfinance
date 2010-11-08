package com.fletamuto.sptb.data;

/**
 * 상위분류와 하위분류
 * @author yongbban
 *
 */
public class Category extends UISelectItem {
	private String mName;
	
	public Category(int id, String name) {
		setID(id);
		this.mName = name;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public String getName() {
		return mName;
	}

	public void set(int id, String name) {
		setID(id);
		this.mName = name;
	}
	
}
