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
        
        updateChildView();
        SetDateBtnClickListener(R.id.BtnLiabilityDate); 
        SetAmountBtnClickListener(R.id.BtnLiabilityAmount);
        SetSaveBtnClickListener(R.id.BtnLiabilitySave);
        SetCategoryClickListener(R.id.BtnLiabilityCategory);
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnLiabilityDate);
    } 
    
    protected void saveItem() {
    	if (inputMode == InputMode.ADD_MODE) {
    		saveNewItem(ReportLiabilityLayout.class);
    	}
    	else if (inputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
    }

    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnLiabilityAmount);
	}

	@Override
	protected void createItemInstance() {
		item = new LiabilityItem();
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		item = DBMgr.getInstance().getItem(LiabilityItem.TYPE, id);
		if (item == null) return false;
		return true;
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

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnCategoryText(R.id.BtnLiabilityCategory);
		updateBtnAmountText(R.id.BtnLiabilityAmount);
		updateEditTitleText(R.id.ETLiabilityTitle);
	}

	@Override
	protected void updateItem() {
		String title = ((TextView)findViewById(R.id.ETLiabilityTitle)).getText().toString();
    	getItem().setTitle(title);
	}
}
