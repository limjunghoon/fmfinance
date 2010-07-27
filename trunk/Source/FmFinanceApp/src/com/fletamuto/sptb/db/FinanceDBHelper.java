package com.fletamuto.sptb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FinanceDBHelper extends SQLiteOpenHelper {

	public FinanceDBHelper(Context context) {
			super(context, "finance.db", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			CreateTables(db);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS expense");
			onCreate(db);
		}
		
		void CreateTables(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE income ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"year INTEGER NOT NULL," +
					"month INTEGER NOT NULL," +
					"day INTEGER NOT NULL," +
					"amount INTEGER NOT NULL," +
					"memo TEXT);");
			
			db.execSQL("CREATE TABLE expense ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"year INTEGER NOT NULL," +
					"month INTEGER NOT NULL," +
					"day INTEGER NOT NULL," +
					"amount INTEGER NOT NULL," +
					"memo TEXT);");
			
			db.execSQL("CREATE TABLE assets ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"year INTEGER NOT NULL," +
					"month INTEGER NOT NULL," +
					"day INTEGER NOT NULL," +
					"amount INTEGER NOT NULL," +
					"memo TEXT);");
			
			db.execSQL("CREATE TABLE liability ( " +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"year INTEGER NOT NULL," +
					"month INTEGER NOT NULL," +
					"day INTEGER NOT NULL," +
					"amount INTEGER NOT NULL," +
					"memo TEXT);");
		}
}
