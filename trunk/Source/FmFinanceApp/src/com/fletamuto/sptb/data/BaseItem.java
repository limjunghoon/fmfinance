package com.fletamuto.sptb.data;

import java.io.Serializable;

public class BaseItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5745513561951474600L;
	private int mID = -1;
	private String mSeparatorTitle;
	
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
//
//	public void setSeparator(boolean mSeparator) {
//		this.mSeparator = mSeparator;
//	}

	public boolean isSeparator() {
		return (mID == -1);
	}

	public void setSeparatorTitle(String separatorTitle) {
		this.mSeparatorTitle = separatorTitle;
	}

	public String getSeparatorTitle() {
		return mSeparatorTitle;
	}
	
	
}
