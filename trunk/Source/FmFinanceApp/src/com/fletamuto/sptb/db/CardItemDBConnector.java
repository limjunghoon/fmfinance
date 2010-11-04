package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.fletamuto.sptb.LogTag;
import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.data.CardItem;

public class CardItemDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "card";
	
	public int addItem(CardItem card) {
		if (checkCardVaildItem(card) != DBDef.ValidError.SUCCESS) return -1;
		
		int newID = -1;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", card.getName());
		rowItem.put("number", card.getNumber());
		rowItem.put("type", card.getType());
		rowItem.put("account_id", card.getAccount().getID());
		rowItem.put("company_name_id", card.getCompenyName().getID());
		rowItem.put("settlement_day", card.getSettlementDay());
		rowItem.put("memo", card.getMemo());
		rowItem.put("maxiup_balance", card.getBalance());
		
		newID = (int)db.insert(TABLE_NAME, null, rowItem);
		card.setID(newID);
		db.close();
		return newID;
	}
	
	

	public boolean updateItem(CardItem card) {
		if (checkCardVaildItem(card) != DBDef.ValidError.SUCCESS) return false;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("type", card.getType());
		rowItem.put("number", card.getNumber());
		rowItem.put("company_name_id", card.getCompenyName().getID());
		rowItem.put("company_name_id", card.getCompenyName().getID());
		rowItem.put("name", card.getName());
		rowItem.put("settlement_day", card.getSettlementDay());
		rowItem.put("memo", card.getMemo());
		rowItem.put("balance", card.getBalance());
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(card.getID())});
		db.close();
		return true;
	}
	
	public ArrayList<CardItem> getAllItems() {
		ArrayList<CardItem> card = new ArrayList<CardItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("card, card_company");
		queryBilder.appendWhere("card.company_name_id=card_company._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				card.add(CreateCardItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return card;
	}
	
	
	public  ArrayList<CardItem> getAllItems(int type) {
		ArrayList<CardItem> card = new ArrayList<CardItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("card, card_company");
		queryBilder.appendWhere("card.company_name_id=card_company._id");
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
		
		queryBilder.setTables("card, card_company");
		queryBilder.appendWhere("card.company_name_id=card_company._id");
		Cursor c = queryBilder.query(db, null, "card._id=?", new String[] {String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			card = CreateCardItem(c);
		}
		c.close();
		db.close();
		return card;
	}
	
	public int deleteItem(int id) {
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete(TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
	
	
	public CardItem CreateCardItem(Cursor c) {
		
		CardItem card = new CardItem(c.getInt(3));
		
		card.setID(c.getInt(0));
		card.setName(c.getString(1));
		card.setNumber(c.getString(2));
		card.getAccount().setID(c.getInt(4));
		card.setSettlementDay(c.getInt(6));
//		card.setStartSettlementDay(c.getInt(7));
//		card.setStartSettlementMonth(c.getInt(8));
//		card.setEndSettlementDay(c.getInt(9));
//		card.setEndSettlementMonth(c.getInt(10));
		card.setMemo(c.getString(9));
		card.setBalance(c.getInt(10));
		
		CardCompanyName compenyName = new CardCompanyName();
		compenyName.setID(c.getInt(11));
		compenyName.setName(c.getString(12));
		compenyName.setCompanyID(c.getInt(13));
		card.setCompenyName(compenyName);
		
		return card;
		
	}
	
	private int checkCardVaildItem(CardItem card) {
		if (card.getCompenyName() == null || card.getCompenyName().getID() == -1) {
			Log.e(LogTag.DB, ":: CARD ITEM INVAILD");
			return DBDef.ValidError.CARD_ITEM_INVAlID;
		}
		return DBDef.ValidError.SUCCESS; 
	}


}
