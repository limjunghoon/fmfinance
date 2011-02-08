package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.view.FmBaseLayout;

public class DetailMyPoketLayout extends DetailMonthHistoryLayout {
	private AccountItem mMyPocket = DBMgr.getAccountMyPoctet();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_mypocket, true);
    }
  
    
    @Override
	public void initialize() {
	  	super.initialize();
	  	
	  	Button btnBalance = (Button) findViewById(R.id.BtnBalance);
	  	btnBalance.setText(String.format("%,d원", mMyPocket.getBalance()));
	  	btnBalance.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(DetailMyPoketLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT);
			}
		});
	  	
	  	Button btnTrans = (Button)findViewById(R.id.BtnTrans);
	  	btnTrans.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(DetailMyPoketLayout.this, TransferAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, mMyPocket);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_TRANFER_ACCOUNT);
			}
		});
    }
    
    
    @Override
	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("내 주머니 내역");
	    setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.INVISIBLE);
	}

	@Override
	public void onEditBtnClick() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		if (requestCode == MsgDef.ActRequest.ACT_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			updateMypocketPopupText(data.getLongExtra("AMOUNT", 0L));
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected void updateMypocketPopupText(long amount) {
		Button btnBalance = (Button) findViewById(R.id.BtnBalance);
		
		btnBalance.setText(String.format("%,d원", amount));
		btnBalance.setTag(new Long(amount));
		
		mMyPocket.setBalance(amount);
		DBMgr.updateAccount(mMyPocket);
	}


	@Override
	public ArrayList<FinanceItem> getExpenseItem() {
		return DBMgr.getExpenseItemFromMypocket(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
	}


	@Override
	public ArrayList<FinanceItem> getIncomeItem() {
		return DBMgr.getIncomeItemFromMypocket(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
	}
	 
}