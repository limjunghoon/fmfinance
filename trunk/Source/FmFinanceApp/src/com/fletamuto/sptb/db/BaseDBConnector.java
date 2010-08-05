package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.database.sqlite.SQLiteDatabase;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;

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
	
	public abstract boolean addItem(FinanceItem item);
	public abstract boolean updateItem(FinanceItem item);
	public abstract ArrayList<FinanceItem> getAllItems();
	public abstract FinanceItem getItem(int id);
	public abstract ArrayList<Category> getCategory();
	public abstract long getTotalAmount();
	public abstract long getTotalAmountDay(Calendar calendar);
	public abstract int getItemCount(Calendar calendar);
	public abstract ArrayList<FinanceItem> getItems(Calendar calendar);
	public abstract int deleteItem(int id);

	public ArrayList<Category> getSubCategory(int mainCategoryId) {
		return null;
	}



	
}
