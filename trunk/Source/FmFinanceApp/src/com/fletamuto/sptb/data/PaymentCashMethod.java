package com.fletamuto.sptb.data;

public class PaymentCashMethod extends PaymentMethod {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -995617744694898761L;

	public PaymentCashMethod() {
		setType(CASH);
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return "Çö±Ý";
	}
}
