package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputIncomeLayout extends InputFinanceItemBaseLayout {
	private IncomeItem mIncomeItem;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_income, true);
        
        updateChildView();
        setDateBtnClickListener(R.id.BtnIncomeDate); 
        setAmountBtnClickListener(R.id.BtnIncomeAmount);
        setSaveBtnClickListener(R.id.BtnIncomeSave);
        setCategoryClickListener(R.id.BtnIncomeCategory);
        
        setTitle(getResources().getString(R.string.input_income_name));
    }
  
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnIncomeDate);
    }
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem(ReportIncomeLayout.class);
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
    }
    
    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnIncomeAmount);
	}

	@Override
	protected void createItemInstance() {
		mIncomeItem = new IncomeItem();
		setItem(mIncomeItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mIncomeItem = (IncomeItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mIncomeItem == null) return false;
		setItem(mIncomeItem);
		return true;
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputIncomeLayout.this, SelectCategoryIncomeLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}
	
	@Override
	protected void updateCategory(int id, String name) {
		mIncomeItem.setCategory(id, name);
		updateBtnCategoryText(R.id.BtnIncomeCategory);
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnCategoryText(R.id.BtnIncomeCategory);
		updateBtnAmountText(R.id.BtnIncomeAmount);
		updateEditMemoText(R.id.ETIncomeMemo);
	}

	@Override
	protected void updateItem() {
    	String memo = ((TextView)findViewById(R.id.ETIncomeMemo)).getText().toString();
    	getItem().setMemo(memo);
	}
    
}
