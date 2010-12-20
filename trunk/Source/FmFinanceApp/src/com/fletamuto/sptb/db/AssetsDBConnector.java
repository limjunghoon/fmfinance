package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsDepositItem;
import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.AssetsInsuranceItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산관련 DB를 관리
 * @author yongbban
 * @version 1.0.0.0
 */
public class AssetsDBConnector extends BaseFinanceDBConnector {
	
	/**
	 * 자산을 DB테이블에 추가
	 * @param item 자산 아이템
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addItem(FinanceItem financeItem) {
		AssetsItem item = (AssetsItem)financeItem;
		if (checkAssetsVaildItem(item) != DBDef.ValidError.SUCCESS) return -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("sub_category", item.getSubCategory().getID());
		rowItem.put("extend", item.getExtendID());
		
		long ret = db.insert("assets", null, rowItem);
		item.setID((int)ret);
		closeDatabase();
		
		if (ret != -1) {
			return addStateChangeItem(item);
		}
			
		return ret;
	}
	
	/**
	 * 자산 DB테이블에서 수정
	 * @param item 자산 아이템
	 * @return the number of rows affected , or -1 if an error occurred
	 */
	public long updateItem(FinanceItem financeItem) {
		AssetsItem item = (AssetsItem)financeItem;
		if (checkAssetsVaildItem(item) != DBDef.ValidError.SUCCESS) return 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("sub_category", item.getSubCategory().getID());
		rowItem.put("extend", item.getExtendID());
		
		long ret = db.update("assets", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getID())});
		closeDatabase();
		return ret;
	}
	
	@Override
	public long updateAmountFinanceItem(int id, long amount) {
		if (amount == 0) return 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		rowItem.put("amount", amount);
		long ret = db.update("assets", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return ret;
	}
	
	/**
	 * 모든 자산 아이템을 DB에서 가져온다.
	 * @return ArrayList<FinanceItem> 자산아이템 목록
	 */
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createAssetsItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
	}
	
	/**
	 * 지정된 날짜의 자산 아이템을 가져온다.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> 자산아이템 목록
	 */
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m-%d', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createAssetsItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
	}
	
	public ArrayList<FinanceItem> getItems(Calendar startCalendar, Calendar endCalendar) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String where = "assets.main_category=assets_main_category._id AND strftime('%Y-%m-%d', assets.create_date) BETWEEN '" + FinanceDataFormat.getDateFormat(startCalendar.getTime()) +"' AND '" + FinanceDataFormat.getDateFormat(endCalendar.getTime()) + "'";
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere(where);
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createAssetsItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
	}
	

	public ArrayList<FinanceItem> getItems(int year, int month) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month)};
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createAssetsItem(c));
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
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND assets.main_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createAssetsItem(c));
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
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, "assets.main_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createAssetsItem(c));
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
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND assets.sub_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createAssetsItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
	}
	
	/**
	 * 지정된 아이디의 자산 아이템을 가져온다.
	 * @param id 가져올 자산 아이디
	 */
	public FinanceItem getItem(int id) {
		FinanceItem item = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(id)};
		
		queryBilder.setTables("assets, assets_main_category");
		queryBilder.appendWhere("assets.main_category=assets_main_category._id");
		Cursor c = queryBilder.query(db, null, "assets._id=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			item = createAssetsItem(c);
		}
		c.close();
		closeDatabase();
		return item;
	}
	
	/**
	 * 자산 객체를 만들고 DB에서 가져온 값으로 설정
	 * @param c Cursor
	 * @return 자산 아이템
	 */
	public AssetsItem createAssetsItem(Cursor c) {
		AssetsItem item = createAssetsItem(c.getInt(12));
		item.setID(c.getInt(0));
		try {
			item.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		item.setAmount(c.getLong(2));
		item.setTitle(c.getString(3));
		item.setMemo(c.getString(4));
		item.setCategory(c.getInt(5), c.getString(9));
		item.setExtendID(c.getInt(12));
		return item;
	}
	
	public AssetsItem createAssetsItem(int extendID) {
		if (ItemDef.ExtendAssets.DEPOSIT == extendID) {
			return new AssetsDepositItem();
		}
		else if (ItemDef.ExtendAssets.SAVINGS == extendID) {
			return new AssetsSavingsItem();
		}
		else if (ItemDef.ExtendAssets.STOCK == extendID) {
			return new AssetsStockItem();
		}
		else if (ItemDef.ExtendAssets.FUND == extendID) {
			return new AssetsFundItem();
		}
		else if (ItemDef.ExtendAssets.ENDOWMENT_MORTGAGE== extendID) {
			return new AssetsInsuranceItem();
		}
		else {
			return new AssetsItem();
		}
	}
	
	public AssetsDepositItem getDepositItem(SQLiteDatabase db, int extendID) {
		AssetsDepositItem depoist = new AssetsDepositItem();
		
		Cursor c = db.query("assets_deposit", null, "_id=?", new String[]{String.valueOf(extendID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			depoist.setDepositID(c.getInt(0));
			try {
				depoist.setExpiryDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			AccountItem account = new AccountItem();
			account.setID(c.getInt(2));
			depoist.setAccount(account);
		}
		c.close();
		
		return depoist;
	}
	
	
	/**
	 * 자산 상위 분류를 추가한다.
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
		rowItem.put("image_index", 0);
		rowItem.put("extend_type", category.getExtndType());
		rowItem.put("type", category.getUIType());
		
		ret = db.insert("assets_main_category", null, rowItem);
		closeDatabase();
		return ret;
	}
	
	/**
	 * 자산 하위 분류를 추가한다.
	 * @param mainCategoryID 상위분류 아이디
	 * @param name 분류 이름
	 * @return the row ID of the newly inserted row, or -1 if an error occurred 
	 */
	public long addSubCategory(long mainCategoryID, String name) {
		long ret = -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		rowItem.put("main_id", mainCategoryID);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		ret = db.insert("assets_sub_category", null, rowItem);
		closeDatabase();
		return ret;
	}
	
	/**
	 * 자산 상위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 상위 분류리스트
	 */
	public ArrayList<Category> getCategory() {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("assets_main_category", null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				Category item = new Category(c.getInt(0), c.getString(1));
				item.setExtndType(c.getInt(4));
				category.add(item);
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		
		return category;
	}
	
	@Override
	public Category getCategory(int extendItem) {
		Category item = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("assets_main_category", null, "extend_type=?", new String[]{String.valueOf(extendItem)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			item = new Category(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
		}
		c.close();
		closeDatabase();
		return item;
	}

	/**
	 * 자산 하위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 하위분류리스트
	 */
	public ArrayList<Category> getSubCategory(long mainCategoryId) {
		ArrayList<Category> subCategory = new ArrayList<Category>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("assets_sub_category", null, "main_id=?", new String[]{String.valueOf(mainCategoryId)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				Category item = new Category(c.getInt(0), c.getString(1));
				subCategory.add(item);
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return subCategory;
	}

	/**
	 *  총 자산금액을 얻는다.
	 * @return long 총 금액
	 */
	public long getTotalAmount() {
		SQLiteDatabase db = openDatabase(READ_MODE);
		String query = "SELECT SUM(amount) FROM assets";
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
	 * 지정된 날짜에 대한 총 자산금액을 얻는다.
	 * @return long 총 금액
	 */
	public long getTotalAmountDay(Calendar calendar) {
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)), 
				String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT SUM(amount) FROM assets WHERE strftime('%Y-%m-%d', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		long amount = 0L;
		
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
		String query = "SELECT SUM(amount) FROM assets WHERE strftime('%Y-%m', create_date)=?";
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
		String query = "SELECT SUM(amount) FROM assets WHERE main_category=? AND strftime('%Y-%m', create_date)=?";
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
			String query = "SELECT SUM(amount) FROM assets WHERE main_category=? AND strftime('%Y-%m', create_date)=?";
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
		String query = "SELECT SUM(amount) FROM assets WHERE strftime('%Y', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}

	/**
	 * 지정된 날짜의 추가된 자산 아이템 갯수를 얻는다.
	 * @return int 아이템 수
	 */
	public int getItemCount(Calendar calendar) {
		int count = 0;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)), 
				String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT COUNT(*) FROM assets WHERE strftime('%Y-%m-%d', create_date)=?";
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
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete("assets", "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}

	/**
	 * 지정된 상위분류  아이템을 DB에서 삭제한다.
	 * @param id 삭제할 상위분류 아이디
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteCategory(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete("assets_main_category", "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		
		deleteSubCategoryFromMainID(id);
		return result;
	}
	
	/**
	 * 지정된 아이디의 상위분류 이름을 변경한다.
	 * @param id 변경할 상위분류 아이디
	 * @param name 변경할 이름
	 * @return int the number of rows affected 
	 */
	public int deleteSubCategoryFromMainID(int mainCategoryID) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete("assets_sub_category", "main_id=?", new String[] {String.valueOf(mainCategoryID)});
		closeDatabase();
		return result;
	}

	/**
	 * 지정된 하위분류  아이템을 DB에서 삭제한다.
	 * @param id 삭제할 하위분류 아이디
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deleteSubCategory(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete("assets_sub_category", "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	/**
	 * 지정된 아이디의 상위분류 이름을 변경한다.
	 * @param id 변경할 상위분류 아이디
	 * @param name 변경할 이름
	 * @return int the number of rows affected 
	 */
	public int updateCategory(int id, String name) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		int result = db.update("assets_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	/**
	 * 지정된 아이디의 하위분류 이름을 변경한다.
	 * @param id 변경할 하위분류 아이디
	 * @param name 변경할 이름
	 * @return int the number of rows affected 
	 */
	public int updateSubCategory(int id, String name) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		int result = db.update("assets_sub_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	/**
	 * 자산 아이템의 유효성을 확인한다.
	 * @param ExpenseItem 지출 아이템
	 * @return int 성공값 
	 */
	public int checkAssetsVaildItem(AssetsItem item) {
		int ret = checkVaildItem(item);
		if (ret != DBDef.ValidError.SUCCESS) {
			return ret;
		}

		return DBDef.ValidError.SUCCESS; 
	}
	
	public long updateRepeat(int expenseID, int repeatID) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();

		rowItem.put("repeat", repeatID); // 임시값
		
		long ret = db.update("assets", rowItem, "_id=?", new String[] {String.valueOf(expenseID)});
		closeDatabase();
		return ret;
	}

	public long addExtendDeposit(AssetsDepositItem deposit) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("expiry_date", deposit.getExpriyDateString());
		rowItem.put("account", deposit.getAccount().getID());
		long extend = db.insert("assets_deposit", null, rowItem);
		closeDatabase();
		if (extend == -1){
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND DEPOSIT");
			return -1;
		}
		deposit.setExtendID((int)extend);
		return addItem(deposit);
	}

	public long addExtendSavings(AssetsSavingsItem savings) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("expiry_date", savings.getExpriyDateString());
		rowItem.put("account", savings.getAccount().getID());
		rowItem.put("payment", savings.getPayment());
		long extend = db.insert("assets_savings", null, rowItem);
		closeDatabase();
		if (extend == -1){
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND DEPOSIT");
			return -1;
		}
		savings.setExtendID((int)extend);
		return addItem(savings);
	}

	public long addExtendStock(AssetsStockItem stock) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("present_price", stock.getPrice());
		long extend = db.insert("assets_stock", null, rowItem);
		
		if (extend == -1){
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND STOCK");
			return -1;
		}
		
		rowItem.clear();
		rowItem.put("stock_id", extend);
		rowItem.put("change_date", stock.getCreateDateString());
		rowItem.put("count", stock.getCount());
		rowItem.put("stock_price", stock.getPrice());
		rowItem.put("price_type", AssetsStockItem.BUY);
		rowItem.put("store", stock.getPrice());
		if (db.insert("assets_chage_stock", null, rowItem) == -1) {
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND STOCK");
			return -1;
		}
		closeDatabase();
		
		stock.setExtendID((int)extend);
		return addItem(stock);
	}

	public long addExtendFund(AssetsFundItem fund) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("mena_price", fund.getAmount());
		rowItem.put("store", fund.getStore());
		long extend = db.insert("assets_fund", null, rowItem);
		
		if (extend == -1){
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND STOCK");
			return -1;
		}
		
		rowItem.clear();
		rowItem.put("fund_id", extend);
		rowItem.put("change_date", fund.getCreateDateString());
		rowItem.put("price", fund.getAmount());
		rowItem.put("price_type", AssetsStockItem.BUY);
		
		if (db.insert("assets_change_fund", null, rowItem) == -1) {
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND FUND");
			return -1;
		}
		closeDatabase();
		
		fund.setExtendID((int)extend);
		return addItem(fund);
	}

	public long addExtendInsurance(AssetsInsuranceItem insurance) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("expiry_date", insurance.getExpriyDateString());
		rowItem.put("payment", insurance.getPayment());
		rowItem.put("company", insurance.getCompany());
		long extend = db.insert("assets_endowment_mortgage", null, rowItem);
		closeDatabase();
		
		if (extend == -1){
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND STOCK");
			return -1;
		}
		
		insurance.setExtendID((int)extend);
		return addItem(insurance);
	}

	private long addDefaultStateChangeItem(AssetsItem item) {
		long ret = -1;
		AssetsItem todayItem = (AssetsItem) getStateChangeItem(item.getCreateDate());
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("assets_id", item.getID());
		rowItem.put("change_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("memo", item.getMemo());
		
		if (todayItem == null) {
			ret = db.insert("assets_change_amount", null, rowItem);
		}
		else {
			ret = db.update("assets_change_amount", rowItem, "_id=?", new String[] {String.valueOf(item.getID())});
		}
		
		closeDatabase();
		return ret;
	}
	
	public long addStateChangeItem(FinanceItem fItem) {
		AssetsItem item = (AssetsItem) fItem;
		if (item.getID() == -1) {
			Log.e(LogTag.DB, ":: INVAILD ASSETS ITEM ID");
			return -1;
		}
		
//		int extendID = item.getExtendID();
//		
//		if (extendID == -1) {
			return addDefaultStateChangeItem(item);
//		}
//	
//		return -1;
	}
	
	public ArrayList<FinanceItem> getStateItems(int id) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>(); 
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("assets_change_amount", null, "assets_id=?", new String[] {String.valueOf(id)}, null, null, "change_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				AssetsItem assets = new AssetsItem();
				assets.setID(c.getInt(0));
				try {
					assets.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				assets.setAmount(c.getLong(3));
				assets.setMemo(c.getString(5));
				assetsItems.add(assets);
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
		
	}
	
	public FinanceItem getStateChangeItem(Calendar calendar) {
		AssetsItem assets = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())}; 
		Cursor c = db.query("assets_change_amount", null, "strftime('%Y-%m-%d', change_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			assets = new AssetsItem();
			assets.setID(c.getInt(0));
			try {
				assets.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			assets.setAmount(c.getLong(3));
			assets.setMemo(c.getString(5));
		}
		c.close();
		closeDatabase();
		return assets;
		
	}

	public ArrayList<Long> getTotalAssetAmountMonthInYear(int assetsID, int year) {
		ArrayList<Long> amountArr = new ArrayList<Long>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		long lastAmount = 0L;
		
		for (int month = 1; month <= 12; month++) {
			String[] params = {String.valueOf(assetsID),  String.format("%d-%02d", year, month)};
			String query = "SELECT amount FROM assets_change_amount WHERE assets_id=? AND strftime('%Y-%m', change_date)=? ORDER BY change_date DESC";
			Cursor c = db.rawQuery(query, params);
			
			if (c.moveToFirst() != false) {
				lastAmount = c.getLong(0);
				amountArr.add(lastAmount);
			}
			else {
				amountArr.add(lastAmount);
			}
			c.close();
		}
		
		closeDatabase();
		return amountArr;
	}

	public long getPurchasePrice(int id) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE); 
		Cursor c = db.query("assets_change_amount", new String[] {"amount"}, "assets_id=?", new String[] {String.valueOf(id)}, null, null, "change_date");
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}

	public long getLatestPrice(int id) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE); 
		Cursor c = db.query("assets_change_amount", new String[] {"amount"}, "assets_id=?", new String[] {String.valueOf(id)}, null, null, "change_date DESC");
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}

	@Override
	public int addOpneUsedItem(int id) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
