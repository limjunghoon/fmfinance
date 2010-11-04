package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.util.FinanceDataFormat;

public class RepeatDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "repeat";

	
	public int addItem(Repeat repeat) {
		int insertID = -1;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("type", repeat.getType());
		rowItem.put("day", repeat.getDayofMonth());
		rowItem.put("weekly", repeat.getWeeklyRepeat());
		rowItem.put("term", 1);
		rowItem.put("item_type", repeat.getItemType());
		rowItem.put("origin_item_id", repeat.getItemID());
		rowItem.put("last_apply_date", repeat.getLastApplyDateString());
		
		insertID = (int)db.insert(TABLE_NAME, null, rowItem);
		repeat.setID(insertID);
		db.close();
		return insertID;
	}
	
	public boolean updateItem(Repeat repeat) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("type", repeat.getType());
		rowItem.put("day", repeat.getDayofMonth());
		rowItem.put("weekly", repeat.getWeeklyRepeat());
		rowItem.put("term", 1);
		rowItem.put("item_type", repeat.getItemType());
		rowItem.put("origin_item_id", repeat.getItemID());
		rowItem.put("last_apply_date", repeat.getLastApplyDateString());
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(repeat.getID())});
		db.close();
		return true;
	}
	
	public  ArrayList<Repeat> getAllItems() {
		ArrayList<Repeat> tag = new ArrayList<Repeat>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				tag.add(createRepeat(c));
			} while (c.moveToNext());
		}
		
		c.close();
		db.close();
		
		return tag;
	}
	
	public Repeat getItem(int id) {		
		Repeat tag = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			tag = createRepeat(c);
		}
		c.close();
		db.close();
		return tag;
	}
	
	
	public Repeat createRepeat(Cursor c) {
		Repeat repeat = new Repeat();
		repeat.setID(c.getInt(0));
		
		int type = c.getInt(1);
		if (type == Repeat.MONTHLY) {
			repeat.setMonthlyRepeat(c.getInt(2));
		}
		else if (type == Repeat.WEEKLY) {
			repeat.setWeeklyRepeat(c.getInt(3));
		}
		else {
			return null;
		}
		
		repeat.setItemType(c.getInt(5));
		repeat.setItemID(c.getInt(6));
		
		try {
			repeat.setLastApplyDay(FinanceDataFormat.DATE_FORMAT.parse(c.getString(7)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return repeat;
	}
	
	public int deleteRepeat(int id) {
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete(TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
	
	
}
