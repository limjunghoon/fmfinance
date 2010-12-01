package com.fletamuto.sptb;



import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;

public class SelectCategoryExpenseLayout extends SelectCategoryBaseLayout {
	private static boolean mSelectSubCategory = true;
	
	private Category mMainCategory;
	
	public SelectCategoryExpenseLayout() {
		setType(ExpenseItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        mSelectSubCategory = getIntent().getBooleanExtra(MsgDef.ExtraNames.SELECT_SUB_CATEGORY_IN_MAIN_CATEGORY, true);
    }
    
    
    protected void onClickCategoryButton(Category category) {
    	mMainCategory = category;
    	if (mSelectSubCategory) {
    		Intent intent = new Intent(SelectCategoryExpenseLayout.this, SelectSubCategoryLayout.class);
        	intent.putExtra("MAIN_CATEGORY_ID", category.getID());
        	intent.putExtra("MAIN_CATEGORY_NAME", category.getName());
        	intent.putExtra("ITEM_TYPE", ExpenseItem.TYPE);
        	startActivityForResult(intent, ACT_SUB_CATEGORY);
    	}
    	else {
    		Intent intent = new Intent();
			intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
			intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
			setResult(RESULT_OK, intent);
			finish();
    	}
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_SUB_CATEGORY) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent();
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
				intent.putExtra("SUB_CATEGORY_ID", data.getIntExtra("SUB_CATEGORY_ID", -1));
				intent.putExtra("SUB_CATEGORY_NAME", data.getStringExtra("SUB_CATEGORY_NAME"));
				setResult(RESULT_OK, intent);
    			finish();
    			return;
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
