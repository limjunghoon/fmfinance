package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;

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
	
	/**
	 * DB�� ��������  �߰��Ѵ�.
	 * @param item ����, ����, �ڻ�, ��ä ��ü
	 * @return �����̸� true ���и� false
	 */
	public boolean addFinanceItem(FinanceItem item) {
		return mDBConnector.addFinanceItem(item);
	}
	
	/**
	 * DB�� ��������  �����Ѵ�.
	 * @param item ����, ����, �ڻ�, ��ä ��ü
	 * @return �����̸� true ���и� false
	 */
	public boolean updateFinanceItem(FinanceItem item) {
		return mDBConnector.updateFinanceItem(item);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �������� ��� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public ArrayList<FinanceItem> getAllItems(int itemType) {
		return mDBConnector.getFinanceAllItems(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �ش� ��¥�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		return mDBConnector.getItems(itemType, calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ID�� �������� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ID
	 * @return ������ ������ ���н� null
	 */
	public FinanceItem getItem(int itemType, int id) {
		return mDBConnector.getItem(itemType, id);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش��ϴ� ���̵� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�]
	 * @return ������ ������ ��
	 */
	public int deleteItem(int itemType, int id) {
		return mDBConnector.deleteItem(itemType, id);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return long �� �׼�
	 */
	public long getTotalAmount(int itemType) {
		return mDBConnector.getTotalAmount(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return long �� �׼�
	 */
	public long getTotalAmountDay(int itemType, Calendar calendar) {
		return mDBConnector.getTotalAmountDay(itemType, calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �Էµ� ������ ������ ��´�.
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ������ ����
	 */
	public int getItemCount(int itemType, Calendar calendar) {
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
	 * DB�� �Էµ� ���� �з��� �����´�.]
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param name  �з��̸�
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addCategory(int itemType, String name) {
		return mDBConnector.addCategory(itemType, name);
	}
	
	/**
	 * DB�� �Էµ� �з� ����Ʈ�� �����´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return �з� ����Ʈ
	 */
	public ArrayList<Category> getCategory(int itemType) {
		return mDBConnector.getCategory(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, �ڻ��� �з� �� ���� �з� ����Ʈ�� �����´�. 
	 * @param itemType ����, �ڻ�  Ÿ��
	 * @param mainCategoryId ���� �з�
	 * @return ���� �з� ����Ʈ
	 */
	public ArrayList<Category> getSubCategory(int itemType, long mainCategoryId) {
		return mDBConnector.getSubCategory(itemType, mainCategoryId);
	}
}
