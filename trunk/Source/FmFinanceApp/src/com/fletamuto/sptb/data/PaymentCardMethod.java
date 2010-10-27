package com.fletamuto.sptb.data;

public class PaymentCardMethod extends PaymentMethod {
	private CardItem mCard;
	
	public PaymentCardMethod() {
		setType(CARD);
	}
	
	@Override
	public String getText() {
		if (mCard == null) return "Ä«µå";
		
		return mCard.getCompenyName().getName();
	}
}
