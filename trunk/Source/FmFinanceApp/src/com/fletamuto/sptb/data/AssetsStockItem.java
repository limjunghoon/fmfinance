package com.fletamuto.sptb.data;


/**
 * �ڻ� �� �ֽ�
 * @author yongbban
 *
 */
public class AssetsStockItem extends AssetsExtendItem {
	
	/**
	 * �ֽ� DB���̺� ���̵�
	 */
	private int mStockID = -1;
	
	/**
	 * ������ �ֽ� ��
	 */
	private int mTotalCount = 0;
	
	/**
	 * ��� ���԰���
	 */
	private long mMeanPrice = 0;
	
	/**
	 * ����ó
	 */
	private FinancialCompany mCompany;
	

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
	public void setTotalCount(int mTotalCount) {
		this.mTotalCount = mTotalCount;
	}

	/**
	 * �� ���� ������ ��´�.
	 * @return
	 */
	public int getTotalCount() {
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

	/**
	 * ����ó ����
	 * @param company
	 */
	public void setCompany(FinancialCompany company) {
		this.mCompany = company;
	}

	/**
	 * ����ó�� ��´�.
	 * @return
	 */
	public FinancialCompany getCompany() {
		return mCompany;
	}


}
