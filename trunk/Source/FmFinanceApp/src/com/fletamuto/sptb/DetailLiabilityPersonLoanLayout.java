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

public class DetailLiabilityPersonLoanLayout extends DetailBaseLayout {  	
	private LiabilityPersonLoanItem mPersonLoan;
	protected ArrayList<LiabilityChangeItem> mListItems = new ArrayList<LiabilityChangeItem>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_liability_person_loan, true);
        
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

		final ToggleButton tbPayment = (ToggleButton) findViewById(R.id.TBPersonLoanPeruse);
		tbPayment.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				findViewById(R.id.LLPersonLoanPayment).setVisibility((tbPayment.isChecked()) ? View.VISIBLE : View.GONE);
			}
		});
	}
	
	public void onRepayBtnClick() {
		Intent intent = new Intent(this, InputLiabilityRepayPersonLoanLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, mPersonLoan);
		startActivityForResult(intent, MsgDef.ActRequest.ACT_INPUT_PEPAY);
	}

	public void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.TVPersonLoanName);
		tvName.setText(mPersonLoan.getTitle());
		TextView tvAmount = (TextView)findViewById(R.id.TVPersonLoanAmount);
		tvAmount.setText(String.format("%,d원", mPersonLoan.getOrignAmount()));
		TextView tvRemainderAmount = (TextView)findViewById(R.id.TVPersonLoanRemainderAmount);
		tvRemainderAmount.setText(String.format("%,d원", mPersonLoan.getAmount()));
		
		
		TextView tvCreateDate = (TextView)findViewById(R.id.TVPersonLoanCreateDate);
		tvCreateDate.setText(mPersonLoan.getCreateDateString());
		TextView tvExpiryDate = (TextView)findViewById(R.id.TVPersonLoanExpiryDate);
		tvExpiryDate.setText(mPersonLoan.getExpiryDateString());
		
		TextView tvMemo = (TextView)findViewById(R.id.TVPersonLoanMemo);
		tvMemo.setText(mPersonLoan.getMemo());
		setPorgress();
//		updateDeleteBtnText();
//		
		addLoanPaymentList();
	}
	
	protected void setPorgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
		int monthPeriodTerm = mPersonLoan.getMonthPeriodTerm();
		int monthProgressCount = mPersonLoan.getMonthProcessCount() ;
		
		progress.setMax(monthPeriodTerm);
		progress.setProgress(monthProgressCount);
		
		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
		tvProgrss.setText(String.format("진행(%d/%d)", mPersonLoan.getMonthProcessCount(), mPersonLoan.getMonthPeriodTerm()));
		tvProgrss.invalidate();
	}

	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		setTitle("빌린돈 내역");
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mPersonLoan = (LiabilityPersonLoanItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		mListItems = DBMgr.getLiabilityChangeStateItems(mPersonLoan.getID());
	}

	@Override
	public void onEditBtnClick() {
		Intent intent = new Intent(this, InputLiabilityPersonLoanLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mPersonLoan.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	

	protected void addLoanPaymentList() {
		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLPersonLoanPayment);
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
				mPersonLoan = (LiabilityPersonLoanItem) DBMgr.getItem(LiabilityItem.TYPE, mPersonLoan.getID());
				mListItems = DBMgr.getLiabilityChangeStateItems(mPersonLoan.getID());
				updateChildView();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}