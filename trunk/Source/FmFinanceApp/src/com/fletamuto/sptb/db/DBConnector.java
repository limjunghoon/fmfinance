
package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;

/**
 * DB와 연동하여 데이타를 관리한다.
 * @author yongbban
 * @version 1.0.0.1
 */
public class DBConnector {
	private BaseDBConnector[] dbConnector = {
			new IncomeDBConnector(), 
			new ExpenseDBConnector(), 
			new AssetsDBConnector(), 
			new LiabilityDBConnector()};
	
	protected BaseDBConnector getDBInstance(int itemType){
		if (itemType >= dbConnector.length) {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + itemType);
			return null;
		}
		return dbConnector[itemType];
	}
	  
	public boolean AddFinanceItem(FinanceItem item) {
		Log.i(DBMgr.DB_TAG, "== DBConnector AddFinanceItem ");
		if (item.getType() >= dbConnector.length) {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + item.getType());
			return false;
		}
		getDBInstance(item.getType()).AddItem(item);
		return true;
	}
	
	public long getTotalAmount(int itemType) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getTotalAmount ");
		return getDBInstance(itemType).getTotalAmount();
	}
	
	public long getTotalAmountDay(int itemType, Calendar calendar) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getTotalAmountDay ");
		return getDBInstance(itemType).getTotalAmountDay(calendar);
	}
	
	public ArrayList<FinanceItem> getFinanceAllItems(int itemType) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getFinanceAllItems ");
		return getDBInstance(itemType).getAllItems();
	}
	
	public ArrayList<Category> getCategory(int itemType) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getCategory ");
		return getDBInstance(itemType).getCategory();
	}

	public ArrayList<Category> getSubCategory(int itemType, int mainCategoryId) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getSubCategory ");
		return getDBInstance(itemType).getSubCategory(mainCategoryId);
	}
	

	public int getItemCount(int itemType, Calendar calendar) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getItemCount ");
		return getDBInstance(itemType).getItemCount(calendar);
	}

	public ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getItems ");
		return getDBInstance(itemType).getItems(calendar);
	}

}


