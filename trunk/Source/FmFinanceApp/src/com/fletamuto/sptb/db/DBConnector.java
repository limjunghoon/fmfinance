
package com.fletamuto.sptb.db;

import android.util.Log;

import com.fletamuto.sptb.util.LogTag;

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
	private final BudgetDBConnector mBudgetDBConnector = new BudgetDBConnector();
	private final TransferDBConnector mTransferDBConnector = new TransferDBConnector();
	private final SMSDBConnector mSMSDBConnector = new SMSDBConnector();
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
	
	public BudgetDBConnector getBudgetDBConnector() {
		return mBudgetDBConnector;
	}
	
	public TransferDBConnector getTransferDBConnector() {
		return mTransferDBConnector;
	}
	
	public SMSDBConnector getSMSDBConnector() {
		return mSMSDBConnector;
	}
}


