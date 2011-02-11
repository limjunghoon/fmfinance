package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.CardPayment;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.TransferItem;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

public class CardItemDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "card";
	
	public int addItem(CardItem card) {
		if (checkCardVaildItem(card) != DBDef.ValidError.SUCCESS) return -1;
		
		int newID = -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", card.getName());
		rowItem.put("number", card.getNumber());
		rowItem.put("type", card.getType());
		rowItem.put("account_id", card.getAccount().getID());
		rowItem.put("company_name_id", card.getCompenyName().getID());
		rowItem.put("settlement_day", card.getSettlementDay());
		rowItem.put("biling_period_day", card.getBillingPeriodDay());
		rowItem.put("biling_period_month", card.getBillingPeriodMonth());
		rowItem.put("memo", card.getMemo());
		rowItem.put("maxiup_balance", card.getBalance());
		
		newID = (int)db.insert(TABLE_NAME, null, rowItem);
		card.setID(newID);
		closeDatabase();
		return newID;
	}
	
	

	public boolean updateItem(CardItem card) {
		if (checkCardVaildItem(card) != DBDef.ValidError.SUCCESS) return false;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", card.getName());
		rowItem.put("number", card.getNumber());
		rowItem.put("type", card.getType());
		rowItem.put("account_id", card.getAccount().getID());
		rowItem.put("company_name_id", card.getCompenyName().getID());
		rowItem.put("settlement_day", card.getSettlementDay());
		rowItem.put("biling_period_day", card.getBillingPeriodDay());
		rowItem.put("biling_period_month", card.getBillingPeriodMonth());
		rowItem.put("memo", card.getMemo());
		rowItem.put("maxiup_balance", card.getBalance());
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(card.getID())});
		closeDatabase();
		return true;
	}
	
	public ArrayList<CardItem> getAllItems() {
		ArrayList<CardItem> card = new ArrayList<CardItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
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
		closeDatabase();
		return card;
	}
	
	
	public  ArrayList<CardItem> getAllItems(int type) {
		ArrayList<CardItem> card = new ArrayList<CardItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
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
		closeDatabase();
		return card;
	}
	
	public CardItem getItem(int id) {
		CardItem card = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("card, card_company");
		queryBilder.appendWhere("card.company_name_id=card_company._id");
		Cursor c = queryBilder.query(db, null, "card._id=?", new String[] {String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			card = CreateCardItem(c);
		}
		c.close();
		closeDatabase();
		return card;
	}
	
	public int deleteItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete(TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	
	public CardItem CreateCardItem(Cursor c) {
		
		CardItem card = new CardItem(c.getInt(3));
		
		card.setID(c.getInt(0));
		card.setName(c.getString(1));
		card.setNumber(c.getString(2));
		card.getAccount().setID(c.getInt(4));
		card.setSettlementDay(c.getInt(6));
		card.setBillingPeriodDay(c.getInt(7));
		card.setBillingPeriodMonth(c.getInt(8));
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


	public int addCardPaymentItem(CardPayment payment) {
		
		int newID = -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("payment_date", payment.getPaymentDateString());
		rowItem.put("card_id", payment.getCardId());
		rowItem.put("payment_amount", payment.getPaymentAmount());
		rowItem.put("remain_amount", payment.getRemainAmount());
		rowItem.put("state", payment.getState());
		
		newID = (int)db.insert("card_payment", null, rowItem);
		payment.setID(newID);
		closeDatabase();
		return newID;
	}
	
	public boolean updateCardPaymentItem(CardPayment payment) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("payment_date", payment.getPaymentDateString());
		rowItem.put("card_id", payment.getCardId());
		rowItem.put("payment_amount", payment.getPaymentAmount());
		rowItem.put("remain_amount", payment.getRemainAmount());
		rowItem.put("state", payment.getState());
		
		db.update("card_payment", rowItem, "_id=?", new String[] {String.valueOf(payment.getID())});
		closeDatabase();
		return true;
	}
	
	public CardPayment getCardPaymentLastItem(int cardId) {
		CardPayment cardPayment = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("card_payment", null, "card_payment.card_id=?", new String[]{String.valueOf(cardId)}, null, null, "payment_date DESC");
		
		if (c.moveToFirst() != false) {
			cardPayment = createCardPaymentItem(c);
		}
		c.close();
		closeDatabase();
		return cardPayment;
	}
	
	public CardPayment createCardPaymentItem(Cursor c) {
		
		CardPayment cardPayment = new CardPayment();
		
		cardPayment.setID(c.getInt(0));
		
		try {
			cardPayment.setPaymentDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		cardPayment.setCardId(c.getInt(2));
		cardPayment.setPaymentAmount(c.getLong(3));
		cardPayment.setRemainAmount(c.getLong(4));
		cardPayment.setState(c.getInt(5));
		
		return cardPayment;
	}



	public int deleteCardPaymentItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete("card_payment", "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	
	public ArrayList<CardPayment> getCardPaymentItems(int accountID, int year, int month) {
		
		ArrayList<CardPayment> cardPaymentItems = new ArrayList<CardPayment>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		queryBilder.setTables("card_payment, card, account");
		queryBilder.appendWhere("card_payment.card_id=card._id AND card.account_id=account._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', card_payment.payment_date)=? AND card.account_id=?", new String [] {String.format("%d-%02d", year, month), String.valueOf(accountID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				cardPaymentItems.add(createCardPaymentItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		
		
		return cardPaymentItems;
	}
}
