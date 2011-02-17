package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardPayment;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.TransferItem;
import com.fletamuto.sptb.db.DBMgr;

public class DetailAccountLayout extends DetailMonthHistoryLayout {
	
    private AccountItem mAccount;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_account, true);
    }
    
	@Override
	public void initialize() {
	  	super.initialize();
	  	
	  	mAccount = (AccountItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ACCOUNT_ITEM);
	  	
	  	TextView tvInstitution = (TextView) findViewById(R.id.TVAccountInstitution);
	  	tvInstitution.setText(mAccount.getCompany().getName());
	  	
	  	TextView tvBalance = (TextView) findViewById(R.id.TVAccountAmount);
	  	tvBalance.setText(String.format("%,d원", mAccount.getBalance()));
	  	
	  	TextView tvType = (TextView) findViewById(R.id.TVAccountType);
	  	tvType.setText(mAccount.getTypeName());
	  	
	  	TextView tvNumber = (TextView) findViewById(R.id.TVAccountNumber);
	  	tvNumber.setText(mAccount.getNumber());
	  	
	  	TextView tvName = (TextView) findViewById(R.id.TVAccountName);
	  	tvName.setText(mAccount.getName());
	}
	
	@Override
	protected void updateMonthlyItems() {
		super.updateMonthlyItems();
		
		ArrayList<CardPayment> paymentItems = DBMgr.getCardPaymentItems(mAccount.getID(), mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
		int paymentSize = paymentItems.size();
		for (int index = 0; index < paymentSize; index++) {
			updateMonthlyItem(paymentItems.get(index));
		}
		
		ArrayList<TransferItem> fromItems = DBMgr.getTranserFromAccount(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1, mAccount);
		int fromItemSize = fromItems.size();
		for (int index = 0; index < fromItemSize; index++) {
			updateMonthlyItem(fromItems.get(index), STATE_TRANSFOR_WITHDRAWAL);
		}
		
		ArrayList<TransferItem> toItems = DBMgr.getTranserToAccount(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1, mAccount);
		int toItemSize = toItems.size();
		for (int index = 0; index < toItemSize; index++) {
			updateMonthlyItem(fromItems.get(index), STATE_TRANSFOR_DEPOSIT);
		}
	}
	
	
    
    @Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("보통예금 내역");
		
		TextView tvCurrentMonth = (TextView)findViewById(R.id.TVCurrentMonth);
		tvCurrentMonth.setText(String.format("%d년 %d월", mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1));
	
		Button btnTrans = (Button)findViewById(R.id.BtnTrans);
		btnTrans.setText("이체");
		
		btnTrans.setOnClickListener(new View.OnClickListener() {
				
			public void onClick(View v) {
				Intent intent = new Intent(DetailAccountLayout.this, TransferAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, mAccount);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_TRANFER_ACCOUNT);
			}
		});
  	}
    
	@Override
	public void onEditBtnClick() {
    	Intent intent = new Intent(this, InputAccountLayout.class);
    	intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mAccount.getID());
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
    
    @Override
    protected void onResume() {
    	updateMonthlyItems();
        setAccountHistoryList();
        updateChildView();
        
    	super.onResume();
    }
    

	public void updateChildView() {
//		Button btnBalance = (Button)findViewById(R.id.BtnTitle);
//    	btnBalance.setText(String.format("잔액 %,d원", mAccount.getBalance()));
//    	btnBalance.setVisibility(View.VISIBLE);
	}
    
	@Override
	public ArrayList<FinanceItem> getExpenseItem() {
		return DBMgr.getExpenseItemFromAccount(mAccount.getID(), mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
	}


	@Override
	public ArrayList<FinanceItem> getIncomeItem() {
		return DBMgr.getIncomeItemFromAccount(mAccount.getID(), mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
	}


}