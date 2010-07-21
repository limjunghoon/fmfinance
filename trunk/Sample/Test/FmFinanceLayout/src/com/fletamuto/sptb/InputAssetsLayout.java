package com.fletamuto.sptb;

import com.fletamuto.sptb.data.InfoAssets;

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
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnAssetsDate);
    } 
    
    protected void saveData() {
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
		dataInfo = new InfoAssets();
	}
}
