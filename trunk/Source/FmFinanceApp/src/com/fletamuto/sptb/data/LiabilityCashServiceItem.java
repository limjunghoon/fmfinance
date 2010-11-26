package com.fletamuto.sptb.data;


/**
 * ��ä �� ���� ����
 * @author yongbban
 *
 */
public class LiabilityCashServiceItem extends LiabilityExtendItem {
	
	/**
	 * ���� ���� DB���̺� ���̵�
	 */
	private int mCashServiceID = -1;
	
	/**
	 * ���� ���񽺹��� ī��
	 */
	private CardItem mCard = new CardItem(CardItem.CREDIT_CARD);;

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
	 * ���� ���� ���� ī�� ����
	 * @param company
	 */
	public void setCard(CardItem card) {
		this.mCard = card;
	}

	/**
	 * ���� ���� ���� ī�带 ��´�.
	 * @return
	 */
	public CardItem getCard() {
		return mCard;
	}
	
	
	
}
