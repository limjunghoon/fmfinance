package com.fletamuto.sptb;

import com.fletamuto.sptb.data.InfoAssets;
import com.fletamuto.sptb.data.InfoIncome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

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
    	
    	dataInfo.setCreateDate(getCreateDate());
    	String memo = ((EditText)findViewById(R.id.ETIncomeMemo)).getText().toString();
    	dataInfo.setMemo(memo);
    	
    	Intent intent = new Intent(InputIncomeLayout.this, ReportIncomeLayout.class);
		startActivity(intent);
    }
    
    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnIncomeAmount);
	}

	@Override
	protected void createInfoDataInstance() {
		dataInfo = new InfoIncome();
	}
    
}
