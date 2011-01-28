package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AccountTransfer;
import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

public class AccountDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "account";
	
	public int addItem(AccountItem account) {
		if (checkAccountVaildItem(account) != DBDef.ValidError.SUCCESS) return -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", account.getCreateDateString());
		rowItem.put("number", account.getNumber());
		rowItem.put("balance", account.getBalance());
		rowItem.put("company", account.getCompany().getID());
		rowItem.put("type", account.getType());
		rowItem.put("expiry_date", account.getExpiryDateString());
		rowItem.put("memo", account.getMemo());
		rowItem.put("name", account.getName());
		
		int insertID = (int)db.insert(TABLE_NAME, null, rowItem);
		account.setID(insertID);
		closeDatabase();
		return insertID;
	}

	public boolean updateItem(AccountItem account) {
		if (checkAccountVaildItem(account) != DBDef.ValidError.SUCCESS) return false;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", account.getCreateDateString());
		rowItem.put("number", account.getNumber());
		rowItem.put("balance", account.getBalance());
		rowItem.put("company", account.getCompany().getID());
		rowItem.put("type", account.getType());
		rowItem.put("expiry_date", account.getExpiryDateString());
		rowItem.put("memo", account.getMemo());
		rowItem.put("name", account.getName());
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(account.getID())});
		closeDatabase();
		return true;
	}
	
	public AccountItem getMyPocket() {
		AccountItem account = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query(TABLE_NAME, null, "type=?", new String[]{String.valueOf(AccountItem.MY_POCKET)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			account = createAccountItem(c);
		}
		c.close();
		closeDatabase();
		return account;
	}
	
	public  ArrayList<AccountItem> getAllItems() {
		ArrayList<AccountItem> incomeItems = new ArrayList<AccountItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("account, finance_company");
		queryBilder.appendWhere("account.company=finance_company._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, "company DESC");
		
		if (c.moveToFirst() != false) {
			do {
				incomeItems.add(createAccountItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return incomeItems;
	}
	
	public AccountItem getItem(int id) {
		AccountItem account = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("account, finance_company");
		queryBilder.appendWhere("account.company=finance_company._id");
		Cursor c = queryBilder.query(db, null, "account._id=?", new String[] {String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			account = createAccountItem(c);
		}
		c.close();
		closeDatabase();
		return account;
	}
	
	
	public AccountItem createAccountItem(Cursor c) {
		AccountItem account = new AccountItem();
		account.setID(c.getInt(0));
		
		try {
			account.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		account.setNumber(c.getString(2));
		account.setBalance(c.getInt(3));
	//	account.setCompany(c.getInt(4));
		account.setType(c.getInt(5));
		
		try {
			account.setExpiryDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(6)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		account.setMemo(c.getString(7));
		account.setName(c.getString(8));
		
		if (account.getType() != AccountItem.MY_POCKET) {
			FinancialCompany Company = new FinancialCompany();
			Company.setID(c.getInt(9));
			Company.setName(c.getString(10));
			Company.setGroup(c.getInt(11));
			account.setCompany(Company);
		}
		return account;
	}
	
	public int deleteAccountItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete(TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	private int checkAccountVaildItem(AccountItem account) {
		if (account.getType() != AccountItem.MY_POCKET) {
			if (account.getCompany() == null || account.getCompany().getID() == -1) {
				Log.e(LogTag.DB, ":: CARD ITEM INVAILD");
				return DBDef.ValidError.ACCOUNT_ITEM_INVAlID;
			}
		}
		
		return DBDef.ValidError.SUCCESS; 
	}
	
	public int addTransfer(AccountTransfer trans) {
		if (trans.getToAccountId() == trans.getFromAccountId()) {
			return -1;
		}
		
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("occurrence_date", trans.getOccurrenceDateString());
		rowItem.put("to_account", trans.getToAccountId());
		rowItem.put("from_account", trans.getFromAccountId());
		rowItem.put("amount", trans.getAmount());
		
		int insertID = (int)db.insert("transfer_history", null, rowItem);
		trans.setID(insertID);
		closeDatabase();
		return insertID;
	}
	
	public boolean updateTransfer(AccountTransfer trans) {
		if (trans.getToAccountId() == trans.getFromAccountId()) {
			return false;
		}
		
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("occurrence_date", trans.getOccurrenceDateString());
		rowItem.put("to_account", trans.getToAccountId());
		rowItem.put("from_account", trans.getFromAccountId());
		rowItem.put("amount", trans.getAmount());
		
		db.update("transfer_history", rowItem, "_id=?", new String[] {String.valueOf(trans.getID())});
		closeDatabase();
		return true;
	}
	
	public AccountTransfer getTransferFromID(int fromAccountID) {
		AccountTransfer trans = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("transfer_history", null, "from_account=?", new String[]{String.valueOf(fromAccountID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			trans = createTransAccount(c);
		}
		c.close();
		closeDatabase();
		return trans;
	}

	public AccountTransfer createTransAccount(Cursor c) {
		AccountTransfer trans = new AccountTransfer();
		trans.setID(c.getInt(0));
		
		try {
			trans.setOccurrenceDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		trans.setToAccountId(c.getInt(2));
		trans.setFromAccountId(c.getInt(3));
		trans.setAmount(c.getLong(4));
	
		return trans;
	}

}
