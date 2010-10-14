package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fletamuto.sptb.LogTag;
import com.fletamuto.sptb.R;
import com.fletamuto.sptb.data.FinancialInstitution;
import com.fletamuto.sptb.data.PaymentMethod;

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
		insertCategory(db);
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
		createIncomeExtendTable(db);
		createSalaryTable(db);
		createIncomeOpenUsedTable(db);
		
		createExpenseTable(db);
		createExpenseOpenUsedTable(db);
		createExpenseTag(db);
		createPaymentMethod(db);
		
		createAssetsTable(db);
		createAssetsExtendTable(db);
		createAssetsExpense(db);
		createFundTable(db);
		createChangeFundTable(db);
		
//		createLiabilityTable(db);
		createLiabilityIncome(db);
		createCategoryTable(db);
//		createAccountTable(db);
//		createInstitution(db);

//		createCard(db);
//		createCardCompanyName(db);
		createNotificationTable(db);
		createRepeat(db);
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
	private void createIncomeExtendTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE income_extend ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"type INTEGER NOT NULL," +
					"type_id INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
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
				"create_time TIME NOT NULL," +
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
	private void createExpenseTag(SQLiteDatabase db) {
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
					"sub_category INTEGER NOT NULL," +
					"extend INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 자산 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createAssetsExtendTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_extend ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"type INTEGER NOT NULL," +
					"type_id INTEGER NOT NULL);");
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
					"store INTEGER);");
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
					"start_date DATE NOT NULL," +
					"end_date DATE NOT NULL," +
					"payment INTEGER NOT NULL," +
					"company INTEGER);");
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
					"endowment_mortgage INTEGER NOT NULL," +
					"change_date DATE NOT NULL," +
					"price DATE NOT NULL," +
					"price_type INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	
	
	/**
	 * 자산을 위해 지출 한  테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createAssetsExpense(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE assets_expense ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"assets_id INTEGER NOT NULL," +
					"expense_id INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
		
	private void createLiabilityTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE liability ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"year INTEGER NOT NULL," +
				"month INTEGER NOT NULL," +
				"day INTEGER NOT NULL," +
				"amount INTEGER NOT NULL," +
				"title TEXT," +
				"memo TEXT," +
				"main_category INTEGER NOT NULL);");
	}
	
	/**
	 * 부채로 인한 수입 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createLiabilityIncome(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE liability_income ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"liability_id INTEGER NOT NULL," +
					"income_id INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
		
	private void createCategoryTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE income_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL);");
			
			db.execSQL("CREATE TABLE expense_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL);");
			
			db.execSQL("CREATE TABLE expense_sub_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL," +
					"main_id INTEGER NOT NULL);");
			
			db.execSQL("CREATE TABLE assets_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL);");
			
			db.execSQL("CREATE TABLE liability_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"prioritize INTEGER NOT NULL," +
					"image_index INTEGER NOT NULL);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	/**
	 * 반복 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createRepeat(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE repeat ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"type INTEGER NOT NULL," +
					"day INTEGER," +
					"weekly INTEGER," +
					"term INTEGER);");
		} catch (SQLException e) {
			Log.e(LogTag.DB, "== SQLException : " + e.getMessage());
		}
	}
	
	private void createAccountTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE account ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"number TEXT NOT NULL," +
				"balance INTEGER," +
				"institution INTEGER," +
				"type INTEGER," +
				"create_date INTEGER," +
				"expiry_date INTEGER," +
				"memo TEXT," +
				"name TEXT);");
	}
	
	private void createInstitution(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE institution ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT NOT NULL," +
				"type INTEGER NOT NULL);");
	}
	
	private void createCard(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE card ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"type INTEGER NOT NULL," +
				"number INTEGER," +
				"account_id INEGER," +
				"company_name_id INTEGER," +
				"name TEXT," +
				"settlement_day INTEGER," +
				"start_settlement_day INTEGER," +
				"start_settlement_month INTEGER," +
				"end_settlement_day INTEGER," +
				"end_settlement_month INTEGER," +
				"memo TEXT," +
				"balance INTEGER);");
	}
	
	private void createCardCompanyName(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE card_company_name ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name INTEGER NOT NULL," +
				"institution_id INEGER);");
	}

	/**
	 * 지불방법 테이블을 만든다.
	 * @param db Exposes methods to manage a SQLite database.
	 */
	private void createPaymentMethod(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE payment_method ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"method_type INTEGER NOT NULL," +
					"account INTEGER," +
					"card INTEGER," +
					"installment_plan INTEGER);");
		}catch (SQLException e) {
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
	
	private void insertCategory(SQLiteDatabase db) {
//		insertIncomeCategory(db);
//		insertExpenseCategory(db);
//		insertAssetsCategory(db);
//		insertLiabilityCategory(db);
//		insertInstitution(db);
//		insertPaymentMethod(db);
	}
	
	private void insertIncomeCategory(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] baseMainCategory = context.getResources().getStringArray(R.array.income_base_main_category);
		int categoryLenth = baseMainCategory.length;
		
		for (int index = 0; index < categoryLenth; index++) {
			rowItem.put("name", baseMainCategory[index]);
			db.insert("income_main_category", null, rowItem);
		}
	}
	private void insertExpenseCategory(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] baseMainCategory = context.getResources().getStringArray(R.array.expense_base_main_category);
		int categoryLenth = baseMainCategory.length;
		
		for (int index = 0; index < categoryLenth; index++) {
			rowItem.put("name", baseMainCategory[index]);
			db.insert("expense_main_category", null, rowItem);
		}
		
		insertExpenseSubCategory(db);
	}
	private void insertAssetsCategory(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] baseMainCategory = context.getResources().getStringArray(R.array.assets_base_main_category);
		int categoryLenth = baseMainCategory.length;
		
		for (int index = 0; index < categoryLenth; index++) {
			rowItem.put("name", baseMainCategory[index]);
			db.insert("assets_main_category", null, rowItem);
		}
		
		insertAssetsSubCategory(db);
	}
	private void insertLiabilityCategory(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] baseMainCategory = context.getResources().getStringArray(R.array.liability_base_main_category);
		int categoryLenth = baseMainCategory.length;
		
		for (int index = 0; index < categoryLenth; index++) {
			rowItem.put("name", baseMainCategory[index]);
			db.insert("liability_main_category", null, rowItem);
		}
	}
	
	private void insertExpenseSubCategory(SQLiteDatabase db) {
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
		
		int subCategoryArrLenth = baseSubCategorys.size();
		
		for (int i = 0; i < subCategoryArrLenth; i++) {
			String [] subCategory = baseSubCategorys.get(i);
			int subCategoryLenth = subCategory.length;
			
			for (int j = 0; j < subCategoryLenth; j++) {
				rowItem.put("name", subCategory[j]);
				rowItem.put("main_id", i+1);
				db.insert("expense_sub_category", null, rowItem);
			}
		}
	}
	
	private void insertAssetsSubCategory(SQLiteDatabase db) {
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
	
	private void insertInstitution(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		String [] bakingNames = context.getResources().getStringArray(R.array.financial_institution_banking);
		String [] cardNames = context.getResources().getStringArray(R.array.financial_institution_card);
		int nameLenth = bakingNames.length;
		int InstitutionID = -1;
		
		for (int index = 0; index < nameLenth; index++) {
			rowItem.put("name", bakingNames[index]);
			rowItem.put("type", FinancialInstitution.BANKING);
			InstitutionID = (int)db.insert("institution", null, rowItem);
			if (InstitutionID != -1) {
				insertCardCompanyName(db, cardNames[index], InstitutionID);
			}
		}
	}
	
	private void insertCardCompanyName(SQLiteDatabase db, String cardName, int instituionID) {
		if (instituionID == -1) return;
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", cardName);
		rowItem.put("institution_id", instituionID);
		db.insert("card_company_name", null, rowItem);
	}
	
	
	private void insertPaymentMethod(SQLiteDatabase db) {
		ContentValues rowItem = new ContentValues();
		rowItem.put("type", PaymentMethod.CASH);
		rowItem.put("type_id", 0);
		db.insert("payment_method", null, rowItem);
	}
}
