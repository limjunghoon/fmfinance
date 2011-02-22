package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;



public abstract class InputLiabilityBaseLayout extends InputFinanceItemBaseLayout {
	 protected void setSaveBtnClickListener(int btnID) {
    	findViewById(btnID).setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				updateItem();
				
				if (checkInputData() == true) {
					IncomeItem income = createIncomeItem();
					if (income == null) {
						Log.e(LogTag.LAYOUT, "Fail to the Create Expense Item");
						saveItem();		
						finish();
					}
					Intent intent = new Intent(InputLiabilityBaseLayout.this, InputIncomeLayout.class);
					intent.putExtra(MsgDef.ExtraNames.ITEM, income);
					startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ITEM);
		    	}
			}
		 });
    }
	 
	 protected IncomeItem createIncomeItem() {
		IncomeItem income = new IncomeItem();
			
		ArrayList<Category> categories = DBMgr.getCategory(IncomeItem.TYPE, ItemDef.ExtendLiablility.NONE);
		if (categories.size() == 0) return null;
		Category mainCategory = categories.get(0);
		if (mainCategory == null) return null;
		
		income.setCategory(mainCategory);
		income.setAmount(getItem().getTotalAmount());
		income.setCreateDate(getItem().getCreateDate());
		
		return income;
	}
	 
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {

			if (requestCode == MsgDef.ActRequest.ACT_ADD_ITEM) {
				if (resultCode == RESULT_OK) {
					saveItem();	
//					DBMgr.addExpenseFromAssets(data.getIntExtra(MsgDef.ExtraNames.ADD_ITEM_ID, -1), getItem().getID());
					
					Intent intent = new Intent();
					intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, getItem().getID());
					setResult(RESULT_OK, intent);
					finish();
				}
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
	 
	 @Override
	protected void updateAmount(Long amount) {
		((LiabilityItem)getItem()).setOrignAmount(amount);
		super.updateAmount(amount);
	}
}
