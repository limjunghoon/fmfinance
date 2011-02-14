package com.fletamuto.sptb;


import android.os.Bundle;

import com.fletamuto.sptb.data.IncomeItem;

public class SelectCategoryIncomeLayout extends SelectCategoryBaseLayout /*InputAfterSelectCategoryLayout */{
	public static final int ACT_ADD_INCOME = MsgDef.ActRequest.ACT_ADD_INCOME;
	
	public SelectCategoryIncomeLayout() {
		setType(IncomeItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

/*
    @Override
	protected void setTitleBtn() {
		 setTitleBtnText(FmTitleLayout.BTN_LEFT_01, "¡ˆ√‚");
		 setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.VISIBLE);   
		 
		super.setTitleBtn();
	}
	
    @Override
    protected void onClickLeft1TitleBtn() {
    	Intent intent = new Intent(SelectCategoryIncomeLayout.this, InputExpenseLayout.class);
		startActivity(intent);
		
    	super.onClickLeft1TitleBtn();
    }
*/
   /*
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
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	*/
}
