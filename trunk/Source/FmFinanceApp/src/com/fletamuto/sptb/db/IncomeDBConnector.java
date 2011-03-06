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
import com.fletamuto.sptb.data.OpenUsedItem;
import com.fletamuto.sptb.data.UISelectItem;
import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * 수입관련 DB를 관리
 * @author yongbban
 * @version 1.0.0.0
 */
public class IncomeDBConnector extends BaseFinanceDBConnector {
	
	/**
	 * 수입을 DB테이블에 추가
	 * @param item 수입 아이템
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
	 * 수입 DB테이블에서 수정
	 * @param item 수입 아이템
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
	 * 모든 수입 아이템을 DB에서 가져온다.
	 * @return ArrayList<FinanceItem> 수입아이템 목록
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
	 * 지정된 날짜의 수입 아이템을 가져온다.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> 수입아이템 목록
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
	
	public ArrayList<FinanceItem> getItems(int year) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d", year)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y', create_date)=?", params, null, null, null);
		
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
	
	public ArrayList<FinanceItem> getItemsFromCategoryID(int mainCategoryID, int year) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d", year), String.valueOf(mainCategoryID)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y', create_date)=? AND income.main_category=?", params, null, null, null);
		
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
	 * 지정된 아이디의 수입 아이템을 가져온다.
	 * @param id 가져올 수입 아이디
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
	 * 수입 객체를 만들고 DB에서 가져온 값으로 설정
	 * @param c Cursor
	 * @return 수입 아이템
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
	 * 수입 상위 분류를 추가한다.
	 * @param name 분류 이름
	 * @return the row ID of the newly inserted row, or -1 if an error occurred 
	 */
	public long addCategory(Category category) {
		long ret = -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", category.getName());
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", category.getImageIndex());
		rowItem.put("extend_type", category.getExtndType());
		rowItem.put("type", category.getUIType());
		
		ret = db.insert("income_main_category", null, rowItem);
		closeDatabase();
		return ret;
	}
	
	/**
	 * 수입 하위 분류를 추가한다.
	 * @param mainCategoryID 상위분류 아이디
	 * @param name 분류 이름
	 * @return the row ID of the newly inserted row, or -1 if an error occurred 
	 */
	public long addSubCategory(long mainCategoryID, String name, int imgIndex) {
		long ret = -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		rowItem.put("main_id", mainCategoryID);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", imgIndex);
		rowItem.put("extend_type", 0);
		rowItem.put("type", 0);
		
		ret = db.insert("income_sub_category", null, rowItem);
		closeDatabase();
		return ret;
	}

