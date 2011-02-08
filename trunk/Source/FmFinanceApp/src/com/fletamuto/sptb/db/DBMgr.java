package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsDepositItem;
import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.AssetsInsuranceItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.BudgetItem;
import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.IncomeSalaryItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityCashServiceItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.data.LiabilityPersonLoanItem;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

/**
 * ���̾ƿ���  DB�� �����ϴ� Ŭ����
 * SingleTone���� ����
 * @author yongbban
 * @version 1.0.0.1
 */
public final class DBMgr {
	/** ������ DB���� �ν��Ͻ� */
	private static final DBMgr mInstance = new DBMgr();
	
	/** DB���̺� ������*/
	private final DBConnector mDBConnector = new DBConnector();
	
	private static FinanceDBHelper mDBHelper = null; 
	
	private static boolean mDBOpenLock = false;
	
	private static SQLiteDatabase mDB;
	
	
	/**
	 * �ܺο��� ��ü�� �������� ���Ѵ�.
	 */
	private DBMgr() {
	}
	
	/**
	 * DB�ʱ�ȭ �� ��� �����Ѵ�.
	 * @param context ������ �� ��ü ���ؽ�Ʈ
	 */
	public static void initialize(Context context) {
		mDBHelper = new FinanceDBHelper(context);
	}
	
	public static void dbUnLock() {
		mDBOpenLock = false;
	}

	public static void dbLock() {
		mDBOpenLock = true;
	}
	
	public static void setDB(SQLiteDatabase db) {
		mDB = db;
	}

	public static SQLiteDatabase getDB() {
		return mDB;
	}
	
	public static SQLiteDatabase getWritableDatabase() {
		if (mDBOpenLock == true) {
			return mDB;
		}
		
		mDB = mDBHelper.getWritableDatabase();
		return mDB;
	}
	
	public static SQLiteDatabase getReadableDatabase() {
		if (mDBOpenLock == true) {
			return mDB;
		}
		
		mDB = mDBHelper.getReadableDatabase();
		return mDB;
	}
	
	public static void closeDatabase() {
		if (mDBOpenLock == true) {
			Log.i(LogTag.DB, "::: DATABASE LOCK. Can't closed db ");
			return;
		}
		
		mDB.close();
	}
	
	
	
	/**
	 * DBHelper ��ü�� ��´�.
	 * @return DBHelper ��ü
	 */
	public static FinanceDBHelper getDBHelper() {
		return mDBHelper;
	}
	
	public static IncomeDBConnector getIncomeDBConnecter() {
		return (IncomeDBConnector)mInstance.mDBConnector.getBaseFinanceDBInstance(IncomeItem.TYPE);
	}
	
	public static ExpenseDBConnector getExpenseDBConnecter() {
		return (ExpenseDBConnector)mInstance.mDBConnector.getBaseFinanceDBInstance(ExpenseItem.TYPE);
	}
	
	public static AssetsDBConnector getAssetsDBConnecter() {
		return (AssetsDBConnector)mInstance.mDBConnector.getBaseFinanceDBInstance(AssetsItem.TYPE);
	}
	
	public static LiabilityDBConnector getLiabilityDBConnecter() {
		return (LiabilityDBConnector)mInstance.mDBConnector.getBaseFinanceDBInstance(LiabilityItem.TYPE);
	}
	
