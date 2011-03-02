package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsRealEstateItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsRealEstateLayout extends InputAssetsExtendLayout {
	private AssetsRealEstateItem mRealEstate;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_real_estate, true);
    	
    	setBtnClickListener();
    	updateChildView();
    }
    
    @Override
	protected void setBtnClickListener() {
    	setCreateDateBtnClickListener(R.id.BtnAssetsDate); 
        setAmountBtnClickListener(R.id.BtnAssetsAmount);
	}

	private void setCreateDateBtnClickListener(int resource) {
		setDateBtnClickListener(resource);
	}

	@Override
	protected void updateRepeat(int type, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateDate() {
		updateBtnDateText(R.id.BtnAssetsDate);
	}
	


	@Override
	protected void createItemInstance() {
		mRealEstate = new AssetsRealEstateItem();
		setItem(mRealEstate);
	}

	@Override
	protected boolean getItemInstance(int id) {
		mRealEstate = (AssetsRealEstateItem) DBMgr.getItem(AssetsItem.TYPE, id);
		if (mRealEstate == null) return false;
		setItem(mRealEstate);
		return true;
	}

	@Override
	protected void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.ETAssetsTitle);
		tvName.setText(mRealEstate.getTitle());
		TextView tvScale = (TextView)findViewById(R.id.ETAssetsScale);
		tvScale.setText(mRealEstate.getScale());
		
		updateDate();
		updateBtnAmountText(R.id.BtnAssetsAmount);
		updateEditMemoText(R.id.ETAssetsMemo);
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETAssetsMemo)).getText().toString();
		mRealEstate.setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETAssetsTitle)).getText().toString();
    	mRealEstate.setTitle(title);
    	
    	String scale = ((TextView)findViewById(R.id.ETAssetsScale)).getText().toString();
    	mRealEstate.setScale(scale);
    	
	}
	
    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnAssetsAmount);
		
	}
    
	@Override
	protected boolean saveNewItem(Class<?> cls) {
		if (DBMgr.addExtendAssetsRealEstate(mRealEstate) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mRealEstate.getID());
    		return false;
    	}
    	
    	if (cls != null) {
    		Intent intent = new Intent(InputAssetsRealEstateLayout.this, cls);
    		startActivity(intent);
    	}
    	else {
    		Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
    	}
    	
		return true;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
		//좀 있다 요청코드 만들기
    	if (requestCode == 37) {
    		if (resultCode == RESULT_OK) {
    			
    			int[] values = data.getIntArrayExtra("SELECTED_DATE");

    			mRealEstate.getCreateDate().set(Calendar.YEAR, values[0]);
    			mRealEstate.getCreateDate().set(Calendar.MONTH, values[1]);
    			mRealEstate.getCreateDate().set(Calendar.DAY_OF_MONTH, values[2]);
				
    			updateDate();
    		}
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
	
	@Override
	String getExpenseTitle() {
		return "구입 금액";
	}
  
}
