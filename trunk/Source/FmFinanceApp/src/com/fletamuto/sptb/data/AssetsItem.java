package com.fletamuto.sptb.data;

public class AssetsItem extends FinanceItem {
	public final static int TYPE = 2;

	private Category subCategory = null;
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return AssetsItem.TYPE;
	}
	public void setSubCategory(Category subCategory) {
		this.subCategory = subCategory;
	}
	public Category getSubCategory() {
		return subCategory;
	}

}
