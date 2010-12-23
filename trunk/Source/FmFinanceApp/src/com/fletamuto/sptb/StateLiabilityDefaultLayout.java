package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.db.DBMgr;

public class StateLiabilityDefaultLayout extends StateDefaultLayout {  	
	long mPurchasePrice = 0L;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ((Button)findViewById(R.id.BtnStateDelete)).setText("상환");
    }
	
	@Override
	protected void initialize() {
		super.initialize();
		
		getData();
	}
	
	@Override
	protected void updateChildView() {
		TextView tvAmount = (TextView)findViewById(R.id.TVStateTitle);
		tvAmount.setText(String.format("금액 : %,d원", mItem.getAmount()));
	}

	@Override
	protected void onClickHistoryBtn() {
		Intent intent = new Intent(StateLiabilityDefaultLayout.this, ReportLiabilityHistoryLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, mItem);
		startActivityForResult(intent, MsgDef.ActRequest.ACT_STATE_HISTORY);
	}

	@Override
	protected void startChangeStateActivtiy() {
		Intent intent = new Intent(this, InputLiabilityLayout.class);
		intent.putExtra(MsgDef.ExtraNames.INPUT_CHANGE_MODE, true);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mItem.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_CHANGE_STATE);
	}

	@Override
	protected void getData() {
		mAmountMonthInYear = DBMgr.getTotalLiabilityAmountMonthInYear(mItem.getID(), mYear);
		mPurchasePrice = DBMgr.getLiabilityPurchasePrice(mItem.getID());
	}
	
	
}