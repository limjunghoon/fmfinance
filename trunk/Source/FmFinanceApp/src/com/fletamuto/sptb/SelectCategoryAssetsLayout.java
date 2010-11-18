package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;

public class SelectCategoryAssetsLayout extends InputAfterSelectCategoryLayout {
	public static final int ACT_ADD_ASSETS = MsgDef.ActRequest.ACT_ADD_ASSETS;
	
	public SelectCategoryAssetsLayout() {
		 setType(AssetsItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
	@Override
	protected void startInputActivity(Category category) {
    	Intent intent = new Intent(SelectCategoryAssetsLayout.this, InputAssetsLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
		startActivityForResult(intent, ACT_ADD_ASSETS);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_ASSETS) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
//				Intent intent = new Intent(SelectCategoryAssetsLayout.this, ReportIncomeLayout.class);
//				startActivity(intent);
				
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
