
package com.fletamuto.sptb.db;

import android.util.Log;

import com.fletamuto.sptb.LogTag;

/**
 * DB와 연동하여 데이타를 관리한다.
 * @author yongbban
 * @version 1.0.0.1
 */
public class DBConnector {
	private final CardCompanyNameDBConnector mCardCompanyNameDBConnector = new CardCompanyNameDBConnector();
	private final CardItemDBConnector mCardDBConnector = new CardItemDBConnector();
	private final CompanyDBConnector mCompanyDBConnector = new CompanyDBConnector();
	private final AccountDBConnector mAccountDBConnector = new AccountDBConnector();
	private final TagDBConnector mTagDBConnector = new TagDBConnector();
	private final RepeatDBConnector mRepeatDBConnector = new RepeatDBConnector();
	private final BaseFinanceDBConnector[] mDBConnector = {
			new IncomeDBConnector(), 
			new ExpenseDBConnector(), 
			new AssetsDBConnector(), 
			new LiabilityDBConnector()};
	
	public BaseFinanceDBConnector getBaseFinanceDBInstance(int itemType){
		if (itemType < 0|| itemType >= mDBConnector.length) {
			Log.e(LogTag.DB, "== invaild finance item itemType : " + itemType);
			return null;
		}
		return mDBConnector[itemType];
	}
	public CompanyDBConnector getCompanyDBConnector() {
		return mCompanyDBConnector;
	}
	
	public AccountDBConnector getAccountDBConnector() {
		return mAccountDBConnector;
	}
	
	public CardCompanyNameDBConnector getCardCompanyNameDBConnector() {
		return mCardCompanyNameDBConnector;
	}
	
	public TagDBConnector getTagDBConnector() {
		return mTagDBConnector;
	}
	
	public CardItemDBConnector getCardDBConnector() {
		return mCardDBConnector;
	}
	public RepeatDBConnector getRepeatDBConnector() {
		return mRepeatDBConnector;
	}
	
	/*  
	public long addFinanceItem(FinanceItem item) {
		Log.i(LogTag.DB, "== DBConnector AddFinanceItem type : " + item.getType());
		if (item.getType() >= mDBConnector.length) {
			Log.e(LogTag.DB, "== invaild finance item " + item.getType());
			return -1;
		}
		
		if (item.getCategory().getId() == -1) {
			Log.e(LogTag.DB, "== invaild category item ID");
			return -1;
		}
		
		return getBaseFinanceDBInstance(item.getType()).addItem(item);
	}
	
	public long updateFinanceItem(FinanceItem item) {
		Log.i(LogTag.DB, "== DBConnector updateFinanceItem type : " + item.getType());
		if (item.getType() >= mDBConnector.length) {
			Log.e(LogTag.DB, "== invaild finance item " + item.getType());
			return -1;
		}
		
		if (item.getId() == -1 || item.getCategory().getId() == -1) {
			Log.e(LogTag.DB, "== invaild item ID");
			return -1;
		}
		
		return getBaseFinanceDBInstance(item.getType()).updateItem(item);
	}
	
	public FinanceItem getItem(int itemType, int id) {
		Log.i(LogTag.DB, "== DBConnector getItem type : " + itemType);
		return getBaseFinanceDBInstance(itemType).getItem(id);
	}
	
	public long getTotalAmount(int itemType) {
		Log.i(LogTag.DB, "== DBConnector getTotalAmount type : " + itemType);
		return getBaseFinanceDBInstance(itemType).getTotalAmount();
	}
	
	public long getTotalAmountDay(int itemType, Calendar calendar) {
		Log.i(LogTag.DB, "== DBConnector getTotalAmountDay type : " + itemType);
		return getBaseFinanceDBInstance(itemType).getTotalAmountDay(calendar);
	}
	
	public ArrayList<FinanceItem> getFinanceAllItems(int itemType) {
		Log.i(LogTag.DB, "== DBConnector getFinanceAllItems type : " + itemType);
		return getBaseFinanceDBInstance(itemType).getAllItems();
	}
	
	public long addCategory(int itemType, String name) {
		Log.i(LogTag.DB, "== DBConnector addCategory type : " + itemType);
		return getBaseFinanceDBInstance(itemType).addCategory(name);
	}
	
	public long addSubCategory(int itemType, long mainCategoryID, String name) {
		Log.i(LogTag.DB, "== DBConnector addSubCategory type : " + itemType);
		return getBaseFinanceDBInstance(itemType).addSubCategory(mainCategoryID, name);
	}
	
	public ArrayList<Category> getCategory(int itemType) {
		Log.i(LogTag.DB, "== DBConnector getCategory type : " + itemType);
		return getBaseFinanceDBInstance(itemType).getCategory();
	}

	public ArrayList<Category> getSubCategory(int itemType, long mainCategoryId) {
		Log.i(LogTag.DB, "== DBConnector getSubCategory type : " + itemType);
		return getBaseFinanceDBInstance(itemType).getSubCategory(mainCategoryId);
	}
	
	public int getItemCount(int itemType, Calendar calendar) {
		Log.i(LogTag.DB, "== DBConnector getItemCount type : " + itemType);
		return getBaseFinanceDBInstance(itemType).getItemCount(calendar);
	}

	public ArrayList<FinanceItem> getItems(int itemType, Calendar calendar) {
		Log.i(LogTag.DB, "== DBConnector getItems type : " + itemType);
		return getBaseFinanceDBInstance(itemType).getItems(calendar);
	}

	public int deleteItem(int itemType, int id) {
		Log.i(LogTag.DB, "== DBConnector deleteItem ");
		int result = getBaseFinanceDBInstance(itemType).deleteItem(id);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not delete id : " + id + " type : " + itemType); 
		}
		return result;
	}

	public int deleteCategory(int itemType, int id) {
		Log.i(LogTag.DB, "== DBConnector deleteCategory ");
		int result = getBaseFinanceDBInstance(itemType).deleteCategory(id);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not delete id : " + id + " type : " + itemType); 
		}
		return result;
	}

	public int deleteSubCategory(int itemType, int id) {
		Log.i(LogTag.DB, "== DBConnector deleteSubCategory ");
		int result = getBaseFinanceDBInstance(itemType).deleteSubCategory(id);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not delete id : " + id + " type : " + itemType); 
		}
		return result;
	}

	public int updateCategory(int itemType, int id, String name) {
		Log.i(LogTag.DB, "== DBConnector updateCategory ");
		int result = getBaseFinanceDBInstance(itemType).updateCategory(id, name);
		if (result == 0) {
			Log.e(LogTag.DB, "== do not update id : " + id + " type : " + itemType); 
		}
		return result;
	}
	
	public int updateSubCategory(int itemType, int id, String name) {
		Log.i(LogTag.DB, "== DBConnector updateSubCategory ");
		int result = getBaseFinanceDBInstance(itemType).updateSubCategory(id, name);
		if (result == 0) {
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

	
*/
}


