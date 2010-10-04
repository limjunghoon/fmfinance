package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fletamuto.sptb.data.FinancialInstitution;

public class InstitutionDBConnector extends BaseDBConnector {
	private static final String TABLE_NAME = "institution";
	
	public boolean addItem(FinancialInstitution institution) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", institution.getName());
		rowItem.put("type", institution.getGroup());
		
		
		db.insert(TABLE_NAME, null, rowItem);
		db.close();
		return true;
	}
	
	public boolean updateItem(FinancialInstitution institution) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", institution.getName());
		rowItem.put("type", institution.getGroup());
		
		db.update(TABLE_NAME, rowItem, "_id=?", new String[] {String.valueOf(institution.getID())});
		db.close();
		return true;
	}
	
	public  ArrayList<FinancialInstitution> getAllItems() {
		ArrayList<FinancialInstitution> institutions = new ArrayList<FinancialInstitution>();
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				institutions.add(CreateInstitutionItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return institutions;
	}
	
	public FinancialInstitution getItem(int id) {
		FinancialInstitution institution = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			institution = CreateInstitutionItem(c);
		}
		c.close();
		db.close();
		return institution;
	}
	
	
	public FinancialInstitution CreateInstitutionItem(Cursor c) {
		FinancialInstitution institution = new FinancialInstitution();
		institution.setID(c.getInt(0));
		institution.setName(c.getString(1));
		institution.setGroup(c.getInt(2));
		return institution;
	}
}
