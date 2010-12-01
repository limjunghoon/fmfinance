package com.fletamuto.sptb;


public final class MsgDef {
	public static final class ActRequest {
		public static final int ACT_AMOUNT = 0;
		public final static int ACT_CATEGORY = 1;
		public static final int ACT_SUB_CATEGORY = 2;
		public static final int ACT_EDIT_CATEGORY = 3;
		public final static int ACT_TAG_SELECTED = 4;
		public final static int ACT_REPEAT = 5;
		public final static int ACT_ADD_CARD = 6;
		public final static int ACT_ADD_ACCOUNT = 7;
		public final static int ACT_CARD_SELECT = 8;
		public final static int ACT_ACCOUNT_SELECT = 9;
		public final static int ACT_COMPANY_SELECT = 10;
		public final static int ACT_CARD_INPUT_SELECT = 11;
		public final static int ACT_TAG_EDIT = 12;
		public final static int ACT_TAG_ADD = 13;
		public final static int ACT_COMPANY_CARD_NAME_EDIT = 14;
		public final static int ACT_COMPANY_CARD_NAME_ADD = 15;
		public final static int ACT_COMPANY_NAME_EDIT = 16;
		public final static int ACT_COMPANY_NAME_ADD = 17;
		public final static int ACT_TAKE_HOME_PAY = 18;
		public final static int ACT_TAKE_HOME_PAY_INSURANCE_AMOUNT = 19;
		public final static int ACT_TAKE_HOME_PAY_TAX_AMOUNT = 20;
		public final static int ACT_TAKE_HOME_PAY_ETC_AMOUNT = 21;
		public final static int ACT_TAKE_HOME_PAY_PENSION_AMOUNT = 22;
		public final static int ACT_ADD_INCOME = 23;
		public final static int ACT_ADD_ASSETS = 24;
		public final static int ACT_ADD_LIABLITY = 25;
		public final static int ACT_EDIT_BUDGET = 26;
	}
	
	public static final class ExtraNames {
		public static final String CATEGORY_ID = "CATEGORY_ID";
		public static final String CATEGORY_SUB_ID = "CATEGORY_SUB_ID";
		public static final String CATEGORY_NAME = "CATEGORY_NAME";
		public static final String TAG_ID = "TAG_ID";
		public static final String TAG_NAME = "TAG_NAME";
		public static final String CARD_ID = "CARD_ID";
		public static final String ACCOUNT_ID = "ACCOUNT_ID";
		public static final String INSTALLMENT_PLAN = "INSTALLMENT_PLAN";
		public static final String COMPANY_ID = "COMPANY_ID";
		public static final String RPEAT_TYPE = "RPEAT_TYPE";
		public static final String RPEAT_WEEKLY = "RPEAT_WEEKLY";
		public static final String RPEAT_DAILY = "RPEAT_DAILY";
		public static final String SALARY_TOTAL_AMOUNT = "SALARY_TOTAL_AMOUNT";
		public static final String TAKE_HOME_PAY_INSURANCE = "TAKE_HOME_PAY_INSURANCE";
		public static final String TAKE_HOME_PAY_TAX = "TAKE_HOME_PAY_TAX";
		public static final String TAKE_HOME_PAY_ETC = "TAKE_HOME_PAY_ETC";
		public static final String TAKE_HOME_PAY_PENSION = "TAKE_HOME_PAY_PENSION";
		public static final String CALENDAR_YEAR = "CALENDAR_YEAR";
		public static final String CALENDAR_MONTH = "CANLENDER_MONTH";
		public static final String CALENDAR_DAY = "CANLENDER_DAY";
		public static final String BUDGET_ITEM_LIST = "BUDGET_ITEM_LIST";
		public static final String SELECT_SUB_CATEGORY_IN_MAIN_CATEGORY = "SELECT_SUB_CATEGORY_IN_MAIN_CATEGORY";
	}
}
