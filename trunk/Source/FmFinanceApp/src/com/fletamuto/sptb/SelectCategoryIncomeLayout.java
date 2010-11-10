package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;

public class SelectCategoryIncomeLayout extends InputAfterSelectCategoryLayout {
	public static final int ACT_ADD_INCOME = MsgDef.ActRequest.ACT_ADD_INCOME;
	
	public SelectCategoryIncomeLayout() {
		setType(IncomeItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	protected void startInputActivity(Category category) {
    	Intent intent = null;
    	
		if (category.getExtndType() == ItemDef.ExtendIncome.SALARY) {
			intent = new Intent(SelectCategoryIncomeLayout.this, InputIncomeSelaryLayout.class);
		}
		else {
			intent = new Intent(SelectCategoryIncomeLayout.this, InputIncomeLayout.class);
		}
		
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_INCOME);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_INCOME) {
			if (resultCode == RESULT_OK) {
				finish();
				Intent intent = new Intent(SelectCategoryIncomeLayout.this, ReportIncomeLayout.class);
				startActivity(intent);
				
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
