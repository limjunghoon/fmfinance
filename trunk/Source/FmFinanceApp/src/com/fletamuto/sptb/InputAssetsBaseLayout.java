package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public abstract class InputAssetsBaseLayout extends InputFinanceItemBaseLayout {
	
	 protected void setSaveBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				updateItem();
				
				if (checkInputData() == true) {
					ExpenseItem expense = createExpenseItem();
					if (expense == null) {
						Log.e(LogTag.LAYOUT, "Fail to the Create Expense Item");
						saveItem();		
						finish();
					}
					
					Intent intent = new Intent(InputAssetsBaseLayout.this, InputExpenseLayout.class);
					intent.putExtra(MsgDef.ExtraNames.ITEM, expense);
					
					startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ITEM);

		    	}
			}
		 });
    }

	protected ExpenseItem createExpenseItem() {
		ExpenseItem expense = new ExpenseItem();
		
		Category mainCategory = DBMgr.getCategory(ExpenseItem.TYPE, ItemDef.ExtendAssets.NONE);
		if (mainCategory == null) return null;
		
		expense.setCategory(mainCategory);
		ArrayList<Category> subCategories = DBMgr.getSubCategory(ExpenseItem.TYPE, mainCategory.getID());
		int subCategorySize = subCategories.size();
		
		for (int index = 0; index < subCategorySize; index++) {
			Category subCategory = subCategories.get(index); 
			if (subCategory.getName().compareTo(getItem().getCategory().getName()) == 0) {
				expense.setSubCategory(subCategory);
				break;
			}
		}
		
		expense.setAmount(getItem().getAmount());
		expense.setCreateDate(getItem().getCreateDate());
		
		return expense;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == MsgDef.ActRequest.ACT_ADD_ITEM) {
			if (resultCode == RESULT_OK) {
				saveItem();		
				finish();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
