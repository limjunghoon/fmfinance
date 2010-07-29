
package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.util.Log;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.LiabilityItem;


public class DBConnector {
	private IncomeDBConnector incomeDB = new IncomeDBConnector();
	private ExpenseDBConnector expenseDB = new ExpenseDBConnector();
	private AssetsDBConnector assetsDB = new AssetsDBConnector();
	private LiabilityDBConnector liabilityDB = new LiabilityDBConnector();
	  
	public boolean AddFinanceItem(FinanceItem item) {
		if (item.getType() == IncomeItem.TYPE) {
			incomeDB.AddItem((IncomeItem)item);
		}
		else if (item.getType() == ExpenseItem.TYPE) {
			expenseDB.AddItem((ExpenseItem)item);
		}
		else if (item.getType() == AssetsItem.TYPE) {
			assetsDB.AddItem((AssetsItem)item);
		}
		else if (item.getType() == LiabilityItem.TYPE) {
			liabilityDB.AddItem((LiabilityItem)item);
		}
		else {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + item.getType());
			return false;
		}

		return true;
	}
	
	public ArrayList<FinanceItem> getFinanceAllItems(int itemType) {
		if (itemType == IncomeItem.TYPE) {
			return incomeDB.getAllItems();
		}
		else if (itemType == ExpenseItem.TYPE) {
			return expenseDB.getAllItems();
		}
		else if (itemType == AssetsItem.TYPE) {
			return assetsDB.getAllItems();
		}
		else if (itemType == LiabilityItem.TYPE) {
			return liabilityDB.getAllItems();
		}
		else {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + itemType);
			return null;
		}
	}
	
	public ArrayList<Category> getCategory(int itemType) {
		if (itemType == IncomeItem.TYPE) {
			return incomeDB.getCategory();
		}
		else if (itemType == ExpenseItem.TYPE) {
			return expenseDB.getCategory();
		}
		else if (itemType == AssetsItem.TYPE) {
			return assetsDB.getCategory();
		}
		else if (itemType == LiabilityItem.TYPE) {
			return liabilityDB.getCategory();
		}
		else {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + itemType);
			return null;
		}
	}

	public ArrayList<Category> getSubCategory(int itemType, int mainCategoryId) {
		if (itemType == IncomeItem.TYPE) {
			return null;
		}
		else if (itemType == ExpenseItem.TYPE) {
			return expenseDB.getSubCategory(mainCategoryId);
		}
		else if (itemType == AssetsItem.TYPE) {
			return assetsDB.getSubCategory(mainCategoryId);
		}
		else if (itemType == LiabilityItem.TYPE) {
			return null;
		}
		else {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + itemType);
			return null;
		}
	}

}


