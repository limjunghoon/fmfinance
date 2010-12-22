package com.fletamuto.sptb.data;


/**
 * 자산 중 주식
 * @author yongbban
 *
 */
public class AssetsStockItem extends AssetsExtendItem {
	private static final long serialVersionUID = -6135037386964932339L;
	public static final int BUY = 0;
	public static final int SELL = 1;
	public final static int EXEND_TYPE = ItemDef.ExtendAssets.STOCK;
	
	/**
	 * 주식 DB테이블 아이디
	 */
	private int mStockID = -1;
	
	/**
	 * 보유한 주식 수
	 */
	private long mTotalCount = 0;
	
	
	/**
	 * 현재 가격
	 */
	private long mPeresentPrice = 0;
	
	/**
	 * 주당 구입가격
	 */
	private long mPrice = 0;
	
	
	/**
	 * 평균 구입가격
	 */
	private long mMeanPrice = 0;
	
	/**
	 * 구입처
	 */
	private String mStore;
	

	/**
	 * 주식 아이디 설정
	 * @param mDepositID 주식 아이디
	 */
	public void setStockID(int stockID) {
		this.mStockID = stockID;
	}

	/**
	 * 주식 아이디를 얻는다.
	 * @return 주식 아이디
	 */
	public int getStockID() {
		return mStockID;
	}

	/**
	 * 총 구입 수량 설정
	 * @param mTotalCount
	 */
	public void setTotalCount(long totalCount) {
		this.mTotalCount = totalCount;
	}

	/**
	 * 총 구입 수량을 얻는다.
	 * @return
	 */
	public long getTotalCount() {
		return mTotalCount;
	}

	/**
	 * 평균가 설정
	 * @param meanPrice
	 */
	public void setMeanPrice(long meanPrice) {
		this.mMeanPrice = meanPrice;
	}

	/**
	 * 평균가를 얻는다.
	 * @return
	 */
	public long getMeanPrice() {
		return mMeanPrice;
	}

	public void setPeresentPrice(long peresentPrice) {
		this.mPeresentPrice = peresentPrice;
	}

	public long getPeresentPrice() {
		return mPeresentPrice;
	}

	public void setPrice(long price) {
		this.mPrice = price;
	}

	public long getPrice() {
		return mPrice;
	}

	public void setStore(String store) {
		this.mStore = store;
	}

	public String getStore() {
		return mStore;
	}

	public int getExtendType() {
		return EXEND_TYPE;
	}

}
