package com.fletamuto.sptb.data;

public class PaymentMethod {
	public final static int CASH = 0;
	public final static int CREDIT_CARD = 1;
	public final static int CASH_CARD = 2;
	public final static int PRIPAID_CARD = 3;
	public final static int ACCOUNT = 4;
	
	private int id = -1;
	private int type;
	private int type_id;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	
	public int getType_id() {
		return type_id;
	}
	 
}
