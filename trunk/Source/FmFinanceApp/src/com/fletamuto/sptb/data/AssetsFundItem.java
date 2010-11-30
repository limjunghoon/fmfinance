package com.fletamuto.sptb.data;


/**
 * �ڻ� �� �ݵ�
 * @author yongbban
 *
 */
public class AssetsFundItem extends AssetsExtendItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9044769708788641644L;

	/**
	 * �ݵ� DB���̺� ���̵�
	 */
	private int mFundID = -1;
	
	/**
	 * ��� ���԰���
	 */
	private long mMeanPrice = 0;
	
	/**
	 * �Ǹ�ó
	 */
	private String mStore;
	
	/**
	 * �ݵ� ���̵� ����
	 * @param mDepositID �ݵ� ���̵�
	 */
	public void setFundID(int fundID) {
		this.mFundID = fundID;
	}

	/**
	 * �ݵ� ���̵� ��´�.
	 * @return �ݵ� ���̵�
	 */
	public int getFundID() {
		return mFundID;
	}

	/**
	 * ��հ��� �����Ѵ�.
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

	/**
	 * �Ǹ�ó�� �����Ѵ�.
	 * @param storeCompany
	 */
	public void setStore(String store) {
		this.mStore = store;
	}

	/**
	 * �Ǹ�ó�� ��´�.
	 * @return
	 */
	public String getStore() {
		return mStore;
	}


}
