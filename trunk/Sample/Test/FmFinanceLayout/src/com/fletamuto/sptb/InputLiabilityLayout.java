package com.fletamuto.sptb;

import com.fletamuto.sptb.data.InfoLiability;
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
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnLiabilityDate);
    } 
    
    protected void saveData() {
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
		dataInfo = new InfoLiability();
	}
}
