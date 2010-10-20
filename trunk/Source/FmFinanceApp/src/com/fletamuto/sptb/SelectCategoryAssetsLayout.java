package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;

public class SelectCategoryAssetsLayout extends SelectCategoryBaseLayout {
	private Category mMainCategory;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setType(AssetsItem.TYPE);
        getCategoryList();
        setCategoryAdaper();
    }
    
    /*
    protected void onClickCategoryButton(Category category) {
    	mMainCategory = category;
    	Intent intent = new Intent(SelectCategoryAssetsLayout.this, SelectSubCategoryLayout.class);
    	intent.putExtra("MAIN_CATEGORY_ID", category.getId());
    	intent.putExtra("MAIN_CATEGORY_NAME", category.getName());
    	intent.putExtra("ITEM_TYPE", AssetsItem.TYPE);
    	startActivityForResult(intent, ACT_SUB_CATEGORY);
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_SUB_CATEGORY) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent();
				
				intent.putExtra("CATEGORY_ID", mMainCategory.getId());
				intent.putExtra("CATEGORY_NAME", mMainCategory.getName());
				intent.putExtra("SUB_CATEGORY_ID", data.getIntExtra("SUB_CATEGORY_ID", -1));
				intent.putExtra("SUB_CATEGORY_NAME", data.getStringExtra("SUB_CATEGORY_NAME"));
				
				setResult(RESULT_OK, intent);
    			finish();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	*/
}
