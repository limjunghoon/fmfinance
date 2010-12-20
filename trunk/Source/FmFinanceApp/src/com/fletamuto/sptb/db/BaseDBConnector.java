package com.fletamuto.sptb.db;

import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDBConnector {
	public static final int READ_MODE = 0;
	public static final int WRITE_MODE = 1;

	SQLiteDatabase openDatabase(int mode) {
		if (WRITE_MODE == mode) {
			return DBMgr.getWritableDatabase();
		}
		else {
			return DBMgr.getReadableDatabase();
		}
	}
	
	void closeDatabase() {
		DBMgr.closeDatabase();
	}
	
	void LOCK() {
		DBMgr.dbLock();
	}
	
	void UNLOCK() {
		DBMgr.dbUnLock();
	}
	
}
