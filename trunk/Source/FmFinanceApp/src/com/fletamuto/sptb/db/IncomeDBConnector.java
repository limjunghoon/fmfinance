package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * ���԰��� DB�� ����
 * @author yongbban
 * @version 1.0.0.0
 */
public class IncomeDBConnector extends BaseFinanceDBConnector {
	
	/**
	 * ������ DB���̺� �߰�
	 * @param item ���� ������
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addItem(FinanceItem item) {
		if (checkVaildItem(item) != DBDef.ValidError.SUCCESS) return -1; 
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getId());
	
		long ret = db.insert("income", null, rowItem);
		item.setId((int)ret);
		db.close();
		
		return ret;
	}
	
	/**
	 * ���� DB���̺��� ����
	 * @param item ���� ������
	 * @return the number of rows affected , or -1 if an error occurred
	 */
	public long updateItem(FinanceItem financeItem) {
		IncomeItem item = (IncomeItem)financeItem;
		if (checkVaildItem(item) != DBDef.ValidError.SUCCESS) return -1;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getId());
		
		long ret = db.update("income", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getId())});
		db.close();
		return ret;
	}
	
	/**
	 * ��� ���� �������� DB���� �����´�.
	 * @return ArrayList<FinanceItem> ���Ծ����� ���
	 */
	public ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> incomeItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				incomeItems.add(CreateIncomeItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return incomeItems;
	}
	
	/**
	 * ������ ��¥�� ���� �������� �����´�.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> ���Ծ����� ���
	 */
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		ArrayList<FinanceItem> incomeItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m-%d', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				incomeItems.add(CreateIncomeItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return incomeItems;
	}
	
	/**
	 * ������ ���̵��� ���� �������� �����´�.
	 * @param id ������ ���� ���̵�
	 */
	public FinanceItem getItem(int id) {
		FinanceItem item = null;
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(id)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "income._id=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			item = CreateIncomeItem(c);
		}
		c.close();
		db.close();
		return item;
	}
	
	/**
	 * ���� ��ü�� ����� DB���� ������ ������ ����
	 * @param c Cursor
	 * @return ���� ������
	 */
	public IncomeItem CreateIncomeItem(Cursor c) {
		IncomeItem item = new IncomeItem();
		item.setId(c.getInt(0));
		
		try {
			item.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		item.setAmount(c.getLong(2));
		item.setMemo(c.getString(3)); 
		item.setCategory(c.getInt(5), c.getString(10));
		return item;
	}
	
	/**
	 * ���� ���� �з��� �߰��Ѵ�.
	 * @param name �з� �̸�
	 * @return the row ID of the newly inserted row, or -1 if an error occurred 
	 */
	public long addCategory(String name) {
		long ret = -1;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//�ӽ��ڵ�
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		ret = db.insert("income_main_category", null, rowItem);
		db.close();
		return ret;
	}
	
	/**
	 * ���� ���� �з��� �߰��Ѵ�.
	 * @param mainCategoryID �����з� ���̵�
	 * @param name �з� �̸�
	 * @return the row ID of the newly inserted row, or -1 if an error occurred 
	 */
	public long addSubCategory(long mainCategoryID, String name) {
		long ret = -1;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		rowItem.put("main_id", mainCategoryID);
		//�ӽ��ڵ�
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		ret = db.insert("income_sub_category", null, rowItem);
		db.close();
		return ret;
	}

	/**
	 * ���� �����з� ����� ��´�.
	 * ī�װ� ��ü����
	 * @return ArrayList<Category> ���� �з�����Ʈ
	 */
	public ArrayList<Category> getCategory() {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query("income_main_category", null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				Category item = new Category(c.getInt(0), c.getString(1));
				category.add(item);
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		
		return category;
	}

	/**
	 * ���� �� �ݾ��� ��´�.
	 * @return long �� �ݾ�
	 */
	public long getTotalAmount() {
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT SUM(amount) FROM income";
		Cursor c = db.rawQuery(query, null);
		long amount = 0L;
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		db.close();
		return amount;
	}
	
	/**
	 * ������ ���ſ� ���� �� ���Աݾ��� ��´�.
	 * @return long �� �ݾ�
	 */
	public long getTotalAmountDay(Calendar calendar) {
		long amount = 0L;
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		String query = "SELECT SUM(amount) FROM income WHERE strftime('%Y-%m-%d', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		db.close();
		return amount;
	}

	/**
	 * ������ ���ſ� �߰��� ���� ������ ������ ��´�.
	 * @return int ������ ��
	 */
	public int getItemCount(Calendar calendar) {
		int count = 0;
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		String query = "SELECT COUNT(*) FROM income WHERE strftime('%Y-%m-%d', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			count = c.getInt(0);
		}
		c.close();
		db.close();
		return count;
	}

	/**
	 * ������ ���̵��� �������� DB���� �����Ѵ�.
	 * @param id ������ ������ ���̵�
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteItem(int id) {
		SQLiteDatabase db = getWritableDatabase();
		int result = db.delete("income", "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}

	/**
	 * ������ �����з�  �������� DB���� �����Ѵ�.
	 * @param id ������ �����з� ���̵�
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteCategory(int id) {
		SQLiteDatabase db = getWritableDatabase();
		int result = db.delete("income_main_category", "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
	
	/**
	 * ������ ���̵��� �����з� �̸��� �����Ѵ�.
	 * @param id ������ �����з� ���̵�
	 * @param name ������ �̸�
	 * @return int the number of rows affected 
	 */
	public int updateCategory(int id, String name) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//�ӽ��ڵ�
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		int result = db.update("income_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
	
	public long updateRepeat(int expenseID, int repeatID) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();

		rowItem.put("repeat", repeatID); // �ӽð�
		
		long ret = db.update("income", rowItem, "_id=?", new String[] {String.valueOf(expenseID)});
		db.close();
		return ret;
	}
}
