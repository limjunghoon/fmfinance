package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.LiabilityCashServiceItem;

public class DetailLiabilityCashServiceLayout extends DetailBaseLayout {  	
	private LiabilityCashServiceItem mCashservice;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_liability_cash_service, true);
        
        setButtonListener();
        updateChildView();
    }

	public void setButtonListener() {
		findViewById(R.id.BtnStateDelete).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onRepayBtnClick();
			}
		});
	}
	
	public void onRepayBtnClick() {
		Intent intent = new Intent(this, InputLiabilityRepayCashServiceLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, mCashservice);
		startActivityForResult(intent, MsgDef.ActRequest.ACT_INPUT_PEPAY);
	}

	public void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.TVCashServiceCard);
		tvName.setText(mCashservice.getCard().getCompenyName().getName());
		TextView tvAmount = (TextView)findViewById(R.id.TVCashServiceAmount);
		tvAmount.setText(String.format("%,d원", mCashservice.getAmount()));
		TextView tvCreateDate = (TextView)findViewById(R.id.TVCashServiceCreateDate);
		tvCreateDate.setText(mCashservice.getCreateDateString());
		TextView tvMemo = (TextView)findViewById(R.id.TVCashServiceMemo);
		tvMemo.setText(mCashservice.getMemo());
	}
	
	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("현금서비스");
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mCashservice = (LiabilityCashServiceItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
	}

	@Override
	public void onEditBtnClick() {
		Intent intent = new Intent(this, InputLiabilityCashServiceLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mCashservice.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_INPUT_PEPAY) {
			if (resultCode == RESULT_OK) {
				updateChildView();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}