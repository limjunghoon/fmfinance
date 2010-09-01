package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fletamuto.sptb.data.CardCompenyName;

public class CardItemDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "card_company_name";
	
	public boolean addItem(CardCompenyName cardCompanyName) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", cardCompanyName.getName());
		rowItem.put("institution_id", cardCompanyName.getInstituionID());
		
		db.insert(TABLE_NAME, null, rowItem);
		db.close();
		return true;
	}
	
	public boolean updateItem(CardCompenyName cardCompanyName) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", cardCompanyName.getName());
		rowItem.put("type", cardCompanyName.getInstituionID());
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(cardCompanyName.getID())});
		db.close();
		return true;
	}
	
	public  ArrayList<CardCompenyName> getAllItems() {
		ArrayList<CardCompenyName> cardCompanyNames = new ArrayList<CardCompenyName>();
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
	
	public CardCompenyName getItem(int id) {
		CardCompenyName cardCompanyName = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			cardCompanyName = CreateCompanyNameItem(c);
		}
		c.close();
		db.close();
		return cardCompanyName;
	}
	
	
	public CardCompenyName CreateCompanyNameItem(Cursor c) {
		CardCompenyName cardCompanyName = new CardCompenyName();
		cardCompanyName.setID(c.getInt(0));
		cardCompanyName.setName(c.getString(1));
		cardCompanyName.setInstituionID(c.getInt(2));
		return cardCompanyName;
	}
}
