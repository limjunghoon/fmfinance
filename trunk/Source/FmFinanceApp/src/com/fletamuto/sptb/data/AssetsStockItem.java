package com.fletamuto.sptb.data;


/**
 * 자산 중 주식
 * @author yongbban
 *
 */
public class AssetsStockItem extends AssetsExtendItem {
	
	/**
	 * 주식 DB테이블 아이디
	 */
	private int mStockID = -1;
	
	/**
	 * 보유한 주식 수
	 */
	private int mTotalCount = 0;
	
	/**
	 * 평균 구입가격
	 */
	private long mMeanPrice = 0;
	
	/**
	 * 구입처
	 */
	private FinancialCompany mCompany;
	

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
	public void setTotalCount(int mTotalCount) {
		this.mTotalCount = mTotalCount;
	}

	/**
	 * 총 구입 수량을 얻는다.
	 * @return
	 */
	public int getTotalCount() {
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

	/**
	 * 구입처 설정
	 * @param company
	 */
	public void setCompany(FinancialCompany company) {
		this.mCompany = company;
	}

	/**
	 * 구입처를 얻는다.
	 * @return
	 */
	public FinancialCompany getCompany() {
		return mCompany;
	}


}
