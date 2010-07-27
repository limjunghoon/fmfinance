package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
		
		db.insert("expense", null, rowItem);
		db.close();
		return true;
	}
	
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query("expense", null, null, null, null, null, null);
		c.moveToFirst();
		
		for (int index = 0; c.moveToNext(); index++) {
			ExpenseItem item = new ExpenseItem();
			item.setId(c.getInt(0));
			item.setCreateDate(c.getInt(1), c.getInt(2), c.getInt(3));
			item.setAmount(c.getInt(4));
			item.setMemo(c.getString(5));
			expenseItems.add(item);
		}
		db.close();
		return expenseItems;
	}
}
