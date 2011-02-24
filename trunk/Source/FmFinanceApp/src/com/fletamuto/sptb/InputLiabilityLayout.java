package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class InputLiabilityLayout extends InputFinanceItemBaseLayout {
	private LiabilityItem mLiabilityItem;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_liability, true);
        
        updateChildView();
    }
    
    @Override
	protected void setBtnClickListener() {
    	setDateBtnClickListener(R.id.BtnLiabilityDate); 
    	setExpiryBtnClickListener(R.id.BtnLiabilityExpiryDate);
        setAmountBtnClickListener(R.id.BtnLiabilityAmount);
        setTitle(mLiabilityItem.getCategory().getName());
	}
    
    @Override
	protected void setTitleBtn() {
		setTitle("부채 등록");
		super.setTitleBtn();
	}
    
    @Override
    protected void initialize() {
    	super.initialize();
    	int categoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1) ;
        String categoryName = getIntent().getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME);
        
        updateCategory(categoryID, categoryName);
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnLiabilityDate);
    } 
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem(null);
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
    	else if (mInputMode == InputMode.STATE_CHANGE_MODE){
  //  		mAssetsItem.setCreateDate(Calendar.getInstance());
    		saveUpdateStateItem();
    	}
    }

    protected void saveUpdateStateItem() {
//    	if (DBMgr.addChangeStateItem(mLiabilityItem) == 0) {
//    		Log.e(LogTag.LAYOUT, "== UpdateState fail to the save item : " + mLiabilityItem.getID());
//    		return;
//    	}
		
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
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
		setItem(mLiabilityItem);
		return true;
	}
	
	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputLiabilityLayout.this, SelectCategoryLiabilityLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}

	@Override
	protected void updateCategory(int id, String name) {
		mLiabilityItem.setCategory(id, name);
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateExpiryDate();
		updateBtnAmountText(R.id.BtnLiabilityAmount);
		updateEditTitleText(R.id.ETLiabilityTitle);
	}

	@Override
	protected void updateItem() {
		String title = ((TextView)findViewById(R.id.ETLiabilityTitle)).getText().toString();
    	getItem().setTitle(title);
	}
	
	private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_SELECT_DATE_EXPIRY);
			}
		 });
	}
	 
	protected void updateExpiryDate() {
    	updateBtnExpiryDateText(R.id.BtnLiabilityExpiryDate);
    }
	 
	 protected void updateBtnExpiryDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mLiabilityItem.getExpiryDateString());
    }
}
