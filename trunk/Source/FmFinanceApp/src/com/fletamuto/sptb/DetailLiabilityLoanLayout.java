package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.LiabilityChangeItem;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.db.DBMgr;

public class DetailLiabilityLoanLayout extends DetailBaseLayout {  	
	private LiabilityLoanItem mLoan;
	protected ArrayList<LiabilityChangeItem> mListItems = new ArrayList<LiabilityChangeItem>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_liability_loan, true);
        
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

		final ToggleButton tbPayment = (ToggleButton) findViewById(R.id.TBLoanPeruse);
		tbPayment.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				findViewById(R.id.LLLoanPayment).setVisibility((tbPayment.isChecked()) ? View.VISIBLE : View.GONE);
			}
		});
	}
	
	public void onRepayBtnClick() {
		Intent intent = new Intent(this, InputLiabilityRepayLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, mLoan);
		startActivityForResult(intent, MsgDef.ActRequest.ACT_INPUT_PEPAY);
	}

	public void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.TVLoanName);
		tvName.setText(mLoan.getTitle());
		TextView tvAmount = (TextView)findViewById(R.id.TVLoanAmount);
		tvAmount.setText(String.format("%,d원", mLoan.getOrignAmount()));
		TextView tvRemainderAmount = (TextView)findViewById(R.id.TVLoanRemainderAmount);
		tvRemainderAmount.setText(String.format("%,d원", mLoan.getAmount()));
		
		((TextView)findViewById(R.id.TVLoanAccount)).setText(String.format("%s : %s", mLoan.getAccount().getCompany().getName(), mLoan.getAccount().getNumber()));
		
		TextView tvCreateDate = (TextView)findViewById(R.id.TVLoanCreateDate);
		tvCreateDate.setText(mLoan.getCreateDateString());
		TextView tvExpiryDate = (TextView)findViewById(R.id.TVLoanExpiryDate);
		tvExpiryDate.setText(mLoan.getExpiryDateString());
		TextView tvCompanyName = (TextView)findViewById(R.id.TVLoanInstitution);
		tvCompanyName.setText(mLoan.getCompany().getName());
		if (mLoan.getPaymentDate() != null) {
			TextView tvPaymentDate = (TextView)findViewById(R.id.TVLoanPaymentDate);
			tvPaymentDate.setText(String.format("%s이후  매월 %d일", mLoan.getPaymentDateString(), mLoan.getPaymentDate().get(Calendar.DAY_OF_MONTH)));
		}
		
		TextView tvMemo = (TextView)findViewById(R.id.TVLoanMemo);
		tvMemo.setText(mLoan.getMemo());
		setPorgress();
//		updateDeleteBtnText();
//		
		addLoanPaymentList();
	}
	
	protected void setPorgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
		int monthPeriodTerm = mLoan.getMonthPeriodTerm();
		int monthProgressCount = mLoan.getMonthProcessCount() ;
		
		progress.setMax(monthPeriodTerm);
		progress.setProgress(monthProgressCount);
		
		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
		tvProgrss.setText(String.format("진행(%d/%d)", mLoan.getMonthProcessCount(), mLoan.getMonthPeriodTerm()));
		tvProgrss.invalidate();
	}

	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("대출 내역");
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mLoan = (LiabilityLoanItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		mListItems = DBMgr.getLiabilityChangeStateItems(mLoan.getID());
	}

	@Override
	public void onEditBtnClick() {
		Intent intent = new Intent(this, InputLiabilityLoanLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mLoan.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	

	protected void addLoanPaymentList() {
		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLLoanPayment);
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
				updateChildView();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}