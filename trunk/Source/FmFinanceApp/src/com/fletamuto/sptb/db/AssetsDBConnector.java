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
import com.fletamuto.sptb.data.AssetsChangeItem;
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
		rowItem.put("amount", item.getAmount() * item.getCount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("sub_category", item.getSubCategory().getID());
		rowItem.put("repeat", item.getRepeat().getID());
		rowItem.put("extend", item.getExtendID());
		rowItem.put("state", item.getState());
		
		long ret = db.insert("assets", null, rowItem);
		item.setID((int)ret);
		closeDatabase();
		
		if (ret != -1) {
			AssetsChangeItem changeItem = new AssetsChangeItem((AssetsItem)financeItem);
			return addChangeStateItem(changeItem);
		}
			
		return ret;
	}
	
	/**
	 * 자산 DB테이블에서 수정
	 * @param item 자산 아이템
	 * @return the number of rows affected 
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
		rowItem.put("repeat", item.getRepeat().getID());
		rowItem.put("extend", item.getExtendID());
		rowItem.put("state", item.getState());
		
		long ret = db.update("assets", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getID())});
		closeDatabase();

		// 상황에 따라 변경되도록 수정
//		updateStateChangeItem(item);
		
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
		LOCK();
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
		UNLOCK();
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
		AssetsItem item = createAssetsItem(c.getInt(14), c.getInt(8));
		item.setID(c.getInt(0));
		try {
			item.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		item.setAmount(c.getLong(2));
		item.setTitle(c.getString(3));
		item.setMemo(c.getString(4));
		item.setCategory(c.getInt(5), c.getString(11));
		item.getRepeat().setID(c.getInt(7));
		item.setExtendID(c.getInt(8));
		item.setState(c.getInt(9));
		return item;
	}
	
	public AssetsItem createAssetsItem(int extendType, int extendID) {
		if (ItemDef.ExtendAssets.DEPOSIT == extendType) {
			return getDepositItem(extendID);
		}
		else if (ItemDef.ExtendAssets.SAVINGS == extendType) {
			return getSavingsItem(extendID);
		}
		else if (ItemDef.ExtendAssets.STOCK == extendType) {
			return getStockItem(extendID);
		}
		else if (ItemDef.ExtendAssets.FUND == extendType) {
			return getFundItem(extendID);
		}
		else if (ItemDef.ExtendAssets.ENDOWMENT_MORTGAGE== extendType) {
			return getInsuranceItem(extendID);
		}
		else {
			return new AssetsItem();
		}
	}
	
	private AssetsItem getStockItem(int stockID) {
		AssetsStockItem stock = new AssetsStockItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("assets_stock", null, "_id=?", new String[]{String.valueOf(stockID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			stock.setStockID(c.getInt(0));
			stock.setPeresentPrice(c.getLong(1));
			stock.setPrice(c.getLong(1));
			stock.setTotalCount(c.getLong(2));
		}
		c.close();
		closeDatabase();
		return stock;
	}

	public AssetsDepositItem getDepositItem(int depositID) {
		AssetsDepositItem depoist = new AssetsDepositItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("assets_deposit", null, "_id=?", new String[]{String.valueOf(depositID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			depoist.setDepositID(c.getInt(0));
			try {
				depoist.setExpiryDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			depoist.setRate(c.getInt(2));
			
			AccountItem account = DBMgr.getAccountItem(c.getInt(3));
			if (account == null) {
				account = new AccountItem();
			}
			depoist.setAccount(account);
		}
		c.close();
		closeDatabase();
		
		return depoist;
	}
	
	public AssetsSavingsItem getSavingsItem(int savingsID) {
		AssetsSavingsItem savings = new AssetsSavingsItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("assets_savings", null, "_id=?", new String[]{String.valueOf(savingsID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			savings.setSavingsID(c.getInt(0));
			try {
				savings.setExpiryDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			savings.setAccount(DBMgr.getAccountItem(c.getInt(2)));
			savings.setRate(c.getInt(3));
			savings.setPayment(c.getLong(4));
			
		}
		c.close();
		closeDatabase();
		
		return savings;
	}
	
	public AssetsInsuranceItem getInsuranceItem(int insuranceID) {
		AssetsInsuranceItem insurance = new AssetsInsuranceItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("assets_endowment_mortgage", null, "_id=?", new String[]{String.valueOf(insuranceID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			insurance.setInsuranceID(c.getInt(0));
			try {
				insurance.setExpiryDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			insurance.setPayment(c.getLong(2));
			insurance.setCompany(c.getString(3));
			
		}
		c.close();
		closeDatabase();
		
		return insurance;
	}
	
	public AssetsFundItem getFundItem(int fundID) {
		AssetsFundItem fund = new AssetsFundItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("assets_stock", null, "_id=?", new String[]{String.valueOf(fundID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			fund.setFundID(c.getInt(0));
			fund.setMeanPrice(c.getLong(1));
			fund.setStore(c.getString(2));
		}
		c.close();
		closeDatabase();
		return fund;
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
	public ArrayList<Category> getCategory(int extendItem) {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("assets_main_category", null, "extend_type=?", new String[]{String.valueOf(extendItem)}, null, null, null);
		
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
		
		Cursor c = db.query("assets_main_category", null, "_id=?", new String[]{String.valueOf(categoryID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			category = new Category(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
		}
		c.close();
		closeDatabase();
		return category;
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
		rowItem.put("rate", deposit.getRate());
		long extend = db.insert("assets_deposit", null, rowItem);
		closeDatabase();
		if (extend == -1){
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND DEPOSIT");
			return -1;
		}
		deposit.setExtendID((int)extend);
		return addItem(deposit);
	}
	
	public long updateExtendDeposit(AssetsDepositItem deposit) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		rowItem.put("expiry_date", deposit.getExpriyDateString());
		rowItem.put("account", deposit.getAccount().getID());
		rowItem.put("rate", deposit.getRate());
		db.update("assets_deposit", rowItem, "_id=?", new String[] {String.valueOf(deposit.getID())});
		closeDatabase();
		return updateItem(deposit);
	}

	public long addExtendSavings(AssetsSavingsItem savings) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("expiry_date", savings.getExpriyDateString());
		rowItem.put("account", savings.getAccount().getID());
		rowItem.put("rate", savings.getRate());
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
	
	public long updateExtendSavings(AssetsSavingsItem savings) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		rowItem.put("expiry_date", savings.getExpriyDateString());
		rowItem.put("account", savings.getAccount().getID());
		rowItem.put("rate", savings.getRate());
		rowItem.put("payment", savings.getPayment());
		db.update("assets_savings", rowItem, "_id=?", new String[] {String.valueOf(savings.getID())});
		closeDatabase();
		return updateItem(savings);
	}

	public long addExtendStock(AssetsStockItem stock) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		stock.setPeresentPrice(stock.getPrice());
		rowItem.put("present_price", stock.getPeresentPrice());
		rowItem.put("total_count", stock.getTotalCount());
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
		rowItem.put("store", stock.getStore());
		if (db.insert("assets_chage_stock", null, rowItem) == -1) {
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND STOCK");
			return -1;
		}
		closeDatabase();
		
		stock.setExtendID((int)extend);
		return addItem(stock);
	}
	
	public long updateExtendStock(AssetsStockItem stock) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		rowItem.put("present_price", stock.getPeresentPrice());
		rowItem.put("total_count", stock.getTotalCount());
		db.update("assets_stock", rowItem, "_id=?", new String[] {String.valueOf(stock.getID())});
		closeDatabase();
		return updateItem(stock);
	}

	public long addExtendFund(AssetsFundItem fund) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("mean_price", fund.getAmount());
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
	
	public long updateExtendFund(AssetsFundItem fund) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		rowItem.put("mean_price", fund.getAmount());
		rowItem.put("store", fund.getStore());
		db.update("assets_fund", rowItem, "_id=?", new String[] {String.valueOf(fund.getID())});
		return updateItem(fund);
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
	
	public long updateExtendInsurance(AssetsInsuranceItem insurance) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		rowItem.put("expiry_date", insurance.getExpriyDateString());
		rowItem.put("payment", insurance.getPayment());
		rowItem.put("company", insurance.getCompany());
		db.update("assets_endowment_mortgage", rowItem, "_id=?", new String[] {String.valueOf(insurance.getID())});
		closeDatabase();
		return updateItem(insurance);
	}

	private long addDefaultStateChangeItem(AssetsChangeItem item) {
		long ret = -1;
		AssetsChangeItem todayItem = getStateChangeItem(item.getID(), item.getChangeDate());
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("assets_id", item.getAssetsID());
		rowItem.put("change_date", item.getChangeDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("change_amount", item.getChangeAmount());
		rowItem.put("memo", item.getMemo());
		rowItem.put("count", item.getCount());
		rowItem.put("state", item.getState());
		
		if (todayItem == null) {
			ret = db.insert("assets_change_amount", null, rowItem);
		}
		else {
			ret = db.update("assets_change_amount", rowItem, "_id=?", new String[] {String.valueOf(item.getID())});
		}
		
		closeDatabase();
		return ret;
	}
	
	private long updateDefaultStateChangeItem(AssetsChangeItem item) {
		long ret = -1;
		AssetsChangeItem todayItem = getFirstStateChangeItem(item.getID());
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("assets_id", item.getAssetsID());
		rowItem.put("change_date", item.getChangeDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("change_amount", item.getChangeAmount());
		rowItem.put("memo", item.getMemo());
		rowItem.put("count", item.getCount());
		rowItem.put("state", item.getState());
		
		if (todayItem == null) {
			ret = db.insert("assets_change_amount", null, rowItem);
		}
		else {
			ret = db.update("assets_change_amount", rowItem, "_id=?", new String[] {String.valueOf(item.getID())});
		}
		
		closeDatabase();
		return ret;
	}
	
	public long addChangeStateItem(AssetsChangeItem item) {
		if (item.getAssetsID() == -1) {
			Log.e(LogTag.DB, ":: INVAILD ASSETS ITEM ID");
			return -1;
		}
		
//		int extendID = item.getExtendID();
//		if (extendID == -1) {
			return addDefaultStateChangeItem(item);
//		}
//	
//		return -1;
	}
	
	public long updateChangeStateItem(AssetsChangeItem item) {
		if (item.getID() == -1) {
			Log.e(LogTag.DB, ":: INVAILD ASSETS ITEM ID");
			return -1;
		}
		
//		int extendID = item.getExtendID();
//		if (extendID == -1) {
			return updateDefaultStateChangeItem(item);
//		}
//	
//		return -1;
	}
	
	public ArrayList<AssetsChangeItem> getStateItems(int id) {
		ArrayList<AssetsChangeItem> assetsItems = new ArrayList<AssetsChangeItem>(); 
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("assets_change_amount", null, "assets_id=?", new String[] {String.valueOf(id)}, null, null, "change_date ASC");
		
		if (c.moveToFirst() != false) {
			do {
				AssetsChangeItem assets = new AssetsChangeItem();
				assets.setID(c.getInt(0));
				try {
					assets.setChangeDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				assets.setAmount(c.getLong(3));
				assets.setChangeAmount(c.getLong(4));
				assets.setMemo(c.getString(5));
				assets.setCount(c.getInt(6));
				assets.setState(c.getInt(7));
				assetsItems.add(assets);
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
		
	}
	
	public AssetsChangeItem getStateChangeItem(int id, Calendar calendar) {
		AssetsChangeItem assets = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.valueOf(id), FinanceDataFormat.getDateFormat(calendar.getTime())}; 
		Cursor c = db.query("assets_change_amount", null, "assets_id=? AND strftime('%Y-%m-%d', change_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			assets = new AssetsChangeItem();
			assets.setID(c.getInt(0));
			try {
				assets.setChangeDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			assets.setAmount(c.getLong(3));
			assets.setAmount(c.getLong(4));
			assets.setMemo(c.getString(5));
			assets.setCount(c.getInt(6));
			assets.setState(c.getInt(7));
		}
		c.close();
		closeDatabase();
		return assets;
	}
	
	public AssetsChangeItem getFirstStateChangeItem(int id) {
		AssetsChangeItem assets = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.valueOf(id)}; 
		Cursor c = db.query("assets_change_amount", null, "assets_id=?", params, null, null, "change_date DESC");
		
		if (c.moveToFirst() != false) {
			assets = new AssetsChangeItem();
			assets.setID(c.getInt(0));
			try {
				assets.setChangeDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			assets.setAmount(c.getLong(3));
			assets.setAmount(c.getLong(4));
			assets.setMemo(c.getString(5));
			assets.setCount(c.getInt(6));
			assets.setState(c.getInt(7));
		}
		c.close();
		closeDatabase();
		return assets;
	}
	
	
	
	public long getMeanPrice(int id) {
		long amount = 0L;
		int count = 1;
		SQLiteDatabase db = openDatabase(READ_MODE);
		 
		String query = "SELECT SUM(amount*count), SUM(count) FROM assets_change_amount WHERE assets_id=?";
		Cursor c = db.rawQuery(query, new String[] {String.valueOf(id)});
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
			count = c.getInt(1);
			if (count == 0) count = 1;
		}
		c.close();
		closeDatabase();
		return amount / count;
	}

	/**
	 * 입력된 금액중 달에 마지막에 입력된 금액을 얻는다. 
	 * @param assetsID
	 * @param year
	 * @return
	 */
	public ArrayList<Long> getLastAmountMonthInYear(int assetsID, int year) {
		ArrayList<Long> amountArr = new ArrayList<Long>();
		long lastAmount = 0L;
		
		AssetsChangeItem item = getFirstStateChangeItem(assetsID);
		if (item != null) {
			Calendar cal = Calendar.getInstance();
			cal.set(year, 0, 1);
			if (item.getChangeDate().before(cal)) {
				lastAmount = item.getAmount() * item.getCount();
			}
		}
		
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		for (int month = 1; month <= 12; month++) {
			String[] params = {String.valueOf(assetsID),  String.format("%d-%02d", year, month)};
			String query = "SELECT amount, count FROM assets_change_amount WHERE assets_id=? AND strftime('%Y-%m', change_date)=? ORDER BY change_date DESC";
			Cursor c = db.rawQuery(query, params);
			
			if (c.moveToFirst() != false) {
				lastAmount = c.getLong(0) * c.getInt(1);
			}

			amountArr.add(lastAmount);
			c.close();
		}
		
		closeDatabase();
		return amountArr;
	}

	public long getPurchasePrice(int id) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE); 
		Cursor c = db.query("assets_change_amount", new String[] {"amount, count"}, "assets_id=?", new String[] {String.valueOf(id)}, null, null, "change_date");
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0) * c.getInt(1);
		}
		c.close();
		closeDatabase();
		return amount;
	}

	public long getLatestPrice(int id) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE); 
		Cursor c = db.query("assets_change_amount", new String[] {"amount, count"}, "assets_id=?", new String[] {String.valueOf(id)}, null, null, "change_date DESC");
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0) * c.getInt(1);
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

	public int addExpenseFromAssets(int expenseID, int assetsID) {
		if (expenseID == -1 || assetsID == -1) {
			return -1;
		}
		
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("assets_id", assetsID);
		rowItem.put("expense_id", expenseID);
		
		int ret = (int)db.insert("assets_expense", null, rowItem);
		closeDatabase();
		return ret;
	}

	public int addIncomeFromAssets(int incomeID, int assetsID) {
		if (incomeID == -1 || assetsID == -1) {
			return -1;
		}
		
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("assets_id", assetsID);
		rowItem.put("income_id", incomeID);
		
		int ret = (int)db.insert("assets_income", null, rowItem);
		closeDatabase();
		return ret;
	}

	public ArrayList<Integer> getExpenseFromAssets(int assetsID) {
		ArrayList<Integer> expenseID = new ArrayList<Integer>(); 
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("assets_expense", null, "assets_id=?", new String[] {String.valueOf(assetsID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseID.add(new Integer(c.getInt(2)));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseID;
	}

	public ArrayList<Integer> getIncomeFromAssets(int assetsID) {
		ArrayList<Integer> incomeID = new ArrayList<Integer>(); 
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("assets_income", null, "assets_id=?", new String[] {String.valueOf(assetsID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				incomeID.add(new Integer(c.getInt(2)));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return incomeID;
	}

	public long updateStock(AssetsStockItem stock) {
		long ret = -1;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("assets_id", stock.getID());
		rowItem.put("change_date", stock.getCreateDateString());
		rowItem.put("amount", stock.getAmount());
		rowItem.put("memo", stock.getMemo());
		rowItem.put("count", stock.getCount());
		rowItem.put("state", stock.getPriceType());
		
		ret = db.insert("assets_change_amount", null, rowItem);
		
		closeDatabase();
		
		if (stock.getPriceType() == AssetsStockItem.BUY) {
			AssetsStockItem origine = (AssetsStockItem) getItem(stock.getID());
			origine.setTotalCount(origine.getTotalCount() + stock.getCount());
			origine.setAmount(stock.getAmount() * origine.getTotalCount());
			origine.setPeresentPrice(stock.getAmount());
			updateExtendStock(origine);
		}
		else if (stock.getPriceType() == AssetsStockItem.SELL) {
			AssetsStockItem origine = (AssetsStockItem) getItem(stock.getID());
			origine.setTotalCount(origine.getTotalCount() - stock.getCount());
			origine.setAmount(stock.getAmount() * origine.getTotalCount());
			origine.setPeresentPrice(stock.getAmount());
			updateExtendStock(origine);
		}
		return ret;
	}


	
	
}
