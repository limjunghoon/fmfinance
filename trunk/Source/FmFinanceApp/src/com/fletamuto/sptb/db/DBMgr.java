package com.fletamuto.sptb.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;

import android.content.Context;

public class DBMgr {
	private static DBMgr instance = null;
	private DBConnect dbConnect = new DBConnect();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private static int INCOME = 1;
	private static int EXPENSE = 2;
	
	private DBMgr() {}
	
	public static DBMgr getInstance() {
		if (instance == null) {
			instance = new DBMgr();
		}
		return instance;
	}
	
	public void initialize(Context context) {
		dbConnect.connectDB(context);
	}
	
	public boolean addExpenseInfo(ExpenseItem infoExpense) {
		String date = dateFormat.format(infoExpense.getCreateDate().getTime());
		date = String.format("날짜 : %s", date);
        String amount = String.format("%,d", infoExpense.getAmount());
        amount = String.format("금액 :  %s 원", amount);
        String memo = infoExpense.getMemo();
        memo = String.format("메모 : %s", memo);
        String _type = "분류 : ";
        
		dbConnect.setData_DATE_TYPE_MUCH_MEMO(date ,_type,amount,memo,EXPENSE);
		
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
}
