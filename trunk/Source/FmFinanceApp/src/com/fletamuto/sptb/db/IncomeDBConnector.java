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
import com.fletamuto.sptb.data.UISelectItem;
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
	public long addItem(FinanceItem financeItem) {
		IncomeItem item = (IncomeItem)financeItem;
		if (checkVaildItem(item) != DBDef.ValidError.SUCCESS) return -1; 
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("account", item.getAccount().getID());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("extend", item.getExtendID());
		
	
		long ret = db.insert("income", null, rowItem);
		item.setID((int)ret);
		closeDatabase();
		
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
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("memo", item.getMemo());
		rowItem.put("account", item.getAccount().getID());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("extend", item.getExtendID());
		
		long ret = db.update("income", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getID())});
		closeDatabase();
		return ret;
	}
	
	@Override
	public long updateAmountFinanceItem(int id, long amount) {
		if (amount == 0) return 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("amount", amount);
		long ret = db.update("income", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return ret;
	}
	
	/**
	 * ��� ���� �������� DB���� �����´�.
	 * @return ArrayList<FinanceItem> ���Ծ����� ���
	 */
	public ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> incomeItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
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
		closeDatabase();
		return incomeItems;
	}
	
	/**
	 * ������ ��¥�� ���� �������� �����´�.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> ���Ծ����� ���
	 */
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		ArrayList<FinanceItem> incomeItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
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
		closeDatabase();
		return incomeItems;
	}
	
	public ArrayList<FinanceItem> getItems(Calendar startCalendar, Calendar endCalendar) {
		ArrayList<FinanceItem> incomeItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String where = "income.main_category=income_main_category._id AND strftime('%Y-%m-%d', income.create_date) BETWEEN '" + FinanceDataFormat.getDateFormat(startCalendar.getTime()) +"' AND '" + FinanceDataFormat.getDateFormat(endCalendar.getTime()) + "'";
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere(where);
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				incomeItems.add(CreateIncomeItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return incomeItems;
	}
	
	public ArrayList<FinanceItem> getItems(int year, int month) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(CreateIncomeItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
	}
	
	public ArrayList<FinanceItem> getItemsFromCategoryID(int mainCategoryID, int year, int month) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(mainCategoryID)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND income.main_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(CreateIncomeItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
	}
	
	public ArrayList<FinanceItem> getItemsFromCategoryID(int mainCategoryID) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(mainCategoryID)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "income.main_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(CreateIncomeItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
	}
	
	public ArrayList<FinanceItem> getItemsFromSubCategoryID(int subCategoryID, int year, int month) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(subCategoryID)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND income.sub_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(CreateIncomeItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
	}
	
	/**
	 * ������ ���̵��� ���� �������� �����´�.
	 * @param id ������ ���� ���̵�
	 */
	public FinanceItem getItem(int id) {
		FinanceItem item = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(id)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "income._id=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			item = CreateIncomeItem(c);
		}
		c.close();
		closeDatabase();
		return item;
	}
	
	/**
	 * ���� ��ü�� ����� DB���� ������ ������ ����
	 * @param c Cursor
	 * @return ���� ������
	 */
	public IncomeItem CreateIncomeItem(Cursor c) {
		IncomeItem item = new IncomeItem();
		item.setID(c.getInt(0));
		
		try {
			item.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		item.setAmount(c.getLong(2));
		item.setMemo(c.getString(4)); 
		item.getAccount().setID(c.getInt(5));
		item.setCategory(c.getInt(6), c.getString(11));
		return item;
	}
	
	/**
	 * ���� ���� �з��� �߰��Ѵ�.
	 * @param name �з� �̸�
	 * @return the row ID of the newly inserted row, or -1 if an error occurred 
	 */
	public long addCategory(Category category) {
		long ret = -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", category.getName());
		//�ӽ��ڵ�
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		rowItem.put("extend_type", category.getExtndType());
		rowItem.put("type", category.getUIType());
		
		ret = db.insert("income_main_category", null, rowItem);
		closeDatabase();
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
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		rowItem.put("main_id", mainCategoryID);
		//�ӽ��ڵ�
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		rowItem.put("extend_type", 0);
		rowItem.put("type", 0);
		
		ret = db.insert("income_sub_category", null, rowItem);
		closeDatabase();
		return ret;
	}

	/**
	 * ���� �����з� ����� ��´�.
	 * ī�װ� ��ü����
	 * @return ArrayList<Category> ���� �з�����Ʈ
	 */
	public ArrayList<Category> getCategory() {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("income_main_category", null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				Category item = new Category(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
				
				if (item.getUIType() != UISelectItem.HIDE) {
					category.add(item);
				}
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		
		return category;
	}
	
	@Override
	public ArrayList<Category> getCategory(int extendItem) {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("income_main_category", null, "extend_type=?", new String[]{String.valueOf(extendItem)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				category.add(new Category(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5)));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return category;
	}

	/**
	 * ���� �� �ݾ��� ��´�.
	 * @return long �� �ݾ�
	 */
	public long getTotalAmount() {
		SQLiteDatabase db = openDatabase(READ_MODE);
		String query = "SELECT SUM(amount) FROM income";
		Cursor c = db.rawQuery(query, null);
		long amount = 0L;
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}
	
	/**
	 * ������ ���ſ� ���� �� ���Աݾ��� ��´�.
	 * @return long �� �ݾ�
	 */
	public long getTotalAmountDay(Calendar calendar) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		String query = "SELECT SUM(amount) FROM income WHERE strftime('%Y-%m-%d', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}
	
	public long getTotalAmountMonth(int year, int month) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.format("%d-%02d", year, month)};
		String query = "SELECT SUM(amount) FROM income WHERE strftime('%Y-%m', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}
	
	public long getTotalAmountMonth(int categoryID, int year, int month) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.valueOf(categoryID), String.format("%d-%02d", year, month)};
		String query = "SELECT SUM(amount) FROM income WHERE main_category=? AND strftime('%Y-%m', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}
	
	@Override
	public ArrayList<Long> getTotalAmountMonthInYear(int categoryID, int year) {
		ArrayList<Long> amountMonthInYear = new ArrayList<Long>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		for (int month = 1; month <= 12; month++) {
			String[] params = {String.valueOf(categoryID), String.format("%d-%02d", year, month)};
			String query = "SELECT SUM(amount) FROM income WHERE main_category=? AND strftime('%Y-%m', create_date)=?";
			Cursor c = db.rawQuery(query, params);
			
			if (c.moveToFirst() != false) {
				amountMonthInYear.add(c.getLong(0));
			}
			c.close();
		}
		closeDatabase();
		return amountMonthInYear;
	}
	
	public long getTotalAmountYear(int year) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.format("%d", year)};
		String query = "SELECT SUM(amount) FROM income WHERE strftime('%Y', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}

	/**
	 * ������ ���ſ� �߰��� ���� ������ ������ ��´�.
	 * @return int ������ ��
	 */
	public int getItemCount(Calendar calendar) {
		int count = 0;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		String query = "SELECT COUNT(*) FROM income WHERE strftime('%Y-%m-%d', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			count = c.getInt(0);
		}
		c.close();
		closeDatabase();
		return count;
	}

	/**
	 * ������ ���̵��� �������� DB���� �����Ѵ�.
	 * @param id ������ ������ ���̵�
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteItem(int id) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		int result = db.delete("income", "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}

	/**
	 * ������ �����з�  �������� DB���� �����Ѵ�.
	 * @param id ������ �����з� ���̵�
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteCategory(int id) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		int result = db.delete("income_main_category", "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	/**
	 * ������ ���̵��� �����з� �̸��� �����Ѵ�.
	 * @param id ������ �����з� ���̵�
	 * @param name ������ �̸�
	 * @return int the number of rows affected 
	 */
	public int updateCategory(int id, String name) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//�ӽ��ڵ�
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		int result = db.update("income_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	public long updateRepeat(int expenseID, int repeatID) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();

		rowItem.put("repeat", repeatID); // �ӽð�
		
		long ret = db.update("income", rowItem, "_id=?", new String[] {String.valueOf(expenseID)});
		closeDatabase();
		return ret;
	}
	
	/**
	 * ������ DB���̺� �߰�
	 * @param item ���� ������
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addExendSalary(int expenseInsuranceID, int expenseTaxID, int expensePensionID, int expenseEtcID) { 
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("pension", expensePensionID);
		rowItem.put("tax", expenseTaxID);
		rowItem.put("assurance", expenseInsuranceID);
		rowItem.put("etc", expenseEtcID);
		long ret = db.insert("income_salary", null, rowItem);
		closeDatabase();
		
		return ret;
	}

	@Override
	public int addOpneUsedItem(int id) {
		// TODO Auto-generated method stub
		return 0;
	}


}
