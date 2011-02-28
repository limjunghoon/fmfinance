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
import com.fletamuto.sptb.data.OpenUsedItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentCardMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.data.UISelectItem;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

/**
 * 지출관련 DB를 관리
 * @author yongbban
 * @version 1.0.0.0
 */
public class ExpenseDBConnector extends BaseFinanceDBConnector {
	
	/**
	 * 지출을 DB테이블에 추가
	 * @param item 지출 아이템
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addItem(FinanceItem financeItem) {
		ExpenseItem item = (ExpenseItem)financeItem;
		if (checkExpenseVaildItem(item) != DBDef.ValidError.SUCCESS) return -1;
		
		if (precedenceAddItem(item) == false) {
			return -1;
		}
		
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		if (item.isOpenUsedItem() == false) {
			rowItem.put("create_date", item.getCreateDateString());
			rowItem.put("create_time", "21:20:00"); // 임시값
		}
		
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("sub_category", item.getSubCategory().getID());
		rowItem.put("payment_method", item.getPaymentMethod().getID()); // 임시값
		rowItem.put("repeat", item.getRepeat().getID()); // 임시값
		rowItem.put("tag", item.getTag().getID());
		
		long ret = db.insert("expense", null, rowItem);
		item.setID((int)ret);
		closeDatabase();
		
		return ret;
	}


	/**
	 * 지출 DB테이블에서 수정
	 * @param item 지출 아이템
	 * @return the number of rows affected , or -1 if an error occurred
	 */
	public long updateItem(FinanceItem financeItem) { 
		ExpenseItem item = (ExpenseItem)financeItem;
		if (checkExpenseVaildItem(item) != DBDef.ValidError.SUCCESS) return -1;
		
		if (precedenceUpdateItem(item) == false) {
			return -1;
		}
		
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("create_time", "21:20:00");
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getID());
		rowItem.put("sub_category", item.getSubCategory().getID());
		rowItem.put("payment_method", item.getPaymentMethod().getID()); // 임시값
		rowItem.put("repeat", item.getRepeat().getID()); // 임시값
		rowItem.put("tag", item.getTag().getID());
		
