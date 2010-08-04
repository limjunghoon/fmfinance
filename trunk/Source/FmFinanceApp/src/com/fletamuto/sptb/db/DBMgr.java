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
	private static DBMgr instance = null;
	private DBConnector dbConnector = new DBConnector();
	private static FinanceDBHelper DBHelper = null; 
	public static final String DB_TAG = "db_tag"; 
	
	/** �ܺο����� ������ �� ����. getInstance()�� ��ü�� ��� ����Ѵ�. */
	private DBMgr() {
	}
	
	/** ��ü�� ��´�. */
	public static DBMgr getInstance() {
		if (instance == null) {
			instance = new DBMgr();
		}
		return instance;
	}
	
	/**
	 * DB�ʱ�ȭ �� ��� �����Ѵ�.
	 * @param context ������ �� ��ü ���ؽ�Ʈ
	 */
	public void initialize(Context context) {
		DBHelper = new FinanceDBHelper(context);
	}
	
	/**
	 * DB�� ��������  �߰��Ѵ�.
	 * @param item ����, ����, �ڻ�, ��ä ��ü
	 * @return �����̸� true ���и� false
	 */
	public boolean addFinanceItem(FinanceItem item) {
		dbConnector.AddFinanceItem(item);
		return true;
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �������� ��� �����´�.
	 * @param itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public ArrayList<FinanceItem> getAllItems(int itemType) {
		return dbConnector.getFinanceAllItems(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �ش� ��¥�� �������� �����´�.
	 * @param itemType itemType ������ ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ArrayList<FinanceItem> ������ ����Ʈ
	 */
	public ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		return dbConnector.getItems(itemType, calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڼ�, ��ä �� �ش��ϴ� ���̵� �����.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param id ������ ���̵�]
	 * @return ������ ������ ��
	 */
	public int deleteItem(int itemType, int id) {
		return dbConnector.deleteItem(itemType, id);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ϳ��� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return long �� �׼�
	 */
	public long getTotalAmount(int itemType) {
		return dbConnector.getTotalAmount(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �� �׼��� ��´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return long �� �׼�
	 */
	public long getTotalAmountDay(int itemType, Calendar calendar) {
		return dbConnector.getTotalAmountDay(itemType, calendar);
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �ش� ��¥�� �Էµ� ������ ������ ��´�.
	 * @param itemType  ����, ����, �ڻ�, ��ä Ÿ��
	 * @param calendar ������ ��¥
	 * @return ������ ����
	 */
	public int getItemCount(int itemType, Calendar calendar) {
		return dbConnector.getItemCount(itemType, calendar);
	}
	
	/**
	 * DBHelper ��ü�� ��´�.
	 * @return DBHelper ��ü
	 */
	public FinanceDBHelper getDBHelper() {
		return DBHelper;
	}
	
	/**
	 * DB�� �Էµ� ����, ����, �ڻ�, ��ä �� �з� ����Ʈ�� �����´�.
	 * @param itemType ����, ����, �ڻ�, ��ä Ÿ��
	 * @return �з� ����Ʈ
	 */
	public ArrayList<Category> getCategory(int itemType) {
		return dbConnector.getCategory(itemType);
	}
	
	/**
	 * DB�� �Էµ� ����, �ڻ� �� �з��� ���� �з� ����Ʈ�� �����´�. 
	 * @param itemType ����, �ڻ�  Ÿ��
	 * @param mainCategoryId ���� �з�
	 * @return ���� �з� ����Ʈ
	 */
	public ArrayList<Category> getSubCategory(int itemType, int mainCategoryId) {
		return dbConnector.getSubCategory(itemType, mainCategoryId);
	}
}
