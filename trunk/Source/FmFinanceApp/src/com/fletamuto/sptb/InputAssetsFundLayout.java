package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsFundLayout extends InputExtendLayout {
	private AssetsFundItem mFund;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_fund, true);
    	
    	updateChildView();
        setDateBtnClickListener(R.id.BtnFundDate); 
        setAmountBtnClickListener(R.id.BtnFundPrice);
    }

	@Override
	protected void updateRepeat(int type, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateDate() {
		updateBtnDateText(R.id.BtnFundDate);
	}
	

	@Override
	protected void createItemInstance() {
		mFund = new AssetsFundItem();
		setItem(mFund);
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mFund == null) return false;
		setItem(mFund);
		return true;
	}

	@Override
	protected void updateChildView() {
		updateDate();
	//	updateBtnAmountText(R.id.BtnFundAmount);
		updateEditMemoText(R.id.ETFundMemo);
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETFundMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETFundTitle)).getText().toString();
    	getItem().setTitle(title);
    	
    	String store = ((TextView)findViewById(R.id.ETFundStore)).getText().toString();
    	mFund.setStore(store);
	}
	
    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnFundPrice);
	}
	
	@Override
	protected boolean saveNewItem(Class<?> cls) {
		if (DBMgr.addExtendAssetsFund(mFund) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mFund.getID());
    		return false;
    	}
    	
    	if (cls != null) {
    		Intent intent = new Intent(InputAssetsFundLayout.this, cls);
    		startActivity(intent);
    	}
    	else {
    		Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
    	}
    	
		return true;
	}
  
}
