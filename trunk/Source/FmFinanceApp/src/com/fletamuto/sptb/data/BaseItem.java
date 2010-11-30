package com.fletamuto.sptb.data;

import java.io.Serializable;

public class BaseItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5745513561951474600L;
	private int mID = -1;

	/**
	 * 아이디를 설정
	 * @param id 아이디
	 */
	public void setID(int mID) {
		this.mID = mID;
	}

	/**
	 * 아이디를 얻는다.
	 * @return 아이티
	 */
	public int getID() {
		return mID;
	}
	
	
}
