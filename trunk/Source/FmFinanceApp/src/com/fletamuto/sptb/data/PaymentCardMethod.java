package com.fletamuto.sptb.data;

public class PaymentCardMethod extends PaymentMethod {
	/**
	 * 
	 */
	private static final long serialVersionUID = 743513086501392523L;
	private CardItem mCard;
	private int mInstallmentPlan = 0;
	
	public PaymentCardMethod() {
		setType(CARD);
	}
	
	public void setCard(CardItem card) {
		mCard = card;
	}
	
	public CardItem getCard() {
		return mCard;
	}
	
	@Override
	public String getText() {
		if (mCard == null) return "ī��";
		String installment;
		
		if (mCard.getType() == CardItem.CREDIT_CARD && mInstallmentPlan > 0) {
			
			installment = String.format("%d���� �Һ�", mInstallmentPlan + 1);
		} else {
			installment = "�Ͻú�";
		}
		
		return mCard.getCompenyName().getName() + " " + installment;
	}

	public void setInstallmentPlan(int installmentPlan) {
		this.mInstallmentPlan = installmentPlan;
	}

	public int getInstallmentPlan() {
		return mInstallmentPlan;
	}
	
	@Override
	public String getName() {
		if (mCard == null) return "ī��";
		else return "ī�� :" + mCard.getCompenyName().getName() +  "(" + mCard.getNumber() + ")";
	}
}
