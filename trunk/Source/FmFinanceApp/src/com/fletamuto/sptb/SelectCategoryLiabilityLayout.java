package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.LiabilityItem;

public class SelectCategoryLiabilityLayout extends InputAfterSelectCategoryLayout {
	public static final int ACT_ADD_LIABLITY = MsgDef.ActRequest.ACT_ADD_LIABLITY;
	
	public SelectCategoryLiabilityLayout() {
		 setType(LiabilityItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
	protected void startInputActivity(Category category) {
    	Intent intent = new Intent(SelectCategoryLiabilityLayout.this, InputLiabilityLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
		startActivityForResult(intent, ACT_ADD_LIABLITY);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_LIABLITY) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	


}
