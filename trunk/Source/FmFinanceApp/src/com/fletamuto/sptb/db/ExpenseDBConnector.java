package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;

public class ExpenseDBConnector extends BaseDBConnector {
	
	public boolean AddItem(ExpenseItem item) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("year", item.getCreateYear());
		rowItem.put("month", item.getCreateMonth());
		rowItem.put("day", item.getCreateDay());
		rowItem.put("amount", item.getAmount());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getId());
		rowItem.put("sub_category", item.getSubCategory().getId());
		
		db.insert("expense", null, rowItem);
		db.close();
		return true;
	}
	
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id ");
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(CreateExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return expenseItems;
	}
	
	public ExpenseItem CreateExpenseItem(Cursor c) {
		ExpenseItem item = new ExpenseItem();
		item.setId(c.getInt(0));
		item.setCreateDate(c.getInt(1), c.getInt(2), c.getInt(3));
		item.setAmount(c.getInt(4));
		item.setMemo(c.getString(6));
		item.setCategory(new Category(c.getInt(9), c.getString(10)));
		item.setSubCategory(new Category(c.getInt(11), c.getString(12)));
		return item;
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

	public ArrayList<Category> getSubCategory(int mainCategoryId) {
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
}
