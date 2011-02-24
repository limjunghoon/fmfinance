package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.AssetsChangeItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityChangeItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.data.LiabilityPersonLoanItem;
import com.fletamuto.sptb.db.DBMgr;

public class DetailLiabilityDefaultLayout extends DetailBaseLayout {  	
	private LiabilityItem mLiability;
	protected ArrayList<LiabilityChangeItem> mListItems = new ArrayList<LiabilityChangeItem>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_liability_default, true);
        
        findViewById(R.id.LLPrograss).setVisibility(View.VISIBLE);
        
        setButtonListener();
        updateChildView();
    }

	public void setButtonListener() {
		findViewById(R.id.BtnStateDelete).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onRepayBtnClick();
			}
		});

		final ToggleButton tbPayment = (ToggleButton) findViewById(R.id.TBLiabilityPeruse);
		tbPayment.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				findViewById(R.id.LLLiabilityPayment).setVisibility((tbPayment.isChecked()) ? View.VISIBLE : View.GONE);
			}
		});
	}
	
	public void onRepayBtnClick() {
		Intent intent = new Intent(this, DetailLiabilityDefaultLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, mLiability);
		startActivityForResult(intent, MsgDef.ActRequest.ACT_INPUT_PEPAY);
	}

	public void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.TVLiabilityTitle);
		tvName.setText(mLiability.getTitle());
		TextView tvAmount = (TextView)findViewById(R.id.TVLiabilityAmount);
		tvAmount.setText(String.format("%,d원", mLiability.getOrignAmount()));
		TextView tvRemainderAmount = (TextView)findViewById(R.id.TVLiabilityRemainderAmount);
		tvRemainderAmount.setText(String.format("%,d원", mLiability.getAmount()));
		
		
		TextView tvCreateDate = (TextView)findViewById(R.id.TVLiabilityCreateDate);
		tvCreateDate.setText(mLiability.getCreateDateString());
		TextView tvExpiryDate = (TextView)findViewById(R.id.TVLiabilityExpiryDate);
		tvExpiryDate.setText(mLiability.getExpiryDateString());
		
		TextView tvMemo = (TextView)findViewById(R.id.TVLiabilityMemo);
		tvMemo.setText(mLiability.getMemo());
		setPorgress();
//		updateDeleteBtnText();
//		
		addLoanPaymentList();
	}
	
	protected void setPorgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
		int monthPeriodTerm = mLiability.getMonthPeriodTerm();
		int monthProgressCount = mLiability.getMonthProcessCount() ;
		
		progress.setMax(monthPeriodTerm);
		progress.setProgress(monthProgressCount);
		
		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
		tvProgrss.setText(String.format("진행(%d/%d)", mLiability.getMonthProcessCount(), mLiability.getMonthPeriodTerm()));
		tvProgrss.invalidate();
	}

	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		setTitle("부채 내역");
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mLiability = (LiabilityItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		mListItems = DBMgr.getLiabilityChangeStateItems(mLiability.getID());
	}

	@Override
	public void onEditBtnClick() {
		Intent intent = new Intent(this, DetailLiabilityDefaultLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mLiability.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	

	protected void addLoanPaymentList() {
		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLLiabilityPayment);
		llPayment.removeAllViewsInLayout();
		
		int size = mListItems.size();
		for (int index = 0; index < size; index++) {
			LiabilityChangeItem item = mListItems.get(index);
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.report_detail_loan_payment, null);
			((TextView)llMember.findViewById(R.id.TVPaymentDate)).setText(item.getChangeDateString());
			((TextView)llMember.findViewById(R.id.TVPaymentAmount)).setText(String.format("잔액 : %,d원", item.getAmount()));
			
			ExpenseItem interest = item.getInterest();
			if (interest.getID() != -1) {
				((TextView)llMember.findViewById(R.id.TVPaymentInterest)).setText(String.format("이자 : %,d원", interest.getAmount()));
			}
			
			ExpenseItem principal = item.getPrincipal();
			if (principal.getID() != -1) {
				((TextView)llMember.findViewById(R.id.TVPaymentPrincipal)).setText(String.format("원금 : %,d원", principal.getAmount()));
			}
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			llPayment.addView(llMember, params);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_INPUT_PEPAY) {
			if (resultCode == RESULT_OK) {
				mLiability = (LiabilityItem) DBMgr.getItem(LiabilityItem.TYPE, mLiability.getID());
				mListItems = DBMgr.getLiabilityChangeStateItems(mLiability.getID());
				updateChildView();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}