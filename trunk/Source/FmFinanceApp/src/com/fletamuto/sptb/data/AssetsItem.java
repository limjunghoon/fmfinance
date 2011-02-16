package com.fletamuto.sptb.data;

public class AssetsItem extends FinanceItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9187871581593336442L;
	public final static int TYPE = ItemDef.FinanceDef.ASSETS;
	
	
	/**
	 * 구입처
	 */
	private String mStore;
	
	/**
	 * 지출타입
	 */
	private int mPriceType = BUY;
	
	
	public static final int BUY = 0;
	public static final int SELL = 1;

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return AssetsItem.TYPE;
	}
	

	public void setStore(String store) {
		this.mStore = store;
	}

	public String getStore() {
		return mStore;
	}



	public void setPriceType(int priceType) {
		this.mPriceType = priceType;
	}

	public int getPriceType() {
		return mPriceType;
	}

}
