package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;

public class ExpenseDBConnector extends BaseDBConnector {
	
	public boolean addItem(FinanceItem financeItem) {
		ExpenseItem item = (ExpenseItem)financeItem;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("year", item.getCreateYear());
		rowItem.put("month", item.getCreateMonth());
		rowItem.put("day", item.getCreateDay());
		rowItem.put("amount", item.getAmount());
		rowItem.put("memo", item.getMemo());
		if (item.getCategory() != null) {
			rowItem.put("main_category", item.getCategory().getId());
		}
		if (item.getSubCategory() != null) {
			rowItem.put("sub_category", item.getSubCategory().getId());
		}
		
		db.insert("expense", null, rowItem);
		db.close();
		return true;
	}
	
	@Override
	public boolean updateItem(FinanceItem financeItem) {
		ExpenseItem item = (ExpenseItem)financeItem;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("year", item.getCreateYear());
		rowItem.put("month", item.getCreateMonth());
		rowItem.put("day", item.getCreateDay());
		rowItem.put("amount", item.getAmount());
		rowItem.put("memo", item.getMemo());
		if (item.getCategory() != null) {
			rowItem.put("main_category", item.getCategory().getId());
		}
		if (item.getSubCategory() != null) {
			rowItem.put("sub_category", item.getSubCategory().getId());
		}
		
		db.update("expense", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getId())});
		db.close();
		return true;
	}
	
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id ");
		Cursor c = queryBilder.query(db, null, null, null, null, null, "year, month, day DESC");
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(CreateExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return expenseItems;
	}
	
	@Override
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)), 
				String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id ");
		Cursor c = queryBilder.query(db, null, "year=? AND month=? AND day=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(CreateExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return expenseItems;
	}
	
	@Override
	public FinanceItem getItem(int id) {
		FinanceItem item = null;
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(id)};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id ");
		Cursor c = queryBilder.query(db, null, "expense._id=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
				item = CreateExpenseItem(c);
		}
		c.close();
		db.close();
		return item;
	}
	
	public ExpenseItem CreateExpenseItem(Cursor c) {
		ExpenseItem item = new ExpenseItem();
		item.setId(c.getInt(0));
		item.setCreateDate(c.getInt(1), c.getInt(2), c.getInt(3));
		item.setAmount(c.getLong(4));
		item.setMemo(c.getString(6));
		item.setCategory(c.getInt(9), c.getString(10));
		item.setSubCategory(c.getInt(11), c.getString(12));
		return item;
	}
	
	public long addCategory(String name) {
		long ret = -1;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		
		ret = db.insert("expense_main_category", null, rowItem);
		db.close();
		return ret;
	}
	
	public long addSubCategory(long mainCategoryID, String name) {
		long ret = -1;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		rowItem.put("main_id", mainCategoryID);
		
		ret = db.insert("expense_sub_category", null, rowItem);
		db.close();
		return ret;
	}
	
	public ArrayList<Category> getCategory() {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query("expense_main_category", null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				Category item = new Category(c.getInt(0), c.getString(1));
				category.add(item);
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		
		return category;
	}

	public ArrayList<Category> getSubCategory(long mainCategoryId) {
		ArrayList<Category> subCategory = new ArrayList<Category>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query("expense_sub_category", null, "main_id=?", new String[]{String.valueOf(mainCategoryId)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				Category item = new Category(c.getInt(0), c.getString(1));
				subCategory.add(item);
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return subCategory;
	}
	
	@Override
	public long getTotalAmount() {
		long amount = 0L;
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT SUM(amount) FROM expense";
		Cursor c = db.rawQuery(query, null);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		db.close();
		return amount;
	}
	
	@Override
	public long getTotalAmountDay(Calendar calendar) {
		long amount = 0L;
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)), 
				String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT SUM(amount) FROM expense WHERE year=? AND month=? AND day=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		db.close();
		return amount;
	}

	@Override
	public int getItemCount(Calendar calendar) {
		int count = 0;
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)), 
				String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT COUNT(*) FROM expense WHERE year=? AND month=? AND day=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			count = c.getInt(0);
		}
		c.close();
		db.close();
		return count;
	}

	@Override
	public int deleteItem(int id) {
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete("expense", "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}

	
}
