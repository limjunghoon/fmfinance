package com.fletamuto.sptb.data;

import java.io.Serializable;

public class BaseItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5745513561951474600L;
	private int mID = -1;

	/**
	 * ���̵� ����
	 * @param id ���̵�
	 */
	public void setID(int mID) {
		this.mID = mID;
	}

	/**
	 * ���̵� ��´�.
	 * @return ����Ƽ
	 */
	public int getID() {
		return mID;
	}
	
	
}
