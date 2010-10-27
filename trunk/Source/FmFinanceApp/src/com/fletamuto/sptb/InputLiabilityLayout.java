package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class InputLiabilityLayout extends InputFinanceItemBaseLayout {
	private LiabilityItem mLiabilityItem;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_liability, true);
        
        updateChildView();
        setDateBtnClickListener(R.id.BtnLiabilityDate); 
        setAmountBtnClickListener(R.id.BtnLiabilityAmount);
        setSaveBtnClickListener(R.id.BtnLiabilitySave);
        setCategoryClickListener(R.id.BtnLiabilityCategory);
        setTitleButtonListener();
        setTitle(getResources().getString(R.string.input_liability_name));
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnLiabilityDate);
    } 
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem(ReportLiabilityLayout.class);
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
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
		mLiabilityItem = new LiabilityItem();
		setItem(mLiabilityItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mLiabilityItem = (LiabilityItem) DBMgr.getItem(LiabilityItem.TYPE, id);
		if (mLiabilityItem == null) return false;
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
		mLiabilityItem.setCategory(id, name);
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
