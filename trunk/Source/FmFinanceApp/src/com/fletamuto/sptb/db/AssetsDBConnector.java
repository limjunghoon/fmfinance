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
	 * 자산 DB테이블에서 수정
	 * @param item 자산 아이템
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
	 * 모든 자산 아이템을 DB에서 가져온다.
	 * @return ArrayList<FinanceItem> 자산아이템 목록
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
	 * 지정된 날짜의 자산 아이템을 가져온다.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> 자산아이템 목록
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
	 * 지정된 아이디의 자산 아이템을 가져온다.
	 * @param id 가져올 자산 아이디
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
	 * 자산 객체를 만들고 DB에서 가져온 값으로 설정
	 * @param c Cursor
	 * @return 자산 아이템
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
	 * 자산 상위 분류를 추가한다.
	 * @param name 분류 이름
	 * @return the row ID of the newly inserted row, or -1 if an error occurred 
	 */
	public long addCategory(String name) {
		long ret = -1;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		ret = db.insert("assets_main_category", null, rowItem);
		db.close();
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
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		rowItem.put("main_id", mainCategoryID);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		ret = db.insert("assets_sub_category", null, rowItem);
		db.close();
		return ret;
	}
	
	/**
	 * 자산 상위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 상위 분류리스트
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
	 * 자산 하위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 하위분류리스트
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
	 *  총 자산금액을 얻는다.
	 * @return long 총 금액
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
	 * 지정된 날짜에 대한 총 자산금액을 얻는다.
	 * @return long 총 금액
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
	 * 지정된 날짜의 추가된 자산 아이템 갯수를 얻는다.
	 * @return int 아이템 수
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
	 * 지정된 아이디의 아이템을 DB에서 삭제한다.
	 * @param id 삭제할 아이템 아이디
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
	 * 지정된 상위분류  아이템을 DB에서 삭제한다.
	 * @param id 삭제할 상위분류 아이디
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
	 * 지정된 아이디의 상위분류 이름을 변경한다.
	 * @param id 변경할 상위분류 아이디
	 * @param name 변경할 이름
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
	 * 지정된 하위분류  아이템을 DB에서 삭제한다.
	 * @param id 삭제할 하위분류 아이디
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
	 * 지정된 아이디의 상위분류 이름을 변경한다.
	 * @param id 변경할 상위분류 아이디
	 * @param name 변경할 이름
	 * @return int the number of rows affected 
	 */
	public int updateCategory(int id, String name) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		int result = db.update("assets_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		db.close();
		return result;
	}
	
	/**
	 * 지정된 아이디의 하위분류 이름을 변경한다.
	 * @param id 변경할 하위분류 아이디
	 * @param name 변경할 이름
	 * @return int the number of rows affected 
	 */
	public int updateSubCategory(int id, String name) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", 0);
		
		int result = db.update("assets_sub_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		db.close();
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
		if (item.getSubCategory() == null || item.getSubCategory().getId() == -1) {
			return DBDef.ValidError.SUB_CATEGORY_INVAlID;
		}
		return DBDef.ValidError.SUCCESS; 
	}
	
	public long updateRepeat(int expenseID, int repeatID) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();

		rowItem.put("repeat", repeatID); // 임시값
		
		long ret = db.update("assets", rowItem, "_id=?", new String[] {String.valueOf(expenseID)});
		db.close();
		return ret;
	}
}
