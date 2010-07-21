package com.fletamuto.sptb;

import com.fletamuto.sptb.data.InfoExpense;
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
        SetCategoryClickListener(R.id.BtnExpenseCategory);
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnExpenseDate);
    } 
    
    protected void saveData() {
    	
    	Intent intent = new Intent(InputExpenseLayout.this, ReportExpenseLayout.class);
		startActivity(intent);
    }

    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnExpenseAmount);
	}

	@Override
	protected void createInfoDataInstance() {
		dataInfo = new InfoExpense();
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputExpenseLayout.this, CategoryExpenseLayout.class);
		startActivity(intent);
	}
}
