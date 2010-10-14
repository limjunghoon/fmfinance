package com.fletamuto.sptb.data;

public class ExpenseItem extends FinanceItem {
	public final static int TYPE = ItemDef.FinanceDef.EXPENSE;
	private final Category mSubCategory = new Category(-1, "");
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return ExpenseItem.TYPE;
	}
	public void setSubCategory(int id, String name) {
		this.mSubCategory.set(id, name);
	}
	public Category getSubCategory() {
		return mSubCategory;
	}

}
