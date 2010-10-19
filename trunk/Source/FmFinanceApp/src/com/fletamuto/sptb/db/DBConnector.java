
package com.fletamuto.sptb.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;

import com.fletamuto.sptb.LogTag;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardCompenyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.FinancialCompany;

/**
 * DB와 연동하여 데이타를 관리한다.
 * @author yongbban
 * @version 1.0.0.1
 */
public class DBConnector {
	private CardCompanyNameDBConnector mCardCompanyNameDBConnector = new CardCompanyNameDBConnector();
	private CardItemDBConnector mCardDBConnector = new CardItemDBConnector();
	private InstitutionDBConnector mInstitutionDBConnector = new InstitutionDBConnector();
	private AccountDBConnector mAccountDBConnector = new AccountDBConnector();
	private BaseFinanceDBConnector[] mDBConnector = {
			new IncomeDBConnector(), 
			new ExpenseDBConnector(), 
			new AssetsDBConnector(), 
			new LiabilityDBConnector()};
	
	protected BaseFinanceDBConnector getDBInstance(int itemType){
		if (itemType < 0|| itemType >= mDBConnector.length) {
			Log.e(LogTag.DB, "== invaild finance item itemType : " + itemType);
			return null;
		}
		return mDBConnector[itemType];
	}
	  
	public boolean addFinanceItem(FinanceItem item) {
		Log.i(LogTag.DB, "== DBConnector AddFinanceItem type : " + item.getType());
		if (item.getType() >= mDBConnector.length) {
			Log.e(LogTag.DB, "== invaild finance item " + item.getType());
			return false;
		}
		
		if (item.getCategory().getId() == -1) {
			Log.e(LogTag.DB, "== invaild category item ID");
			return false;
		}
		
		return getDBInstance(item.getType()).addItem(item);
	}
	
	public boolean updateFinanceItem(FinanceItem item) {
		Log.i(LogTag.DB, "== DBConnector updateFinanceItem type : " + item.getType());
		if (item.getType() >= mDBConnector.length) {
			Log.e(LogTag.DB, "== invaild finance item " + item.getType());
			return false;
		}
		
		if (item.getId() == -1 || item.getCategory().getId() == -1) {
			Log.e(LogTag.DB, "== invaild item ID");
			return false;
		}
		
		return getDBInstance(item.getType()).updateItem(item);
	}
	
	public FinanceItem getItem(int itemType, int id) {
		Log.i(LogTag.DB, "== DBConnector getItem type : " + itemType);
		return getDBInstance(itemType).getItem(id);
	}
	
	public long getTotalAmount(int itemType) {
		Log.i(LogTag.DB, "== DBConnector getTotalAmount type : " + itemType);
		return getDBInstance(itemType).getTotalAmount();
	}
	
	public long getTotalAmountDay(int itemType, Calendar calendar) {
		Log.i(LogTag.DB, "== DBConnector getTotalAmountDay type : " + itemType);
		return getDBInstance(itemType).getTotalAmountDay(calendar);
	}
	
	public ArrayList<FinanceItem> getFinanceAllItems(int itemType) {
		Log.i(LogTag.DB, "== DBConnector getFinanceAllItems type : " + itemType);
		return getDBInstance(itemType).getAllItems();
	}
	
	public long addCategory(int itemType, String name) {
		Log.i(LogTag.DB, "== DBConnector addCategory type : " + itemType);
		return getDBInstance(itemType).addCategory(name);
	}
	
	public long addSubCategory(int itemType, long mainCategoryID, String name) {
		Log.i(LogTag.DB, "== DBConnector addSubCategory type : " + itemType);
		return getDBInstance(itemType).addSubCategory(mainCategoryID, name);
	}
	
	public ArrayList<Category> getCategory(int itemType) {
		Log.i(LogTag.DB, "== DBConnector getCategory type : " + itemType);
		return getDBInstance(itemType).getCategory();
	}

	public ArrayList<Category> getSubCategory(int itemType, long mainCategoryId) {
		Log.i(LogTag.DB, "== DBConnector getSubCategory type : " + itemType);
		return getDBInstance(itemType).getSubCategory(mainCategoryId);
	}
	
	public int getItemCount(int itemType, Calendar calendar) {
		Log.i(LogTag.DB, "== DBConnector getItemCount type : " + itemType);
		return getDBInstance(itemType).getItemCount(calendar);
	}

	public ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		Log.i(LogTag.DB, "== DBConnector getItems type : " + itemType);
		return getDBInstance(itemType).getItems(calendar);
	}

	public int deleteItem(int itemType, int id) {
		Log.i(LogTag.DB, "== DBConnector deleteItem ");
		int result = getDBInstance(itemType).deleteItem(id);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not delete id : " + id + " type : " + itemType); 
		}
		return result;
	}

	public int deleteCategory(int itemType, int id) {
		Log.i(LogTag.DB, "== DBConnector deleteCategory ");
		int result = getDBInstance(itemType).deleteCategory(id);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not delete id : " + id + " type : " + itemType); 
		}
		return result;
	}

	public int deleteSubCategory(int itemType, int id) {
		Log.i(LogTag.DB, "== DBConnector deleteSubCategory ");
		int result = getDBInstance(itemType).deleteSubCategory(id);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not delete id : " + id + " type : " + itemType); 
		}
		return result;
	}

	public boolean updateCategory(int itemType, int id, String name) {
		Log.i(LogTag.DB, "== DBConnector updateCategory ");
		boolean result = getDBInstance(itemType).updateCategory(id, name);
		if (result == false) {
			Log.e(LogTag.DB, "== do not update id : " + id + " type : " + itemType); 
		}
		return result;
	}
	
	public boolean updateSubCategory(int itemType, int id, String name) {
		Log.i(LogTag.DB, "== DBConnector updateSubCategory ");
		boolean result = getDBInstance(itemType).updateSubCategory(id, name);
		if (result == false) {
			Log.e(LogTag.DB, "== do not update id : " + id + " type : " + itemType); 
		}
		return result;
	}

	public ArrayList<FinancialCompany> getInstitutions() {
		return mInstitutionDBConnector.getAllItems();
	}

	public FinancialCompany getInstitution(int id) {
		return mInstitutionDBConnector.getItem(id);
	}

	public int addAccountItem(AccountItem account) {
		return mAccountDBConnector.addItem(account);
	}

	public AccountItem getAccountItem(int id) {
		return mAccountDBConnector.getItem(id);
	}

	public ArrayList<AccountItem> getAccountAllItems() {
		return mAccountDBConnector.getAllItems();
	}

	public int deleteAccount(int id) {
		return mAccountDBConnector.deleteAccountItem(id);
	}

	public ArrayList<CardCompenyName> getCardCompanyNames() {
		return mCardCompanyNameDBConnector.getAllItems();
	}

	public CardCompenyName getCardCompanyName(int id) {
		return mCardCompanyNameDBConnector.getItem(id);
	}

	public int addCard(CardItem card) {
		return mCardDBConnector.addItem(card);
	}

	public ArrayList<CardItem> getCardItems(int type) {
		if (type < CardItem.CREDIT_CARD  ||  type > CardItem.CASH_CARD) {
			Log.e(LogTag.DB, "== invaild card item type : " + type); 
			return null;
		}
		return mCardDBConnector.getAllItems(type);
	}

	public CardItem getCardItem(int id) {
		return mCardDBConnector.getItem(id);
	}
}


