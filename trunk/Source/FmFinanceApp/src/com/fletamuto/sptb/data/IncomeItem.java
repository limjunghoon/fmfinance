package com.fletamuto.sptb.data;

public class IncomeItem extends FinanceItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 172725728587158778L;
	public final static int TYPE = ItemDef.FinanceDef.INCOME;

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return IncomeItem.TYPE;
	}
}
