package com.fletamuto.sptb.data;


/**
 * �ڻ� �� �ֽ�
 * @author yongbban
 *
 */
public class AssetsStockItem extends AssetsExtendItem {
	private static final long serialVersionUID = -6135037386964932339L;
	public static final int BUY = 0;
	public static final int SELL = 1;
	public final static int EXEND_TYPE = ItemDef.ExtendAssets.STOCK;
	
	/**
	 * �ֽ� DB���̺� ���̵�
	 */
	private int mStockID = -1;
	
	/**
	 * ������ �ֽ� ��
	 */
	private long mTotalCount = 0;
	
	
	/**
	 * ���� ����
	 */
	private long mPeresentPrice = 0;
	
	/**
	 * �ִ� ���԰���
	 */
	private long mPrice = 0;
	
	
	/**
	 * ��� ���԰���
	 */
	private long mMeanPrice = 0;
	
	/**
	 * ����ó
	 */
	private String mStore;
	

	/**
	 * �ֽ� ���̵� ����
	 * @param mDepositID �ֽ� ���̵�
	 */
	public void setStockID(int stockID) {
		this.mStockID = stockID;
	}

	/**
	 * �ֽ� ���̵� ��´�.
	 * @return �ֽ� ���̵�
	 */
	public int getStockID() {
		return mStockID;
	}

	/**
	 * �� ���� ���� ����
	 * @param mTotalCount
	 */
	public void setTotalCount(long totalCount) {
		this.mTotalCount = totalCount;
	}

	/**
	 * �� ���� ������ ��´�.
	 * @return
	 */
	public long getTotalCount() {
		return mTotalCount;
	}

	/**
	 * ��հ� ����
	 * @param meanPrice
	 */
	public void setMeanPrice(long meanPrice) {
		this.mMeanPrice = meanPrice;
	}

	/**
	 * ��հ��� ��´�.
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
