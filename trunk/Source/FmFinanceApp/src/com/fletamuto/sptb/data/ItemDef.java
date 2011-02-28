package com.fletamuto.sptb.data;

/**
 * �������� ���� ����.
 * @author yongbban
 * @version 1.0.0.0
 */
public final class ItemDef {
	public static final int VIEW_DAY_OF_MONTH = 0;
	public static final int VIEW_MONTH = 1;
	public static final int LAST_DAY_OF_MONTH = 31;
	
	public static final int EXTEND_NONE = 0;
	
	public static final int NOT_CATEGORY = 400;
	
	public static final int MOVE_SENSITIVITY = 60;
	
	/**	 ����, ����, �ڻ�, ��ä�� ���� �� ����.*/
	public static final class FinanceDef {
		public static final int INCOME = 0;
		public static final int EXPENSE = 1;
		public static final int ASSETS = 2;
		public static final int LIABILITY = 3;
		public static final int CARD = 4;
		public static final int ACCOUNT = 5;
	}
	
	/**	 ����� ���� ����Ʈ*/
	public static final class ExtendIncome {
		public static final int NONE = 100;
		public static final int SALARY = 101;
	}
	
	/**	 ����� �ڻ� ����Ʈ*/
	public static final class ExtendAssets {
		public static final int NONE = 200;
		public static final int DEPOSIT = 201;
		public static final int SAVINGS = 202;
		public static final int STOCK = 203;
		public static final int FUND = 204;
		public static final int ENDOWMENT_MORTGAGE = 205;
		public static final int REAL_ESTATE = 206;
	}
	
	/**	 ����� ��ä ����Ʈ*/
	public static final class ExtendLiablility {
		public static final int NONE = 300;
		public static final int LOAN = 301;
		public static final int CASH_SERVICE = 302;
		public static final int PERSON_LOAN = 303;
		public static final int CREDIT_LINE = 304;
	}
}
