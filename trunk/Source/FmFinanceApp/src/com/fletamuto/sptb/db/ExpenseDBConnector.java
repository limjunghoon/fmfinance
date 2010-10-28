package com.fletamuto.sptb.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.fletamuto.sptb.LogTag;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentCardMethod;
import com.fletamuto.sptb.data.PaymentCashMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.util.FinanceDataFormat;

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
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("create_time", "21:20:00"); // 임시값
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getId());
		rowItem.put("sub_category", item.getSubCategory().getId());
		rowItem.put("payment_method", item.getPaymentMethod().getId()); // 임시값
		rowItem.put("tag", item.getTag().getID());
		
		long ret = db.insert("expense", null, rowItem);
		db.close();
		
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
		SQLiteDatabase db = getWritableDatabase();
		ContentValues rowItem = new ContentValues();
		
		rowItem.put("create_date", item.getCreateDateString());
		rowItem.put("create_time", "21:20:00");
		rowItem.put("amount", item.getAmount());
		rowItem.put("title", item.getTitle());
		rowItem.put("memo", item.getMemo());
		rowItem.put("main_category", item.getCategory().getId());
		rowItem.put("sub_category", item.getSubCategory().getId());
		rowItem.put("tag", item.getTag().getID());
		
		long ret = db.update("expense", rowItem, "_id=?", new String[] {String.valueOf(financeItem.getId())});
		db.close();
		return ret;
	}
	
	/**
	 * 모든 지출 아이템을 DB에서 가져온다.
	 * @return ArrayList<FinanceItem> 지출아이템 목록
	 */
	public  ArrayList<FinanceItem> getAllItems() {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
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
	 * 지정된 날짜의 지출 아이템을 가져온다.
	 * @param Calendar calendar instance
	 * @return ArrayList<FinanceItem> 지출아이템 목록
	 */
	public ArrayList<FinanceItem> getItems(Calendar calendar) {
		ArrayList<FinanceItem> expenseItems = new ArrayList<FinanceItem>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
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
	 * 지정된 아이디의 지출 아이템을 가져온다.
	 * @param id 가져올 지출 아이디
	 */
	public FinanceItem getItem(int id) {
		FinanceItem item = null;
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder queryBilder = new SQLiteQueryBuilder();
		String[] params = {String.valueOf(id)};
		
		queryBilder.setTables("expense, expense_main_category, expense_sub_category, expense_tag, payment_method");
		queryBilder.appendWhere("expense.main_category=expense_main_category._id AND expense.sub_category=expense_sub_category._id AND expense.tag=expense_tag._id AND expense.payment_method=payment_method._id");
		Cursor c = queryBilder.query(db, null, "expense._id=?", params, null, null, null);
		
		if (c.moveToFirst() != false) {
				item = CreateExpenseItem(c);
		}
		c.close();
		db.close();
		return item;
	}
	
	/**
	 * 지출 객체를 만들고 DB에서 가져온 값으로 설정
	 * @param c Cursor
	 * @return 지출 아이템
	 */
	public ExpenseItem CreateExpenseItem(final Cursor c) {
		
		ExpenseItem item = new ExpenseItem();
		item.setId(c.getInt(0));
		try {
			item.setCreateDate(FinanceDataFormat.DATE_FORMAT.parse(c.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		item.setAmount(c.getLong(3));
		item.setTitle(c.getString(4));
		item.setMemo(c.getString(5));
		item.setCategory(c.getInt(6), c.getString(13));
		item.setSubCategory(c.getInt(7), c.getString(17));
		item.setTag(c.getInt(11), c.getString(22));
		
		 createPaymentMethod(item, c);
		
		
		return item;
	}
	
	protected PaymentMethod createPaymentMethod(ExpenseItem item, final Cursor c) {
		int type = c.getInt(26);
		PaymentMethod paymentMethod = item.createPaymentMethod(type);
		if (paymentMethod == null) return null;
		
		paymentMethod.setId(c.getInt(25));
		return paymentMethod;
	}
	
	/**
	 * 지출 상위 분류를 추가한다.
	 * @param name 분류 이름
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
	 * 지출 하위 분류를 추가한다.
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
		
		ret = db.insert("expense_sub_category", null, rowItem);
		db.close();
		return ret;
	}
	
	/**
	 * 지출 상위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 상위 분류리스트
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
	 * 지출 하위분류 목록을 얻는다.
	 * 카테고리 객체생성
	 * @return ArrayList<Category> 하위분류리스트
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
	 *  총 지출금액을 얻는다.
	 * @return long 총 금액
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
	 * 지정된 날짜에 대한 총 지출금액을 얻는다.
	 * @return long 총 금액
	 */
	public long getTotalAmountDay(Calendar calendar) {
		long amount = 0L;
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
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
	 * 지정된 날짜의 추가된 지출 아이템 갯수를 얻는다.
	 * @return int 아이템 수
	 */
	public int getItemCount(Calendar calendar) {
		int count = 0;
		SQLiteDatabase db = getReadableDatabase();
		String[] params = {FinanceDataFormat.getDateFormat(calendar.getTime())};
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
	 * 지정된 아이디의 아이템을 DB에서 삭제한다.
	 * @param id 삭제할 아이템 아이디
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
	 * 지정된 상위분류  아이템을 DB에서 삭제한다.
	 * @param id 삭제할 상위분류 아이디
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
	 * 지정된 아이디의 상위분류 이름을 변경한다.
	 * @param id 변경할 상위분류 아이디
	 * @param name 변경할 이름
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
	 * 지정된 하위분류  아이템을 DB에서 삭제한다.
	 * @param id 삭제할 하위분류 아이디
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
	 * 지정된 아이디의 상위분류 이름을 변경한다.
	 * @param id 변경할 상위분류 아이디
	 * @param name 변경할 이름
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
	 * 지정된 아이디의 하위분류 이름을 변경한다.
	 * @param id 변경할 하위분류 아이디
	 * @param name 변경할 이름
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
	 * 지출 아이템의 유효성을 확인한다.
	 * @param ExpenseItem 지출 아이템
	 * @return int 성공값 
	 */
	public int checkExpenseVaildItem(ExpenseItem item) {
		int ret = checkVaildItem(item);
		if (ret != DBDef.ValidError.SUCCESS) {
			return ret;
		}
		if (item.getSubCategory() == null || item.getSubCategory().getId() == -1) {
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
		SQLiteDatabase db = getWritableDatabase();
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
			db.close();
			return -1;
		}
		
		rowItem.put("method_type", type);
		
		long ret = db.insert("payment_method", null, rowItem);
		paymentMethod.setId((int)ret);
		db.close();
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
	 * 지정된 상위분류  아이템을 DB에서 삭제한다.
	 * @param id 삭제할 상위분류 아이디
	 * @return int the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	 */
	public int deletePaymentMethod(int id) {
		int result = 0;
		SQLiteDatabase db = getWritableDatabase();
		result = db.delete("payment_method", "_id=?", new String[] {String.valueOf(id)});
		db.close();
		
		deleteSubCategoryFromMainID(id);
		return result;
	}
}
