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
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityCashServiceItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.data.LiabilityPersonLoanItem;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

/**
 * ��ä���� DB�� ����
 * @author yongbban
 * @version 1.0.0.0
 */
public class LiabilityDBConnector extends BaseFinanceDBConnector {
	
	/**
	 * �ڻ��� DB���̺� �߰�
	 * @param item ��ä ������
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addItem(FinanceItem financeItem) {
		LiabilityItem item = (LiabilityItem)financeItem;
		if (checkVaildItem(item) != DBDef.ValidError.SUCCESS) return -1; 
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("extend", item.getExtendID());
		
		long ret = db.insert("liability", null, rowItem);
		item.setID((int)ret);
		closeDatabase();
		
		if (ret != -1) {
			return addStateChangeItem(item);
		}
		return ret;
	}
	
	/**
	 * ��ä DB���̺��� ����
	 * @param item ��ä ������
	 * @return the number of rows affected , or -1 if an error occurred
	 */
	public long updateItem(FinanceItem financeItem) {
		LiabilityItem item = (LiabilityItem)financeItem;
		if (checkVaildItem(item) != DBDef.ValidError.SUCCESS) return -1; 
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
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
	 * ��� ��ä �������� DB���� �����´�.
	 * @return ArrayList<FinanceItem> ��ä������ ���
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
	 * ������ ��¥�� ��ä �������� �����´�.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> ��ä������ ���
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
	 * ������ ���̵��� ��ä �������� �����´�.
	 * @param id ������ ��ä ���̵�
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
	 * ��ä ��ü�� ����� DB���� ������ ������ ����
	 * @param c Cursor
	 * @return ��ä ������
	 */
	public LiabilityItem createLiabilityItem(Cursor c) {
		LiabilityItem item = createLiabilityItem(c.getInt(13), c.getInt(8));
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
		item.setExtendID(c.getInt(8));
		item.setState(c.getInt(9));
		
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
	
	private LiabilityItem getLoanItem(int stockID) {
		LiabilityLoanItem loan = new LiabilityLoanItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("liability_loan", null, "_id=?", new String[]{String.valueOf(stockID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			loan.setLoanID(c.getInt(0));
			loan.setCompany(DBMgr.getCompany(c.getInt(1)));
		}
		c.close();
		closeDatabase();
		return loan;
	}
	
	private LiabilityItem getPersonLoanItem(int personID) {
		LiabilityPersonLoanItem personLoan = new LiabilityPersonLoanItem();
		SQLiteDatabase db = openDatabase(READ_MODE);
		
		Cursor c = db.query("liability_loan", null, "_id=?", new String[]{String.valueOf(personID)}, null, null, null);
		
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
	 * ��ä ���� �з��� �߰��Ѵ�.
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
		
		ret = db.insert("liability_main_category", null, rowItem);
		closeDatabase();
		return ret;
	}
	
	/**
	 * ��ä ���� �з��� �߰��Ѵ�.
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
		
		ret = db.insert("liability_sub_category", null, rowItem);
		closeDatabase();
		return ret;
	}
	
	/**
	 * ��ä �����з� ����� ��´�.
	 * ī�װ� ��ü����
	 * @return ArrayList<Category> ���� �з�����Ʈ
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
	 *  �� ��ä�ݾ��� ��´�.
	 * @return long �� �ݾ�
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
	 * ������ ��¥�� ���� �� ��ä�ݾ��� ��´�.
	 * @return long �� �ݾ�
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
	 * ������ ��¥�� �߰��� ��ä ������ ������ ��´�.
	 * @return int ������ ��
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
	 * ������ ���̵��� �������� DB���� �����Ѵ�.
	 * @param id ������ ������ ���̵�
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
	 * ������ �����з�  �������� DB���� �����Ѵ�.
	 * @param id ������ �����з� ���̵�
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
		
		int result = db.update("liability_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	public long updateRepeat(int expenseID, int repeatID) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();

		rowItem.put("repeat", repeatID); // �ӽð�
		
		long ret = db.update("liability", rowItem, "_id=?", new String[] {String.valueOf(expenseID)});
		closeDatabase();
		return ret;
	}

	public long addExtendLoan(LiabilityLoanItem loan) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("finance_company", loan.getCompany().getID());
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
	
	private long addDefaultStateChangeItem(LiabilityItem item) {
		long ret = -1;
		LiabilityItem todayItem = (LiabilityItem) getStateChangeItem(item.getCreateDate());
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("liability_id", item.getID());
		rowItem.put("change_date", item.getCreateDateString());
		rowItem.put("amount", item.getAmount());
		rowItem.put("memo", item.getMemo());
		
		if (todayItem == null) {
			ret = db.insert("liability_change_amount", null, rowItem);
		}
		else {
			ret = db.update("liability_change_amount", rowItem, "_id=?", new String[] {String.valueOf(item.getID())});
		}
		
		closeDatabase();
		return ret;
	}
	
	public long addStateChangeItem(FinanceItem fItem) {
		LiabilityItem item = (LiabilityItem) fItem;
		if (item.getID() == -1) {
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
	
	public ArrayList<FinanceItem> getStateItems(int id) {
		ArrayList<FinanceItem> LiabilityItems = new ArrayList<FinanceItem>(); 
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("liability_change_amount", null, "liability_id=?", new String[] {String.valueOf(id)}, null, null, "change_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				LiabilityItem liability = new LiabilityItem();
				liability.setID(c.getInt(0));
				try {
					liability.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				liability.setAmount(c.getLong(3));
				liability.setMemo(c.getString(5));
				LiabilityItems.add(liability);
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return LiabilityItems;
	}
	
	public FinanceItem getStateChangeItem(Calendar calendar) {
		LiabilityItem liability = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())}; 
		Cursor c = db.query("liability_change_amount", null, "strftime('%Y-%m-%d', change_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			liability = new LiabilityItem();
			liability.setID(c.getInt(0));
			try {
				liability.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(2)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			liability.setAmount(c.getLong(3));
			liability.setMemo(c.getString(5));
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
	public int addOpenUsedItem(int id, int prioritize) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteOpenUsedItem(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<FinanceItem> getOpenUsedItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOpenUsedItem(int id, int itemID, int prioritize) {
		// TODO Auto-generated method stub
		
	}
	
}
