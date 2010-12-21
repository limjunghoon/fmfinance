package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fletamuto.sptb.R;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.UISelectItem;
import com.fletamuto.sptb.util.LogTag;

/**
 * DB 생성되거나 버전이 업데이트 될 경우 
 * 테이블관리를 도와주는 클래스
 * @author yongbban
 *
 */
public class FinanceDBHelper extends SQLiteOpenHelper {
	private Context context;

	public FinanceDBHelper(Context context) {
			super(context, DBDef.DBInfo.DB_NAME, null, DBDef.DBInfo.DB_VERSION);
			this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
		insertBaseItems(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}
	
	/**
	 * 테이블 생성
	 * @param db Exposes methods to manage a SQLite database.
	 */
	void createTables(SQLiteDatabase db) {
		Log.i(LogTag.DB, "== start make db table ==");
		createIncomeTable(db);
	//	createIncomeExtendTable(db);
		createSalaryTable(db);
		createIncomeOpenUsedTable(db);
		
		createExpenseTable(db);
		createExpenseOpenUsedTable(db);
		createExpenseTagTable(db);
		createPaymentMethodTable(db);
		createExpenseSMSTable(db);
		
		createAssetsTable(db);
		createChangeAssetsAmountTable(db);
//		createAssetsExtendTable(db);
		createAssetsExpenseTable(db);
		createAssetsSavingsTable(db);
		createAssetsDepositTable(db);
		createFundTable(db);
		createChangeFundTable(db);
		createEndowmentMortgageTable(db);
		createChangeEndowmentMortgageTable(db);
		createAssetsStockTable(db);
		createChangeAssetsStockTable(db);
		createAssetsPurposeTable(db);
		createPurposeTable(db);
		
		createLiabilityTable(db);
		createChangeLiabilityAmountTable(db);
		createLiabilityExtendTable(db);
		createLiabilityIncomeTable(db);
		createLiabilityLoanTable(db);
		createLiabilityCashServiceTable(db);
		createLiabilityPersonLoanTable(db);
		
		createCategoryTable(db);
		
		createAccountTable(db);
		creaetTransferHistoryTable(db);

		createCardTable(db);
		createCardCompanyTable(db);
		
		createBudgetTable(db);
		createBudgetMainCategoryTable(db);
		createBudgetSubCategoryTable(db);
		
		createNotificationTable(db);
		
		createReceiptTable(db);
		
		createRepeatTable(db);
		createFinanceCompanyTable(db);
		Log.i(LogTag.DB, "== end make db table ==");
	}
		
	/**
	 * 수입 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createIncomeTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE income ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"create_date DATE NOT NULL," +
					"amount INTEGER NOT NULL," +
					"title TEXT," +
					"memo TEXT," +
					"account INTEGER," +
					"main_category INTEGER NOT NULL," +
					"sub_category INTEGER," +
					"repeat INTEGER," +
					"extend INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 확장된 수입지원  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
//	private void createIncomeExtendTable(SQLiteDatabase db) {
//		try {
//			db.execSQL("CREATE TABLE income_extend ( " +
//					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//					"type INTEGER NOT NULL," +
//					"type_id INTEGER NOT NULL);");
//		} catch (SQLException e) {
//			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
//		}
//	}
//		
	/**
	 * 월급 명세서 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createSalaryTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE income_salary ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"pension INTEGER," +
					"tax INTEGER," +
					"assurance INTEGER," +
					"etc INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 자주 사용되는 수입 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createIncomeOpenUsedTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE income_open_used ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"income_id INTEGER NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 지출 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createExpenseTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE expense ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"create_date DATE NOT NULL," +
				"create_time TIME ," +
				"amount INTEGER NOT NULL," +
				"title TEXT," +
				"memo TEXT," +
				"main_category INTEGER NOT NULL," +
				"sub_category INTEGER NOT NULL," +
				"waste INTEGER," +
				"repeat INTEGER," +
				"payment_method INTEGER NOT NULL," +
				"tag INTEGER);");
	}
		
	/**
	 * 자주 사용되는 지출 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createExpenseOpenUsedTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE expense_open_used ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"income_id INTEGER NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 자주 사용되는 지출 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createExpenseTagTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE expense_tag ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 자산 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createAssetsTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"create_date DATE NOT NULL," +
					"amount INTEGER NOT NULL," +
					"title TEXT," +
					"memo TEXT," +
					"main_category INTEGER NOT NULL," +
					"sub_category INTEGER," +
					"extend INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 자산 확장  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
//	private void createAssetsExtendTable(SQLiteDatabase db) {
//		try {
//			db.execSQL("CREATE TABLE assets_extend ( " +
//					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//					"type INTEGER NOT NULL," +
//					"type_id INTEGER NOT NULL);");
//		} catch (SQLException e) {
//			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
//		}
//	}
	
	/**
	 * 자산 변동  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createChangeAssetsAmountTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_change_amount ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"assets_id INTEGER NOT NULL," +
					"change_date DATE NOT NULL," +
					"amount INTEGER NOT NULL," +
					"memo TEXT," +
					"count INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 적금 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createAssetsSavingsTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_savings ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"expiry_date DATE," +
					"account INTEGER," +
					"rate INTEGER," +
					"payment INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 예금 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createAssetsDepositTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_deposit ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"expiry_date DATE," +
					"rate INTEGER," +
					"account INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}

	/**
	 * 펀드 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createFundTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_fund ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"mena_price INTEGER," +
					"store TEXT);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 펀드 가격변동  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createChangeFundTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_change_fund ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"fund_id INTEGER NOT NULL," +
					"change_date DATE NOT NULL," +
					"price DATE NOT NULL," +
					"price_type INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 저축성 보험 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createEndowmentMortgageTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_endowment_mortgage ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"expiry_date DATE NOT NULL," +
					"payment INTEGER NOT NULL," +
					"company TEXT);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 저축성 보험  변동  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createChangeEndowmentMortgageTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_change_endowment_mortgage ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"endowment_mortgage_id INTEGER NOT NULL," +
					"change_date DATE NOT NULL," +
					"count INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 주식 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createAssetsStockTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_stock ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"present_price INTEGER NOT NULL," +
					"total_count INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 저축성 보험  변동  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createChangeAssetsStockTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_chage_stock ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"stock_id INTEGER NOT NULL," +
					"change_date DATE NOT NULL," +
					"count INTEGER NOT NULL," +
					"stock_price DATE NOT NULL," +
					"price_type INTEGER NOT NULL," +
					"store TEXT);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 목표와 연관되어 있는 자산 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createAssetsPurposeTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_purpose ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"purpose_id INTEGER NOT NULL," +
					"assets_id INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 자산을 위해 지출 한  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createAssetsExpenseTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_expense ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"assets_id INTEGER NOT NULL," +
					"expense_id INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
		
	/**
	 * 부채 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createLiabilityTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE liability ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"create_date DATE NOT NULL," +
				"amount INTEGER NOT NULL," +
				"title TEXT," +
				"memo TEXT," +
				"main_category INTEGER NOT NULL," +
				"sub_category INTEGER," +
				"extend INTEGER);");
	}
	
	/**
	 * 부채 확장 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createLiabilityExtendTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE liability_extend ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"type TYPE NOT NULL," +
				"extend_id INTEGER);");
	}
	
	/**
	 * 부채 변동  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createChangeLiabilityAmountTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE liability_change_amount ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"liability_id INTEGER NOT NULL," +
					"change_date DATE NOT NULL," +
					"amount INTEGER NOT NULL," +
					"memo TEXT," +
					"count INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 부채로 인한 수입 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createLiabilityIncomeTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE liability_income ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"liability_id INTEGER NOT NULL," +
					"income_id INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}

	/**
	 * 대출 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createLiabilityLoanTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE liability_loan ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"finance_company INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
		
	/**
	 * 현금서비스 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createLiabilityCashServiceTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE liability_cash_service ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"card INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 빌린돈 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createLiabilityPersonLoanTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE liability_person_loan ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 분류 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createCategoryTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE income_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL," +
					"extend_type INTEGER," +
					"type INTEGER);");
			
			db.execSQL("CREATE TABLE expense_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL," +
					"extend_type INTEGER," +
					"type INTEGER);");
			
			db.execSQL("CREATE TABLE expense_sub_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL," +
					"main_id INTEGER NOT NULL," +
					"extend_type INTEGER," +
					"type INTEGER);");
			
			db.execSQL("CREATE TABLE assets_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL," +
					"extend_type INTEGER," +
					"type INTEGER);");
			
			db.execSQL("CREATE TABLE liability_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL," +
					"extend_type INTEGER," +
					"type INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 반복 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createRepeatTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE repeat ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"type INTEGER NOT NULL," +
					"day INTEGER," +
					"weekly INTEGER," +
					"term INTEGER," +
					"item_type INTEGER NOT NULL," +
					"origin_item_id INTEGER NOT NULL," +
					"last_apply_date DATE NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 계좌 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createAccountTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE account ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"create_date DATE," +
				"number TEXT," +
				"balance INTEGER," +
				"company INTEGER," +
				"type INTEGER NOT NULL," +
				"expiry_date DATE," +
				"memo TEXT," +
				"name TEXT);");
	}
	
	/**
	 * 이체기록 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void creaetTransferHistoryTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE transfer_history ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"occurrence_date DATE," +
				"to_account INTEGER," +
				"from_account INTEGER," +
				"amount INTEGER NOT NULL);");
	}
	
	/**
	 * 금융회사 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createFinanceCompanyTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE finance_company ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT NOT NULL," +
				"type INTEGER NOT NULL," +
				"prioritize INTEGER NOT NULL," +
				"image_index INTEGER NOT NULL);");
	}
	
	/**
	 * 카드 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createCardTable(SQLiteDatabase db) {
		try{
			db.execSQL("CREATE TABLE card ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT," +
					"number INTEGER," +
					"type INTEGER NOT NULL," +
					"account_id INEGER," +
					"company_name_id INTEGER," +
					"settlement_day INTEGER," +
					"biling_period_day INTEGER," +
					"biling_period_month INTEGER," +
					"memo TEXT," +
					"maxiup_balance INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 카드회사  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createCardCompanyTable(SQLiteDatabase db) {
		try{
			db.execSQL("CREATE TABLE card_company ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"card_name TEXT NOT NULL," +
					"finance_company_id INEGER," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}

	/**
	 * 예산  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createBudgetTable(SQLiteDatabase db) {
		try{
			db.execSQL("CREATE TABLE budget ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"total_amount INTEGER NOT NULL," +
					"year INTEGER NOT NULL," +
					"month INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 예산  상위분류 설정 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createBudgetMainCategoryTable(SQLiteDatabase db) {
		try{
			db.execSQL("CREATE TABLE budget_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"amount INTEGER," +
					"year INTEGER," +
					"month INTEGER," +
					"budget_id INTEGER," +
					"main_category INTEGER);");
					
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 예산  하위분류 설정 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createBudgetSubCategoryTable(SQLiteDatabase db) {
		try{
			db.execSQL("CREATE TABLE budget_sub_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"amount INTEGER," +
					"year INTEGER," +
					"month INTEGER," +
					"budget_id INTEGER," +
					"sub_category INTEGER);");
					
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 지불방법 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createPaymentMethodTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE payment_method ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"method_type INTEGER NOT NULL," +
					"account INTEGER," +
					"card INTEGER," +
					"installment_plan INTEGER," +
					"message TEXT);");
		}catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * SMS 정보 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createExpenseSMSTable(SQLiteDatabase db) {
		try{
			db.execSQL("CREATE TABLE expense_sms ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"create_date DATE NOT NULL," +
					"card INTEGER NOT NULL," +
					"message INTEGER NOT NULL," +
					"done INTEGER," +
					"receive_number INTEGER);");
					
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 알림 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createNotificationTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE notification ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"type INTEGER NOT NULL," +
					"item_id INTEGER NOT NULL," +
					"message_info INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 목표 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createPurposeTable(SQLiteDatabase db) {
		try{
			db.execSQL("CREATE TABLE purpose ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"amount INTEGER NOT NULL," +
					"start_date DATE NOT NULL," +
					"end_date DATE NOT NULL);");
					
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 영수증  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createReceiptTable(SQLiteDatabase db) {
		try{
			db.execSQL("CREATE TABLE receipt ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"create_date DATE NOT NULL," +
					"expense_main_category INTEGER," +
					"expense_sub_category INTEGER," +
					"done INTEGER," +
					"memo INTEGER," +
					"picture_info TEXT);");
					
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 기본적인 데이타를 테이블에 추가한다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void insertBaseItems(SQLiteDatabase db) {
		insertCategoryTables(db);
		insertCompanyTable(db);
		insertExpenseTagTable(db);
		insertAccountTables(db);
	//	insertPaymentMethodTable(db);
	}
	
	/**
	 * 기본적인 분류 항목을 추가한다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void insertCategoryTables(SQLiteDatabase db) {
		insertIncomeCategoryTable(db);
		insertExpenseCategoryTable(db);
		insertAssetsCategoryTable(db);
		insertLiabilityCategoryTable(db);
	}
	
	/**
	 * 기본적인 수입 상위  분류 항목을 추가한다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void insertIncomeCategoryTable(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] baseMainCategory = context.getResources().getStringArray(R.array.income_base_main_category);
		int categoryLenth = baseMainCategory.length;
		
		for (int index = 0; index < categoryLenth; index++) {
			rowItem.put("name", baseMainCategory[index]);
			rowItem.put("prioritize", index+1);
			rowItem.put("image_index", index+1);
	
			// 임시 코드  //////////////////////////////////////////////////////
			if (index == 0) rowItem.put("extend_type", ItemDef.ExtendIncome.SALARY);
			else rowItem.put("extend_type", ItemDef.ExtendIncome.NONE);
			//////////////////////////////////////////////////////////////////
			if (db.insert("income_main_category", null, rowItem) == -1) {
				Log.e(LogTag.DB, "== DB Insert ERROR ==");
			}
		}
	}
	
	/**
	 * 기본적인 지출 상위  분류 항목을 추가한다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void insertExpenseCategoryTable(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] baseMainCategory = context.getResources().getStringArray(R.array.expense_base_main_category);
		int categoryLenth = baseMainCategory.length;
		int salaryCategory = categoryLenth-1;
		
		for (int index = 0; index < categoryLenth; index++) {
			rowItem.put("name", baseMainCategory[index]);
			rowItem.put("prioritize", index+1);
			rowItem.put("image_index", index+1);
			
			if (salaryCategory == index) {
				rowItem.put("type", UISelectItem.HIDE);
				rowItem.put("extend_type", ItemDef.ExtendIncome.SALARY);
			}
			
			if (db.insert("expense_main_category", null, rowItem) == -1) {
				Log.e(LogTag.DB, "== DB Insert ERROR ==");
			}
		}
		
		insertExpenseSubCategoryTable(db);
	}
	
	
	/**
	 * 기본적인 자산 상위  분류 항목을 추가한다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void insertAssetsCategoryTable(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] baseMainCategory = context.getResources().getStringArray(R.array.assets_base_main_category);
		int categoryLenth = baseMainCategory.length;
		
		for (int index = 0; index < categoryLenth; index++) {
			rowItem.put("name", baseMainCategory[index]);
			rowItem.put("prioritize", index+1);
			rowItem.put("image_index", index+1);
			
			// 임시 코드  //////////////////////////////////////////////////////
			if (index == 0) rowItem.put("extend_type", ItemDef.ExtendAssets.DEPOSIT);
			else if (index == 1) rowItem.put("extend_type", ItemDef.ExtendAssets.SAVINGS);
			else if (index == 2) rowItem.put("extend_type", ItemDef.ExtendAssets.STOCK);
			else if (index == 3) rowItem.put("extend_type", ItemDef.ExtendAssets.FUND);
			else if (index == 4) rowItem.put("extend_type", ItemDef.ExtendAssets.ENDOWMENT_MORTGAGE);
			else rowItem.put("extend_type", ItemDef.ExtendAssets.NONE);
			//////////////////////////////////////////////////////////////////
			if (db.insert("assets_main_category", null, rowItem) == -1) {
				Log.e(LogTag.DB, "== DB Insert ERROR ==");
			}
		}
	}
	
	/**
	 * 기본적인 부채 상위  분류 항목을 추가한다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void insertLiabilityCategoryTable(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] baseMainCategory = context.getResources().getStringArray(R.array.liability_base_main_category);
		int categoryLenth = baseMainCategory.length;
		
		for (int index = 0; index < categoryLenth; index++) {
			rowItem.put("name", baseMainCategory[index]);
			rowItem.put("prioritize", index+1);
			rowItem.put("image_index", index+1);
			
			// 임시 코드  //////////////////////////////////////////////////////
			if (index == 0) rowItem.put("extend_type", ItemDef.ExtendLiablility.LOAN);
			else if (index == 1) rowItem.put("extend_type", ItemDef.ExtendLiablility.CASH_SERVICE);
			else if (index == 2) rowItem.put("extend_type", ItemDef.ExtendLiablility.PERSON_LOAN);
			else rowItem.put("extend_type", ItemDef.ExtendAssets.NONE);
			//////////////////////////////////////////////////////////////////
			
			if (db.insert("liability_main_category", null, rowItem) == -1) {
				Log.e(LogTag.DB, "== DB Insert ERROR ==");
			}
		}
	}
	
	/**
	 * 기본적인 지출 하위  분류 항목을 추가한다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void insertExpenseSubCategoryTable(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		ArrayList<String []> baseSubCategorys = new ArrayList<String []>();
		
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_1));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_2));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_3));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_4));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_5));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_6));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_7));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_8));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_9));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_10));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_11));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_12));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_13));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_base_sub_category_14));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.assets_base_main_category));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.liability_base_main_category));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.expense_salary_sub_category));
		
		
		int subCategoryArrLenth = baseSubCategorys.size();
		
		for (int i = 0; i < subCategoryArrLenth; i++) {
			String [] subCategory = baseSubCategorys.get(i);
			int subCategoryLenth = subCategory.length;
			
			for (int j = 0; j < subCategoryLenth; j++) {
				rowItem.put("name", subCategory[j]);
				rowItem.put("prioritize", i+1);
				rowItem.put("image_index", i+1);
				rowItem.put("main_id", i+1);
				
				if (db.insert("expense_sub_category", null, rowItem) == -1) {
					Log.e(LogTag.DB, "== DB Insert ERROR ==");
				}
			}
		}
	}
	

	private void insertAccountTables(SQLiteDatabase db) {
		AccountItem account = new AccountItem();
		account.setType(AccountItem.MY_POCKET);
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", account.getCreateDateString());
		rowItem.put("number", account.getNumber());
		rowItem.put("balance", account.getBalance());
		rowItem.put("company", account.getCompany().getID());
		rowItem.put("type", account.getType());
		rowItem.put("expiry_date", account.getExpiryDateString());
		rowItem.put("memo", account.getMemo());
		rowItem.put("name", account.getName());
		db.insert("account", null, rowItem);
				
	}
	/*
	private void insertAssetsSubCategoryTable(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		ArrayList<String []> baseSubCategorys = new ArrayList<String []>();
		
		baseSubCategorys.add(context.getResources().getStringArray(R.array.assets_base_sub_category_1));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.assets_base_sub_category_2));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.assets_base_sub_category_3));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.assets_base_sub_category_4));
		baseSubCategorys.add(context.getResources().getStringArray(R.array.assets_base_sub_category_5));
		
		int subCategoryArrLenth = baseSubCategorys.size();
		for (int i = 0; i < subCategoryArrLenth; i++) {
			String [] subCategory = baseSubCategorys.get(i);
			int subCategoryLenth = subCategory.length;
			
			for (int j = 0; j < subCategoryLenth; j++) {
				rowItem.put("name", subCategory[j]);
				rowItem.put("main_id", i+1);
				db.insert("assets_sub_category", null, rowItem);
			}
		}
	}
	*/
	private void insertCompanyTable(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] bakingNames = context.getResources().getStringArray(R.array.financial_institution_banking);
		String [] cardNames = context.getResources().getStringArray(R.array.financial_institution_card);
		int nameLenth = bakingNames.length;
		int InstitutionID = -1;
		
		for (int index = 0; index < nameLenth; index++) {
			rowItem.put("name", bakingNames[index]);
			rowItem.put("type", FinancialCompany.BANKING);
			rowItem.put("prioritize", index+1);
			rowItem.put("image_index", index+1);
			InstitutionID = (int)db.insert("finance_company", null, rowItem);
			if (InstitutionID != -1) {
				insertCardCompanyNameTable(db, cardNames[index], InstitutionID);
			}
		}
	}
	
	private void insertCardCompanyNameTable(SQLiteDatabase db, String cardName, int companyID) {
		if (companyID == -1) return;
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("card_name", cardName);
		rowItem.put("finance_company_id", companyID);
		rowItem.put("prioritize", companyID);
		rowItem.put("image_index", companyID);
		if (db.insert("card_company", null, rowItem) == -1) {
			Log.e(LogTag.DB, "== DB Insert ERROR ==");
		}
	}
	
	private void insertExpenseTagTable(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] tagNames = context.getResources().getStringArray(R.array.expense_default_tag_list);
		int tagLenth = tagNames.length;
		
		for (int index = 0; index < tagLenth; index++) {
			rowItem.put("name", tagNames[index]);
			rowItem.put("prioritize", index+1);
			rowItem.put("image_index", index+1);
			db.insert("expense_tag", null, rowItem);
		}
	}
	
	/*
	private void insertPaymentMethodTable(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		rowItem.put("type", PaymentMethod.CASH);
		rowItem.put("type_id", 0);
		if (db.insert("payment_method", null, rowItem) == -1) {
			Log.e(LogTag.DB, "== DB Insert ERROR ==");
		}
	}
	*/
}
