package com.fletamuto.sptb.data;

public class LiabilityItem extends FinanceItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 504474056076380394L;
	public final static int TYPE = ItemDef.FinanceDef.LIABILITY;
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return LiabilityItem.TYPE;
	}

}
