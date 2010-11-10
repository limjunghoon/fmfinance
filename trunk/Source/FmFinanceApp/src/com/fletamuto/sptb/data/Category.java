package com.fletamuto.sptb.data;

/**
 * 상위분류와 하위분류
 * @author yongbban
 *
 */
public class Category extends UISelectItem {
	public static final int EXTEND_NONE = 0;
	
	private String mName;
	private int	mExtndType = EXTEND_NONE;
	
	public Category(String name) {
		this.mName = name;
	}
	
	public Category(int id, String name) {
		setID(id);
		this.mName = name;
	}
	
	public Category(int id, String name, int prioritize, int imageIndex, int extendType, int UIType) {
		setID(id);
		setUI(prioritize, imageIndex, UIType);
		mExtndType = extendType; 
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
	
	public void set(int id, String name, int prioritize, int imageIndex, int extendType, int UIType) {
		setID(id);
		setUI(prioritize, imageIndex, UIType);
		mExtndType = extendType; 
		this.mName = name;
	}
	
	public void setExtndType(int extndType) {
		this.mExtndType = extndType;
	}
	
	public int getExtndType() {
		return mExtndType;
	}
	
}
