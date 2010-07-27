
package com.fletamuto.sptb.db;

import java.util.ArrayList;

import android.util.Log;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.LiabilityItem;


public class DBConnector {
	private IncomeDBConnector incomeDB = new IncomeDBConnector();
	private ExpenseDBConnector expenseDB = new ExpenseDBConnector();
	private AssetsDBConnector assetsDB = new AssetsDBConnector();
	private LiabilityDBConnector liabilityDB = new LiabilityDBConnector();
	  
	public boolean AddFinanceItem(FinanceItem item) {
		if (item.getType() == IncomeItem.TYPE) {
			incomeDB.AddItem((IncomeItem)item);
		}
		else if (item.getType() == ExpenseItem.TYPE) {
			expenseDB.AddItem((ExpenseItem)item);
		}
		else if (item.getType() == AssetsItem.TYPE) {
			assetsDB.AddItem((AssetsItem)item);
		}
		else if (item.getType() == LiabilityItem.TYPE) {
			liabilityDB.AddItem((LiabilityItem)item);
		}
		else {
			Log.e(DBMgr.DB_TAG, "== invaild finance item " + item.getType());
			return false;
		}

		return true;
	}
	
	public ArrayList<FinanceItem> getFinanceAllItems(int itemType) {
		return expenseDB.getAllItems();
	}
	
	/*
	private static SQLiteDatabase mDb;
	Cursor mCursor;
	private static Context context;
	private static ArrayList<String> sqlData;
	public static void DBConnetc()
	{
		sqlData = new ArrayList<String>();
	}
	public void connectDB(Context _context){
		mDb = _context.openOrCreateDatabase("MyFinance",context.MODE_PRIVATE, null);
		mDb.execSQL("drop table if exists inoutcome");
		mDb.execSQL("drop table if exists spendtype");
		mDb.execSQL("create table inoutcome ( " +
				"_id integer primary key autoincrement," +
				"_date text," +
				"_type text," +
				"_much text not null , " +
				"_memo text," +
				"_how text, " +
				"_inorout integer not null);"	);
		
		mDb.execSQL("create table spendtype ( " +
				"_id integer primary key autoincrement," +
				"_spendtype text not null );"	);
		
		
		ContentValues insertValues = new ContentValues();
		insertValues.put("_spendtype", "음식");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "교통");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "쇼핑");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "문화생활");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "집");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "통신비");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "의료비");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "경조사비");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "자동차");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "교육비");
		mDb.insert("spendtype", null, insertValues);
		insertValues.put("_spendtype", "자산");
		mDb.insert("spendtype", null, insertValues);
		
		
	}
	public void getSpendType(ArrayList<String> myArray){
		mCursor = mDb.query(
				"spendtype",	// String table
				new String[] {"_spendtype"},	// String[] columns
				null,	// String selection
				null,		// String[] selectionArgs
				null,		// String groupBy
				null,		// String having
				"_id",	// String orderBy
				null);		// String limit  
		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					for (int j=0; j<mCursor.getColumnCount(); j++) {
						myArray.add(0,mCursor.getString(j)); // orijin
					}
				} while (mCursor.moveToNext()); 
			}
		}
		
	}
	
	public void setData_DATE_TYPE_MUCH_MEMO(String _date,String _type,String _much,String _memo,int _inorout){
		
		ContentValues insertValues = new ContentValues();
		insertValues.put("_date", _date);
		insertValues.put("_type", _type);
		insertValues.put("_much", _much);
		insertValues.put("_memo", _memo);
		insertValues.put("_inorout", _inorout);
		mDb.insert("inoutcome", null, insertValues);
	
	}
	
	
	public void  getExpenseDataAll(ArrayList<HashMap<String,String>> myArray){
		
		mCursor = mDb.query(
				"inoutcome",	// String table
				new String[] {"_date","_type","_much","_memo"},	// String[] columns
				"_inorout = 2",	// String selection
				null,		// String[] selectionArgs
				null,		// String groupBy
				null,		// String having
				null,	// String orderBy
				null);		// String limit
		

		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					HashMap<String,String> item = new HashMap<String,String>();   
					for (int j=0; j<mCursor.getColumnCount(); j++) {
						item.put(mCursor.getColumnName(j) , mCursor.getString(j));   
					}
					myArray.add(item);  
				} while (mCursor.moveToNext()); 
			}
		}
		
	}
public void  getIncomeDataAll(ArrayList<HashMap<String,String>> myArray){
		
		mCursor = mDb.query(
				"inoutcome",	// String table
				new String[] {"_date","_type","_much","_memo"},	// String[] columns
				"_inorout = 1",	// String selection
				null,		// String[] selectionArgs
				null,		// String groupBy
				null,		// String having
				null,	// String orderBy
				null);		// String limit
		
	
		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					HashMap<String,String> item = new HashMap<String,String>();   
					for (int j=0; j<mCursor.getColumnCount(); j++) {
						item.put(mCursor.getColumnName(j) , mCursor.getString(j));   
					}
					myArray.add(item);  
				} while (mCursor.moveToNext()); 
			}
		}
		
	}	
*/
}


