package com.fletamuto.sptb.data;


/**
 * ��ä �� ���� ����
 * @author yongbban
 *
 */
public class LiabilityCashServiceItem extends AssetsExtendItem {
	
	/**
	 * ���� ���� DB���̺� ���̵�
	 */
	private int mCashServiceID = -1;
	
	/**
	 * ���� ���񽺹��� ���
	 */
	private FinancialCompany mCompany;

	/**
	 * ���� ���� ���̵� ����
	 * @param loanID
	 */
	public void setCashServiceID(int cashServiceID) {
		this.mCashServiceID = cashServiceID;
	}

	/**
	 * ���� ���� ���̵� ��´�.
	 * @return
	 */
	public int getCashServiceID() {
		return mCashServiceID;
	}

	/**
	 * ���� ���� ���� ��� ����
	 * @param company
	 */
	public void setCompany(FinancialCompany company) {
		this.mCompany = company;
	}

	/**
	 * ���� ���� ���� ����� ��´�.
	 * @return
	 */
	public FinancialCompany getCompany() {
		return mCompany;
	}
	
	
	
}