	/**
	 * 수입 상위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 상위 분류리스트
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
	
	@Override
	public Category getCategoryFromID(int categoryID) {
		Category category = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("income_main_category", null, "_id=?", new String[]{String.valueOf(categoryID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			category = new Category(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
		}
		c.close();
		closeDatabase();
		return category;
	}
	
	@Override
	public Category getSubCategoryFromID(int categoryID) {
		Category category = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("income_sub_category", null, "_id=?", new String[]{String.valueOf(categoryID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			category = new Category(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
		}
		c.close();
		closeDatabase();
		return category;
	}

	/**
	 * 수입 총 금액을 얻는다.
	 * @return long 총 금액
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
	 * 지정된 날쩨에 대한 총 수입금액을 얻는다.
	 * @return long 총 금액
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
	
	public ArrayList<Long> getTotalAmount(int year, int month, int beforMonthCount) {
		ArrayList<Long> amountMonthInYear = new ArrayList<Long>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		int targetMonth = month - beforMonthCount;
		if (targetMonth <= 0) {
			targetMonth += 12 + 1;
			year--;
		}
		for (int index = 0; index < beforMonthCount; index++) {
			
			if (targetMonth > 12) {
				targetMonth = 1;
				year++;
			}
			
			String[] params = {String.format("%d-%02d", year, targetMonth)};
			String query = "SELECT SUM(amount) FROM income WHERE strftime('%Y-%m', create_date)=?";
			Cursor c = db.rawQuery(query, params);
			
			if (c.moveToFirst() != false) {
				amountMonthInYear.add(c.getLong(0));
			}
			targetMonth++;
			c.close();
		}
		
		closeDatabase();
		return amountMonthInYear;
	}
	
	public ArrayList<Long> getTotalMainCategoryAmount(int mainCategoryID, int year, int month, int beforMonthCount) {
		ArrayList<Long> amountMonthInYear = new ArrayList<Long>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		int targetMonth = month - beforMonthCount;
		if (targetMonth <= 0) {
			targetMonth += 12 + 1;
			year--;
		}
		for (int index = 0; index < beforMonthCount; index++) {
			
			if (targetMonth > 12) {
				targetMonth = 1;
				year++;
			}
			
			String[] params = {String.valueOf(mainCategoryID), String.format("%d-%02d", year, targetMonth)};
			String query = "SELECT SUM(amount) FROM income WHERE main_category=? AND strftime('%Y-%m', create_date)=?";
			Cursor c = db.rawQuery(query, params);
			
			if (c.moveToFirst() != false) {
				amountMonthInYear.add(c.getLong(0));
			}
			targetMonth++;
			c.close();
		}
		
		closeDatabase();
		return amountMonthInYear;
	}
	
	public ArrayList<Long> getTotalSubCategoryAmount(int subCategoryID, int year, int month, int beforMonthCount) {
		ArrayList<Long> amountMonthInYear = new ArrayList<Long>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		int targetMonth = month - beforMonthCount;
		if (targetMonth <= 0) {
			targetMonth += 12 + 1;
			year--;
		}
		for (int index = 0; index < beforMonthCount; index++) {
			
			if (targetMonth > 12) {
				targetMonth = 1;
				year++;
			}
			
			String[] params = {String.valueOf(subCategoryID), String.format("%d-%02d", year, targetMonth)};
			String query = "SELECT SUM(amount) FROM income WHERE sub_category=? AND strftime('%Y-%m', create_date)=?";
			Cursor c = db.rawQuery(query, params);
			
			if (c.moveToFirst() != false) {
				amountMonthInYear.add(c.getLong(0));
			}
			targetMonth++;
			c.close();
		}
		
		closeDatabase();
		return amountMonthInYear;
	}
	
	public ArrayList<Long> getTotalAmountMonth(int year) {
		ArrayList<Long> amountMonthInYear = new ArrayList<Long>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		for (int month = 1; month <= 12; month++) {
			String[] params = {String.format("%d-%02d", year, month)};
			String query = "SELECT SUM(amount) FROM income WHERE strftime('%Y-%m', create_date)=?";
			Cursor c = db.rawQuery(query, params);
			
			if (c.moveToFirst() != false) {
				amountMonthInYear.add(c.getLong(0));
			}
			c.close();
		}
		
		closeDatabase();
		return amountMonthInYear;
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
	 * 지정된 날쩨에 추가된 수입 아이템 갯수를 얻는다.
	 * @return int 아이템 수
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
	 * 지정된 아이디의 아이템을 DB에서 삭제한다.
	 * @param id 삭제할 아이템 아이디
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteItem(int id) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		int result = db.delete("income", "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}

	/**
	 * 지정된 상위분류  아이템을 DB에서 삭제한다.
	 * @param id 삭제할 상위분류 아이디
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteCategory(int id) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		int result = db.delete("income_main_category", "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	/**
	 * 지정된 아이디의 상위분류 이름을 변경한다.
	 * @param id 변경할 상위분류 아이디
	 * @param name 변경할 이름
	 * @return int the number of rows affected 
	 */
	public int updateCategory(int id, String name, int imgIndex) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", imgIndex);
		
		int result = db.update("income_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	public long updateRepeat(int expenseID, int repeatID) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();

		rowItem.put("repeat", repeatID); // 임시값
		
		long ret = db.update("income", rowItem, "_id=?", new String[] {String.valueOf(expenseID)});
		closeDatabase();
		return ret;
	}
	
	/**
	 * 수입을 DB테이블에 추가
	 * @param item 수입 아이템
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

	public ArrayList<FinanceItem> getItemFromAccount(int accountId, int year,
			int month) {
		ArrayList<FinanceItem> incomeItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(accountId)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND income.account=?", params, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				incomeItems.add(CreateIncomeItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return incomeItems;
	}

	public ArrayList<FinanceItem> getItemFromMypocket(int year, int month) {
		ArrayList<FinanceItem> incomeItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(-1)};
		
		queryBilder.setTables("income, income_main_category");
		queryBilder.appendWhere("income.main_category=income_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND income.account=?", params, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				incomeItems.add(CreateIncomeItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return incomeItems;
	}

	@Override
	public int addOpenUsedItem(OpenUsedItem item) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("income_id", item.getItem().getID());
		rowItem.put("prioritize", item.getPriority());
		rowItem.put("image_index", item.getImageIndex());
		
		int insertID = (int)db.insert("income_open_used", null, rowItem);
		closeDatabase();
		return insertID;
	}
	
	@Override
	public void updateOpenUsedItem(int id, int itemID, int prioritize) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("income_id", id);
		rowItem.put("prioritize", prioritize);
		rowItem.put("image_index", -1);
		
		db.update("income_open_used", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
	}


	@Override
	public int deleteOpenUsedItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete("income_open_used", "income_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}


	@Override
	public ArrayList<OpenUsedItem> getOpenUsedItems() {
		ArrayList<OpenUsedItem> incomeItems = new ArrayList<OpenUsedItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		LOCK();
		
		Cursor c = db.query("income_open_used", null, null, null, null, null, "prioritize DESC");
		
		if (c.moveToFirst() != false) {
			do {
				OpenUsedItem item = new OpenUsedItem(getItem(c.getInt(1)));
				item.setID(c.getInt(0));
				item.setPriority(c.getInt(2));
				item.setImageIndex(c.getInt(3));
				incomeItems.add(item);
				
			} while (c.moveToNext());
		}
		
		UNLOCK();
		c.close();
		closeDatabase();
		return incomeItems;
	}
	
	
	public int updateSubCategory(int id, String name, int imgIndex) {
		return 0;
	}


	


}
