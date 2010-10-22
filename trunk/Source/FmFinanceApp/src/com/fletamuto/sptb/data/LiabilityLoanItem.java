package com.fletamuto.sptb.data;


/**
 * ��ä �� ����
 * @author yongbban
 *
 */
public class LiabilityLoanItem extends AssetsExtendItem {
	
	/**
	 * ���� DB���̺� ���̵�
	 */
	private int mLoanID = -1;
	
	/**
	 * ������� ���
	 */
	private FinancialCompany mCompany;

	/**
	 * ���� ���̵� ����
	 * @param loanID
	 */
	public void setLoanID(int loanID) {
		this.mLoanID = loanID;
	}

	/**
	 * ���� ���̵� ��´�.
	 * @return
	 */
	public int getLoanID() {
		return mLoanID;
	}

	/**
	 * ���� ���� ��� ����
	 * @param company
	 */
	public void setCompany(FinancialCompany company) {
		this.mCompany = company;
	}

	/**
	 * ���� ���� ����� ��´�.
	 * @return
	 */
	public FinancialCompany getCompany() {
		return mCompany;
	}
	
	
	
}
