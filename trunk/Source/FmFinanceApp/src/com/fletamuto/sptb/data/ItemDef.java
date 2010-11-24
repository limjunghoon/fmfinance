package com.fletamuto.sptb.data;

/**
 * �������� ���� ����.
 * @author yongbban
 * @version 1.0.0.0
 */
public final class ItemDef {
	
	/**	 ����, ����, �ڻ�, ��ä�� ���� �� ����.*/
	public static final class FinanceDef {
		public static final int INCOME = 0;
		public static final int EXPENSE = 1;
		public static final int ASSETS = 2;
		public static final int LIABILITY = 3;
	}
	
	/**	 ����� ���� ����Ʈ*/
	public static final class ExtendIncome {
		public static final int NONE = 0;
		public static final int SALARY = 1;
	}
	
	/**	 ����� �ڻ� ����Ʈ*/
	public static final class ExtendAssets {
		public static final int NONE = 0;
		public static final int DEPOSIT = 1;
		public static final int SAVINGS = 2;
		public static final int STOCK = 3;
		public static final int FUND = 4;
		public static final int ENDOWMENT_MORTGAGE = 5;
	}
}
