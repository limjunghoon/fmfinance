package com.fletamuto.sptb.data;

public class OpenUsedItem extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4868250190900800789L;
	private FinanceItem mItem = null;
	private int mPriority = -1;
	private int mImageIndex = -1;
	
	public OpenUsedItem(FinanceItem item) {
		mItem = item;
	}

	public void setPriority(int mPriority) {
		this.mPriority = mPriority;
	}

	public int getPriority() {
		return mPriority;
	}

	public void setImageIndex(int mImageIndex) {
		this.mImageIndex = mImageIndex;
	}

	public int getImageIndex() {
		return mImageIndex;
	}
	
	public FinanceItem getItem() {
		return mItem;
	}
	
	public void setItem(FinanceItem item) {
		
	}
	
	public int getType () {
		return mItem.getType();
	}
	
	
}
