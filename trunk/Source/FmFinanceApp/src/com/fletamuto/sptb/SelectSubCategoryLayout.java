package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;

public class SelectSubCategoryLayout extends SelectCategoryBaseLayout {
	private int mMainCagegoryID = -1;
	private String mMainCagegoryName;
	private int mType = -1;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainCagegoryID = getIntent().getIntExtra("MAIN_CATEGORY_ID", -1) ;
        mMainCagegoryName = getIntent().getStringExtra("MAIN_CATEGORY_NAME") ;
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
    
    public void setEditButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(SelectSubCategoryLayout.this, EditCategoryLayout.class);
				intent.putExtra("CATEGORY_TYPE", mType);
				intent.putExtra("CATEGORY_HAS_MAIN", true);
				intent.putExtra("CATEGORY_MAIN_CATEGORY_ID", mMainCagegoryID);
				intent.putExtra("CATEGORY_MAIN_CATEGORY_NAME", mMainCagegoryName);
				
				startActivityForResult(intent, ACT_EDIT_CATEGORY);
			}
		});
	}
    
  
    protected void onClickCategoryButton(Category category) {
		Intent intent = new Intent();
		intent.putExtra("SUB_CATEGORY_ID", category.getId());
		intent.putExtra("SUB_CATEGORY_NAME", category.getName());
		
		Log.i(LogTag.LAYOUT, ":: Selected sub category ID : " + category.getId());
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