	/**
	 * �ݺ� �������� �߰�
	 * ��쿡 ���� �ӵ����� �ʿ�
	 * @return
	 */
	public static boolean addRepeatItems() {
		getWritableDatabase();
		dbLock();
		
		ArrayList<Repeat> repeatItems = mInstance.mDBConnector.getRepeatDBConnector().getAllItems();
		if (repeatItems == null) {
			dbUnLock();
			return true;
		}
		
		int length = repeatItems.size();
		Calendar today = Calendar.getInstance();
		
		for (int index = 0; index < length; index++) {
			Repeat repeat = repeatItems.get(index);
			Calendar lastApplyDate = repeat.getLastApplyDate();
			
			while (isAfterDay(today, lastApplyDate)) {
				lastApplyDate.add(Calendar.DAY_OF_MONTH, 1);
				
				if (repeat.isRepeatDay(lastApplyDate) == false) {
					continue;
				}
				
				FinanceItem item = getItem(repeat.getItemType(), repeat.getItemID());
				if (item == null) {
					Log.e(LogTag.DB, ":: Fail to Repeat Item " + repeat.getItemID());
					continue;
				}
				
				if (item.getType() == ExpenseItem.TYPE || item.getType() == IncomeItem.TYPE) {
					item.setCreateDate(lastApplyDate);
					addFinanceItem(item);
				}
				else if (item.getType() == AssetsItem.TYPE) {
					if (item.getExtendType() == ItemDef.ExtendAssets.SAVINGS) {
						AssetsSavingsItem saving = (AssetsSavingsItem) item;
						
						ArrayList<Integer> expenseIDs = getExpenseFromAssets(saving.getID());
						if (expenseIDs.size() == 0) {
							continue;
						}
						
						ExpenseItem expense = (ExpenseItem) getItem(ExpenseItem.TYPE, expenseIDs.get(0));
						if (expense != null) {
							expense.setCreateDate(lastApplyDate);
							addFinanceItem(expense);
							addExpenseFromAssets(expense.getID(), saving.getID());
							saving.setAmount(saving.getAmount() + expense.getAmount());
							updateAmountFinanceItem(saving);
							saving.setCreateDate(lastApplyDate);
//							saving.setAmount(saving.getPayment());
							if (DBMgr.addStateChangeItem(saving) == 0) {
					    		Log.e(LogTag.LAYOUT, "== UpdateState fail to the save item : " + saving.getID());
					    	}
						}
					}
					
				}
			}
			
			repeat.setLastApplyDay(lastApplyDate);
			updateRepeat(repeat);
		}
		
		dbUnLock();
		return true;
	}
	
	
	private static boolean isAfterDay(Calendar today, Calendar lastApplyDate) {
		return (Integer.parseInt(FinanceDataFormat.getNumberDateFormat(today.getTime())) > Integer.parseInt(FinanceDataFormat.getNumberDateFormat(lastApplyDate.getTime())));
	}

	/**
	 * ���԰��� DB�����͸� ��´�.
	 * @return IncomeDBConnector
	 */
	public static IncomeDBConnector connectIncomeDB() {
		return (IncomeDBConnector)mInstance.mDBConnector.getBaseFinanceDBInstance(ItemDef.FinanceDef.INCOME);
	}
	
	/**
	 * ������ Ÿ���� Ȯ���Ѵ�.
	 * @param type ItemDef.FinanceDef
	 * @return
	 */
	private static boolean checkFinanceItemType(int type) {
		if (type < 0) {
			Log.e(LogTag.DB, "== invaild finance item " + type);
			return false;
		}
		return true;
	}
	
