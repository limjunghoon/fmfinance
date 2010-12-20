package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fletamuto.sptb.data.ExpenseTag;

public class TagDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "expense_tag";

	
	public int addItem(ExpenseTag tag) {
		int insertID = -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", tag.getName());
		rowItem.put("prioritize", 1);
		rowItem.put("image_index", 1);
		
		insertID = (int)db.insert(TABLE_NAME, null, rowItem);
		tag.setID(insertID);
		closeDatabase();
		return insertID;
	}
	
	public boolean updateItem(ExpenseTag tag) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", tag.getName());
		rowItem.put("prioritize", 1);
		rowItem.put("image_index", 1);
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(tag.getID())});
		closeDatabase();
		return true;
	}
	
	public  ArrayList<ExpenseTag> getAllItems() {
		ArrayList<ExpenseTag> tag = new ArrayList<ExpenseTag>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			// 더미 테그 스킵
			c.moveToNext();
			
			do {
				tag.add(createExpenseTag(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		
		return tag;
	}
	
	public ExpenseTag getItem(int id) {
		if (id == ExpenseTag.NONE_ID) return null;
		
		ExpenseTag tag = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query(TABLE_NAME, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			tag = createExpenseTag(c);
		}
		c.close();
		closeDatabase();
		return tag;
	}
	
	
	public ExpenseTag createExpenseTag(Cursor c) {
		ExpenseTag tag = new ExpenseTag();
		tag.setID(c.getInt(0));
		tag.setName(c.getString(1));
		tag.setPrioritize(c.getInt(2));
		tag.setImageIndex(c.getInt(3));

		return tag;
	}
	
	public int deleteItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete(TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
}
