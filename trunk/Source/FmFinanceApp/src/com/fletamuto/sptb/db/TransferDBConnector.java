package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.TransferItem;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

public class TransferDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "transfer_history";
	
	public int addItem(TransferItem trans) {
		if (checkTransVaildItem(trans) != DBDef.ValidError.SUCCESS) return -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("occurrence_date", trans.getOccurrentceDateString());
		rowItem.put("to_account", trans.getToAccount().getID());
		rowItem.put("from_account", trans.getFromAccount().getID());
		rowItem.put("amount", trans.getAmount());
		rowItem.put("memo", trans.getMemo());
		
		int insertID = (int)db.insert(TABLE_NAME, null, rowItem);
		trans.setID(insertID);
		closeDatabase();
		return insertID;
	}

	public boolean updateItem(TransferItem trans) {
		if (checkTransVaildItem(trans) != DBDef.ValidError.SUCCESS) return false;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("occurrence_date", trans.getOccurrentceDateString());
		rowItem.put("to_account", trans.getToAccount().getID());
		rowItem.put("from_account", trans.getFromAccount().getID());
		rowItem.put("amount", trans.getAmount());
		rowItem.put("memo", trans.getMemo());
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(trans.getID())});
		closeDatabase();
		return true;
	}
	
	public  ArrayList<TransferItem> getAllItems() {
		ArrayList<TransferItem> items = new ArrayList<TransferItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("transfer_history, account, account");
		queryBilder.appendWhere("transfer_history.to_account=account._id AND transfer_history.from_account=account._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				items.add(createTransferItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return items;
	}
	
	public TransferItem getItem(int id) {
		TransferItem trans = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		LOCK();
		
		Cursor c = db.query(TABLE_NAME, null, "transfer_history._id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			trans = createTransferItem(c);
			trans.setToAccount(DBMgr.getAccountItem(trans.getToAccount().getID()));
			trans.setFromAccount(DBMgr.getAccountItem(trans.getFromAccount().getID()));
		}
		
		UNLOCK();
		c.close();
		closeDatabase();
		return trans;
	}
	
	
	public TransferItem createTransferItem(Cursor c) {
		TransferItem trans = new TransferItem();
		trans.setID(c.getInt(0));
		
		try {
			trans.setOccurrentceDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		trans.getToAccount().setID(c.getInt(2));
		trans.getFromAccount().setID(c.getInt(3));
		trans.setAmount(c.getLong(4));
		trans.setMemo(c.getString(5));
		
		return trans;
	}
	
	public int deleteItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete(TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	private int checkTransVaildItem(TransferItem trans) {
		if (trans.getToAccount().getID() == -1 || trans.getFromAccount().getID() == -1) {
			Log.e(LogTag.DB, ":: TRANS ITEM INVAILD");
			return DBDef.ValidError.TRANS_ITEM_INVAlID;
		}
		
		return DBDef.ValidError.SUCCESS; 
	}

	public ArrayList<TransferItem> getTranserFromAccount(int year, int month, AccountItem fromAccount) {
		ArrayList<TransferItem> transItems = new ArrayList<TransferItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);		
		LOCK();
		
		Cursor c = db.query(TABLE_NAME, null, "strftime('%Y-%m', occurrence_date)=? AND transfer_history.from_account=?", new String[]{String.format("%d-%02d", year, month), String.valueOf(fromAccount.getID())}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				TransferItem trans = createTransferItem(c);
				trans.setFromAccount(fromAccount);
				trans.setToAccount(DBMgr.getAccountItem(trans.getToAccount().getID()));
				transItems.add(trans);
			} while (c.moveToNext());
		}
		
		UNLOCK();
		c.close();
		closeDatabase();
		return transItems;
	}

	public ArrayList<TransferItem> getTranserToAccount(int year, int month, AccountItem toAccount) {
		ArrayList<TransferItem> transItems = new ArrayList<TransferItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);		
		LOCK();
		
		Cursor c = db.query(TABLE_NAME, null, "strftime('%Y-%m', occurrence_date)=? AND transfer_history.to_account=?", new String[]{String.format("%d-%02d", year, month), String.valueOf(toAccount.getID())}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				TransferItem trans = createTransferItem(c);
				trans.setToAccount(toAccount);
				trans.setFromAccount(DBMgr.getAccountItem(trans.getFromAccount().getID()));
				transItems.add(trans);
			} while (c.moveToNext());
		}
		
		UNLOCK();
		c.close();
		closeDatabase();
		return transItems;
	}
}
