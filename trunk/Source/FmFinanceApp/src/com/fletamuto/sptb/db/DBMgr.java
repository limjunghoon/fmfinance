package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;

import com.fletamuto.sptb.LogTag;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardCompenyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.data.ItemDef;

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
		return mInstance.mDBConnector.updateFinanceItem(item);
	}
	
	/**
	 * ���̵� �ش��ϴ� �з� �̸��� �����Ѵ�. 
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�
	 * @param name ����Ǵ� �з� �̸�
	 * @return ��������
	 */
	public static int updateCategory(int itemType, int id, String name) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return 0;
		return mInstance.mDBConnector.updateCategory(itemType, id, name);
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
		return mInstance.mDBConnector.updateSubCategory(itemType, id, name);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �������� ��� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public static ArrayList<FinanceItem> getAllItems(int itemType) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getFinanceAllItems(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �ش� ��¥�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public static ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getItems(itemType, calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ID�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ID
	 * @return ������ ������ ���н� null
	 */
	public static FinanceItem getItem(int itemType, int id) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getItem(itemType, id);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش��ϴ� ���̵� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�]
	 * @return ������ ������ ��
	 */
	public static int deleteItem(int itemType, int id) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
		return mInstance.mDBConnector.deleteItem(itemType, id);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return long �� �׼�
	 */
	public static long getTotalAmount(int itemType) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
		return mInstance.mDBConnector.getTotalAmount(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return long �� �׼�
	 */
	public static long getTotalAmountDay(int itemType, Calendar calendar) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return 0L;
		return mInstance.mDBConnector.getTotalAmountDay(itemType, calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �Էµ� ������ ������ ��´�.
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ������ ����
	 */
	public static int getItemCount(int itemType, Calendar calendar) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
		return mInstance.mDBConnector.getItemCount(itemType, calendar);
	}
	
	/**
	 * DBHelper ��ü�� ��´�.
	 * @return DBHelper ��ü
	 */
	public static FinanceDBHelper getDBHelper() {
		return DBMgr.mDBHelper;
	}
	
	/**
	 * ���ο� �з��� �߰��Ѵ�.
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param name  �з��̸�
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public static int addCategory(int itemType, String name) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return -1;
		return (int)mInstance.mDBConnector.addCategory(itemType, name);
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
		return (int)mInstance.mDBConnector.addSubCategory(itemType, mainCategoryID, name);
	}
	
	/**
	 * DB�� �Էµ� �з� ����Ʈ�� �����´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return �з� ����Ʈ
	 */
	public static ArrayList<Category> getCategory(int itemType) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getCategory(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, �ڻ��� �з� �� ���� �з� ����Ʈ�� �����´�. 
	 * @param itemType ����, �ڻ�  Ÿ��
	 * @param mainCategoryId ���� �з�
	 * @return ���� �з� ����Ʈ
	 */
	public static ArrayList<Category> getSubCategory(int itemType, long mainCategoryId) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return null;
		return mInstance.mDBConnector.getSubCategory(itemType, mainCategoryId);
	}
	
	/**
	 * DB���� ID�� �ش��ϴ� ī�װ��� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param itemID ������ ���̵�
	 * @return 
	 */
	public static int deleteCategory(int itemType, int itemID) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return 0;
		return mInstance.mDBConnector.deleteCategory(itemType, itemID);
	}
	
	public static int deleteSubCategory(int itemType, int itemID) {
		if (DBMgr.checkFinanceItemType(itemType) == false) return 0;
		return mInstance.mDBConnector.deleteSubCategory(itemType, itemID);
	}

	public static ArrayList<FinancialCompany> getInstitutions() {
		return mInstance.mDBConnector.getInstitutions();
	}

	public static FinancialCompany getInstitution(int id) {
		return mInstance.mDBConnector.getInstitution(id);
	}
	
	public static int addAccountItem(AccountItem account) {
		return mInstance.mDBConnector.addAccountItem(account);
	}
	
	public static AccountItem getAccountItem(int id) {
		return mInstance.mDBConnector.getAccountItem(id);
	}
	
	public static ArrayList<AccountItem> getAccountAllItems() {
		return mInstance.mDBConnector.getAccountAllItems();
	}
	
	public static int deleteAccount(int id) {
		return mInstance.mDBConnector.deleteAccount(id);
	}

	public static ArrayList<CardCompenyName> getCardCompanyNames() {
		return mInstance.mDBConnector.getCardCompanyNames();
	}

	public static CardCompenyName getCardCompanyName(int id) {
		return mInstance.mDBConnector.getCardCompanyName(id);
	}

	public static int addCardItem(CardItem card) {
		return mInstance.mDBConnector.addCard(card);
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


	public static ArrayList<ExpenseTag> getTag() {
		return mInstance.mDBConnector.getTagDBConnector().getAllItems();
	}
}
