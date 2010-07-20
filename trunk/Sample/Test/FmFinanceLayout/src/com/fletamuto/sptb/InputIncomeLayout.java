package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;

public class InputIncomeLayout extends InputBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_income);
        
        updateDate();
        SetDateBtnClickListener(R.id.BtnIncomeDate); 
        SetAmountBtnClickListener(R.id.BtnIncomeAmount);
        SetSaveBtnClickListener(R.id.BtnIncomeSave);
    }
  
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnIncomeDate);
    }
    
    protected void saveData() {
    	Intent intent = new Intent(InputIncomeLayout.this, ReportIncomeLayout.class);
		startActivity(intent);
    }
    
}
