package com.fletamuto.sptb.db;

import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDBConnector {
	FinanceDBHelper getDBHelper() {
		return DBMgr.getInstance().getDBHelper();
	}
	
	SQLiteDatabase getWritableDatabase() {
		return getDBHelper().getWritableDatabase();
	}
	
	SQLiteDatabase getReadableDatabase() {
		return getDBHelper().getReadableDatabase();
	}
	
}
