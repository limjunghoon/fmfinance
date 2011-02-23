package com.fletamuto.sptb.data;


/**
 * 부채 중 대출
 * @author yongbban
 *
 */
public class LiabilityLoanItem extends LiabilityExtendItem {
	private static final long serialVersionUID = 3945062624058999546L;
	
	public final static int EXEND_TYPE = ItemDef.ExtendLiablility.LOAN;

	/**
	 * 대출 DB테이블 아이디
	 */
	private int mLoanID = -1;
	
	/**
	 * 대출받은 기관
	 */
	private FinancialCompany mCompany = new FinancialCompany();
	
	/** 대출 상환 계좌*/
	private AccountItem mAccountItem = new AccountItem();

	/**
	 * 대출 아이디 설정
	 * @param loanID
	 */
	public void setLoanID(int loanID) {
		this.mLoanID = loanID;
	}

	/**
	 * 대출 아이디를 얻는다.
	 * @return
	 */
	public int getLoanID() {
		return mLoanID;
	}

	/**
	 * 대출 받은 기관 설정
	 * @param company
	 */
	public void setCompany(FinancialCompany company) {
		this.mCompany = company;
	}

	/**
	 * 대출 바은 기관을 얻는다.
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
