package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;

/**
 * 레이아웃과  DB를 연동하는 클래스
 * SingleTone으로 구성되어 getInstance()를 이용해 접근
 * @author yongbban
 * @version 1.0.0.1
 */
public class DBMgr {
	private static DBMgr mInstance = null;
	private DBConnector mDBConnector = new DBConnector();
	private static FinanceDBHelper mDBHelper = null; 
	public static final String DB_TAG = "db_tag"; 
	
	/** 외부에서는 생성할 수 없다. getInstance()로 객체를 얻어 사용한다. */
	private DBMgr() {
	}
	
	/** 객체를 얻는다. */
	public static DBMgr getInstance() {
		if (mInstance == null) {
			mInstance = new DBMgr();
		}
		return mInstance;
	}
	
	/**
	 * DB초기화 및 뷰와 연동한다.
	 * @param context 연동할 뷰 객체 컨텍스트
	 */
	public void initialize(Context context) {
		mDBHelper = new FinanceDBHelper(context);
	}
	
	/**
	 * DB에 아이템을  추가한다.
	 * @param item 수입, 지출, 자산, 부채 객체
	 * @return 성공이면 true 실패면 false
	 */
	public boolean addFinanceItem(FinanceItem item) {
		return mDBConnector.addFinanceItem(item);
	}
	
	/**
	 * DB에 아이템을  갱신한다.
	 * @param item 수입, 지출, 자산, 부채 객체
	 * @return 성공이면 true 실패면 false
	 */
	public boolean updateFinanceItem(FinanceItem item) {
		return mDBConnector.updateFinanceItem(item);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 하나의 아이템을 모두 가져온다.
	 * @param itemType 가져올 수입, 지출, 자산, 부채 타입
	 * @return ArrayList<FinanceItem> 아이템 리스트
	 */
	public ArrayList<FinanceItem> getAllItems(int itemType) {
		return mDBConnector.getFinanceAllItems(itemType);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 해당 날짜의 아이템을 가져온다.
	 * @param itemType 가져올 수입, 지출, 자산, 부채 타입
	 * @param calendar 가져올 날짜
	 * @return ArrayList<FinanceItem> 아이템 리스트
	 */
	public ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		return mDBConnector.getItems(itemType, calendar);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 해당 ID의 아이템을 가져온다.
	 * @param itemType 가져올 수입, 지출, 자산, 부채 타입
	 * @param id 가져올 ID
	 * @return 성공시 아이템 실패시 null
	 */
	public FinanceItem getItem(int itemType, int id) {
		return mDBConnector.getItem(itemType, id);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 해당하는 아이디를 지운다.
	 * @param itemType 수입, 지출, 자산, 부채 타입
	 * @param id 삭제할 아이디]
	 * @return 삭제된 아이템 수
	 */
	public int deleteItem(int itemType, int id) {
		return mDBConnector.deleteItem(itemType, id);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 하나의 총 액수를 얻는다.
	 * @param itemType 수입, 지출, 자산, 부채 타입
	 * @return long 총 액수
	 */
	public long getTotalAmount(int itemType) {
		return mDBConnector.getTotalAmount(itemType);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 해당 날짜의 총 액수를 얻는다.
	 * @param itemType 수입, 지출, 자산, 부채 타입
	 * @param calendar 가져올 날짜
	 * @return long 총 액수
	 */
	public long getTotalAmountDay(int itemType, Calendar calendar) {
		return mDBConnector.getTotalAmountDay(itemType, calendar);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 해당 날짜의 입력된 아이템 갯수를 얻는다.
	 * @param itemType  수입, 지출, 자산, 부채 타입
	 * @param calendar 가져올 날짜
	 * @return 아이템 갯수
	 */
	public int getItemCount(int itemType, Calendar calendar) {
		return mDBConnector.getItemCount(itemType, calendar);
	}
	
	/**
	 * DBHelper 객체를 얻는다.
	 * @return DBHelper 객체
	 */
	public FinanceDBHelper getDBHelper() {
		return mDBHelper;
	}
	
	/**
	 * DB에 입력된 메인 분류를 가져온다.]
	 * @param itemType  수입, 지출, 자산, 부채 타입
	 * @param name  분류이름
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addCategory(int itemType, String name) {
		return mDBConnector.addCategory(itemType, name);
	}
	
	/**
	 * DB에 입력된 분류 리스트를 가져온다.
	 * @param itemType 수입, 지출, 자산, 부채 타입
	 * @return 분류 리스트
	 */
	public ArrayList<Category> getCategory(int itemType) {
		return mDBConnector.getCategory(itemType);
	}
	
	/**
	 * DB에 입력된 지출, 자산의 분류 중 하위 분류 리스트를 가져온다. 
	 * @param itemType 지출, 자산  타입
	 * @param mainCategoryId 상위 분류
	 * @return 하위 분류 리스트
	 */
	public ArrayList<Category> getSubCategory(int itemType, long mainCategoryId) {
		return mDBConnector.getSubCategory(itemType, mainCategoryId);
	}
}
