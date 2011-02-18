package com.fletamuto.sptb.data;


/**
 * 자산 중 부동산
 * @author yongbban
 *
 */
public class AssetsRealEstateItem extends AssetsExtendItem {
	public final static int EXEND_TYPE = ItemDef.ExtendAssets.REAL_ESTATE;
 
	private static final long serialVersionUID = -2224085382368286553L;

	/**
	 * 부동산 DB테이블 아이디
	 */
	private int mRealEstateID = -1;
	
	
	private String mScale;


	/**
	 * 부동산 아이디 설정
	 * @param mEndowmentMortgageID 보장성 보험 아이디
	 */
	public void setRealEstateID(int RealEstateID) {
		this.mRealEstateID = RealEstateID;
	}

	/**
	 * 부동산 아이디를 얻는다.
	 * @return 부동산 아이디
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
