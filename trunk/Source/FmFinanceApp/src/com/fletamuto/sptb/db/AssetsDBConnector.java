package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;

public class AssetsDBConnector extends BaseDBConnector {
	boolean AddItem(AssetsItem item) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("year", item.getCreateYear());
		rowItem.put("month", item.getCreateMonth());
		rowItem.put("day", item.getCreateDay());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("main_category", item.getCategory().getId());
		rowItem.put("sub_category", item.getSubCategory().getId());
		
		db.insert("assets", null, rowItem);
		db.close();
		return true;
	}
	
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("assets, assets_main_category, assets_sub_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id AND assets.sub_category=assets_sub_category._id ");
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(CreateAssetsItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return assetsItems;
	}
	
	public AssetsItem CreateAssetsItem(Cursor c) {
		AssetsItem item = new AssetsItem();
		item.setId(c.getInt(0));
		item.setCreateDate(c.getInt(1), c.getInt(2), c.getInt(3));
		item.setAmount(c.getInt(4));
		item.setTitle(c.getString(5));
		item.setCategory(new Category(c.getInt(9), c.getString(10)));
		item.setSubCategory(new Category(c.getInt(11), c.getString(12)));
		return item;
	}
	
	public ArrayList<Category> getCategory() {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query("assets_main_category", null, null, null, null, null, null);
		
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
		Cursor c = db.query("assets_sub_category", null, "main_id=?", new String[]{String.valueOf(mainCategoryId)}, null, null, null);
		
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
