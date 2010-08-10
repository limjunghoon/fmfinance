package com.fletamuto.sptb.data;

public class ExpenseItem extends FinanceItem {
	public final static int TYPE = 1;
	private Category mSubCategory = null;
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return ExpenseItem.TYPE;
	}
	public void setSubCategory(Category subCategory) {
		this.mSubCategory = subCategory;
	}
	public Category getSubCategory() {
		return mSubCategory;
	}

}
