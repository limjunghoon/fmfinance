package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.CardCompenyName;
import com.fletamuto.sptb.data.CardItem;

public class CardItemDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "card";
	
	public int addItem(CardItem card) {
		int newID = -1;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("number", card.getNumber());
		rowItem.put("account_id", card.getAccountID());
		rowItem.put("company_name_id", card.getCompenyName().getID());
		rowItem.put("name", card.getName());
		rowItem.put("settlement_day", card.getSettlementDay());
		rowItem.put("start_settlement_day", card.getStartSettlementDay());
		rowItem.put("start_settlement_month", card.getStartSettlementMonth());
		rowItem.put("end_settlement_day", card.getEndSettlementDay());
		rowItem.put("end_settlement_month", card.getEndSettlementMonth());
		rowItem.put("memo", card.getMemo());
		rowItem.put("type", card.getType());
		
		newID = (int)db.insert(TABLE_NAME, null, rowItem);
		card.setID(newID);
		db.close();
		return newID;
	}
	
	public boolean updateItem(CardItem card) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("number", card.getNumber());
		rowItem.put("account_id", card.getAccountID());
		rowItem.put("company_name_id", card.getCompenyName().getID());
		rowItem.put("name", card.getName());
		rowItem.put("settlement_day", card.getSettlementDay());
		rowItem.put("start_settlement_day", card.getStartSettlementDay());
		rowItem.put("start_settlement_month", card.getStartSettlementMonth());
		rowItem.put("end_settlement_day", card.getEndSettlementDay());
		rowItem.put("end_settlement_month", card.getEndSettlementMonth());
		rowItem.put("memo", card.getMemo());
		rowItem.put("type", card.getType());
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(card.getID())});
		db.close();
		return true;
	}
	
	public  ArrayList<CardItem> getAllItems(int type) {
		ArrayList<CardItem> card = new ArrayList<CardItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("card, card_company_name");
		queryBilder.appendWhere("card.company_name_id=card_company_name._id");
		Cursor c = queryBilder.query(db, null, "card.type=?", new String[] {String.valueOf(type)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				card.add(CreateCardItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return card;
	}
	
	public CardItem getItem(int id) {
		CardItem card = null;
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("card, card_company_name");
		queryBilder.appendWhere("card.company_name_id=card_company_name._id");
		Cursor c = queryBilder.query(db, null, "card._id=?", new String[] {String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			card = CreateCardItem(c);
		}
		c.close();
		db.close();
		return card;
	}
	
	
	public CardItem CreateCardItem(Cursor c) {
		
		CardItem card = new CardItem(c.getInt(11));
		
		card.setID(c.getInt(0));
		card.setNumber(c.getString(1));
		card.setAccountID(c.getInt(2));
		card.setName(c.getString(4));
		card.setSettlementDay(c.getInt(5));
		card.setStartSettlementDay(c.getInt(6));
		card.setStartSettlementMonth(c.getInt(7));
		card.setEndSettlementDay(c.getInt(8));
		card.setEndSettlementMonth(c.getInt(9));
		card.setMemo(c.getString(10));
		
		CardCompenyName compenyName = new CardCompenyName();
		compenyName.setID(c.getInt(12));
		compenyName.setName(c.getString(13));
		compenyName.setInstituionID(c.getInt(14));
		card.setCompenyName(compenyName);
		
		return card;
		
	}
}
