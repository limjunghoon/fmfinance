package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsLayout extends InputAssetsBaseLayout {
	private AssetsItem mAssetsItem;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_assets, true);
        
        updateChildView();
        
      //달력을 이용한 날짜 입력을 위해
/*
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputAssets);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
*/
        
        
    }
    
    @Override
	protected void setBtnClickListener() {
    	setDateBtnClickListener(R.id.BtnAssetsDate); 
        setAmountBtnClickListener(R.id.BtnAssetsAmount);
        setTitle(mAssetsItem.getCategory().getName());
	}
    
    @Override
    protected void initialize() {
    	super.initialize();
    	
    	if (mInputMode == InputMode.ADD_MODE) {
    		int categoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1) ;
            String categoryName = getIntent().getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME);
            updateCategory(categoryID, categoryName);
    	}
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnAssetsDate);
    } 
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem(null);
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
    	else if (mInputMode == InputMode.STATE_CHANGE_MODE){
    		saveUpdateStateItem();
    	}
    }

    protected void saveUpdateStateItem() {
    	if (DBMgr.addStateChangeItem(mAssetsItem) == 0) {
    		Log.e(LogTag.LAYOUT, "== UpdateState fail to the save item : " + mAssetsItem.getID());
    		return;
    	}
		
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnAssetsAmount);
	}
    
	@Override
	protected void createItemInstance() {
		mAssetsItem = new AssetsItem();
		setItem(mAssetsItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mAssetsItem = (AssetsItem) DBMgr.getItem(AssetsItem.TYPE, id);
		if (InputMode.STATE_CHANGE_MODE == mInputMode) {
			mAssetsItem.setCreateDate(Calendar.getInstance());
		}
		if (mAssetsItem == null) return false;
		setItem(mAssetsItem);
		
		return true;
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputAssetsLayout.this, SelectCategoryAssetsLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}

	@Override
	protected void updateCategory(int id, String name) {
		mAssetsItem.setCategory(id, name);
	}
	
	protected void updateBtnCategoryText(int btnID) {
		String categoryText = getResources().getString(R.string.input_select_category);
		if (mAssetsItem.isVaildCatetory()) {
			// categoryText = String.format("%s - %s", assetsItem.getCategory().getName(), assetsItem.getSubCategory().getName());
			categoryText = String.format("%s", mAssetsItem.getCategory().getName());
		}
		((Button)findViewById(btnID)).setText(categoryText);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_CATEGORY) {
    		if (resultCode == RESULT_OK) {
    			mAssetsItem.setSubCategory(data.getIntExtra("SUB_CATEGORY_ID", 0), data.getStringExtra("SUB_CATEGORY_NAME"));
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnAmountText(R.id.BtnAssetsAmount);
		updateEditTitleText(R.id.ETAssetsTitle);
	}

	@Override
	protected void updateItem() {
		String title = ((TextView)findViewById(R.id.ETAssetsTitle)).getText().toString();
    	getItem().setTitle(title);
    	
    	String memo = ((TextView)findViewById(R.id.ETAssetsMemo)).getText().toString();
    	getItem().setMemo(memo);
	}
}
