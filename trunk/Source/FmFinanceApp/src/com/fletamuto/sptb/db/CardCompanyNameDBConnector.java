package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fletamuto.sptb.data.CardCompanyName;

public class CardCompanyNameDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "card_company";
	
	public int addItem(CardCompanyName cardCompanyName) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("card_name", cardCompanyName.getName());
		rowItem.put("finance_company_id", cardCompanyName.getCompanyID());
		rowItem.put("prioritize", 1);
		rowItem.put("image_index", 1);
		
		int ret = (int)db.insert(TABLE_NAME, null, rowItem);
		db.close();
		return ret;
	}
	
	public boolean updateItem(CardCompanyName cardCompanyName) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("card_name", cardCompanyName.getName());
		rowItem.put("finance_company_id", cardCompanyName.getCompanyID());
		rowItem.put("prioritize", 1);
		rowItem.put("image_index", 1);
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(cardCompanyName.getID())});
		db.close();
		return true;
	}
	
	public  ArrayList<CardCompanyName> getAllItems() {
		ArrayList<CardCompanyName> cardCompanyNames = new ArrayList<CardCompanyName>();
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				cardCompanyNames.add(CreateCompanyNameItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return cardCompanyNames;
	}
	
	public CardCompanyName getItem(int id) {
		CardCompanyName cardCompanyName = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			cardCompanyName = CreateCompanyNameItem(c);
		}
		c.close();
		db.close();
		return cardCompanyName;
	}
	
	
	public CardCompanyName CreateCompanyNameItem(Cursor c) {
		CardCompanyName cardCompanyName = new CardCompanyName();
		cardCompanyName.setID(c.getInt(0));
		cardCompanyName.setName(c.getString(1));
		cardCompanyName.setCompanyID(c.getInt(2));
		return cardCompanyName;
	}
	
	public int deleteItem(int id) {
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete(TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
}
