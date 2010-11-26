package com.fletamuto.sptb.data;


/**
 * ��ä �� ������
 * @author yongbban
 *
 */
public class LiabilityPersonLoanItem extends LiabilityExtendItem {
	
	/**
	 * ������ DB���̺� ���̵�
	 */
	private int mPersonLoanID = -1;
	
	/**
	 * �������
	 */
	private String mName;

	/**
	 * ������ ���̵� ����
	 * @param loanID
	 */
	public void setPersonLoanID(int personLoanID) {
		this.mPersonLoanID = personLoanID;
	}

	/**
	 * ������ ���̵� ��´�.
	 * @return
	 */
	public int getPersonLoanID() {
		return mPersonLoanID;
	}

	/**
	 * ������ ��� �̸��� �����Ѵ�.
	 * @param name
	 */
	public void setLoanPeopleName(String name) {
		this.mName = name;
	}

	/**
	 * ������ ��� �̸��� ��´�.
	 * @return
	 */
	public String getLoanPeopleName() {
		return mName;
	}


	
}
