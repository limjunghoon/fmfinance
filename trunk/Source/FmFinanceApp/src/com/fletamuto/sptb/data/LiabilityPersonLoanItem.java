package com.fletamuto.sptb.data;


/**
 * 부채 중 빌린돈
 * @author yongbban
 *
 */
public class LiabilityPersonLoanItem extends LiabilityExtendItem {
	
	/**
	 * 빌린돈 DB테이블 아이디
	 */
	private int mPersonLoanID = -1;
	
	/**
	 * 빌린사람
	 */
	private String mName;

	/**
	 * 빌린돈 아이디 설정
	 * @param loanID
	 */
	public void setPersonLoanID(int personLoanID) {
		this.mPersonLoanID = personLoanID;
	}

	/**
	 * 빌린돈 아이디를 얻는다.
	 * @return
	 */
	public int getPersonLoanID() {
		return mPersonLoanID;
	}

	/**
	 * 빌려준 사람 이름을 설정한다.
	 * @param name
	 */
	public void setLoanPeopleName(String name) {
		this.mName = name;
	}

	/**
	 * 빌려준 사람 이름을 얻는다.
	 * @return
	 */
	public String getLoanPeopleName() {
		return mName;
	}


	
}
