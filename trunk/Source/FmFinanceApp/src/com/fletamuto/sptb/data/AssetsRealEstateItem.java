package com.fletamuto.sptb.data;


/**
 * �ڻ� �� �ε���
 * @author yongbban
 *
 */
public class AssetsRealEstateItem extends AssetsExtendItem {
	public final static int EXEND_TYPE = ItemDef.ExtendAssets.REAL_ESTATE;
 
	private static final long serialVersionUID = -2224085382368286553L;

	/**
	 * �ε��� DB���̺� ���̵�
	 */
	private int mRealEstateID = -1;
	
	
	private String mScale;


	/**
	 * �ε��� ���̵� ����
	 * @param mEndowmentMortgageID ���强 ���� ���̵�
	 */
	public void setRealEstateID(int RealEstateID) {
		this.mRealEstateID = RealEstateID;
	}

	/**
	 * �ε��� ���̵� ��´�.
	 * @return �ε��� ���̵�
	 */
	public int getRealEstateID() {
		return mRealEstateID;
	}

	public void setScale(String mScale) {
		this.mScale = mScale;
	}

	public String getScale() {
		return mScale;
	}
	
	public int getExtendType() {
		return EXEND_TYPE;
	}

}
