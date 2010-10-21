package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * ������� DB�� ����
 * @author yongbban
 * @version 1.0.0.0
 */
public class ExpenseDBConnector extends BaseFinanceDBConnector {
	
	/**
	 * ������ DB���̺� �߰�
	 * @param item ���� ������
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addItem(FinanceItem financeItem) {
		ExpenseItem item = (ExpenseItem)financeItem;
		if (checkExpenseVaildItem(item) != DBDef.ValidError.SUCCESS) return -1;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("create_time", "21:20:00"); // �ӽð�
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getId());
		rowItem.put("sub_category", item.getSubCategory().getId());
		rowItem.put("payment_method", 1); // �ӽð�
		
		long ret = db.insert("expense", null, rowItem);
		db.close();
		return ret;
	}
	
	/**
	 * ���� DB���̺��� ����
	 * @param item ���� ������
	 * @return the number of rows affected , or -1 if an error occurred
	 */
	public long updateItem(FinanceItem financeItem) { 
		ExpenseItem item = (ExpenseItem)financeItem;
		if (checkExpenseVaildItem(item) != DBDef.ValidError.SUCCESS) return -1;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("create_date", item.getCreateYear());
		rowItem.put("create_time", "21:20:00");
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getId());
		rowItem.put("sub_category", item.getSubCategory().getId());
		
		long ret = db.update("expense", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getId())});
		db.close();
		return ret;
	}
	
	/**
	 * ��� ���� �������� DB���� �����´�.
	 * @return ArrayList<FinanceItem> ��������� ���
	 */
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id ");
		Cursor c = queryBilder.query(db, null, null, null, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(CreateExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return expenseItems;
	}
	
	/**
	 * ������ ��¥�� ���� �������� �����´�.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> ��������� ���
	 */
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {FinanceDataFormat.getFormat(calendar.getTime())};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id ");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m-%d', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(CreateExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return expenseItems;
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
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id ");
		Cursor c = queryBilder.query(db, null, "expense._id=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
				item = CreateExpenseItem(c);
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
	public ExpenseItem CreateExpenseItem(Cursor c) {
		ExpenseItem item = new ExpenseItem();
		item.setId(c.getInt(0));
		try {
			item.setCreateDate(FinanceDataFormat.DATA_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		item.setAmount(c.getLong(3));
		item.setTitle(c.getString(4));
		item.setMemo(c.getString(5));
		item.setCategory(c.getInt(6), c.getString(13));
		item.setSubCategory(c.getInt(7), c.getString(17));
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
		
		ret = db.insert("expense_main_category", null, rowItem);
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
		
		ret = db.insert("expense_sub_category", null, rowItem);
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
		Cursor c = db.query("expense_main_category", null, null, null, null, null, null);
		
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
	 * ���� �����з� ����� ��´�.
	 * ī�װ� ��ü����
	 * @return ArrayList<Category> �����з�����Ʈ
	 */
	public ArrayList<Category> getSubCategory(long mainCategoryId) {
		ArrayList<Category> subCategory = new ArrayList<Category>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query("expense_sub_category", null, "main_id=?", new String[]{String.valueOf(mainCategoryId)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				Category item = new Category(c.getInt(0), c.getString(1));
				subCategory.add(item);
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return subCategory;
	}
	
	/**
	 *  �� ����ݾ��� ��´�.
	 * @return long �� �ݾ�
	 */
	public long getTotalAmount() {
		long amount = 0L;
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT SUM(amount) FROM expense";
		Cursor c = db.rawQuery(query, null);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		db.close();
		return amount;
	}
	
	/**
	 * ������ ��¥�� ���� �� ����ݾ��� ��´�.
	 * @return long �� �ݾ�
	 */
	public long getTotalAmountDay(Calendar calendar) {
		long amount = 0L;
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)) 
				+ String.valueOf(calendar.get(Calendar.MONTH)) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT SUM(amount) FROM expense WHERE strftime('%Y-%m-%d', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		db.close();
		return amount;
	}

	/**
	 * ������ ��¥�� �߰��� ���� ������ ������ ��´�.
	 * @return int ������ ��
	 */
	public int getItemCount(Calendar calendar) {
		int count = 0;
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)) 
				+ String.valueOf(calendar.get(Calendar.MONTH)) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT COUNT(*) FROM expense WHERE strftime('%Y-%m-%d', create_date)=?";
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
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete("expense", "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}

	/**
	 * ������ �����з�  �������� DB���� �����Ѵ�.
	 * @param id ������ �����з� ���̵�
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteCategory(int id) {
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete("expense_main_category", "_id=?", new String[] {String.valueOf(id)});
		db.close();
		
		deleteSubCategoryFromMainID(id);
		return result;
	}
	
	/**
	 * ������ ���̵��� �����з� �̸��� �����Ѵ�.
	 * @param id ������ �����з� ���̵�
	 * @param name ������ �̸�
	 * @return int the number of rows affected 
	 */
	public int deleteSubCategoryFromMainID(int mainCategoryID) {
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete("expense_sub_category", "main_id=?", new String[] {String.valueOf(mainCategoryID)});
		db.close();
		return result;
	}
	
	/**
	 * ������ �����з�  �������� DB���� �����Ѵ�.
	 * @param id ������ �����з� ���̵�
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteSubCategory(int id) {
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete("expense_sub_category", "_id=?", new String[] {String.valueOf(id)});
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
		
		int result = db.update("expense_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
	
	/**
	 * ������ ���̵��� �����з� �̸��� �����Ѵ�.
	 * @param id ������ �����з� ���̵�
	 * @param name ������ �̸�
	 * @return int the number of rows affected 
	 */
	public int updateSubCategory(int id, String name) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		
		int result = db.update("expense_sub_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
	
	/**
	 * ���� �������� ��ȿ���� Ȯ���Ѵ�.
	 * @param ExpenseItem ���� ������
	 * @return int ������ 
	 */
	public int checkExpenseVaildItem(ExpenseItem item) {
		int ret = checkVaildItem(item);
		if (ret != DBDef.ValidError.SUCCESS) {
			return ret;
		}
		if (item.getSubCategory() == null || item.getSubCategory().getId() == -1) {
			return DBDef.ValidError.SUB_CATEGORY_INVAlID;
		}
		return DBDef.ValidError.SUCCESS; 
	}
	
}
