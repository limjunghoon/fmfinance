package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

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
    	getItem().setMemo(memo);
    	
    	if (DBMgr.getInstance().addFinanceItem(item) == true) {
    		
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
		item = new IncomeItem();
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputIncomeLayout.this, SelectCategoryIncomeLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}
	
	@Override
	protected void updateCategory(int id, String name) {
		// TODO Auto-generated method stub
		item.setCategory(new Category(id, name));
		updateBtnCategoryText(R.id.BtnIncomeCategory);
	}
    
}
