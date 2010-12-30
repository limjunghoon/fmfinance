package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public abstract class InputAssetsExtendLayout extends InputAssetsBaseLayout {
	
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
    	else if (mInputMode == InputMode.STATE_CHANGE_MODE){
    		saveUpdateStateItem();
    	}
	}
	
    protected void saveUpdateStateItem() {
    	if (DBMgr.addStateChangeItem(getItem()) == 0) {
    		Log.e(LogTag.LAYOUT, "== UpdateState fail to the save item : " + getItem().getID());
    		return;
    	}
		
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
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
