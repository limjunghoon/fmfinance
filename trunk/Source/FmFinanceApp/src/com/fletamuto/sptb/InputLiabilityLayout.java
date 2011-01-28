package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class InputLiabilityLayout extends InputFinanceItemBaseLayout {
	private LiabilityItem mLiabilityItem;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_liability, true);
        
        updateChildView();
        
        //달력을 이용한 날짜 입력을 위해
/*
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputLiability);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
*/
        
        
    }
    
    @Override
	protected void setBtnClickListener() {
    	setDateBtnClickListener(R.id.BtnLiabilityDate); 
        setAmountBtnClickListener(R.id.BtnLiabilityAmount);
        setTitle(mLiabilityItem.getCategory().getName());
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
    	if (DBMgr.addStateChangeItem(mLiabilityItem) == 0) {
    		Log.e(LogTag.LAYOUT, "== UpdateState fail to the save item : " + mLiabilityItem.getID());
    		return;
    	}
		
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
		// TODO Auto-generated method stub
		mLiabilityItem.setCategory(id, name);
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnAmountText(R.id.BtnLiabilityAmount);
		updateEditTitleText(R.id.ETLiabilityTitle);
	}

	@Override
	protected void updateItem() {
		String title = ((TextView)findViewById(R.id.ETLiabilityTitle)).getText().toString();
    	getItem().setTitle(title);
	}
}
