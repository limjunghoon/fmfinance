package com.fletamuto.sptb;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.db.DBMgr;

import android.content.Intent;
import android.os.Bundle;

public class InputAssetsLayout extends InputBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_assets);
        
        updateDate();
        SetDateBtnClickListener(R.id.BtnAssetsDate); 
        SetAmountBtnClickListener(R.id.BtnAssetsAmount);
        SetSaveBtnClickListener(R.id.BtnAssetsSave);
        SetCategoryClickListener(R.id.BtnAssetsCategory);
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnAssetsDate);
    } 
    
    protected void saveData() {
    	if (DBMgr.getInstance().addFinanceItem(dataInfo) == true) {
    		
    	}
    	else {
    		
    	}
    	
    	Intent intent = new Intent(InputAssetsLayout.this, ReportAssetsLayout.class);
		startActivity(intent);
    }

    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnAssetsAmount);
	}
    
	@Override
	protected void createInfoDataInstance() {
		dataInfo = new AssetsItem();
	}

	@Override
	protected void onCategoryClick() {
    	Intent intent = new Intent(InputAssetsLayout.this, CategoryAssetsLayout.class);
		startActivity(intent);
	}
}
