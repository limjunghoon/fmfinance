package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.FinancialCompany;

public class AccountDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "account";
	
	public int addItem(AccountItem account) {
		int insertID = -1;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("number", account.getNumber());
		rowItem.put("balance", account.getBalance());
		rowItem.put("institution", account.getInstitution().getID());
		rowItem.put("type", account.getType());
//		rowItem.put("create_date", account.getCreateDate());
//		rowItem.put("expriy_date", account.getExpiryDate());
		rowItem.put("memo", account.getMemo());
		rowItem.put("name", account.getName());
		
		insertID = (int)db.insert(TABLE_NAME, null, rowItem);
		account.setID(insertID);
		db.close();
		return insertID;
	}
	
	public boolean updateItem(AccountItem account) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("number", account.getNumber());
		rowItem.put("balance", account.getBalance());
		rowItem.put("institution", account.getInstitution().getID());
		rowItem.put("type", account.getType());
//		rowItem.put("create_date", account.getCreateDate());
//		rowItem.put("expriy_date", account.getExpiryDate());
		rowItem.put("memo", account.getMemo());
		rowItem.put("name", account.getName());
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(account.getID())});
		db.close();
		return true;
	}
	
	public  ArrayList<AccountItem> getAllItems() {
		ArrayList<AccountItem> incomeItems = new ArrayList<AccountItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("account, institution");
		queryBilder.appendWhere("account.institution=institution._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				incomeItems.add(createAccountItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return incomeItems;
	}
	
	public AccountItem getItem(int id) {
		AccountItem account = null;
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("account, institution");
		queryBilder.appendWhere("account.institution=institution._id");
		Cursor c = queryBilder.query(db, null, "account._id=?", new String[] {String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			account = createAccountItem(c);
		}
		c.close();
		db.close();
		return account;
	}
	
	
	public AccountItem createAccountItem(Cursor c) {
		AccountItem account = new AccountItem();
		account.setID(c.getInt(0));
		account.setNumber(c.getString(1));
		account.setBalance(c.getInt(2));
//		account.setInstitution(c.getInt(3));
//		account.setType(c.getInt(4));
//		account.setCreateDate(c.getInt(5));
//		account.setExpiryDate(c.getString(6));
		account.setMemo(c.getString(7));
		account.setName(c.getString(8));
		
		FinancialCompany institution = new FinancialCompany();
		institution.setID(c.getInt(9));
		institution.setName(c.getString(10));
		institution.setGroup(c.getInt(11));
		account.setInstitution(institution);

		return account;
	}
	
	public int deleteAccountItem(int id) {
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete(TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
}
