package com.fletamuto.sptb.data;


/**
 * �ڻ� �� �ݵ�
 * @author yongbban
 *
 */
public class AssetsFundItem extends AssetsExtendItem {
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
	private int mStoreCompany = -1;
	
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
	public void setStoreCompany(int storeCompany) {
		this.mStoreCompany = storeCompany;
	}

	/**
	 * �Ǹ�ó�� ��´�.
	 * @return
	 */
	public int getStoreCompany() {
		return mStoreCompany;
	}


}
