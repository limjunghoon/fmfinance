package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;

import com.fletamuto.sptb.LogTag;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * ���̾ƿ���  DB�� �����ϴ� Ŭ����
 * SingleTone���� ����
 * @author yongbban
 * @version 1.0.0.1
 */
public final class DBMgr {
	private static final DBMgr mInstance = new DBMgr();
	private final DBConnector mDBConnector = new DBConnector();
	private static FinanceDBHelper mDBHelper = null; 
	
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
		DBMgr.mDBHelper = new FinanceDBHelper(context);
	}
	
	
	/**
	 * DBHelper ��ü�� ��´�.
	 * @return DBHelper ��ü
	 */
	public static FinanceDBHelper getDBHelper() {
		return DBMgr.mDBHelper;
	}
	
	/**
	 * �ݺ� �������� �߰�
	 * ��쿡 ���� �ӵ����� �ʿ�
	 * @return
	 */
	public static boolean addRepeatItems() {
		ArrayList<Repeat> repeatItems = mInstance.mDBConnector.getRepeatDBConnector().getAllItems();
		if (repeatItems == null) return true;
		
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
				
				FinanceItem item = DBMgr.getItem(repeat.getItemType(), repeat.getItemID());
				if (item == null) {
					Log.e(LogTag.DB, ":: Fail to Repeat Item " + repeat.getItemID());
					continue;
				}
				item.setCreateDate(lastApplyDate);
				DBMgr.addFinanceItem(item);
			}
			
			repeat.setLastApplyDay(lastApplyDate);
			DBMgr.updateRepeat(repeat);
			
		}
		return true;
	}
	
	
	private static boolean isAfterDay(Calendar today, Calendar lastApplyDate) {
		return (Integer.parseInt(FinanceDataFormat.getNumverDateFormat(today.getTime())) > Integer.parseInt(FinanceDataFormat.getNumverDateFormat(lastApplyDate.getTime())));
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
		if (DBMgr.checkFinanceItemType(item.getType()) == false) return -1; 
		return mInstance.mDBConnector.getBaseFinanceDBInstance(item.getType()).addItem(item);
	}
	
	/**
	 * DB�� ��������  �����Ѵ�.
	 * @param item ����, ����, �ڻ�, ��ä ��ü
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public static long updateFinanceItem(FinanceItem item) {
		if (DBMgr.checkFinanceItemType(item.getType()) == false) return -1;
		//return mInstance.mDBConnector.updateFinanceItem(item);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(item.getType()).updateItem(item);
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
		//return mInstance.mDBConnector.updateCategory(itemType, id, name);
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
		if (DBMgr.checkFinanceItemType(itemType) == false) return 0;
//		return mInstance.mDBConnector.updateSubCategory(itemType, id, name);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).updateSubCategory(id, name);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �������� ��� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public static ArrayList<FinanceItem> getAllItems(int itemType) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
		//return mInstance.mDBConnector.getFinanceAllItems(itemType);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getAllItems();
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �ش� ��¥�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public static ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
	//	return mInstance.mDBConnector.getItems(itemType, calendar);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItems(calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ID�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ID
	 * @return ������ ������ ���н� null
	 */
	public static FinanceItem getItem(int itemType, int id) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
//		return mInstance.mDBConnector.getItem(itemType, id);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getItem(id);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش��ϴ� ���̵� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�]
	 * @return ������ ������ ��
	 */
	public static int deleteItem(int itemType, int id) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
		//return mInstance.mDBConnector.deleteItem(itemType, id);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).deleteItem(id);
	}
	
	public static long updateRepeat(int itemType, int itemID, int repeatID) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
		//return mInstance.mDBConnector.deleteItem(itemType, id);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).updateRepeat(itemID, repeatID);
	}
	
	
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return long �� �׼�
	 */
	public static long getTotalAmount(int itemType) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
		//return mInstance.mDBConnector.getTotalAmount(itemType);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getTotalAmount();
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return long �� �׼�
	 */
	public static long getTotalAmountDay(int itemType, Calendar calendar) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return 0L;
	//	return mInstance.mDBConnector.getTotalAmountDay(itemType, calendar);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getTotalAmountDay(calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �Էµ� ������ ������ ��´�.
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ������ ����
	 */
	public static int getItemCount(int itemType, Calendar calendar) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
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
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
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
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
		//return (int)mInstance.mDBConnector.addSubCategory(itemType, mainCategoryID, name);
		return (int)mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).addSubCategory(mainCategoryID, name);
	}
	
	/**
	 * DB�� �Էµ� �з� ����Ʈ�� �����´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return �з� ����Ʈ
	 */
	public static ArrayList<Category> getCategory(int itemType) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
		//return mInstance.mDBConnector.getCategory(itemType);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getCategory();
	}
	
	/**
	 * DB�� �Էµ� ����, �ڻ��� �з� �� ���� �з� ����Ʈ�� �����´�. 
	 * @param itemType ����, �ڻ�  Ÿ��
	 * @param mainCategoryId ���� �з�
	 * @return ���� �з� ����Ʈ
	 */
	public static ArrayList<Category> getSubCategory(int itemType, long mainCategoryId) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
