
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
	private BaseDBConnector[] mDBConnector = {
			new IncomeDBConnector(), 
			new ExpenseDBConnector(), 
			new AssetsDBConnector(), 
			new LiabilityDBConnector()};
	
	protected BaseDBConnector getDBInstance(int itemType){
		if (itemType < 0|| itemType >= mDBConnector.length) {
			Log.e(DBMgr.DB_TAG, "== invaild finance item itemType : " + itemType);
			return null;
		}
		return mDBConnector[itemType];
	}
	  
	public boolean addFinanceItem(FinanceItem item) {
		Log.i(DBMgr.DB_TAG, "== DBConnector AddFinanceItem type : " + item.getType());
		if (item.getType() >= mDBConnector.length) {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + item.getType());
			return false;
		}
		
		if (item.getCategory().getId() == -1) {
			Log.e(DBMgr.DB_TAG, "== invaild category item ID");
			return false;
		}
		
		return getDBInstance(item.getType()).addItem(item);
	}
	
	public boolean updateFinanceItem(FinanceItem item) {
		Log.i(DBMgr.DB_TAG, "== DBConnector updateFinanceItem type : " + item.getType());
		if (item.getType() >= mDBConnector.length) {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + item.getType());
			return false;
		}
		
		if (item.getId() == -1 || item.getCategory().getId() == -1) {
			Log.e(DBMgr.DB_TAG, "== invaild item ID");
			return false;
		}
		
		return getDBInstance(item.getType()).updateItem(item);
	}
	
	public FinanceItem getItem(int itemType, int id) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getItem type : " + itemType);
		return getDBInstance(itemType).getItem(id);
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
	
	public long addCategory(int itemType, String name) {
		Log.i(DBMgr.DB_TAG, "== DBConnector addCategory type : " + itemType);
		return getDBInstance(itemType).addCategory(name);
	}
	
	public long addSubCategory(int itemType, long mainCategoryID, String name) {
		Log.i(DBMgr.DB_TAG, "== DBConnector addSubCategory type : " + itemType);
		return getDBInstance(itemType).addSubCategory(mainCategoryID, name);
	}
	
	public ArrayList<Category> getCategory(int itemType) {
		Log.i(DBMgr.DB_TAG, "== DBConnector getCategory type : " + itemType);
		return getDBInstance(itemType).getCategory();
	}

	public ArrayList<Category> getSubCategory(int itemType, long mainCategoryId) {
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

	public int deleteCategory(int itemType, int id) {
		Log.i(DBMgr.DB_TAG, "== DBConnector deleteCategory ");
		int result = getDBInstance(itemType).deleteCategory(id);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not delete id : " + id + " type : " + itemType); 
		}
		return result;
	}

	public int deleteSubCategory(int itemType, int id) {
		Log.i(DBMgr.DB_TAG, "== DBConnector deleteSubCategory ");
		int result = getDBInstance(itemType).deleteSubCategory(id);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not delete id : " + id + " type : " + itemType); 
		}
		return result;
	}

	public boolean updateCategory(int itemType, int id, String name) {
		Log.i(DBMgr.DB_TAG, "== DBConnector updateCategory ");
		boolean result = getDBInstance(itemType).updateCategory(id, name);
		if (result == false) {
			Log.e(LogTag.DB, "== do not update id : " + id + " type : " + itemType); 
		}
		return result;
	}
	
	public boolean updateSubCategory(int itemType, int id, String name) {
		Log.i(DBMgr.DB_TAG, "== DBConnector updateSubCategory ");
		boolean result = getDBInstance(itemType).updateSubCategory(id, name);
		if (result == false) {
			Log.e(LogTag.DB, "== do not update id : " + id + " type : " + itemType); 
		}
		return result;
	}
	
}


