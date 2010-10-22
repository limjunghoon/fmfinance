package com.fletamuto.sptb.data;

/**
 * 그리드나 리스트처럼 UI에서 이미지와 우선순위가 필요한 클래스
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
