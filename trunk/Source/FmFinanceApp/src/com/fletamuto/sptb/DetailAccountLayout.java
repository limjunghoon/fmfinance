package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ViewHolder;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
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
		//				Intent intent = new Intent(ReportAccountHistoryLayout.this, TransferAccountLayout.class);
		//				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, mAccount);
		//				startActivityForResult(intent, MsgDef.ActRequest.ACT_TRANFER_ACCOUNT);
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
    

	protected void updateChildView() {
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