package com.fletamuto.sptb.data;


/**
 * 부채 중 현금 서비스
 * @author yongbban
 *
 */
public class LiabilityCashServiceItem extends AssetsExtendItem {
	
	/**
	 * 현금 서비스 DB테이블 아이디
	 */
	private int mCashServiceID = -1;
	
	/**
	 * 현금 서비스받은 기관
	 */
	private FinancialCompany mCompany;

	/**
	 * 현금 서비스 아이디 설정
	 * @param loanID
	 */
	public void setCashServiceID(int cashServiceID) {
		this.mCashServiceID = cashServiceID;
	}

	/**
	 * 현금 서비스 아이디를 얻는다.
	 * @return
	 */
	public int getCashServiceID() {
		return mCashServiceID;
	}

	/**
	 * 현금 서비스 받은 기관 설정
	 * @param company
	 */
	public void setCompany(FinancialCompany company) {
		this.mCompany = company;
	}

	/**
	 * 현금 서비스 바은 기관을 얻는다.
	 * @return
	 */
	public FinancialCompany getCompany() {
		return mCompany;
	}
	
	
	
}
