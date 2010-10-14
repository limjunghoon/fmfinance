package com.fletamuto.sptb.data;

public class AssetsItem extends FinanceItem {
	public final static int TYPE = ItemDef.FinanceDef.ASSETS;

	private final Category mSubCategory = new Category(-1, "");
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return AssetsItem.TYPE;
	}
	public void setSubCategory(int id, String name) {
		this.mSubCategory.set(id, name);
	}
	public Category getSubCategory() {
		return mSubCategory;
	}
	
	public boolean isVaildCatetory() {
		if (super.isVaildCatetory() == false) {
			return false;
		}
		
		if (mSubCategory.getId() == -1 || mSubCategory.getName() == "") {
			return false;
		}
		return true;
	}
}
