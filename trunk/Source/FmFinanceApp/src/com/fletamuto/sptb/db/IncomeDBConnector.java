package com.fletamuto.sptb.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.fletamuto.sptb.data.IncomeItem;

public class IncomeDBConnector extends BaseDBConnector {
	boolean AddItem(IncomeItem item) {
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("year", item.getCreateYear());
		rowItem.put("month", item.getCreateMonth());
		rowItem.put("day", item.getCreateDay());
		rowItem.put("amount", item.getAmount());
		rowItem.put("memo", item.getMemo());
		
		db.insert("income", null, rowItem);
		db.close();
		return true;
	}
}
