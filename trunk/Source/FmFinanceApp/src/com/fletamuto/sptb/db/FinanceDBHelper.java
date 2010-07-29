package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fletamuto.sptb.R;

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
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS expense");
			onCreate(db);
		}
		
		void createTables(SQLiteDatabase db) {
			createIncomeTable(db);
			createExpenseTable(db);
			createAssetsTable(db);
			createLiabilityTable(db);
			createCategoryTable(db);
			
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
					"main_category INTEGER);");
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
					"main_category INTEGER," +
					"sub_category INTEGER);");
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
					"main_category INTEGER," +
					"sub_category INTEGER);");
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
					"main_category INTEGER);");
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
		
		private void insertCategory(SQLiteDatabase db) {
			insertIncomeCategory(db);
			insertExpenseCategory(db);
			insertAssetsCategory(db);
			insertLiabilityCategory(db);
		}
		
		private void insertIncomeCategory(SQLiteDatabase db) {
			ContentValues rowItem = new ContentValues();
			String [] baseMainCategory = context.getResources().getStringArray(R.array.income_base_main_category);
			
			for (int index = 0; index < baseMainCategory.length; index++) {
				rowItem.put("name", baseMainCategory[index]);
				db.insert("income_main_category", null, rowItem);
			}
		}
		private void insertExpenseCategory(SQLiteDatabase db) {
			ContentValues rowItem = new ContentValues();
			String [] baseMainCategory = context.getResources().getStringArray(R.array.expense_base_main_category);
			
			for (int index = 0; index < baseMainCategory.length; index++) {
				rowItem.put("name", baseMainCategory[index]);
				db.insert("expense_main_category", null, rowItem);
			}
			
			insertExpenseSubCategory(db);
		}
		private void insertAssetsCategory(SQLiteDatabase db) {
			ContentValues rowItem = new ContentValues();
			String [] baseMainCategory = context.getResources().getStringArray(R.array.assets_base_main_category);
			
			for (int index = 0; index < baseMainCategory.length; index++) {
				rowItem.put("name", baseMainCategory[index]);
				db.insert("assets_main_category", null, rowItem);
			}
			
			insertAssetsSubCategory(db);
		}
		private void insertLiabilityCategory(SQLiteDatabase db) {
			ContentValues rowItem = new ContentValues();
			String [] baseMainCategory = context.getResources().getStringArray(R.array.liability_base_main_category);
			
			for (int index = 0; index < baseMainCategory.length; index++) {
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
			
			for (int i = 0; i < baseSubCategorys.size(); i++) {
				String [] subCategory = baseSubCategorys.get(i);
				
				for (int j = 0; j < subCategory.length; j++) {
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
			
			for (int i = 0; i < baseSubCategorys.size(); i++) {
				String [] subCategory = baseSubCategorys.get(i);
				
				for (int j = 0; j < subCategory.length; j++) {
					rowItem.put("name", subCategory[j]);
					rowItem.put("main_id", i+1);
					db.insert("assets_sub_category", null, rowItem);
				}
			}
		}
}
