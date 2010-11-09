package com.fletamuto.sptb;

import com.fletamuto.sptb.InputBaseLayout.InputMode;

import android.os.Bundle;

public abstract class InputIncomeExtendLayout extends InputFinanceItemBaseLayout {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    
	}
	
	@Override
	protected void setTitleBtn() {
		 setTitle(getItem().getCategory().getName());
		 
		super.setTitleBtn();
	}
	
	protected void initialize() {
    	super.initialize();
    	int categoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1) ;
        String categoryName = getIntent().getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME);
        updateCategory(categoryID, categoryName);
	}
	
	@Override
	protected void updateCategory(int id, String name) {
		getItem().setCategory(id, name);
	}
	
	@Override
	protected void onCategoryClick() {
	}
	
	@Override
	protected void saveItem() {
		if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem(ReportIncomeLayout.class);
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
	}
	
}