//		return mInstance.mDBConnector.getSubCategory(itemType, mainCategoryId);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).getSubCategory(mainCategoryId);
	}
	
	/**
	 * DB���� ID�� �ش��ϴ� ī�װ��� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param itemID ������ ���̵�
	 * @return 
	 */
	public static int deleteCategory(int itemType, int itemID) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return 0;
		//return mInstance.mDBConnector.deleteCategory(itemType, itemID);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).deleteCategory(itemID);
	}
	
	public static int deleteSubCategory(int itemType, int itemID) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return 0;
		//return mInstance.mDBConnector.deleteSubCategory(itemType, itemID);
		return mInstance.mDBConnector.getBaseFinanceDBInstance(itemType).deleteSubCategory(itemID);
	}
	
	public static int addCompany(FinancialCompany company) {
		return mInstance.mDBConnector.getCompanyDBConnector().addItem(company);
	}
	
	public static boolean updateCompany(FinancialCompany company) {
		return mInstance.mDBConnector.getCompanyDBConnector().updateItem(company);
	}

	public static ArrayList<FinancialCompany> getCompanys() {
		//return mInstance.mDBConnector.getInstitutions();
		return mInstance.mDBConnector.getCompanyDBConnector().getAllItems();
	}

	public static FinancialCompany getCompany(int id) {
		return mInstance.mDBConnector.getCompanyDBConnector().getItem(id);
	}
	
	public static int deleteCompany(int id) {
		//	return mInstance.mDBConnector.deleteAccount(id);
			return mInstance.mDBConnector.getCompanyDBConnector().deleteItem(id);
		}
	
	public static int addAccountItem(AccountItem account) {
		return mInstance.mDBConnector.getAccountDBConnector().addItem(account);
	}
	
	public static AccountItem getAccountItem(int id) {
		//return mInstance.mDBConnector.getAccountItem(id);
		return mInstance.mDBConnector.getAccountDBConnector().getItem(id);
	}
	
	public static ArrayList<AccountItem> getAccountAllItems() {
	//	return mInstance.mDBConnector.getAccountAllItems();
		return mInstance.mDBConnector.getAccountDBConnector().getAllItems();
	}
	
	public static int deleteAccount(int id) {
	//	return mInstance.mDBConnector.deleteAccount(id);
		return mInstance.mDBConnector.getAccountDBConnector().deleteAccountItem(id);
	}
	
	public static int addCardCompanyName(CardCompanyName cardCompanyName) {
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().addItem(cardCompanyName);
	}
	
	public static boolean updateCardCompanyName(CardCompanyName cardCompanyName) {
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().updateItem(cardCompanyName);
	}

	public static ArrayList<CardCompanyName> getCardCompanyNames() {
		//return mInstance.mDBConnector.getCardCompanyNames();
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().getAllItems();
	}

	public static CardCompanyName getCardCompanyName(int id) {
//		return mInstance.mDBConnector.getCardCompanyName(id);
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().getItem(id);
	}
	
	public static int deleteCardCompanyName(int id) {
		return mInstance.mDBConnector.getCardCompanyNameDBConnector().deleteItem(id);
	}

	public static int addCardItem(CardItem card) {
		//return mInstance.mDBConnector.addCard(card);
		return mInstance.mDBConnector.getCardDBConnector().addItem(card);
	}
	
	public static ArrayList<CardItem> getCardItems() {
		//return mInstance.mDBConnector.getCardDBConnector().getAllItems();
		return mInstance.mDBConnector.getCardDBConnector().getAllItems();
	}

	public static ArrayList<CardItem> getCardItems(int type) {
		//return mInstance.mDBConnector.getCardDBConnector().getAllItems(type);
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
}
