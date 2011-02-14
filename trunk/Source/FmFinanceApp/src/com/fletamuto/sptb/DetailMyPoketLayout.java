package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.DetailMonthHistoryLayout.AccountDailyItem;
import com.fletamuto.sptb.DetailMonthHistoryLayout.AccountDailyItems;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.TransferItem;
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
    protected void onResume() {
    	updateChildView();
    	super.onResume();
    }
  
    @Override
	public void initialize() {
	  	super.initialize();
	  	
	  	Button btnBalance = (Button) findViewById(R.id.BtnBalance);
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
		
		setTitle("�� �ָӴ� ����");
	    setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.INVISIBLE);
	}

	@Override
	public void onEditBtnClick() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void updateMonthlyItems() {
		super.updateMonthlyItems();
		
		ArrayList<TransferItem> fromItems = DBMgr.getTranserFromAccount(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1, mMyPocket);
		int fromItemSize = fromItems.size();
		for (int index = 0; index < fromItemSize; index++) {
			updateMonthlyItem(fromItems.get(index), STATE_TRANSFOR_WITHDRAWAL);
		}
		
		ArrayList<TransferItem> toItems = DBMgr.getTranserToAccount(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1, mMyPocket);
		int toItemSize = toItems.size();
		for (int index = 0; index < toItemSize; index++) {
			updateMonthlyItem(toItems.get(index), STATE_TRANSFOR_DEPOSIT);
		}
		
//		ArrayList<TransferItem> fromItems = DBMgr.getTranserFromAccount(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1, mMyPocket);
//		
//		int fromItemSize = fromItems.size();
//		for (int index = 0; index < fromItemSize; index++) {
//			TransferItem trans = fromItems.get(index);
//			int day = trans.getOccurrentceDate().get(Calendar.DAY_OF_MONTH);
//			
//			if (mMonthlyItems[day] == null) {
//				mMonthlyItems[day] = new AccountDailyItems(trans.getOccurrentceDate());
//			}
//			
//			AccountDailyItem dailyItem = new AccountDailyItem(STATE_TRANSFOR_WITHDRAWAL, 
//					trans.getToAccount().getCompany().getName(), trans.getMemo(), trans.getAmount());
//			
//			dailyItem.setTag(trans);
//			mMonthlyItems[day].add(dailyItem);
//		}
//		
//		ArrayList<TransferItem> toItems = DBMgr.getTranserToAccount(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1, mMyPocket);
//		int toItemSize = toItems.size();
//		for (int index = 0; index < toItemSize; index++) {
//			TransferItem trans = toItems.get(index);
//			int day = trans.getOccurrentceDate().get(Calendar.DAY_OF_MONTH);
//			
//			if (mMonthlyItems[day] == null) {
//				mMonthlyItems[day] = new AccountDailyItems(trans.getOccurrentceDate());
//			}
//			
//			AccountDailyItem dailyItem = new AccountDailyItem(STATE_TRANSFOR_DEPOSIT, 
//					trans.getFromAccount().getCompany().getName(), trans.getMemo(), trans.getAmount());
//			
//			dailyItem.setTag(trans);
//			mMonthlyItems[day].add(dailyItem);
//		}
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
		
		btnBalance.setText(String.format("%,d��", amount));
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

	@Override
	public void updateChildView() {
		Button btnBalance = (Button) findViewById(R.id.BtnBalance);
	  	btnBalance.setText(String.format("%,d��", mMyPocket.getBalance()));
	}
	 
}