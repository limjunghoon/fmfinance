package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;

public class LiabilityDBConnector extends BaseDBConnector {
	boolean AddItem(LiabilityItem item) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("year", item.getCreateYear());
		rowItem.put("month", item.getCreateMonth());
		rowItem.put("day", item.getCreateDay());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("main_category", item.getCategory().getId());
		
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
		item.setAmount(c.getInt(4));
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
}
