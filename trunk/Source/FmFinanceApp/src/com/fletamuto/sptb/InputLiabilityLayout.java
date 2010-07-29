package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

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
    	String title = ((TextView)findViewById(R.id.ETLiabilityTitle)).getText().toString();
    	getItem().setTitle(title);
    	
    	if (DBMgr.getInstance().addFinanceItem(item) == true) {
    		
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
		item = new LiabilityItem();
	}
	
	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputLiabilityLayout.this, SelectCategoryLiabilityLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}

	@Override
	protected void updateCategory(int id, String name) {
		// TODO Auto-generated method stub
		item.setCategory(new Category(id, name));
		updateBtnCategoryText(R.id.BtnLiabilityCategory);
	}
}
