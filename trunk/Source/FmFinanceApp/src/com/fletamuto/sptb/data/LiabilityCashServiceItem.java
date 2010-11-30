package com.fletamuto.sptb.data;


/**
 * 부채 중 현금 서비스
 * @author yongbban
 *
 */
public class LiabilityCashServiceItem extends LiabilityExtendItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7908878232505354682L;

	/**
	 * 현금 서비스 DB테이블 아이디
	 */
	private int mCashServiceID = -1;
	
	/**
	 * 현금 서비스받은 카드
	 */
	private CardItem mCard = new CardItem(CardItem.CREDIT_CARD);;

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
	 * 현금 서비스 받은 카드 설정
	 * @param company
	 */
	public void setCard(CardItem card) {
		this.mCard = card;
	}

	/**
	 * 현금 서비스 받은 카드를 얻는다.
	 * @return
	 */
	public CardItem getCard() {
		return mCard;
	}
	
	
	
}
