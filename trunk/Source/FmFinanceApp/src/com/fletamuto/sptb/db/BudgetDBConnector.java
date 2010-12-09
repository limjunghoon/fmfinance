package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fletamuto.sptb.data.BudgetItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;

/**
 * 지출관련 DB를 관리
 * @author yongbban
 * @version 1.0.0.0
 */
public class BudgetDBConnector extends BaseDBConnector {
	private static final String MAIN_TABLE_NAME = "budget";
	private static final String CATEGORY_TABLE_NAME = "budget_main_category";
	
	public long addItem(BudgetItem item) {
		if (item.getID() != -1) return -1;
		if (item.getExpenseCategory() == null) {
			return addMainBudget(item);
		}
		else {
			return addCategoryBudget(item);
		}
	}

	private long addMainBudget(BudgetItem item) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("total_amount", item.getAmount());
		rowItem.put("year", item.getYear());
		rowItem.put("month", item.getMonth());
		
		int insertID = (int)db.insert(MAIN_TABLE_NAME, null, rowItem);
		item.setID(insertID);
		db.close();
		return insertID;
	}
	
	private long addCategoryBudget(BudgetItem item) {
		if (item.getExpenseCategory().getID() == -1) {
			return -1;
		}
		
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("amount", item.getAmount());
		rowItem.put("year", item.getYear());
		rowItem.put("month", item.getMonth());
		rowItem.put("main_category", item.getExpenseCategory().getID());
		
		int insertID = (int)db.insert(CATEGORY_TABLE_NAME, null, rowItem);
		item.setID(insertID);
		db.close();
		return insertID;
	}
	
	public ArrayList<BudgetItem> getItems(final int year, final int month) {
		ArrayList<BudgetItem> budgetItems = new ArrayList<BudgetItem>();
		//BudgetItem budgetTotalItem = new BudgetItem(year, month);
		BudgetItem budgetTotalItem = getMainBudget(year, month);
		budgetItems.add(budgetTotalItem);
		
		ArrayList<Category> expenseCategories = DBMgr.getCategory(ExpenseItem.TYPE);
		int categorySize = expenseCategories.size();
		for (int categoryIndex = 0; categoryIndex < categorySize; categoryIndex++) {
			Category category = expenseCategories.get(categoryIndex);
			BudgetItem budgetItem = getCategoryBudget(year, month, category.getID());
			budgetItem.setExpenseCategory(category);
			budgetItem.setExpenseAmountMonth(DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, category.getID(), year, month));
			budgetItems.add(budgetItem);
			
		}
		return budgetItems;
	}
	
	BudgetItem getMainBudget(final int year, final int month) {
		BudgetItem budget = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(MAIN_TABLE_NAME, null, "month=? AND year=?", new String[]{String.valueOf(month), String.valueOf(year)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			budget = CreateBudgetItem(c);
		}
		else {
			budget = new BudgetItem(year, month);
		}
		c.close();
		db.close();
		
		budget.setExpenseAmountMonth(DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, year, month));
		return budget;
	}
	
	BudgetItem getCategoryBudget(final int year, final int month, final int categoryID) {
		BudgetItem budget = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(CATEGORY_TABLE_NAME, null, "month=? AND year=? AND main_category=?", new String[]{String.valueOf(month), String.valueOf(year), String.valueOf(categoryID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			budget = CreateCategoryBudgetItem(c);
		}
		else {
			budget = new BudgetItem(year, month);
		}
		c.close();
		db.close();
		
//		budget.setExpenseAmountMonth(DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, year, month));
		return budget;
	}

	private BudgetItem CreateCategoryBudgetItem(Cursor c) {
		BudgetItem budget = new BudgetItem(c.getInt(2), c.getInt(3));
		budget.setID(c.getInt(0));
		budget.setAmount(c.getLong(1));
		return budget;
	}


	private BudgetItem CreateBudgetItem(Cursor c) {
		BudgetItem budget = new BudgetItem(c.getInt(2), c.getInt(3));
		budget.setID(c.getInt(0));
		budget.setAmount(c.getLong(1));
		return budget;
	}

	public long updateItem(BudgetItem item) {
		if (item.getExpenseCategory() == null) {
			return updateMainBudget(item);
		}
		else {
			return updateCategoryBudget(item);
		}
	}

	private long updateMainBudget(BudgetItem item) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("total_amount", item.getAmount());
		rowItem.put("year", item.getYear());
		rowItem.put("month", item.getMonth());
		
		long ret = db.update(MAIN_TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(item.getID())});
		db.close();
		return ret;
	}
	
	private long updateCategoryBudget(BudgetItem item) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("amount", item.getAmount());
		rowItem.put("year", item.getYear());
		rowItem.put("month", item.getMonth());
		rowItem.put("main_category", item.getExpenseCategory().getID());
		
		long ret = db.update(CATEGORY_TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(item.getID())});
		db.close();
		return ret;
	}
	

	

}
