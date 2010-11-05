package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * �ڻ���� DB�� ����
 * @author yongbban
 * @version 1.0.0.0
 */
public class AssetsDBConnector extends BaseFinanceDBConnector {
	
	/**
	 * �ڻ��� DB���̺� �߰�
	 * @param item �ڻ� ������
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addItem(FinanceItem financeItem) {
		AssetsItem item = (AssetsItem)financeItem;
		if (checkAssetsVaildItem(item) != DBDef.ValidError.SUCCESS) return -1;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getId());
		rowItem.put("sub_category", item.getSubCategory().getId());
		
		long ret = db.insert("assets", null, rowItem);
		item.setId((int)ret);
		db.close();
		return ret;
	}
	
	/**
	 * �ڻ� DB���̺��� ����
	 * @param item �ڻ� ������
	 * @return the number of rows affected , or -1 if an error occurred
	 */
	public long updateItem(FinanceItem financeItem) {
		AssetsItem item = (AssetsItem)financeItem;
		if (checkAssetsVaildItem(item) != DBDef.ValidError.SUCCESS) return -1;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getId());
		rowItem.put("sub_category", item.getSubCategory().getId());
		
		long ret = db.update("assets", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getId())});
		db.close();
		return ret;
	}
	
	/**
	 * ��� �ڻ� �������� DB���� �����´�.
	 * @return ArrayList<FinanceItem> �ڻ������ ���
	 */
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(CreateAssetsItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return assetsItems;
	}
	
	/**
	 * ������ ��¥�� �ڻ� �������� �����´�.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> �ڻ������ ���
	 */
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m-%d', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(CreateAssetsItem(c));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return assetsItems;
	}
	
	/**
	 * ������ ���̵��� �ڻ� �������� �����´�.
	 * @param id ������ �ڻ� ���̵�
	 */
	public FinanceItem getItem(int id) {
		FinanceItem item = null;
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(id)};
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, "assets._id=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
				item = CreateAssetsItem(c);
		}
		c.close();
		db.close();
		return item;
	}
	
	/**
	 * �ڻ� ��ü�� ����� DB���� ������ ������ ����
	 * @param c Cursor
	 * @return �ڻ� ������
	 */
	public AssetsItem CreateAssetsItem(Cursor c) {
		AssetsItem item = new AssetsItem();
		item.setId(c.getInt(0));
		try {
			item.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		item.setAmount(c.getLong(2));
		item.setTitle(c.getString(3));
		item.setMemo(c.getString(4));
		item.setCategory(c.getInt(5), c.getString(9));
		return item;
	}
	
	/**
	 * �ڻ� ���� �з��� �߰��Ѵ�.
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
		
		ret = db.insert("assets_main_category", null, rowItem);
		db.close();
		return ret;
	}
	
	/**
	 * �ڻ� ���� �з��� �߰��Ѵ�.
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
		
		ret = db.insert("assets_sub_category", null, rowItem);
		db.close();
		return ret;
	}
	
	/**
	 * �ڻ� �����з� ����� ��´�.
	 * ī�װ� ��ü����
	 * @return ArrayList<Category> ���� �з�����Ʈ
	 */
	public ArrayList<Category> getCategory() {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query("assets_main_category", null, null, null, null, null, null);
		
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
	 * �ڻ� �����з� ����� ��´�.
	 * ī�װ� ��ü����
	 * @return ArrayList<Category> �����з�����Ʈ
	 */
	public ArrayList<Category> getSubCategory(long mainCategoryId) {
		ArrayList<Category> subCategory = new ArrayList<Category>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query("assets_sub_category", null, "main_id=?", new String[]{String.valueOf(mainCategoryId)}, null, null, null);
		
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
	 *  �� �ڻ�ݾ��� ��´�.
	 * @return long �� �ݾ�
	 */
	public long getTotalAmount() {
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT SUM(amount) FROM assets";
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
	 * ������ ��¥�� ���� �� �ڻ�ݾ��� ��´�.
	 * @return long �� �ݾ�
	 */
	public long getTotalAmountDay(Calendar calendar) {
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)), 
				String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT SUM(amount) FROM assets WHERE strftime('%Y-%m-%d', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		long amount = 0L;
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		db.close();
		return amount;
	}

	/**
	 * ������ ��¥�� �߰��� �ڻ� ������ ������ ��´�.
	 * @return int ������ ��
	 */
	public int getItemCount(Calendar calendar) {
		int count = 0;
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)), 
				String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT COUNT(*) FROM assets WHERE strftime('%Y-%m-%d', create_date)=?";
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
		result = db.delete("assets", "_id=?", new String[] {String.valueOf(id)});
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
		result = db.delete("assets_main_category", "_id=?", new String[] {String.valueOf(id)});
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
		result = db.delete("assets_sub_category", "main_id=?", new String[] {String.valueOf(mainCategoryID)});
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
		result = db.delete("assets_sub_category", "_id=?", new String[] {String.valueOf(id)});
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
		
		int result = db.update("assets_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
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
		//�ӽ��ڵ�
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		int result = db.update("assets_sub_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
	
	/**
	 * �ڻ� �������� ��ȿ���� Ȯ���Ѵ�.
	 * @param ExpenseItem ���� ������
	 * @return int ������ 
	 */
	public int checkAssetsVaildItem(AssetsItem item) {
		int ret = checkVaildItem(item);
		if (ret != DBDef.ValidError.SUCCESS) {
			return ret;
		}
		if (item.getSubCategory() == null || item.getSubCategory().getId() == -1) {
			return DBDef.ValidError.SUB_CATEGORY_INVAlID;
		}
		return DBDef.ValidError.SUCCESS; 
	}
	
	public long updateRepeat(int expenseID, int repeatID) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();

		rowItem.put("repeat", repeatID); // �ӽð�
		
		long ret = db.update("assets", rowItem, "_id=?", new String[] {String.valueOf(expenseID)});
		db.close();
		return ret;
	}
}
