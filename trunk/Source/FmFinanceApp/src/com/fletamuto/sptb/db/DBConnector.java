
package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;

import com.fletamuto.sptb.LogTag;
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
			Log.e(DBMgr.DB_TAG, "== invaild finance item itemType : " + itemType);
			return null;
		}
		return dbConnector[itemType];
	}
	  
	public boolean AddFinanceItem(FinanceItem item) {
		Log.i(DBMgr.DB_TAG, "== DBConnector AddFinanceItem type : " + item.getType());
		if (item.getType() >= dbConnector.length) {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + item.getType());
			return false;
		}
		getDBInstance(item.getType()).AddItem(item);
		return true;
	}
	
	public long getTotalAmount(int itemType) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getTotalAmount type : " + itemType);
		return getDBInstance(itemType).getTotalAmount();
	}
	
	public long getTotalAmountDay(int itemType, Calendar calendar) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getTotalAmountDay type : " + itemType);
		return getDBInstance(itemType).getTotalAmountDay(calendar);
	}
	
	public ArrayList<FinanceItem> getFinanceAllItems(int itemType) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getFinanceAllItems type : " + itemType);
		return getDBInstance(itemType).getAllItems();
	}
	
	public ArrayList<Category> getCategory(int itemType) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getCategory type : " + itemType);
		return getDBInstance(itemType).getCategory();
	}

	public ArrayList<Category> getSubCategory(int itemType, int mainCategoryId) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getSubCategory type : " + itemType);
		return getDBInstance(itemType).getSubCategory(mainCategoryId);
	}
	
	public int getItemCount(int itemType, Calendar calendar) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getItemCount type : " + itemType);
		return getDBInstance(itemType).getItemCount(calendar);
	}

	public ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getItems type : " + itemType);
		return getDBInstance(itemType).getItems(calendar);
	}

	public int deleteItem(int itemType, int id) {
		Log.i(DBMgr.DB_TAG, "== DBConnector deleteItem ");
		int result = getDBInstance(itemType).deleteItem(id);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not delete id : " + id + " type : " + itemType); 
		}
		return result;
	}

}


