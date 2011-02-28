package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.OpenUsedItem;
import com.fletamuto.sptb.util.LogTag;

public abstract class BaseFinanceDBConnector extends BaseDBConnector{
	public abstract long addItem(FinanceItem item);
	public abstract long updateItem(FinanceItem item);
	public abstract long updateAmountFinanceItem(int id, long amount);
	public abstract ArrayList<FinanceItem> getAllItems();
	public abstract FinanceItem getItem(int id);
	public abstract long addSubCategory(long mainCategoryID, String name, int imgIndex);
	public abstract long addCategory(Category category);
	public abstract ArrayList<Category> getCategory();
	public abstract ArrayList<Category> getCategory(int extendItem);
	public abstract Category getCategoryFromID(int categoryID);
	public abstract long getTotalAmount();
	public abstract long getTotalAmountDay(Calendar calendar);
	public abstract long getTotalAmountMonth(int year, int month);
	public abstract long getTotalAmountMonth(int categoryID, int year, int month);
	public abstract ArrayList<Long> getTotalAmountMonthInYear(int categorID, int year);
	public abstract long getTotalAmountYear(int year);
	public abstract int getItemCount(Calendar calendar);
	public abstract ArrayList<FinanceItem> getItems(Calendar calendar);
	public abstract ArrayList<FinanceItem> getItems(Calendar startCalendar, Calendar endCalendar);
	public abstract ArrayList<FinanceItem> getItems(int year, int month);
	public abstract ArrayList<FinanceItem> getItemsFromCategoryID(int mainCategoryID, int year, int month);
	public abstract ArrayList<FinanceItem> getItemsFromCategoryID(int mainCategoryID);
	public abstract ArrayList<FinanceItem> getItemsFromSubCategoryID(int subCategoryID, int year, int month);
	public abstract int deleteItem(int id);
	public abstract int deleteCategory(int id);
	public abstract int updateCategory(int id, String name, int imgIndex);
	public abstract long updateRepeat(int itemID, int repeatID);
	public abstract int addOpenUsedItem(OpenUsedItem item);
	public abstract ArrayList<OpenUsedItem> getOpenUsedItems();
	public abstract void updateOpenUsedItem(int id, int itemID, int prioritize);
	public abstract int deleteOpenUsedItem(int id);
	
	public int checkVaildItem(FinanceItem item) {
		if (item == null) {
			Log.e(LogTag.DB, "== null point error == ");
			return DBDef.ValidError.NULL_ERR;
		}
		if (item.getCategory() == null || item.getCategory().getID() == -1) {
			Log.e(LogTag.DB, "== invaild main category error == ");
			return DBDef.ValidError.MAIN_CATEGORY_INVAlID;
		}
		
		Log.i(LogTag.DB, "== check vaild item success == ");
		return DBDef.ValidError.SUCCESS;
	}
	
	public ArrayList<Category> getSubCategory(long mainCategoryId) {
		return null;
	}
	
	public int deleteSubCategoryFromMainID(int mainCategoryID) {
		return 0;
	}

	public int deleteSubCategory(int id) {
		return 0;
	}
	
	public abstract int updateSubCategory(int id, String name, int imgIndex);

	public ArrayList<Long> getTotalTagAmountMonthInYear(int tagID, int year) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
