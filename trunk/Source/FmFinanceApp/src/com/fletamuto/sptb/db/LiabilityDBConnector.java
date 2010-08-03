package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;

public class LiabilityDBConnector extends BaseDBConnector {
	public boolean AddItem(FinanceItem financeItem) {
		LiabilityItem item = (LiabilityItem)financeItem;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("year", item.getCreateYear());
		rowItem.put("month", item.getCreateMonth());
		rowItem.put("day", item.getCreateDay());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		if (item.getCategory() != null) {
			rowItem.put("main_category", item.getCategory().getId());
		}
		
		db.insert("liability", null, rowItem);
		db.close();
		return true;
	}
	
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> LiabilityItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere("liability.main_category=liability_main_category._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				LiabilityItems.add(CreateLiabilityItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return LiabilityItems;
	}
	
	public LiabilityItem CreateLiabilityItem(Cursor c) {
		LiabilityItem item = new LiabilityItem();
		item.setId(c.getInt(0));
		item.setCreateDate(c.getInt(1), c.getInt(2), c.getInt(3));
		item.setAmount(c.getLong(4));
		item.setTitle(c.getString(5));
		item.setCategory(new Category(c.getInt(8), c.getString(9)));
		
		return item;
	}
	
	public ArrayList<Category> getCategory() {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query("liability_main_category", null, null, null, null, null, null);
		
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
	
	@Override
	public long getTotalAmount() {
		long amount = 0L;
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT SUM(amount) FROM liability";
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
		String query = "SELECT SUM(amount) FROM liability WHERE year=? AND month=? AND day=?";
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
		String query = "SELECT COUNT(*) FROM liability WHERE year=? AND month=? AND day=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			count = c.getInt(0);
		}
		c.close();
		db.close();
		return count;
	}

	@Override
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		// TODO Auto-generated method stub
		return null;
	}
}
