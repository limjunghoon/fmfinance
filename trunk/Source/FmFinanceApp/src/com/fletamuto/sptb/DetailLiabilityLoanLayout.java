package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.AssetsChangeItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.db.DBMgr;

public class DetailLiabilityLoanLayout extends DetailBaseLayout {  	
	private LiabilityLoanItem mLoan;

	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_liability_loan, true);
        
//        findViewById(R.id.LLPrograss).setVisibility(View.VISIBLE);
//        
       setButtonListener();
        updateChildView();
    }

	public void setButtonListener() {
		findViewById(R.id.BtnStateDelete).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onRepayBtnClick();
			}

			
		});
		
//		final ToggleButton tbPayment = (ToggleButton) findViewById(R.id.TBSavingPeruse);
//		tbPayment.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				findViewById(R.id.LLSavingsPayment).setVisibility((tbPayment.isChecked()) ? View.VISIBLE : View.GONE);
//			}
//		});
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
//		setPorgress();
//		updateDeleteBtnText();
//		
//		addLoanPaymentList();
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
//		mListItems = DBMgr.getAssetsChangeStateItems(mLoan.getID());
	}
//	
//	private void setPorgress() {
//		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
//		int monthPeriodTerm = mLoan.getMonthPeriodTerm();
//		int monthProgressCount = mLoan.getMonthProcessCount() ;
//		
//		progress.setMax(monthPeriodTerm);
//		progress.setProgress(monthProgressCount);
//		
//		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
//		tvProgrss.setText(String.format("진행(%d/%d)", mLoan.getMonthProcessCount(), mLoan.getMonthPeriodTerm()));
//		tvProgrss.invalidate();
//	}
//
//	protected void updateDeleteBtnText() {
//		if (mLoan.isOverExpirationDate()) {
//			((Button)findViewById(R.id.BtnStateDelete)).setText("해약");
//		}
//		else {
//			((Button)findViewById(R.id.BtnStateDelete)).setText("만기");
//		}
//		
//	}
//
	@Override
	public void onEditBtnClick() {
//		Intent intent = new Intent(this, InputAssetsLoanLayout.class);
//		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mLoan.getID());
//		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
//	
//	protected IncomeItem createIncomeItem() {
//		IncomeItem income = new IncomeItem();
//		
//		ArrayList<Category> categories = DBMgr.getCategory(IncomeItem.TYPE, ItemDef.ExtendAssets.NONE);
//		int categorySize = categories.size();
//		
//		for (int index = 0; index < categorySize; index++) {
//			Category category = categories.get(index); 
//			if (category.getName().compareTo(mLoan.getCategory().getName()) == 0) {
//				category.setExtndType(ItemDef.ExtendAssets.Loan);
//				income.setCategory(category);
//				break;
//			}
//		}
//		
//		income.setAmount(mLoan.getTotalAmount());
////		income.setCount(getItem().getCount());
////		income.setCreateDate(getItem().getCreateDate());
//		
//		return income;
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == MsgDef.ActRequest.ACT_ADD_ITEM) {
//			if (resultCode == RESULT_OK) {
//				completionAssets(data.getIntExtra(MsgDef.ExtraNames.ADD_ITEM_ID, -1));
//			}
//		}
//
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//
//	protected void completionAssets(int incomeID) {
//		mLoan.setState(FinanceItem.STATE_COMPLEATE);
//		DBMgr.updateFinanceItem(mLoan);
//		DBMgr.addIncomeFromAssets(incomeID, mLoan.getID());
//		finish();
//	}
//	
//	protected void addLoanPaymentList() {
//		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLLoanPayment);
//		llPayment.removeAllViewsInLayout();
//		
//		int size = mListItems.size();
//		for (int index = 0; index < size; index++) {
//			AssetsChangeItem item = mListItems.get(index);
//			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.report_detail_Loan_payment, null);
//			((TextView)llMember.findViewById(R.id.TVPaymentDate)).setText(item.getChangeDateString());
//			((TextView)llMember.findViewById(R.id.TVPaymentAmount)).setText(String.format("%,d원", item.getChangeAmount()));
//			
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
//			llPayment.addView(llMember, params);
//		}
//	}
//	
//	
//	protected void setListViewText(FinanceItem financeItem, View convertView) {
//    	
//		((TextView)convertView.findViewById(R.id.TVPaymentDate)).setText(financeItem.getCreateDateString());
//		((TextView)convertView.findViewById(R.id.TVPaymentAmount)).setText(String.format("%,d원", financeItem.getAmount()));
//	
//	}
//	
//	public class ReportLoanPaymentItemAdapter extends ArrayAdapter<FinanceItem> {
//		private int mResource;
//    	private LayoutInflater mInflater;
//
//		public ReportLoanPaymentItemAdapter(Context context, int resource,
//				 List<FinanceItem> objects) {
//			super(context, resource, objects);
//			this.mResource = resource;
//			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			
//			AssetsLoanItem item = (AssetsLoanItem)getItem(position);
//			
////				if (item.isSeparator()) {
////					return createSeparator(mInflater, parent, item);
////				}
////				else {
////					convertView = mInflater.inflate(mResource, parent, false);
////				}
//					
//				if (convertView == null) {
//					convertView = mInflater.inflate(mResource, parent, false);	
//				}
//				
//				setListViewText(item, convertView);
////				setDeleteBtnListener(convertView, item.getID(), position);
//			return convertView;
//		}
//		
////		public View createSeparator(LayoutInflater inflater, ViewGroup parent, FinanceItem item) {
////			View convertView = inflater.inflate(R.layout.list_separators, parent, false);
////			TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
////			tvTitle.setText(item.getSeparatorTitle());
////			tvTitle.setTextColor(Color.BLACK);
////			convertView.setBackgroundColor(Color.WHITE);
////			return convertView;
////		}
//		
//		@Override
//		public boolean isEnabled(int position) {
//			return !mListItems.get(position).isSeparator();
//		}
//    }

}