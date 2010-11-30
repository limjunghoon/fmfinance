package com.fletamuto.sptb.data;

/**
 * 그리드나 리스트처럼 UI에서 이미지와 우선순위가 필요한 클래스
 * @author yongbban
 *
 */
public abstract class UISelectItem extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4825272309760290307L;
	static public final int NORMAL = 0;
	static public final int HIDE = 1;
	static public final int NOT_DELETE = 2;
	
	private int mPrioritize = -1;
	private int mImageIndex = -1;
	private int mUIType = NORMAL;
	
	public void setUI(int prioritize, int imageIndex, int UIType) {
		mPrioritize = prioritize;
		mImageIndex = imageIndex;
		mUIType = UIType;
	}
	
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
	
	public void setUIType(int UIType) {
		this.mUIType = UIType;
	}
	
	public int getUIType() {
		return mUIType;
	}
	
}
