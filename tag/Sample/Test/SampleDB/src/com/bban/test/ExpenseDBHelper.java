package com.bban.test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ExpenseDBHelper extends SQLiteOpenHelper {

	public ExpenseDBHelper(Context context) {
	//	super(context, "Expense.db", null, 1);
		super(context, "Expense1.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE expense ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"year INTEGER NOT NULL," +
				"month INTEGER NOT NULL," +
				"day INTEGER NOT NULL," +
				"amount INTEGER NOT NULL," +
				"memo TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS expense");
		onCreate(db);
	}

}