	/**
	 * DB�� ��������  �߰��Ѵ�.
	 * @param item ����, ����, �ڻ�, ��ä ��ü
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public static long addFinanceItem(FinanceItem item) {
		if (checkFinanceItemType(item.getType()) == false) return -1; 
		return mInstance.mDBConnector.getBaseFinanceDBInstance(item.getType()).addItem(item);
	}
	
	/**
	 * DB�� ��������  �����Ѵ�.
	 * @param item ����, ����, �ڻ�, ��ä ��ü
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public static long updateFinanceItem(FinanceItem item) {
		if (checkFinanceItemType(item.getType()) == false) return -1;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(item.getType()).updateItem(item);
	}
	
	public static long updateAmountFinanceItem(FinanceItem item) {
		if (checkFinanceItemType(item.getType()) == false) return -1;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(item.getType()).updateAmountFinanceItem(item.getID(), item.getAmount());
	}
	
	/**
	 * ���̵� �ش��ϴ� �з� �̸��� �����Ѵ�. 
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�
	 * @param name ����Ǵ� �з� �̸�
	 * @return ��������
	 */
	public static int updateCategory(int itemType, int id, String name) {
		if (checkFinanceItemType(itemType) == false) return 0;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).updateCategory(id, name);
	}
	
	/**
	 * ���̵� �ش��ϴ� ���� �з� �̸��� �����Ѵ�. 
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�
	 * @param name ����Ǵ� ���� �з� �̸�
	 * @return ��������
	 */
	public static int updateSubCategory(int itemType, int id, String name) {
		if (checkFinanceItemType(itemType) == false) return 0;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).updateSubCategory(id, name);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �������� ��� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public static ArrayList<FinanceItem> getAllItems(int itemType) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getAllItems();
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �ش� ��¥�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public static ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItems(calendar);
	}
	
	public static ArrayList<FinanceItem> getItems(int itemType, Calendar start, Calendar end) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItems(start, end);
	}
	
	public static ArrayList<FinanceItem> getItems(int itemType, int year, int month) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItems(year, month);
	}
	
	public static ArrayList<FinanceItem> getItemsFromCategoryID(int itemType, int mainCategoryID, int year, int month) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItemsFromCategoryID(mainCategoryID, year, month);
	}
	
	public static ArrayList<FinanceItem> getItemsFromCategoryID(int itemType, int mainCategoryID) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItemsFromCategoryID(mainCategoryID);
	}
	
	public static ArrayList<FinanceItem> getItemsFromSubCategoryID(int itemType, int subCategoryID, int year, int month) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItemsFromSubCategoryID(subCategoryID, year, month);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ID�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ID
	 * @return ������ ������ ���н� null
	 */
	public static FinanceItem getItem(int itemType, int id) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItem(id);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش��ϴ� ���̵� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�
	 * @return ������ ������ ��
	 */
	public static int deleteItem(int itemType, int id) {
		if (checkFinanceItemType(itemType) == false) return -1;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).deleteItem(id);
	}
	
	public static long updateRepeat(int itemType, int itemID, int repeatID) {
		if (checkFinanceItemType(itemType) == false) return -1;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).updateRepeat(itemID, repeatID);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return long �� �׼�
	 */
	public static long getTotalAmount(int itemType) {
		if (checkFinanceItemType(itemType) == false) return -1;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getTotalAmount();
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return long �� �׼�
	 */
	public static long getTotalAmountDay(int itemType, Calendar calendar) {
		if (checkFinanceItemType(itemType) == false) return 0L;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getTotalAmountDay(calendar);
	}
	
	public static long getTotalAmountMonth(int itemType, int year, int month) {
		if (checkFinanceItemType(itemType) == false) return 0L;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getTotalAmountMonth(year, month);
	}
	
	public static long getTotalAmountMonth(int itemType, int categorID, int year, int month) {
		if (checkFinanceItemType(itemType) == false) return 0L;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getTotalAmountMonth(categorID, year, month);
	}
	
	public static ArrayList<Long> getTotalAmountMonthInYear(int itemType, int categorID, int year) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getTotalAmountMonthInYear(categorID, year);
	}
	
	public static ArrayList<Long> getTotalTagAmountMonthInYear(int tagID, int year) {
		return mInstance.mDBConnector.getBaseFinanceDBInstance(ExpenseItem.TYPE).getTotalTagAmountMonthInYear(tagID, year);
	}
	
	public static long getTotalAmountYear(int itemType, int year) {
		if (checkFinanceItemType(itemType) == false) return 0L;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getTotalAmountYear(year);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �Էµ� ������ ������ ��´�.
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ������ ����
	 */
	public static int getItemCount(int itemType, Calendar calendar) {
		if (checkFinanceItemType(itemType) == false) return -1;
		//return mInstance.mDBConnector.getItemCount(itemType, calendar);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItemCount(calendar);
	}

	
	/**
	 * ���ο� �з��� �߰��Ѵ�.
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param name  �з��̸�
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public static int addCategory(int itemType, Category category) {
		if (checkFinanceItemType(itemType) == false) return -1;
		return (int)mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).addCategory(category);
	}
	
	/**
	 * ���� �з��� �߰��Ѵ�.
	 * @param itemTyee ����, ����, �ڻ�, ��ä Ÿ��
	 * @param mainCategoryID ���� �з� ���̵�
	 * @param name ���� �з� �̸�
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public static int addSubCategory(int itemType, long mainCategoryID, String name) {
		if (checkFinanceItemType(itemType) == false) return -1;
		return (int)mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).addSubCategory(mainCategoryID, name);
	}
	
	/**
	 * DB�� �Էµ� �з� ����Ʈ�� �����´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return �з� ����Ʈ
	 */
	public static ArrayList<Category> getCategory(int itemType) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getCategory();
	}
	
	public static Category getCategoryFromID(int itemType, int categoryID) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getCategoryFromID(categoryID);
	}
	
	/**
	 * DB�� �Էµ� �з� ����Ʈ�� �����´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return �з� ����Ʈ
	 */
	public static ArrayList<Category> getCategory(int itemType, int extendItem) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getCategory(extendItem);
	}
	
	/**
	 * DB�� �Էµ� ����, �ڻ��� �з� �� ���� �з� ����Ʈ�� �����´�. 
	 * @param itemType ����, �ڻ�  Ÿ��
	 * @param mainCategoryId ���� �з�
	 * @return ���� �з� ����Ʈ
	 */
	public static ArrayList<Category> getSubCategory(int itemType, long mainCategoryId) {
		if (checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getSubCategory(mainCategoryId);
	}
	
	/**
	 * DB���� ID�� �ش��ϴ� ī�װ��� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param itemID ������ ���̵�
	 * @return 
	 */
	public static int deleteCategory(int itemType, int itemID) {
		if (checkFinanceItemType(itemType) == false) return 0;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).deleteCategory(itemID);
	}
	
	public static int deleteSubCategory(int itemType, int itemID) {
		if (checkFinanceItemType(itemType) == false) return 0;
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).deleteSubCategory(itemID);
	}
	
	public static int addCompany(FinancialCompany company) {
		return mInstance.mDBConnector.getCompanyDBConnector().addItem(company);
	}
	
	public static boolean updateCompany(FinancialCompany company) {
		return mInstance.mDBConnector.getCompanyDBConnector().updateItem(company);
	}

	public static ArrayList<FinancialCompany> getCompanys() {
		return mInstance.mDBConnector.getCompanyDBConnector().getAllItems();
	}

	public static FinancialCompany getCompany(int id) {
		return mInstance.mDBConnector.getCompanyDBConnector().getItem(id);
	}
	
	public static int deleteCompany(int id) {
		return mInstance.mDBConnector.getCompanyDBConnector().deleteItem(id);
	}
	
	public static int addAccountItem(AccountItem account) {
		return mInstance.mDBConnector.getAccountDBConnector().addItem(account);
	}
	
	public static AccountItem getAccountItem(int id) {
		return mInstance.mDBConnector.getAccountDBConnector().getItem(id);
	}
	
	public static AccountItem getAccountMyPoctet() {
		return mInstance.mDBConnector.getAccountDBConnector().getMyPocket();
	}
	
	public static ArrayList<AccountItem> getAccountAllItems() {
		return mInstance.mDBConnector.getAccountDBConnector().getAllItems();
	}
	
	public static boolean updateAccount(AccountItem account) {
		return mInstance.mDBConnector.getAccountDBConnector().updateItem(account);
	}
	
	public static int deleteAccount(int id) {
		return mInstance.mDBConnector.getAccountDBConnector().deleteAccountItem(id);
	}
	
	public static int addCardCompanyName(CardCompanyName cardCompanyName) {
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().addItem(cardCompanyName);
	}
	
	public static boolean updateCardCompanyName(CardCompanyName cardCompanyName) {
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().updateItem(cardCompanyName);
	}

	public static ArrayList<CardCompanyName> getCardCompanyNames() {
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().getAllItems();
	}

	public static CardCompanyName getCardCompanyName(int id) {
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().getItem(id);
	}
	
	public static int deleteCardCompanyName(int id) {
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().deleteItem(id);
	}

	public static int addCardItem(CardItem card) {
		return mInstance.mDBConnector.getCardDBConnector().addItem(card);
	}
	
	public static boolean updateCardItem(CardItem card) {
		return mInstance.mDBConnector.getCardDBConnector().updateItem(card);
	}
	
	public static ArrayList<CardItem> getCardItems() {
		return mInstance.mDBConnector.getCardDBConnector().getAllItems();
	}

	public static ArrayList<CardItem> getCardItems(int type) {
		return mInstance.mDBConnector.getCardDBConnector().getAllItems(type);
	}

	public static CardItem getCardItem(int id) {
		return mInstance.mDBConnector.getCardDBConnector().getItem(id);
	}
	
	public static int deleteCardItem(int id) {
		return mInstance.mDBConnector.getCardDBConnector().deleteItem(id);
	}

	public static int addTag(ExpenseTag tag) {
		return mInstance.mDBConnector.getTagDBConnector().addItem(tag);
	}
	
	public static boolean updateTag(ExpenseTag tag) {
		return mInstance.mDBConnector.getTagDBConnector().updateItem(tag);
	}

	public static ArrayList<ExpenseTag> getTag() {
		return mInstance.mDBConnector.getTagDBConnector().getAllItems();
	}
	
	public static ExpenseTag getTag(int id) {
		return mInstance.mDBConnector.getTagDBConnector().getItem(id);
	}
	
	public static int deleteTag(int id) {
		return mInstance.mDBConnector.getTagDBConnector().deleteItem(id);
	}
	
	public static Repeat getRepeat(int id) {
		return mInstance.mDBConnector.getRepeatDBConnector().getItem(id);
	}
	
	public static int addRepeat(Repeat repeat) {
		return mInstance.mDBConnector.getRepeatDBConnector().addItem(repeat);
	}
	
	public static boolean updateRepeat(Repeat repeat) {
		return mInstance.mDBConnector.getRepeatDBConnector().updateItem(repeat);
	}
	
	public static int deleteRepeat(int id) {
		return mInstance.mDBConnector.getRepeatDBConnector().deleteRepeat(id);
	}
	
	public static long addExtendIncomeSalary(IncomeSalaryItem salary) {
		ExpenseItem assurance =  salary.getExpenseAssurance();
		ExpenseItem etc =  salary.getExpenseEtc();
		ExpenseItem pension =  salary.getExpensePension();
		ExpenseItem tax =  salary.getExpenseTax();
		int assuranceID = -1;
		int etcID = -1;
		int pensionID = -1;
		int taxID = -1;
		
		if (assurance != null) assuranceID = (int)addFinanceItem(assurance);
		if (etc != null) etcID = (int)addFinanceItem(etc);
		if (pension != null) pensionID = (int)addFinanceItem(pension);
		if (tax != null) taxID = (int)addFinanceItem(tax);
		
		long extendID = getIncomeDBConnecter().addExendSalary(assuranceID, taxID, pensionID, etcID);
		salary.setExtendID((int)extendID);
		return addFinanceItem(salary);
	}

	public static long addExtendAssetsDeposit(AssetsDepositItem deposit) {
		return getAssetsDBConnecter().addExtendDeposit(deposit);
	}

	public static long addExtendAssetsSavings(AssetsSavingsItem savings) {
		return getAssetsDBConnecter().addExtendSavings(savings);
	}

	public static long addExtendAssetsStock(AssetsStockItem stock) {
		return getAssetsDBConnecter().addExtendStock(stock);
	}
	
	public static long updateExtendAssetsStock(AssetsStockItem stock) {
		return getAssetsDBConnecter().updateExtendStock(stock);
	}

	public static long addExtendAssetsFund(AssetsFundItem fund) {
		return getAssetsDBConnecter().addExtendFund(fund);
	}

	public static long addExtendAssetsInsurance(AssetsInsuranceItem insurance) {
		return getAssetsDBConnecter().addExtendInsurance(insurance);
	}

	public static long addExtendLiabilityLoan(LiabilityLoanItem loan) {
		return getLiabilityDBConnecter().addExtendLoan(loan);
	}

	public static long addExtendLiabilityCashService(LiabilityCashServiceItem cashService) {
		return getLiabilityDBConnecter().addExtendCashService(cashService);
	}

	public static long addExtendLiabilityPersonLoan(LiabilityPersonLoanItem personLoan) {
		return getLiabilityDBConnecter().addExtendPersonLoan(personLoan);
	}

	public static ArrayList<BudgetItem> getBudgetItems(int year, int month) {
		return mInstance.mDBConnector.getBudgetDBConnector().getItems(year, month);
	}
	
	public static BudgetItem getBudgetItem(int year, int month) {
		return mInstance.mDBConnector.getBudgetDBConnector().getMainBudget(year, month);
	}

	public static long addBudget(BudgetItem item) {
		return mInstance.mDBConnector.getBudgetDBConnector().addItem(item);
	}

	public static long updateBudget(BudgetItem item) {
		return mInstance.mDBConnector.getBudgetDBConnector().updateItem(item);
	}
	
	public static long getCardTotalExpense(int year, int month, int cardID) {
		ExpenseDBConnector expenseDB = (ExpenseDBConnector) mInstance.mDBConnector.getBaseFinanceDBInstance(ExpenseItem.TYPE);
		return expenseDB.getCardTotalExpense(year, month, cardID);
	}
	
	public static long getCardTotalExpense(int cardID, Calendar start, Calendar end) {
		ExpenseDBConnector expenseDB = (ExpenseDBConnector) mInstance.mDBConnector.getBaseFinanceDBInstance(ExpenseItem.TYPE);
		return expenseDB.getCardTotalExpense(cardID, start, end);
	}
	
	public static ArrayList<FinanceItem> getCardExpenseItems(int year, int month, int cardID) {
		ExpenseDBConnector expenseDB = (ExpenseDBConnector) mInstance.mDBConnector.getBaseFinanceDBInstance(ExpenseItem.TYPE);
		return expenseDB.getCardExpenseItems(year, month, cardID);
	}

	public static ArrayList<FinanceItem> getCardExpenseItems(int cardID, Calendar start, Calendar end) {
		ExpenseDBConnector expenseDB = (ExpenseDBConnector) mInstance.mDBConnector.getBaseFinanceDBInstance(ExpenseItem.TYPE);
		return expenseDB.getCardExpenseItems(cardID, start, end);
	}
	
	public static long addStateChangeItem(FinanceItem item) {
		if (checkFinanceItemType(item.getType()) == false) return -1;
		long ret = mInstance.mDBConnector.getBaseFinanceDBInstance(item.getType()).addStateChangeItem(item);
		if (ret != -1) {
			if (item.getType() == AssetsItem.TYPE) {
				item.setAmount(getAssetsDBConnecter().getLatestPrice(item.getID()));
			}
			else if (item.getType() == LiabilityItem.TYPE) {
				item.setAmount(getLiabilityDBConnecter().getLatestPrice(item.getID()));
			}
			
			updateAmountFinanceItem(item);
		}
		return ret;
	}
	
	public static ArrayList<Long> getLastAmountMonthInYear(int assetsID, int year) {
		return getAssetsDBConnecter().getLastAmountMonthInYear(assetsID, year);
	}

	public static long getAssetsPurchasePrice(int id) {
		return getAssetsDBConnecter().getPurchasePrice(id);
	}
	
	public static ArrayList<FinanceItem> getAssetsStateItems(int id) {
		return getAssetsDBConnecter().getStateItems(id);
	}
	
	public static long getAssetsMeanPrice(int id) {
		return getAssetsDBConnecter().getMeanPrice(id);
	}
	
	public static ArrayList<Long> getTotalLiabilityAmountMonthInYear(int assetsID, int year) {
		return getLiabilityDBConnecter().getTotalLiabilityAmountMonthInYear(assetsID, year);
	}

	public static long getLiabilityPurchasePrice(int id) {
		return getLiabilityDBConnecter().getPurchasePrice(id);
	}
	
	public static ArrayList<FinanceItem> getLiabilityStateItems(int id) {
		return getLiabilityDBConnecter().getStateItems(id);
	}
	
	public static int addOpneUsedItem(int type, int id) {
		return mInstance.mDBConnector.getBaseFinanceDBInstance(type).addOpneUsedItem(id);
	}
	
	
	public static int addExpenseFromAssets(int expenseID, int assetsID) {
		return getAssetsDBConnecter().addExpenseFromAssets(expenseID, assetsID);
	}
	
	public static int addIncomeFromAssets(int incomeID, int assetsID) {
		return getAssetsDBConnecter().addIncomeFromAssets(incomeID, assetsID);
	}
	
	public static ArrayList<Integer> getExpenseFromAssets(int assetsID) {
		return getAssetsDBConnecter().getExpenseFromAssets(assetsID);
	}
	
	public static ArrayList<Integer> getIncomeFromAssets(int assetsID) {
		return getAssetsDBConnecter().getIncomeFromAssets(assetsID);
	}

	public static long updateAssetsStock(AssetsStockItem stock) {
		return getAssetsDBConnecter().updateStock(stock);
	}

	public static ArrayList<FinanceItem> getExpenseItemFromAccount(int accountId, int year, int month) {
		return getExpenseDBConnecter().getItemFromAccount(accountId, year, month);
	}

	public static ArrayList<FinanceItem> getIncomeItemFromAccount(int accountId, int year, int month) {
		return getIncomeDBConnecter().getItemFromAccount(accountId, year, month);
	}
	
	public static ArrayList<FinanceItem> getExpenseItemFromMypocket(int year, int month) {
		return getExpenseDBConnecter().getItemFromMypocket(year, month);
	}

	public static ArrayList<FinanceItem> getIncomeItemFromMypocket(int year, int month) {
		return getIncomeDBConnecter().getItemFromMypocket(year, month);
	}

	

	

	
}
