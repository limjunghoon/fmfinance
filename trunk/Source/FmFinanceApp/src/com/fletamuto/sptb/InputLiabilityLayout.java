package com.fletamuto.sptb;

import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

import android.content.Intent;
import android.os.Bundle;

public class InputLiabilityLayout extends InputBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_liability);
        
        updateDate();
        SetDateBtnClickListener(R.id.BtnLiabilityDate); 
        SetAmountBtnClickListener(R.id.BtnLiabilityAmount);
        SetSaveBtnClickListener(R.id.BtnLiabilitySave);
        SetCategoryClickListener(R.id.BtnLiabilityCategory);
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnLiabilityDate);
    } 
    
    protected void saveData() {
    	if (DBMgr.getInstance().addFinanceItem(dataInfo) == true) {
    		
    	}
    	else {
    		
    	}
    	Intent intent = new Intent(InputLiabilityLayout.this, ReportLiabilityLayout.class);
		startActivity(intent);
    }

    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnLiabilityAmount);
	}

	@Override
	protected void createInfoDataInstance() {
		dataInfo = new LiabilityItem();
	}
	
	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputLiabilityLayout.this, CategoryLiabilityLayout.class);
		startActivity(intent);
	}
}
