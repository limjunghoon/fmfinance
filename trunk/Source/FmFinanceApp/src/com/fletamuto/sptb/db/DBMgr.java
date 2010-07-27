package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.content.Context;
import com.fletamuto.sptb.data.FinanceItem;

public class DBMgr {
	private static DBMgr instance = null;
	private DBConnector dbConnector = new DBConnector();
	private static FinanceDBHelper DBHelper = null; 
	public static final String DB_TAG = "db_tag"; 
	
	private DBMgr() {
	}
	
	public static DBMgr getInstance() {
		if (instance == null) {
			instance = new DBMgr();
		}
		return instance;
	}
	
	public void initialize(Context context) {
		DBHelper = new FinanceDBHelper(context);
		DBHelper.getWritableDatabase();
	}
	
	public boolean addFinanceItem(FinanceItem item) {
		dbConnector.AddFinanceItem(item);
		return true;
	}
	
	public ArrayList<FinanceItem> getAllItems(int itemType) {
		return dbConnector.getFinanceAllItems(itemType);
	}
	
	public FinanceDBHelper getDBHelper() {
		return DBHelper;
	}
	
	/*public boolean addExpenseInfo(ExpenseItem itemExpense) {
		String date = itemExpense.getDateString();
		date = String.format("날짜 : %s", date);
        String amount = String.format("%,d", itemExpense.getAmount());
        amount = String.format("금액 :  %s 원", amount);
        String memo = itemExpense.getMemo();
        memo = String.format("메모 : %s", memo);
        String _type = "분류 : ";
        
//		dbConnect.setData_DATE_TYPE_MUCH_MEMO(date ,_type,amount,memo,EXPENSE);
		
		return true;
	}
	
	public boolean addIncomeInfo(IncomeItem infoIncome) {
		String date = dateFormat.format(infoIncome.getCreateDate().getTime());
		date = String.format("날짜 : %s", date);
        String amount = String.format("%,d", infoIncome.getAmount());
        amount = String.format("금액 :  %s 원", amount);
        String memo = infoIncome.getMemo();
        memo = String.format("메모 : %s", memo);
        String _type = "분류 : ";
        
		dbConnect.setData_DATE_TYPE_MUCH_MEMO(date ,_type,amount,memo,INCOME);
		
		return true;
	}
	*/
	
	/*
	public ArrayList<HashMap<String,String>> getExpenseInfo() {
		ArrayList<HashMap<String,String>> expenseItems = new ArrayList<HashMap<String,String>>();
		dbConnect.getExpenseDataAll(expenseItems);
		return expenseItems;
	}
	
	public ArrayList<HashMap<String,String>> getIncomeInfo() {
		ArrayList<HashMap<String,String>> expenseItems = new ArrayList<HashMap<String,String>>();
		dbConnect.getIncomeDataAll(expenseItems);
		return expenseItems;
	}
	
	*/
}
