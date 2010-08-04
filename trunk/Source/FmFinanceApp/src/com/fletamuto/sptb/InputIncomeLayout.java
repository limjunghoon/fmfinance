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
        
        updateChildView();
        SetDateBtnClickListener(R.id.BtnIncomeDate); 
        SetAmountBtnClickListener(R.id.BtnIncomeAmount);
        SetSaveBtnClickListener(R.id.BtnIncomeSave);
        SetCategoryClickListener(R.id.BtnIncomeCategory);
    }
  
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnIncomeDate);
    }
    
    protected void saveData() {
    	if (DBMgr.getInstance().addFinanceItem(item) == false) {
    		return;
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
		item.setCategory(new Category(id, name));
		updateBtnCategoryText(R.id.BtnIncomeCategory);
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnCategoryText(R.id.BtnIncomeCategory);
		updateBtnAmountText(R.id.BtnIncomeAmount);
	}

	@Override
	protected void updateData() {
    	String memo = ((TextView)findViewById(R.id.ETIncomeMemo)).getText().toString();
    	getItem().setMemo(memo);
	}
    
}
