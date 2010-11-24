package com.fletamuto.sptb.data;

/**
 * 아이템의 값을 정의.
 * @author yongbban
 * @version 1.0.0.0
 */
public final class ItemDef {
	
	/**	 수입, 지출, 자산, 부채의 고유 값 정의.*/
	public static final class FinanceDef {
		public static final int INCOME = 0;
		public static final int EXPENSE = 1;
		public static final int ASSETS = 2;
		public static final int LIABILITY = 3;
	}
	
	/**	 학장된 수입 리스트*/
	public static final class ExtendIncome {
		public static final int NONE = 0;
		public static final int SALARY = 1;
	}
	
	/**	 학장된 자산 리스트*/
	public static final class ExtendAssets {
		public static final int NONE = 0;
		public static final int DEPOSIT = 1;
		public static final int SAVINGS = 2;
		public static final int STOCK = 3;
		public static final int FUND = 4;
		public static final int ENDOWMENT_MORTGAGE = 5;
	}
}
