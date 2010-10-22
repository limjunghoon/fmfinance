package com.fletamuto.sptb.data;

/**
 * �׸��峪 ����Ʈó�� UI���� �̹����� �켱������ �ʿ��� Ŭ����
 * @author yongbban
 *
 */
public abstract class UISelectItem {
	private int mPrioritize = -1;
	private int mImageIndex = -1;
	
	public void setPrioritize(int prioritize) {
		this.mPrioritize = prioritize;
	}
	public int getPrioritize() {
		return mPrioritize;
	}
	
	public void setImageIndex(int imageIndex) {
		this.mImageIndex = imageIndex;
	}
	
	public int getImageIndex() {
		return mImageIndex;
	}
	
}