		long ret = db.update("expense", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getID())});
		closeDatabase();
		return ret;
	}
	
	@Override
	public long updateAmountFinanceItem(int id, long amount) {
		if (amount == 0) return 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("amount", amount);
		long ret = db.update("expense", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return ret;
	}
	
	/**
	 * 모든 지출 아이템을 DB에서 가져온다.
	 * @return ArrayList<FinanceItem> 지출아이템 목록
	 */
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, null, null, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}
	
	/**
	 * 지정된 날짜의 지출 아이템을 가져온다.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> 지출아이템 목록
	 */
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m-%d', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}
	
	public ArrayList<FinanceItem> getItems(Calendar startCalendar, Calendar endCalendar) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String where = "expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id AND strftime('%Y-%m-%d', expense.create_date) BETWEEN '" + FinanceDataFormat.getDateFormat(startCalendar.getTime()) +"' AND '" + FinanceDataFormat.getDateFormat(endCalendar.getTime()) + "'";
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere(where);
		Cursor c = queryBilder.query(db, null, null, null, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}
	
	public ArrayList<FinanceItem> getItems(int year, int month) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month)};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}
	
	public ArrayList<FinanceItem> getItemsFromCategoryID(int mainCategoryID, int year, int month) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(mainCategoryID)};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND expense.main_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}
	
	public ArrayList<FinanceItem> getItemsFromCategoryID(int mainCategoryID) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(mainCategoryID)};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "expense.main_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}
	
	public ArrayList<FinanceItem> getItemsFromSubCategoryID(int subCategoryID, int year, int month) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(subCategoryID)};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND expense.sub_category=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}
	
	/**
	 * 지정된 아이디의 지출 아이템을 가져온다.
	 * @param id 가져올 지출 아이디
	 */
	public FinanceItem getItem(int id) {
		FinanceItem item = null;
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(id)};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "expense._id=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			item = createExpenseItem(c);
		}
		c.close();
		closeDatabase();
		return item;
	}
	
	/**
	 * 지출 객체를 만들고 DB에서 가져온 값으로 설정
	 * @param c Cursor
	 * @return 지출 아이템
	 */
	public ExpenseItem createExpenseItem(final Cursor c) {
		
		ExpenseItem item = new ExpenseItem();
		item.setID(c.getInt(0));
		
		String createDate = c.getString(1);
		
		if (createDate != null)
		{
			try {
				item.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(createDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else {
			item.setOpenUsedItem(true);
		}
		
		item.setAmount(c.getLong(3));
		item.setTitle(c.getString(4));
		item.setMemo(c.getString(5));
		item.setCategory(c.getInt(6), c.getString(13), c.getInt(14), c.getInt(15), c.getInt(16), c.getInt(17));
		item.setSubCategory(c.getInt(7), c.getString(19), c.getInt(20), c.getInt(21), c.getInt(22), c.getInt(23));
		item.getRepeat().setID(c.getInt(9));
		item.setTag(c.getInt(11), c.getString(26));
		
		createPaymentMethod(item, c);
		
		return item;
	}



	protected PaymentMethod createPaymentMethod(final ExpenseItem item, final Cursor c) {
		int type = c.getInt(30);
		PaymentMethod paymentMethod = item.createPaymentMethod(type);
		if (paymentMethod == null) return null;
		
		paymentMethod.setID(c.getInt(29));
		if (type == PaymentMethod.CARD) {
			PaymentCardMethod cardMethod = (PaymentCardMethod)paymentMethod;
			cardMethod.setMethodItemID(c.getInt(32));
			cardMethod.setInstallmentPlan(c.getInt(33));
		}
		else if (type == PaymentMethod.ACCOUNT) {
			paymentMethod.setMethodItemID(c.getInt(31));
		}
		
		
		return paymentMethod;
	}
	
	/**
	 * 지출 상위 분류를 추가한다.
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
		
		ret = db.insert("expense_main_category", null, rowItem);
		closeDatabase();
		return ret;
	}
	
	/**
	 * 지출 하위 분류를 추가한다.
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
		
		ret = db.insert("expense_sub_category", null, rowItem);
		closeDatabase();
		return ret;
	}
	
	/**
	 * 지출 상위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 상위 분류리스트
	 */
	public ArrayList<Category> getCategory() {
		ArrayList<Category> category = new ArrayList<Category>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("expense_main_category", null, null, null, null, null, null);
		
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
		
		Cursor c = db.query("expense_main_category", null, "extend_type=?", new String[]{String.valueOf(extendItem)}, null, null, null);
		
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
		
		Cursor c = db.query("expense_main_category", null, "_id=?", new String[]{String.valueOf(categoryID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			category = new Category(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
		}
		c.close();
		closeDatabase();
		return category;
	}

	/**
	 * 지출 하위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 하위분류리스트
	 */
	public ArrayList<Category> getSubCategory(long mainCategoryId) {
		ArrayList<Category> subCategory = new ArrayList<Category>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		Cursor c = db.query("expense_sub_category", null, "main_id=?", new String[]{String.valueOf(mainCategoryId)}, null, null, null);

		if (c.moveToFirst() != false) {
			do {
				Category item = new Category(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
				subCategory.add(item);
			} while (c.moveToNext());
		}

		c.close();
		closeDatabase();
		return subCategory;
	}
	
	/**
	 *  총 지출금액을 얻는다.
	 * @return long 총 금액
	 */
	public long getTotalAmount() {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String query = "SELECT SUM(amount) FROM expense";
		Cursor c = db.rawQuery(query, null);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}
	
	/**
	 * 지정된 날짜에 대한 총 지출금액을 얻는다.
	 * @return long 총 금액
	 */
	public long getTotalAmountDay(Calendar calendar) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		String query = "SELECT SUM(amount) FROM expense WHERE strftime('%Y-%m-%d', create_date)=?";
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
		String query = "SELECT SUM(amount) FROM expense WHERE strftime('%Y-%m', create_date)=?";
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
		String query = "SELECT SUM(amount) FROM expense WHERE main_category=? AND strftime('%Y-%m', create_date)=?";
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
			String query = "SELECT SUM(amount) FROM expense WHERE main_category=? AND strftime('%Y-%m', create_date)=?";
			Cursor c = db.rawQuery(query, params);
			
			if (c.moveToFirst() != false) {
				amountMonthInYear.add(c.getLong(0));
			}
			c.close();
		}
		closeDatabase();
		return amountMonthInYear;
	}
	
	public ArrayList<Long> getTotalTagAmountMonthInYear(int tagID, int year) {
		ArrayList<Long> amountMonthInYear = new ArrayList<Long>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		for (int month = 1; month <= 12; month++) {
			String[] params = {String.valueOf(tagID), String.format("%d-%02d", year, month)};
			String query = "SELECT SUM(amount) FROM expense WHERE tag=? AND strftime('%Y-%m', create_date)=?";
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
		String query = "SELECT SUM(amount) FROM expense WHERE strftime('%Y', create_date)=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}
	
	

	/**
	 * 지정된 날짜의 추가된 지출 아이템 갯수를 얻는다.
	 * @return int 아이템 수
	 */
	public int getItemCount(Calendar calendar) {
		int count = 0;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		String query = "SELECT COUNT(*) FROM expense WHERE strftime('%Y-%m-%d', create_date)=?";
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
		result = db.delete("expense", "_id=?", new String[] {String.valueOf(id)});
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
		result = db.delete("expense_main_category", "_id=?", new String[] {String.valueOf(id)});
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
		result = db.delete("expense_sub_category", "main_id=?", new String[] {String.valueOf(mainCategoryID)});
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
		result = db.delete("expense_sub_category", "_id=?", new String[] {String.valueOf(id)});
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
		
		int result = db.update("expense_main_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	/**
	 * 지정된 아이디의 하위분류 이름을 변경한다.
	 * @param id 변경할 하위분류 아이디
	 * @param name 변경할 이름
	 * @return int the number of rows affected 
	 */
	public int updateSubCategory(int id, String name, int imgIndex) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("name", name);
		//임시코드
		rowItem.put("prioritize", 0);
		rowItem.put("image_index", imgIndex);
		
		int result = db.update("expense_sub_category", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}
	
	/**
	 * 지출 아이템의 유효성을 확인한다.
	 * @param ExpenseItem 지출 아이템
	 * @return int 성공값 
	 */
	public int checkExpenseVaildItem(ExpenseItem item) {
		int ret = checkVaildItem(item);
		if (ret != DBDef.ValidError.SUCCESS) {
			return ret;
		}
		if (item.getSubCategory() == null || item.getSubCategory().getID() == -1) {
			Log.e(LogTag.DB, "== sub category invalid == ");
			return DBDef.ValidError.SUB_CATEGORY_INVAlID;
		}
		
		if (item.getPaymentMethod() == null) {
			Log.e(LogTag.DB, "== sub payment method invalid == ");
			return DBDef.ValidError.SUB_PAYMENT_METHOD_INVAlID;
		}
		
		Log.i(LogTag.DB, "== EXPENSE VAILD ITEM == ");
		return DBDef.ValidError.SUCCESS; 
	}
	
	/**
	 * 지출을 DB테이블에 추가
	 * @param item 지출 아이템
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addPaymentMethod(PaymentMethod paymentMethod) {
		if (paymentMethod == null) return -1;
		int type = paymentMethod.getType();
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		if (type == PaymentMethod.CASH) {
			rowItem.put("installment_plan", 0);
		} 
		else if (type == PaymentMethod.CARD){
			PaymentCardMethod card = (PaymentCardMethod)paymentMethod;
			if (card.getCard() != null) {
				rowItem.put("card", card.getCard().getID());
				rowItem.put("installment_plan", card.getInstallmentPlan());
			}
			rowItem.put("method_type", paymentMethod.getType());
		} 
		else if (type == PaymentMethod.ACCOUNT){
			PaymentAccountMethod account = (PaymentAccountMethod)paymentMethod;
			
			if (account.getAccount() != null) {
				rowItem.put("account", account.getAccount().getID()); // 임시값
				rowItem.put("installment_plan", 0);
			}
			rowItem.put("method_type", account.getType());
		} 
		else {
			Log.e(LogTag.DB, ":: PAYMENT METHOD TYPE ERROR ::");
			closeDatabase();
			return -1;
		}
		
		rowItem.put("method_type", type);
		
		long ret = db.insert("payment_method", null, rowItem);
		paymentMethod.setID((int)ret);
		closeDatabase();
		return ret;
	}
	
	/**
	 * 지출을 DB테이블에 추가
	 * @param item 지출 아이템
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long updatePaymentMethod(PaymentMethod paymentMethod) {
		if (paymentMethod == null) return -1;
		int type = paymentMethod.getType();
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();
		
		if (type == PaymentMethod.CASH) {
			rowItem.put("installment_plan", 0);
		} 
		else if (type == PaymentMethod.CARD){
			PaymentCardMethod card = (PaymentCardMethod)paymentMethod;
			if (card.getCard() != null) {
				rowItem.put("card", card.getCard().getID());
				rowItem.put("installment_plan", card.getInstallmentPlan());
			}
			rowItem.put("method_type", paymentMethod.getType());
		} 
		else if (type == PaymentMethod.ACCOUNT){
			PaymentAccountMethod account = (PaymentAccountMethod)paymentMethod;
			
			if (account.getAccount() != null) {
				rowItem.put("account", account.getAccount().getID()); // 임시값
				rowItem.put("installment_plan", 0);
			}
			rowItem.put("method_type", account.getType());
		} 
		else {
			Log.e(LogTag.DB, ":: PAYMENT METHOD TYPE ERROR ::");
			closeDatabase();
			return -1;
		}
		
		rowItem.put("method_type", type);
		
		long ret = db.update("payment_method", rowItem, "_id=?", new String[] {String.valueOf(paymentMethod.getID())});
		paymentMethod.setID((int)ret);
		closeDatabase();
		return ret;
	}
	
	/**
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 * @param 지출테이블에 입력되기전에 추가되어야 할 테이블
	 */
	private boolean precedenceAddItem(ExpenseItem item) {
		if (addPaymentMethod(item.getPaymentMethod()) == -1) {
			Log.e(LogTag.DB, ":: ADD PAYMENT METHOD ERROR ::");
			return false;
		}
		return true;
	}
	
	/**
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 * @param 지출테이블에 수정되기전에 수정되어야 할 테이블
	 */
	private boolean precedenceUpdateItem(ExpenseItem item) {
		if (updatePaymentMethod(item.getPaymentMethod()) == -1) {
			Log.e(LogTag.DB, ":: UPDATE PAYMENT METHOD ERROR ::");
			return false;
		}
		return true;
	}
	
	/**
	 * 지정된 상위분류  아이템을 DB에서 삭제한다.
	 * @param id 삭제할 상위분류 아이디
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deletePaymentMethod(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete("payment_method", "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		
		deleteSubCategoryFromMainID(id);
		return result;
	}
	
	public long updateRepeat(int expenseID, int repeatID) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		ContentValues rowItem = new ContentValues();

		rowItem.put("repeat", repeatID); // 임시값
		
		long ret = db.update("expense", rowItem, "_id=?", new String[] {String.valueOf(expenseID)});
		closeDatabase();
		return ret;
	}


	public ArrayList<FinanceItem> getCardExpenseItems(int year, int month, int cardID) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(cardID)};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND payment_method.card=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}
	
	public ArrayList<FinanceItem> getCardExpenseItems(int cardID, Calendar start, Calendar end) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String where = "expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id AND strftime('%Y-%m-%d', expense.create_date) BETWEEN '" + FinanceDataFormat.getDateFormat(start.getTime()) +"' AND '" + FinanceDataFormat.getDateFormat(end.getTime()) + "'";
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere(where);
		Cursor c = queryBilder.query(db, null, "payment_method.card=?", new String [] {String.valueOf(cardID)}, null, null, null);
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}

	public long getCardTotalExpense(int year, int month, int cardID) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(cardID)};
		String query = "SELECT SUM(expense.amount) FROM expense, payment_method WHERE expense.payment_method=payment_method._id AND strftime('%Y-%m', expense.create_date)=? AND payment_method.card=?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}


	public long getCardTotalExpense(int cardID, Calendar start, Calendar end) {
		long amount = 0L;
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.valueOf(cardID), FinanceDataFormat.getDateFormat(start.getTime()), FinanceDataFormat.getDateFormat(end.getTime())};
		String query = "SELECT SUM(expense.amount/(payment_method.installment_plan+1)) FROM expense, payment_method WHERE expense.payment_method=payment_method._id AND payment_method.card=? AND strftime('%Y-%m-%d', expense.create_date) BETWEEN ? AND ?";
		Cursor c = db.rawQuery(query, params);
		
		if (c.moveToFirst() != false) {
			amount = c.getLong(0);
		}
		c.close();
		closeDatabase();
		return amount;
	}



	public ArrayList<FinanceItem> getItemFromAccount(int accountId, int year, int month) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(accountId)};
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND payment_method.account=?", params, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}


	public ArrayList<FinanceItem> getItemFromMypocket(int year, int month) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		String[] params = {String.format("%d-%02d", year, month), String.valueOf(PaymentMethod.CASH)};
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "strftime('%Y-%m', create_date)=? AND payment_method.method_type=?", params, null, null, "create_date DESC");
		
		if (c.moveToFirst() != false) {
			do {
				expenseItems.add(createExpenseItem(c));
			} while (c.moveToNext());
		}
		c.close();
		closeDatabase();
		return expenseItems;
	}


	@Override
	public int addOpenUsedItem(OpenUsedItem item) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("expense_id", item.getItem().getID());
		rowItem.put("prioritize", item.getPriority());
		rowItem.put("image_index", item.getImageIndex());
		
		int insertID = (int)db.insert("expense_open_used", null, rowItem);
		closeDatabase();
		return insertID;
	}
	

	@Override
	public void updateOpenUsedItem(int id, int itemID, int prioritize) {
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		
		ContentValues rowItem = new ContentValues();
		rowItem.put("expense_id", id);
		rowItem.put("prioritize", prioritize);
		rowItem.put("image_index", -1);
		
		db.update("expense_open_used", rowItem, "_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
	}


	@Override
	public int deleteOpenUsedItem(int id) {
		int result = 0;
		SQLiteDatabase db = openDatabase(WRITE_MODE);
		result = db.delete("expense_open_used", "expense_id=?", new String[] {String.valueOf(id)});
		closeDatabase();
		return result;
	}


	@Override
	public ArrayList<OpenUsedItem> getOpenUsedItems() {
		ArrayList<OpenUsedItem> expenseItems = new ArrayList<OpenUsedItem>();
		SQLiteDatabase db = openDatabase(READ_MODE);
		LOCK();
		
		Cursor c = db.query("expense_open_used", null, null, null, null, null, "prioritize DESC");
		
		if (c.moveToFirst() != false) {
			do {
				OpenUsedItem item = new OpenUsedItem(getItem(c.getInt(1)));
				item.setID(c.getInt(0));
				item.setPriority(c.getInt(2));
				item.setImageIndex(c.getInt(3));
				expenseItems.add(item);
			} while (c.moveToNext());
		}
		
		UNLOCK();
		c.close();
		closeDatabase();
		return expenseItems;
	}



}
