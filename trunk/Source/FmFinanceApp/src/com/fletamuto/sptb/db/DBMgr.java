package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.Context;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;

public class DBMgr {
	private static DBMgr instance = null;
	private DBConnector dbConnector = new DBConnector();
	private static FinanceDBHelper DBHelper = null; 
	public static final String DB_TAG = "db_tag"; 
	
	private DBMgr() {
	}
	
	public static DBMgr getInstance() {
		if (instance == null) {
			instance = new DBMgr();
		}
		return instance;
	}
	
	public void initialize(Context context) {
		DBHelper = new FinanceDBHelper(context);
		DBHelper.getWritableDatabase();
	}
	
	public boolean addFinanceItem(FinanceItem item) {
		dbConnector.AddFinanceItem(item);
		return true;
	}
	
	public ArrayList<FinanceItem> getAllItems(int itemType) {
		return dbConnector.getFinanceAllItems(itemType);
	}
	
	public FinanceDBHelper getDBHelper() {
		return DBHelper;
	}
	
	public ArrayList<Category> getCategory(int itemType) {
		return dbConnector.getCategory(itemType);
	}
	
	public ArrayList<Category> getSubCategory(int itemType, int mainCategoryId) {
		return dbConnector.getSubCategory(itemType, mainCategoryId);
	}
}
