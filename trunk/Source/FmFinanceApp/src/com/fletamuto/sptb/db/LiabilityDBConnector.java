package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityCashServiceItem;
import com.fletamuto.sptb.data.LiabilityChangeItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.data.LiabilityPersonLoanItem;
import com.fletamuto.sptb.data.OpenUsedItem;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

/**
 * 부채관련 DB를 관리
 * @author yongbban
 * @version 1.0.0.0
 */
public class LiabilityDBConnector extends BaseFinanceDBConnector {
	
	/**
	 * 자산을 DB테이블에 추가
	 * @param item 부채 아이템
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addItem(FinanceItem financeItem) {
		LiabilityItem item = (LiabilityItem)financeItem;
		if (checkVaildItem(item) != DBDef.ValidError.SUCCESS) return -1; 
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("expiry_date", item.getExpiryDateString());
		rowItem.put("payment_date", item.getPaymentDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("orign_amount", item.getOrignAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("extend", item.getExtendID());
		
		
		long ret = db.insert("liability", null, rowItem);
		item.setID((int)ret);
		closeDatabase();
		
//		if (ret != -1) {
//			return addChangeStateItem(item);
//		}
		return ret;
	}
	
	/**
	 * 부채 DB테이블에서 수정
	 * @param item 부채 아이템
	 * @return the number of rows affected , or -1 if an error occurred
	 */
	public long updateItem(FinanceItem financeItem) {
		LiabilityItem item = (LiabilityItem)financeItem;
		if (checkVaildItem(item) != DBDef.ValidError.SUCCESS) return -1; 
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("expiry_date", item.getExpiryDateString());
		rowItem.put("payment_date", item.getPaymentDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("orign_amount", item.getOrignAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("extend", item.getExtendID());
		
		long ret = db.update("liability", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getID())});
		closeDatabase();
		return ret;
	}
	
