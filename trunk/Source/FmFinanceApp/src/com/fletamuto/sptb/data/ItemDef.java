package com.fletamuto.sptb.data;

/**
 * 아이템의 값을 정의.
 * @author yongbban
 * @version 1.0.0.0
 */
public final class ItemDef {
	public static final int EXTEND_NONE = 0;
	
	/**	 수입, 지출, 자산, 부채의 고유 값 정의.*/
	public static final class FinanceDef {
		public static final int INCOME = 0;
		public static final int EXPENSE = 1;
		public static final int ASSETS = 2;
		public static final int LIABILITY = 3;
	}
	
	/**	 학장된 수입 리스트*/
	public static final class ExtendIncome {
		public static final int NONE = 100;
		public static final int SALARY = 101;
	}
	
	/**	 학장된 자산 리스트*/
	public static final class ExtendAssets {
		public static final int NONE = 200;
		public static final int DEPOSIT = 201;
		public static final int SAVINGS = 202;
		public static final int STOCK = 203;
		public static final int FUND = 204;
		public static final int ENDOWMENT_MORTGAGE = 205;
	}
	
	/**	 학장된 부채 리스트*/
	public static final class ExtendLiablility {
		public static final int NONE = 300;
		public static final int LOAN = 301;
		public static final int CASH_SERVICE = 302;
		public static final int PERSON_LOAN = 303;
	}
}
