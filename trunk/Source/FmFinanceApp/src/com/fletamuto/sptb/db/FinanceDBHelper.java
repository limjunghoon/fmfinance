package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fletamuto.sptb.R;
import com.fletamuto.sptb.data.FinancialInstitution;
import com.fletamuto.sptb.data.PaymentMethod;

public class FinanceDBHelper extends SQLiteOpenHelper {
	private Context context;

	public FinanceDBHelper(Context context) {
			super(context, "finance.db", null, 1);
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
		
		void createTables(SQLiteDatabase db) {
			createIncomeTable(db);
			createExpenseTable(db);
			createAssetsTable(db);
			createLiabilityTable(db);
			createCategoryTable(db);
			createAccountTable(db);
			createInstitution(db);
			createPaymentMethod(db);
			createCard(db);
			createCardCompanyName(db);
		}
		
		

		private void createIncomeTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE income ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"year INTEGER NOT NULL," +
					"month INTEGER NOT NULL," +
					"day INTEGER NOT NULL," +
					"amount INTEGER NOT NULL," +
					"title TEXT," +
					"memo TEXT," +
					"main_category INTEGER NOT NULL);");
		}
		
		private void createExpenseTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE expense ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"year INTEGER NOT NULL," +
					"month INTEGER NOT NULL," +
					"day INTEGER NOT NULL," +
					"amount INTEGER NOT NULL," +
					"title TEXT," +
					"memo TEXT," +
					"main_category INTEGER NOT NULL," +
					"sub_category INTEGER NOT NULL);");
		}
		
		private void createAssetsTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE assets ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"year INTEGER NOT NULL," +
					"month INTEGER NOT NULL," +
					"day INTEGER NOT NULL," +
					"amount INTEGER NOT NULL," +
					"title TEXT," +
					"memo TEXT," +
					"main_category INTEGER NOT NULL," +
					"sub_category INTEGER NOT NULL);");
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
		
		private void createCategoryTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE income_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL);");
			
			db.execSQL("CREATE TABLE expense_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL);");
			
			db.execSQL("CREATE TABLE expense_sub_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"main_id INTEGER NOT NULL);");
			
			db.execSQL("CREATE TABLE assets_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL);");
			
			db.execSQL("CREATE TABLE assets_sub_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"main_id INTEGER NOT NULL);");
			
			db.execSQL("CREATE TABLE liability_main_category ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL);");
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
					"number INTEGER," +
					"account INEGER," +
					"name TEXT," +
					"instituion INTEGER," +
					"settlement_day INTEGER," +
					"start_settlement_day INTEGER," +
					"start_settlement_month INTEGER," +
					"end_settlement_day INTEGER," +
					"end_settlement_month INTEGER," +
					"memo TEXT," +
					"type INTEGER NOT NULL);");
		}
		
		private void createCardCompanyName(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE card_company_name ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name INTEGER NOT NULL," +
					"institution_id INEGER);");
		}

		private void createPaymentMethod(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE payment_method ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"type INTEGER NOT NULL," +
					"type_id INTEGER NOT NULL);");
		}
		
		private void insertCategory(SQLiteDatabase db) {
			insertIncomeCategory(db);
			insertExpenseCategory(db);
			insertAssetsCategory(db);
			insertLiabilityCategory(db);
			insertInstitution(db);
			insertPaymentMethod(db);
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
