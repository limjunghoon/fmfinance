package com.fletamuto.sptb;


import com.fletamuto.sptb.data.IncomeInfo;
import com.fletamuto.sptb.db.DBMgr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class InputIncomeLayout extends InputBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_income);
        
        updateDate();
        SetDateBtnClickListener(R.id.BtnIncomeDate); 
        SetAmountBtnClickListener(R.id.BtnIncomeAmount);
        SetSaveBtnClickListener(R.id.BtnIncomeSave);
        SetCategoryClickListener(R.id.BtnIncomeCategory);
    }
  
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnIncomeDate);
    }
    
    protected void saveData() {
    	String memo = ((TextView)findViewById(R.id.ETIncomeMemo)).getText().toString();
    	getData().setMemo(memo);
    	
    	if (DBMgr.getInstance().addIncomeInfo((IncomeInfo)dataInfo) == true) {
    		
    	}
    	else {
    		
    	}
    	
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
		dataInfo = new IncomeInfo();
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputIncomeLayout.this, CategoryIncomeLayout.class);
		startActivity(intent);
	}
    
}
