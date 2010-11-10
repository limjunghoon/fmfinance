package com.fletamuto.sptb;

import android.content.Intent;
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
    		if (saveNewItem(null) == true) {
    			Intent intent = new Intent();
    			setResult(RESULT_OK, intent);
    			finish();
    		}
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
	}

	
}
