package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.data.ExpenseSMS;
import com.fletamuto.sptb.data.SMSParseItem;
import com.fletamuto.sptb.data.TransferItem;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

public class SMSDBConnector extends BaseDBConnector {
	private static final String EXPENSE_SMS_TABLE_NAME = "expense_sms";
	private static final String SMS_PARSE_DATA_TABLE_NAME = "sms_parse_data";
	
	public int addItem(ExpenseSMS expenseSMS) {
		if (checkExpenseSMSVaildItem(expenseSMS) != DBDef.ValidError.SUCCESS) return -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", expenseSMS.getCreateDateString());
		rowItem.put("message", expenseSMS.getMessage());
		rowItem.put("done", expenseSMS.isDone() ? 1 : 0);
		rowItem.put("number", expenseSMS.getNmuber());
		
		int insertID = (int)db.insert(EXPENSE_SMS_TABLE_NAME, null, rowItem);
		expenseSMS.setID(insertID);
		closeDatabase();
		return insertID;
	}

	public boolean updateItem(ExpenseSMS expenseSMS) {
		if (checkExpenseSMSVaildItem(expenseSMS) != DBDef.ValidError.SUCCESS) return false;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", expenseSMS.getCreateDateString());
		rowItem.put("message", expenseSMS.getMessage());
		rowItem.put("done", expenseSMS.isDone() ? 1 : 0);
		rowItem.put("number", expenseSMS.getNmuber());
		
		db.update(EXPENSE_SMS_TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(expenseSMS.getID())});
		closeDatabase();
		return true;
	}
	
	public  ArrayList<ExpenseSMS> getAllItems() {
		ArrayList<ExpenseSMS> expenseSMSItems = new ArrayList<ExpenseSMS>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query(EXPENSE_SMS_TABLE_NAME, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseSMSItems.add(createExpenseSMSItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseSMSItems;
	}
	
	public ExpenseSMS getItem(int id) {
		ExpenseSMS expenseSMS = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query(EXPENSE_SMS_TABLE_NAME, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			expenseSMS = createExpenseSMSItem(c);
		}
		
		c.close();
		closeDatabase();
		return expenseSMS;
	}
	
	
	public ExpenseSMS createExpenseSMSItem(Cursor c) {
		ExpenseSMS expenseSMS = new ExpenseSMS();
		expenseSMS.setID(c.getInt(0));
		
		try {
			expenseSMS.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		expenseSMS.setMessage(c.getString(2));
		expenseSMS.setDone((c.getInt(3) == 0) ? false : true );
		expenseSMS.setNmuber(c.getString(4));
		
		return expenseSMS;
	}
	
	public int deleteItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete(EXPENSE_SMS_TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	private int checkExpenseSMSVaildItem(ExpenseSMS expenseSMS) {
		if (expenseSMS.getNmuber().length() == 0) {
			Log.e(LogTag.DB, ":: ExpenseSMS ITEM INVAILD");
			return DBDef.ValidError.SMS_ITEM_INVAlID;
		}
		
		return DBDef.ValidError.SUCCESS; 
	}
	
	public int addParseItem(SMSParseItem parse) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("type_id", parse.getTypeId());
		rowItem.put("card_id", parse.getCardId());
		rowItem.put("amount_row", parse.getAmountRow());
		rowItem.put("amount_start_position", parse.getAmountStartPosition());
		rowItem.put("amount_end_text", parse.getAmountEndText());
		rowItem.put("installment_row", parse.getInstallmentRow());
		rowItem.put("installment_start_position", parse.getInstallmentStartPosition());
		rowItem.put("installment_end_text", parse.getInstallmentEndText());
		rowItem.put("shop_row", parse.getShopRow());
		rowItem.put("shop_start_position", parse.getShopStartPosition());
		rowItem.put("shop_end_text", parse.getShopEndText());
		rowItem.put("parse_source", parse.getParseSource());
		
		int insertID = (int)db.insert(SMS_PARSE_DATA_TABLE_NAME, null, rowItem);
		parse.setID(insertID);
		closeDatabase();
		return insertID;
	}

	public boolean updateParseSMSItem(SMSParseItem parse) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("type_id", parse.getTypeId());
		rowItem.put("card_id", parse.getCardId());
		rowItem.put("amount_row", parse.getAmountRow());
		rowItem.put("amount_start_position", parse.getAmountStartPosition());
		rowItem.put("amount_end_text", parse.getAmountEndText());
		rowItem.put("installment_row", parse.getInstallmentRow());
		rowItem.put("installment_start_position", parse.getInstallmentStartPosition());
		rowItem.put("installment_end_text", parse.getInstallmentEndText());
		rowItem.put("shop_row", parse.getShopRow());
		rowItem.put("shop_start_position", parse.getShopStartPosition());
		rowItem.put("shop_end_text", parse.getShopEndText());
		rowItem.put("parse_source", parse.getParseSource());
		
		db.update(SMS_PARSE_DATA_TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(parse.getID())});
		closeDatabase();
		return true;
	}
	
	public  ArrayList<SMSParseItem> getAllParseSMSItems() {
		ArrayList<SMSParseItem> parse = new ArrayList<SMSParseItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query(SMS_PARSE_DATA_TABLE_NAME, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				parse.add(createParseSMSItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return parse;
	}
	
	public SMSParseItem getParseSMSItem(int id) {
		SMSParseItem expenseSMS = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query(SMS_PARSE_DATA_TABLE_NAME, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			expenseSMS = createParseSMSItem(c);
		}
		
		c.close();
		closeDatabase();
		return expenseSMS;
	}
	
	
	public SMSParseItem createParseSMSItem(Cursor c) {
		SMSParseItem parse = new SMSParseItem();
		
		parse.setID(c.getInt(0));
		parse.setTypeId(c.getInt(1));
		parse.setCardId(c.getInt(2));
		parse.setAmountRow(c.getInt(3));
		parse.setAmountStartPosition(c.getInt(4));
		parse.setAmountEndText(c.getString(5));
		parse.setInstallmentRow(c.getInt(6));
		parse.setInstallmentStartPosition(c.getInt(7));
		parse.setInstallmentEndText(c.getString(8));
		parse.setShopRow(c.getInt(9));
		parse.setShopStartPosition(c.getInt(10));
		parse.setShopEndText(c.getString(11));
		parse.setParseSource(c.getString(12));
		
		return parse;
	}
	
	public int deleteParseSMSItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete(SMS_PARSE_DATA_TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	


}