	@Override
	public long updateAmountFinanceItem(int id, long amount) {
		if (amount == 0) return 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("amount", amount);
		long ret = db.update("liability", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return ret;
	}
	
	/**
	 * 모든 부채 아이템을 DB에서 가져온다.
	 * @return ArrayList<FinanceItem> 부채아이템 목록
	 */
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> LiabilityItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere("liability.main_category=liability_main_category._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				LiabilityItems.add(createLiabilityItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return LiabilityItems;
	}
	
	/**
	 * 지정된 날짜의 부채 아이템을 가져온다.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> 부채아이템 목록
	 */
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		ArrayList<FinanceItem> LiabilityItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere("liability.main_category=liability_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m-%d', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				LiabilityItems.add(createLiabilityItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return LiabilityItems;
	}
	
	public ArrayList<FinanceItem> getItems(Calendar startCalendar, Calendar endCalendar) {
		ArrayList<FinanceItem> LiabilityItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String where = "liability.main_category=liability_main_category._id AND strftime('%Y-%m-%d', liability.create_date) BETWEEN '" + FinanceDataFormat.getDateFormat(startCalendar.getTime()) +"' AND '" + FinanceDataFormat.getDateFormat(endCalendar.getTime()) + "'";
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere(where);
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				LiabilityItems.add(createLiabilityItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return LiabilityItems;
	}
	
	public ArrayList<FinanceItem> getItems(int year, int month) {
		ArrayList<FinanceItem> assetsItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month)};
		
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere("liability.main_category=liability_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createLiabilityItem(c));
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
		
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere("liability.main_category=liability_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND liability.main_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createLiabilityItem(c));
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
		
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere("liability.main_category=liability_main_category._id");
		Cursor c = queryBilder.query(db, null, "liability.main_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createLiabilityItem(c));
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
		
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere("liability.main_category=liability_main_category._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND liability.sub_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				assetsItems.add(createLiabilityItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return assetsItems;
	}
	
	/**
	 * 지정된 아이디의 부채 아이템을 가져온다.
	 * @param id 가져올 부채 아이디
	 */
	public FinanceItem getItem(int id) {
		FinanceItem item = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(id)};
		
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere("liability.main_category=liability_main_category._id");
		Cursor c = queryBilder.query(db, null, "liability._id=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			item = createLiabilityItem(c);
		}
		c.close();
		closeDatabase();
		return item;
	}
	
	/**
	 * 부채 객체를 만들고 DB에서 가져온 값으로 설정
	 * @param c Cursor
	 * @return 부채 아이템
	 */
	public LiabilityItem createLiabilityItem(Cursor c) {
		LiabilityItem item = createLiabilityItem(c.getInt(17), c.getInt(13));
		item.setID(c.getInt(0));
		try {
			item.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			item.setExpiryDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		String paymentDate = c.getString(3);
		
		if (paymentDate != null) {
			try {
				item.setPaymentDate(FinanceDataFormat.DATE_FORMAT.parse(paymentDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		item.setAmount(c.getLong(4));
		item.setOrignAmount(c.getLong(5));
		item.setTitle(c.getString(6));
		item.setMemo(c.getString(7));
		item.setCategory(c.getInt(8), c.getString(14));
		item.setExtendID(c.getInt(11));
		item.setState(c.getInt(12));
		
		return item;
	}
	
	public LiabilityItem createLiabilityItem(int extendType, int extendID) {
		if (ItemDef.ExtendLiablility.LOAN == extendType) {
			return getLoanItem(extendID);
		}
		else if (ItemDef.ExtendLiablility.CASH_SERVICE == extendType) {
			return getCashServiceItem(extendID);
		}
		else if (ItemDef.ExtendLiablility.PERSON_LOAN == extendType) {
			return getPersonLoanItem(extendID);
		}
		else {
			return new LiabilityItem();
		}
	}
	
	private LiabilityItem getLoanItem(int loanID) {
		LiabilityLoanItem loan = new LiabilityLoanItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("liability_loan", null, "_id=?", new String[]{String.valueOf(loanID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			loan.setLoanID(c.getInt(0));
			loan.setCompany(DBMgr.getCompany(c.getInt(1)));
			loan.setAccount(DBMgr.getAccountItem(c.getInt(2)));
		}
		c.close();
		closeDatabase();
		return loan;
	}
	
	private LiabilityItem getPersonLoanItem(int personID) {
		LiabilityPersonLoanItem personLoan = new LiabilityPersonLoanItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("liability_person_loan", null, "_id=?", new String[]{String.valueOf(personID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			personLoan.setPersonLoanID(c.getInt(0));
			personLoan.setLoanPeopleName(c.getString(1));
		}
		c.close();
		closeDatabase();
		return personLoan;
	}
	
	private LiabilityItem getCashServiceItem(int cashServiceID) {
		LiabilityCashServiceItem cashService = new LiabilityCashServiceItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("liability_cash_service", null, "_id=?", new String[]{String.valueOf(cashServiceID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			cashService.setCashServiceID(c.getInt(0));
			cashService.setCard(DBMgr.getCardItem(c.getInt(1)));
		}
		c.close();
		closeDatabase();
		return cashService;
	}
	
	/**
	 * 부채 상위 분류를 추가한다.
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
		
		ret = db.insert("liability_main_category", null, rowItem);
		closeDatabase();
		return ret;
	}
	
	/**
	 * 부채 하위 분류를 추가한다.
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
		
		ret = db.insert("liability_sub_category", null, rowItem);
		closeDatabase();
		return ret;
	}
	
	/**
	 * 부채 상위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 상위 분류리스트
	 */
	public ArrayList<Category> getCategory() {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("liability_main_category", null, null, null, null, null, null);
		
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
		
		Cursor c = db.query("liability_main_category", null, "extend_type=?", new String[]{String.valueOf(extendItem)}, null, null, null);
		
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
		
		Cursor c = db.query("liability_main_category", null, "_id=?", new String[]{String.valueOf(categoryID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			category = new Category(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
		}
		c.close();
		closeDatabase();
		return category;
	}

	
	/**
	 *  총 부채금액을 얻는다.
	 * @return long 총 금액
	 */
	public long getTotalAmount() {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String query = "SELECT SUM(amount) FROM liability";
		Cursor c = db.rawQuery(query, null);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}
	
	/**
	 * 지정된 날짜에 대한 총 부채금액을 얻는다.
	 * @return long 총 금액
	 */
	public long getTotalAmountDay(Calendar calendar) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)), 
				String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT SUM(amount) FROM liability WHERE strftime('%Y-%m-%d', create_date)=?";
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
		String query = "SELECT SUM(amount) FROM liability WHERE strftime('%Y-%m', create_date)=?";
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
		String query = "SELECT SUM(amount) FROM liability WHERE main_category=? AND strftime('%Y-%m', create_date)=?";
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
			String query = "SELECT SUM(amount) FROM liability WHERE main_category=? AND strftime('%Y-%m', create_date)=?";
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
		String query = "SELECT SUM(amount) FROM liability WHERE strftime('%Y', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}

	/**
	 * 지정된 날짜의 추가된 부채 아이템 갯수를 얻는다.
	 * @return int 아이템 수
	 */
	public int getItemCount(Calendar calendar) {
		int count = 0;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.valueOf(calendar.get(Calendar.YEAR)), 
				String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};
		String query = "SELECT COUNT(*) FROM liability WHERE strftime('%Y-%m-%d', create_date)=?";
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
		result = db.delete("liability", "_id=?", new String[] {String.valueOf(id)});
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
		result = db.delete("liability_main_category", "_id=?", new String[] {String.valueOf(id)});
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
		
		int result = db.update("liability_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	public long updateRepeat(int expenseID, int repeatID) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();

		rowItem.put("repeat", repeatID); // 임시값
		
		long ret = db.update("liability", rowItem, "_id=?", new String[] {String.valueOf(expenseID)});
		closeDatabase();
		return ret;
	}

	public long addExtendLoan(LiabilityLoanItem loan) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("finance_company", loan.getCompany().getID());
		rowItem.put("account_id", loan.getAccount().getID());
		long extend = db.insert("liability_loan", null, rowItem);
		closeDatabase();
		if (extend == -1){
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND DEPOSIT");
			return -1;
		}
		loan.setExtendID((int)extend);
		return addItem(loan);
	}

	public long addExtendCashService(LiabilityCashServiceItem cashService) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("card", cashService.getCard().getID());
		long extend = db.insert("liability_cash_service", null, rowItem);
		closeDatabase();
		if (extend == -1){
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND DEPOSIT");
			return -1;
		}
		cashService.setExtendID((int)extend);
		return addItem(cashService);
	}

	public long addExtendPersonLoan(LiabilityPersonLoanItem personLoan) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("name", personLoan.getLoanPeopleName());
		long extend = db.insert("liability_person_loan", null, rowItem);
		closeDatabase();
		if (extend == -1){
			Log.e(LogTag.DB, ":: FAIL TO CREATE EXTEND DEPOSIT");
			return -1;
		}
		personLoan.setExtendID((int)extend);
		return addItem(personLoan);
	}
	
	private long addDefaultStateChangeItem(LiabilityChangeItem item) {
		long ret = -1;
		LiabilityChangeItem todayItem = (LiabilityChangeItem) getStateChangeItem(item.getChangeDate());
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("liability_id", item.getLiabilityID());
		rowItem.put("change_date", item.getChangeDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("principal", item.getPrincipal().getID());
		rowItem.put("interest", item.getInterest().getID());
		rowItem.put("memo", item.getMemo());
	//	rowItem.put("state", item.getMemo());
		
		if (todayItem == null) {
			ret = db.insert("liability_change_amount", null, rowItem);
		}
		else {
			ret = db.update("liability_change_amount", rowItem, "_id=?", new String[] {String.valueOf(item.getID())});
		}
		
		closeDatabase();
		return ret;
	}
	
	public long addChangeStateItem(LiabilityChangeItem item) {
		if (item.getLiabilityID() == -1) {
			Log.e(LogTag.DB, ":: INVAILD liability ITEM ID");
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
	
	public ArrayList<LiabilityChangeItem> getStateItems(int id) {
		ArrayList<LiabilityChangeItem> LiabilityItems = new ArrayList<LiabilityChangeItem>(); 
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("liability_change_amount", null, "liability_id=?", new String[] {String.valueOf(id)}, null, null, "change_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				LiabilityChangeItem liability = new LiabilityChangeItem();
				liability.setID(c.getInt(0));
				liability.setLiabilityID(c.getInt(1));
				try {
					liability.setChangeDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				liability.setAmount(c.getLong(3));
				
				int principalID = c.getInt(4);
				if (principalID != -1) {
					ExpenseItem principal = (ExpenseItem) DBMgr.getItem(ExpenseItem.TYPE, principalID);
					if (principal != null) {
						liability.setPrincipal(principal);
					}
				}
				
				int interestID = c.getInt(5);
				if (interestID != -1) {
					ExpenseItem interest = (ExpenseItem) DBMgr.getItem(ExpenseItem.TYPE, interestID);
					if (interest != null) {
						liability.setInterest(interest);
					}
				}
				
				liability.setMemo(c.getString(6));
				LiabilityItems.add(liability);
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return LiabilityItems;
	}
	
	public LiabilityChangeItem getStateChangeItem(Calendar calendar) {
		LiabilityChangeItem liability = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())}; 
		Cursor c = db.query("liability_change_amount", null, "strftime('%Y-%m-%d', change_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			liability = new LiabilityChangeItem();
			liability.setID(c.getInt(0));
			liability.setLiabilityID(c.getInt(1));
			try {
				liability.setChangeDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			liability.setAmount(c.getLong(3));
			
			int principalID = c.getInt(4);
			if (principalID != -1) {
				ExpenseItem principal = (ExpenseItem) DBMgr.getItem(ExpenseItem.TYPE, principalID);
				if (principal != null) {
					liability.setPrincipal(principal);
				}
			}
			
			int interestID = c.getInt(5);
			if (interestID != -1) {
				ExpenseItem interest = (ExpenseItem) DBMgr.getItem(ExpenseItem.TYPE, interestID);
				if (interest != null) {
					liability.setInterest(interest);
				}
			}
			
			liability.setMemo(c.getString(6));
		}
		c.close();
		closeDatabase();
		return liability;
		
	}

	public ArrayList<Long> getTotalLiabilityAmountMonthInYear(int liabilityID, int year) {
		ArrayList<Long> amountArr = new ArrayList<Long>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		long lastAmount = 0L;
		
		for (int month = 1; month <= 12; month++) {
			String[] params = {String.valueOf(liabilityID), String.format("%d-%02d", year, month)};
			String query = "SELECT amount FROM liability_change_amount WHERE liability_id=? AND strftime('%Y-%m', change_date)=? ORDER BY change_date DESC";
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
		Cursor c = db.query("liability_change_amount", new String[] {"amount"}, "liability_id=?", new String[] {String.valueOf(id)}, null, null, "change_date");
		
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
		Cursor c = db.query("liability_change_amount", new String[] {"amount"}, "liability_id=?", new String[] {String.valueOf(id)}, null, null, "change_date DESC");
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}

	@Override
	public int addOpenUsedItem(OpenUsedItem item) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteOpenUsedItem(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<OpenUsedItem> getOpenUsedItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOpenUsedItem(int id, int itemID, int prioritize) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<FinanceItem> getCompletionAll() {
		ArrayList<FinanceItem> liabilityItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(FinanceItem.STATE_COMPLEATE)};
		
		queryBilder.setTables("liability, liability_main_category");
		queryBilder.appendWhere("liability.main_category=liability_main_category._id");
		Cursor c = queryBilder.query(db, null, "liability.state=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				liabilityItems.add(createLiabilityItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return liabilityItems;
	}
	
	public int updateSubCategory(int id, String name, int imgIndex) {
		return 0;
	}
	
}
