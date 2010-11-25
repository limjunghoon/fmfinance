package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ItemDef;

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
		Intent intent = null;
		if (category.getExtndType() == ItemDef.ExtendAssets.DEPOSIT) {
			intent = new Intent(SelectCategoryAssetsLayout.this, InputAssetsDepositLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.SAVINGS) {
			intent = new Intent(SelectCategoryAssetsLayout.this, InputAssetsSavingsLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.STOCK) {
			intent = new Intent(SelectCategoryAssetsLayout.this, InputAssetsStockLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.FUND) {
			intent = new Intent(SelectCategoryAssetsLayout.this, InputAssetsFundLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.ENDOWMENT_MORTGAGE) {
			intent = new Intent(SelectCategoryAssetsLayout.this, InputAssetsInsuranceLayout.class);
		}
		else {
			intent = new Intent(SelectCategoryAssetsLayout.this, InputAssetsLayout.class);
		}
    	
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
