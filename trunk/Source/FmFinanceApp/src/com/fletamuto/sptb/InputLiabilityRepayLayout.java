package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityChangeItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentCashMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.data.TransferItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public abstract class InputLiabilityRepayLayout extends InputBaseLayout {
	abstract LiabilityItem getLiabilityItem();
	
	/** 변경정보*/
	protected LiabilityChangeItem mChangeItem;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  
		
	}

	
	@Override
	protected void setTitleBtn() {
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		
		super.setTitleBtn();
	}

	@Override
	public boolean checkInputData() {
		if (mChangeItem.getPrincipal().getAmount() == 0L && mChangeItem.getInterest().getAmount() == 0L) {
			displayAlertMessage("원금과 이자 중 하나이상 입력이 되어야 합니다.");
			return false;
		}
		
		return true;
	}
	
	@Override
	protected void createItemInstance() {
		mChangeItem = new LiabilityChangeItem(getLiabilityItem());
		mChangeItem.setChangeDate(Calendar.getInstance());
	}

	@Override
	protected void saveItem() {
		if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem();
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
	}
	
	private void saveNewItem() {
		if (mChangeItem.getPrincipal().getAmount() > 0L) {
			if (DBMgr.addFinanceItem(createExpensePrincipalItem()) == -1) {
				Log.e(LogTag.LAYOUT, ":: Fail to create ExpensePrincipalItem item");
			}
		}
		
		if (mChangeItem.getInterest().getAmount() > 0L) {
			if (DBMgr.addFinanceItem(createExpenseInterestItem()) == -1) {
				Log.e(LogTag.LAYOUT, ":: Fail to create ExpenseInterestItem item");
			}
		}
		
		
		LiabilityItem liability = getLiabilityItem();
		
		if (liability.getAmount() <= mChangeItem.getPrincipal().getAmount()) {
			liability.setAmount(0L);
		}
		else {
			liability.setAmount(liability.getAmount() - mChangeItem.getPrincipal().getAmount());
		}
		DBMgr.updateFinanceItem(liability);
		
		mChangeItem.setAmount(liability.getAmount());
		if (DBMgr.addLiabilityChangeStateItem(mChangeItem) == -1) {
			Log.e(LogTag.LAYOUT, ":: Fail to addLiabilityChangeStateItem");
		}

		setResult(RESULT_OK);
		finish();
	}
	
	public ExpenseItem createExpensePrincipalItem() {
		createExpenseItem(mChangeItem.getPrincipal());
		mChangeItem.getPrincipal().setMemo(String.format("%s(원금)", getLiabilityItem().getTitle()));
		return mChangeItem.getPrincipal();
	}
	
	public ExpenseItem createExpenseInterestItem() {
		createExpenseItem(mChangeItem.getInterest());
		mChangeItem.getInterest().setMemo(String.format("%s(이자)", getLiabilityItem().getTitle()));
		return mChangeItem.getInterest();
	}
	
	public ExpenseItem createExpenseItem(ExpenseItem expense) {
		ArrayList<Category> categories = DBMgr.getCategory(ExpenseItem.TYPE, ItemDef.ExtendLiablility.NONE);
		LiabilityItem liability = getLiabilityItem();
		if (categories.size() != 1) return null;
		Category mainCategory = categories.get(0);
		if (mainCategory == null) return null;
		
		expense.setCategory(mainCategory);
		ArrayList<Category> subCategories = DBMgr.getSubCategory(ExpenseItem.TYPE, mainCategory.getID());
		int subCategorySize = subCategories.size();
		
		for (int index = 0; index < subCategorySize; index++) {
			Category subCategory = subCategories.get(index); 
			if (subCategory.getName().compareTo(liability.getCategory().getName()) == 0) {
				expense.setSubCategory(subCategory);
				break;
			}
		}
		
		expense.setCreateDate(mChangeItem.getChangeDate());
		return expense;
	}
	
	private void saveUpdateItem() {
//		if (DBMgr.updateTranser(mTrans) == false) {
//			Log.e(LogTag.LAYOUT, ":: Fail to update tranfer item");
//		}
//		long fromItemBalance = mTrans.getFromAccount().getBalance();
//		long toItemBalance = mTrans.getToAccount().getBalance();
//		
//		mTrans.getFromAccount().setBalance(fromItemBalance + mOrgAmount - mTrans.getAmount());
//		mTrans.getToAccount().setBalance(toItemBalance + mOrgAmount + mTrans.getAmount());
//		DBMgr.updateAccount(mTrans.getFromAccount());
//		DBMgr.updateAccount(mTrans.getToAccount());
		finish();
	}

	@Override
	protected void setBtnClickListener() {
		setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		findViewById(R.id.BtnRepayDate).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_SELECT_DATE);
			}
		});
		
		findViewById(R.id.BtnRepayPrincipal).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT_PRINCIPAL);
			}
		});
		
		findViewById(R.id.BtnRepayInterest).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT_INTEREST);
			}
		});
	}
}