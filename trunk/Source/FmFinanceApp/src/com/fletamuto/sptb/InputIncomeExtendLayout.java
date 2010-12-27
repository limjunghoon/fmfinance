package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.Repeat;

public abstract class InputIncomeExtendLayout extends InputFinanceItemBaseLayout {
	
	protected abstract void updateRepeat(int type, int value);
	
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
    	
    	if (mInputMode == InputMode.ADD_MODE) {
    		int categoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1) ;
            String categoryName = getIntent().getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME);
            updateCategory(categoryID, categoryName);
    	}
    	
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
			if (mInputMode == InputMode.ADD_MODE) {
	    		if (saveNewItem(null) == true) {
	    			saveRepeat();
	    		}
	    		
	    		Intent intent = new Intent();
    			setResult(RESULT_OK, intent);
    			finish();
	    	}
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_REPEAT) {
			if (resultCode == RESULT_OK) {
				int repeatType = data.getIntExtra(MsgDef.ExtraNames.RPEAT_TYPE, -1);
				
				if (repeatType == Repeat.MONTHLY) {
					int daily = data.getIntExtra(MsgDef.ExtraNames.RPEAT_DAILY, -1);
					if (daily == -1) return;
					
					updateRepeat(Repeat.MONTHLY, daily);
				}
				else if (repeatType == Repeat.WEEKLY) {
					int weekly = data.getIntExtra(MsgDef.ExtraNames.RPEAT_WEEKLY, -1);
					if (weekly == -1) return;
					
					updateRepeat(Repeat.WEEKLY, weekly);
				}
				else {
					return;
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
