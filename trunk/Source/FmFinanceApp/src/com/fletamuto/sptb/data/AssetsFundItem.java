package com.fletamuto.sptb.data;


/**
 * 자산 중 펀드
 * @author yongbban
 *
 */
public class AssetsFundItem extends AssetsExtendItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9044769708788641644L;

	/**
	 * 펀드 DB테이블 아이디
	 */
	private int mFundID = -1;
	
	/**
	 * 평균 구입가격
	 */
	private long mMeanPrice = 0;
	
	/**
	 * 판매처
	 */
	private String mStore;
	
	/**
	 * 펀드 아이디 설정
	 * @param mDepositID 펀드 아이디
	 */
	public void setFundID(int fundID) {
		this.mFundID = fundID;
	}

	/**
	 * 펀드 아이디를 얻는다.
	 * @return 펀드 아이디
	 */
	public int getFundID() {
		return mFundID;
	}

	/**
	 * 편균가를 설정한다.
	 * @param meanPrice
	 */
	public void setMeanPrice(long meanPrice) {
		this.mMeanPrice = meanPrice;
	}

	/**
	 * 편균가를 얻는다.
	 * @return
	 */
	public long getMeanPrice() {
		return mMeanPrice;
	}

	/**
	 * 판매처를 설정한다.
	 * @param storeCompany
	 */
	public void setStore(String store) {
		this.mStore = store;
	}

	/**
	 * 판매처를 얻는다.
	 * @return
	 */
	public String getStore() {
		return mStore;
	}


}
