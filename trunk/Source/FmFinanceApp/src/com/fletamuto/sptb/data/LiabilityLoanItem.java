package com.fletamuto.sptb.data;


/**
 * ��ä �� ����
 * @author yongbban
 *
 */
public class LiabilityLoanItem extends LiabilityExtendItem {
	private static final long serialVersionUID = 3945062624058999546L;
	
	public final static int EXEND_TYPE = ItemDef.ExtendLiablility.LOAN;

	/**
	 * ���� DB���̺� ���̵�
	 */
	private int mLoanID = -1;
	
	/**
	 * ������� ���
	 */
	private FinancialCompany mCompany = new FinancialCompany();
	
	/** ���� ��ȯ ����*/
	private AccountItem mAccountItem = new AccountItem();

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
	
	public int getExtendType() {
		return EXEND_TYPE;
	}

	public void setAccount(AccountItem accountItem) {
		this.mAccountItem = accountItem;
	}

	public AccountItem getAccount() {
		return mAccountItem;
	}
	
	
	
}
