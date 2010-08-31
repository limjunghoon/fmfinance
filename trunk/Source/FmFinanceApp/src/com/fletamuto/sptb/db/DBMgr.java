package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.FinancialInstitution;

/**
 * ���̾ƿ���  DB�� �����ϴ� Ŭ����
 * SingleTone���� �����Ǿ� getInstance()�� �̿��� ����
 * @author yongbban
 * @version 1.0.0.1
 */
public class DBMgr {
	private static DBMgr mInstance = null;
	private DBConnector mDBConnector = new DBConnector();
	private static FinanceDBHelper mDBHelper = null; 
	public static final String DB_TAG = "db_tag"; 
	
	/** �ܺο����� ������ �� ����. getInstance()�� ��ü�� ��� ����Ѵ�. */
	private DBMgr() {
	}
	
	/** ��ü�� ��´�. */
	public static DBMgr getInstance() {
		if (mInstance == null) {
			mInstance = new DBMgr();
		}
		return mInstance;
	}
	
	/**
	 * DB�ʱ�ȭ �� ��� �����Ѵ�.
	 * @param context ������ �� ��ü ���ؽ�Ʈ
	 */
	public void initialize(Context context) {
		mDBHelper = new FinanceDBHelper(context);
	}
	
	private boolean checkItemType(int type) {
		if (type < 0) {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + type);
			return false;
		}
		return true;
	}
	
	/**
	 * DB�� ��������  �߰��Ѵ�.
	 * @param item ����, ����, �ڻ�, ��ä ��ü
	 * @return �����̸� true ���и� false
	 */
	public boolean addFinanceItem(FinanceItem item) {
		if (checkItemType(item.getType()) == false) return false;
		return mDBConnector.addFinanceItem(item);
	}
	
	/**
	 * DB�� ��������  �����Ѵ�.
	 * @param item ����, ����, �ڻ�, ��ä ��ü
	 * @return �����̸� true ���и� false
	 */
	public boolean updateFinanceItem(FinanceItem item) {
		if (checkItemType(item.getType()) == false) return false;
		return mDBConnector.updateFinanceItem(item);
	}
	
	/**
	 * ���̵� �ش��ϴ� �з� �̸��� �����Ѵ�. 
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�
	 * @param name ����Ǵ� �з� �̸�
	 * @return ��������
	 */
	public boolean updateCategory(int itemType, int id, String name) {
		if (checkItemType(itemType) == false) return false;
		return mDBConnector.updateCategory(itemType, id, name);
	}
	
	/**
	 * ���̵� �ش��ϴ� ���� �з� �̸��� �����Ѵ�. 
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�
	 * @param name ����Ǵ� ���� �з� �̸�
	 * @return ��������
	 */
	public boolean updateSubCategory(int itemType, int id, String name) {
		if (checkItemType(itemType) == false) return false;
		return mDBConnector.updateSubCategory(itemType, id, name);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �������� ��� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public ArrayList<FinanceItem> getAllItems(int itemType) {
		if (checkItemType(itemType) == false) return null;
		return mDBConnector.getFinanceAllItems(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �ش� ��¥�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		if (checkItemType(itemType) == false) return null;
		return mDBConnector.getItems(itemType, calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ID�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ID
	 * @return ������ ������ ���н� null
	 */
	public FinanceItem getItem(int itemType, int id) {
		if (checkItemType(itemType) == false) return null;
		return mDBConnector.getItem(itemType, id);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش��ϴ� ���̵� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�]
	 * @return ������ ������ ��
	 */
	public int deleteItem(int itemType, int id) {
		if (checkItemType(itemType) == false) return -1;
		return mDBConnector.deleteItem(itemType, id);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return long �� �׼�
	 */
	public long getTotalAmount(int itemType) {
		if (checkItemType(itemType) == false) return -1;
		return mDBConnector.getTotalAmount(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return long �� �׼�
	 */
	public long getTotalAmountDay(int itemType, Calendar calendar) {
		if (checkItemType(itemType) == false) return 0L;
		return mDBConnector.getTotalAmountDay(itemType, calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �Էµ� ������ ������ ��´�.
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ������ ����
	 */
	public int getItemCount(int itemType, Calendar calendar) {
		if (checkItemType(itemType) == false) return -1;
		return mDBConnector.getItemCount(itemType, calendar);
	}
	
	/**
	 * DBHelper ��ü�� ��´�.
	 * @return DBHelper ��ü
	 */
	public FinanceDBHelper getDBHelper() {
		return mDBHelper;
	}
	
	/**
	 * ���ο� �з��� �߰��Ѵ�.
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param name  �з��̸�
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public int addCategory(int itemType, String name) {
		if (checkItemType(itemType) == false) return -1;
		return (int)mDBConnector.addCategory(itemType, name);
	}
	
	/**
	 * ���� �з��� �߰��Ѵ�.
	 * @param itemTyee ����, ����, �ڻ�, ��ä Ÿ��
	 * @param mainCategoryID ���� �з� ���̵�
	 * @param name ���� �з� �̸�
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public int addSubCategory(int itemType, long mainCategoryID, String name) {
		if (checkItemType(itemType) == false) return -1;
		return (int)mDBConnector.addSubCategory(itemType, mainCategoryID, name);
	}
	
	/**
	 * DB�� �Էµ� �з� ����Ʈ�� �����´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return �з� ����Ʈ
	 */
	public ArrayList<Category> getCategory(int itemType) {
		if (checkItemType(itemType) == false) return null;
		return mDBConnector.getCategory(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, �ڻ��� �з� �� ���� �з� ����Ʈ�� �����´�. 
	 * @param itemType ����, �ڻ�  Ÿ��
	 * @param mainCategoryId ���� �з�
	 * @return ���� �з� ����Ʈ
	 */
	public ArrayList<Category> getSubCategory(int itemType, long mainCategoryId) {
		if (checkItemType(itemType) == false) return null;
		return mDBConnector.getSubCategory(itemType, mainCategoryId);
	}
	
	/**
	 * DB���� ID�� �ش��ϴ� ī�װ��� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param itemID ������ ���̵�
	 * @return 
	 */
	public int deleteCategory(int itemType, int itemID) {
		if (checkItemType(itemType) == false) return 0;
		return mDBConnector.deleteCategory(itemType, itemID);
	}
	
	public int deleteSubCategory(int itemType, int itemID) {
		if (checkItemType(itemType) == false) return 0;
		return mDBConnector.deleteSubCategory(itemType, itemID);
	}

	public ArrayList<FinancialInstitution> getInstitutions() {
		return mDBConnector.getInstitutions();
	}

	public FinancialInstitution getInstitution(int id) {
		return mDBConnector.getInstitution(id);
	}
	
	public int addAccountItem(AccountItem account) {
		return mDBConnector.addAccountItem(account);
	}
	
	public AccountItem getAccountItem(int id) {
		return mDBConnector.getAccountItem(id);
	}
	
	public ArrayList<AccountItem> getAccountAllItems() {
		return mDBConnector.getAccountAllItems();
	}
	
	public int deleteAccount(int id) {
		return mDBConnector.deleteAccount(id);
	}
}
