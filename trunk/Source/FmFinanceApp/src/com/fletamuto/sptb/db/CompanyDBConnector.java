package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fletamuto.sptb.data.FinancialCompany;

public class CompanyDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "finance_company";
	
	public int addItem(FinancialCompany company) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", company.getName());
		rowItem.put("type", company.getGroup());
		rowItem.put("prioritize", 1);
		rowItem.put("image_index", 1);
		
		int ret = (int)db.insert(TABLE_NAME, null, rowItem);
		closeDatabase();
		return ret;
	}
	
	public boolean updateItem(FinancialCompany company) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", company.getName());
		rowItem.put("type", company.getGroup());
		rowItem.put("prioritize", 1);
		rowItem.put("image_index", 1);
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(company.getID())});
		closeDatabase();
		return true;
	}
	
	public  ArrayList<FinancialCompany> getAllItems() {
		ArrayList<FinancialCompany> institutions = new ArrayList<FinancialCompany>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				institutions.add(CreateInstitutionItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return institutions;
	}
	
	public FinancialCompany getItem(int id) {
		FinancialCompany institution = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query(TABLE_NAME, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			institution = CreateInstitutionItem(c);
		}
		c.close();
		closeDatabase();
		return institution;
	}
	
	
	public FinancialCompany CreateInstitutionItem(Cursor c) {
		FinancialCompany institution = new FinancialCompany();
		institution.setID(c.getInt(0));
		institution.setName(c.getString(1));
		institution.setGroup(c.getInt(2));
		return institution;
	}
	
	public int deleteItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete(TABLE_NAME, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
}
