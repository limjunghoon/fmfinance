package com.fletamuto.sptb;

import java.text.DecimalFormat;

import com.fletamuto.sptb.db.DBMgr;

import android.os.Bundle;
import android.widget.TextView;

public class StateAssetsDefaultLayout extends StateDefaultLayout {  	
	long mPurchasePrice = 0L;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
	
	@Override
	protected void initialize() {
		super.initialize();
		
		mPurchasePrice = DBMgr.getAssetsPurchasePrice();
	}
	
	@Override
	protected void updateChildView() {
		double amount = mItem.getAmount();
		double percent = 1.0 - (amount / (double)mPurchasePrice);
		DecimalFormat decimalFormat = new DecimalFormat("##0.0%");
		TextView tvAmount = (TextView)findViewById(R.id.TVStateTitle);
		
		if (mItem.getAmount() >= mPurchasePrice) {
			percent = -percent;
			tvAmount.setText(String.format("현재가 : %,d원   손익율 : %s", mItem.getAmount(), decimalFormat.format(percent)));
		}
		else {
			
			tvAmount.setText(String.format("현재가 : %,d원   손익율 : -%s", mItem.getAmount(), decimalFormat.format(percent)));
		}
		
		
		
	}
}