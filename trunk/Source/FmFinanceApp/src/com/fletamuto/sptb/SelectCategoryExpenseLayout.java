package com.fletamuto.sptb;



import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;

public class SelectCategoryExpenseLayout extends SelectCategoryBaseLayout {
	protected final static int ACT_SUB_CATEGORY = 1;
	private Category mMainCategory;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getCategoryList();
    }
    
    protected void getCategoryList() {
    	getCategoryList(ExpenseItem.TYPE);
    }
    
    protected void onClickCategoryButton(CategoryButton btn) {
    	mMainCategory = new Category(btn.getCategoryID(), btn.getCategoryName());
    	Intent intent = new Intent(SelectCategoryExpenseLayout.this, SelectSubCategoryLayout.class);
    	intent.putExtra("MAIN_CATEGORY_ID", btn.getCategoryID());
    	intent.putExtra("ITEM_TYPE", ExpenseItem.TYPE);
    	startActivityForResult(intent, ACT_SUB_CATEGORY);
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_SUB_CATEGORY) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent();
				
				intent.putExtra("CATEGORY_ID", mMainCategory.getId());
				intent.putExtra("CATEGORY_NAME", mMainCategory.getName());
				intent.putExtra("SUB_CATEGORY_ID", data.getIntExtra("SUB_CATEGORY_ID", 0));
				intent.putExtra("SUB_CATEGORY_NAME", data.getStringExtra("SUB_CATEGORY_NAME"));
				setResult(RESULT_OK, intent);
    			finish();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
