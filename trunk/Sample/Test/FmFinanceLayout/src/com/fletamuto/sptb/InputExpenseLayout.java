package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;


public class InputExpenseLayout extends InputBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_expense);
        
        updateDate();
        SetDateBtnClickListener(R.id.BtnExpenseDate);
        SetAmountBtnClickListener(R.id.BtnExpenseAmount);
        SetSaveBtnClickListener(R.id.BtnExpenseSave);
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnExpenseDate);
    } 
    
    protected void saveData() {
    	
    	Intent intent = new Intent(InputExpenseLayout.this, ReportExpenseLayout.class);
		startActivity(intent);
    }
    
    
}
