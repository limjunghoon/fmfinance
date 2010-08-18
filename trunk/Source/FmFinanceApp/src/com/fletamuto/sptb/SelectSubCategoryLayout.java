package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.db.DBMgr;

public class SelectSubCategoryLayout extends SelectCategoryBaseLayout {
	private long mMainCagegoryID = -1;
	private int mType = -1;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainCagegoryID = getIntent().getLongExtra("MAIN_CATEGORY_ID", -1) ;
        mType = getIntent().getIntExtra("ITEM_TYPE", -1) ;
        
        getCategoryList();
    }
    
    protected void getCategoryList() {
		if (mType == -1 || mMainCagegoryID == -1) {
			Log.e(LogTag.LAYOUT, "== invaild category type");
			return;
		}
		mArrCategory = DBMgr.getInstance().getSubCategory(mType, mMainCagegoryID);
		setCategoryAdaper();
        
	}
  
    protected void onClickCategoryButton(Category category) {
		Intent intent = new Intent();
		intent.putExtra("SUB_CATEGORY_ID", category.getId());
		intent.putExtra("SUB_CATEGORY_NAME", category.getName());
		setResult(RESULT_OK, intent);
		finish();
	}

    View.OnClickListener categoryListener = new View.OnClickListener() {
		public void onClick(View v) {
			Category category= (Category)v.getTag();
			onClickCategoryButton(category);
		}
	};
    
 
}
