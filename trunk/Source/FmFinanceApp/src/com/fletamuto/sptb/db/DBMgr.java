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
	private static DBMgr instance = null;
	private DBConnector dbConnector = new DBConnector();
	private static FinanceDBHelper DBHelper = null; 
	public static final String DB_TAG = "db_tag"; 
	
	/** 외부에서는 생성할 수 없다. getInstance()로 객체를 얻어 사용한다. */
	private DBMgr() {
	}
	
	/** 객체를 얻는다. */
	public static DBMgr getInstance() {
		if (instance == null) {
			instance = new DBMgr();
		}
		return instance;
	}
	
	/**
	 * DB초기화 몇 뷰와 연동한다.
	 * @param context 연동할 뷰 객체 컨텍스트
	 */
	public void initialize(Context context) {
		DBHelper = new FinanceDBHelper(context);
	}
	
	/**
	 * DB에 아이템을  추가한다.
	 * @param item 수입, 지출, 자산, 부채 객체
	 * @return 성공이면 true 실패면 false
	 */
	public boolean addFinanceItem(FinanceItem item) {
		dbConnector.AddFinanceItem(item);
		return true;
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 하나의 아이템을 모두 가져온다.
	 * @param itemType 가져올 수입, 지출, 자산, 부채 타입
	 * @return ArrayList<FinanceItem> 아이템 리스트
	 */
	public ArrayList<FinanceItem> getAllItems(int itemType) {
		return dbConnector.getFinanceAllItems(itemType);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 해당 날짜의 아이템을 가져온다.
	 * @param itemType itemType 가져올 수입, 지출, 자산, 부채 타입
	 * @param calendar 가져올 날짜
	 * @return ArrayList<FinanceItem> 아이템 리스트
	 */
	public ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		return dbConnector.getItems(itemType, calendar);
	}
	
	/**
	 * DB에 입력된 수입, 주출, 자순, 부채 중 해당하는 아이디를 지운다.
	 * @param itemType 수입, 지출, 자산, 부채 타입
	 * @param id 삭제할 아이디]
	 * @return 삭제된 아이템 수
	 */
	public int deleteItem(int itemType, int id) {
		return dbConnector.deleteItem(itemType, id);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 하나의 총 액수를 얻는다.
	 * @param itemType 수입, 지출, 자산, 부채 타입
	 * @return long 총 액수
	 */
	public long getTotalAmount(int itemType) {
		return dbConnector.getTotalAmount(itemType);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 해당 날짜의 총 액수를 얻는다.
	 * @param itemType 수입, 지출, 자산, 부채 타입
	 * @param calendar 가져올 날짜
	 * @return long 총 액수
	 */
	public long getTotalAmountDay(int itemType, Calendar calendar) {
		return dbConnector.getTotalAmountDay(itemType, calendar);
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 중 해당 날짜의 입력된 아이템 갯수를 얻는다.
	 * @param itemType  수입, 지출, 자산, 부채 타입
	 * @param calendar 가져올 날짜
	 * @return 아이템 갯수
	 */
	public int getItemCount(int itemType, Calendar calendar) {
		return dbConnector.getItemCount(itemType, calendar);
	}
	
	/**
	 * DBHelper 객체를 얻는다.
	 * @return DBHelper 객체
	 */
	public FinanceDBHelper getDBHelper() {
		return DBHelper;
	}
	
	/**
	 * DB에 입력된 수입, 지출, 자산, 부채 의 분류 리스트를 가져온다.
	 * @param itemType 수입, 지출, 자산, 부채 타입
	 * @return 분류 리스트
	 */
	public ArrayList<Category> getCategory(int itemType) {
		return dbConnector.getCategory(itemType);
	}
	
	/**
	 * DB에 입력된 지출, 자산 의 분류의 하위 분류 리스트를 가져온다. 
	 * @param itemType 지출, 자산  타입
	 * @param mainCategoryId 상위 분류
	 * @return 하위 분류 리스트
	 */
	public ArrayList<Category> getSubCategory(int itemType, int mainCategoryId) {
		return dbConnector.getSubCategory(itemType, mainCategoryId);
	}
}
