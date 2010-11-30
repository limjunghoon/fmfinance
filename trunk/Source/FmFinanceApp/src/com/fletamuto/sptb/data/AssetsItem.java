package com.fletamuto.sptb.data;

public class AssetsItem extends FinanceItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9187871581593336442L;
	public final static int TYPE = ItemDef.FinanceDef.ASSETS;

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return AssetsItem.TYPE;
	}

}
