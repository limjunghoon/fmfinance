package com.fletamuto.sptb;

import java.util.ArrayList;
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
import com.fletamuto.sptb.data.AssetsInsuranceItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class DetailAssetsInsuranceLayout extends DetailBaseLayout {  	
	private AssetsInsuranceItem mInsurance;
	protected ArrayList<AssetsChangeItem> mListItems = new ArrayList<AssetsChangeItem>();
	protected ReportInsurancePaymentItemAdapter mInsurancePaymentItemAdapter = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_assets_insurance, true);
        
        findViewById(R.id.LLPrograss).setVisibility(View.VISIBLE);
        
        setButtonListener();
        updateChildView();
    }

	public void setButtonListener() {
		findViewById(R.id.BtnStateDelete).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onDeleteBtnClick();
			}
		});
		
		final ToggleButton tbPayment = (ToggleButton) findViewById(R.id.TBSavingPeruse);
		tbPayment.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				findViewById(R.id.LLInsurancePayment).setVisibility((tbPayment.isChecked()) ? View.VISIBLE : View.GONE);
			}
		});
	}

	public void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.TVInsuranceName);
		tvName.setText(mInsurance.getTitle());
		TextView tvAmount = (TextView)findViewById(R.id.TVInsuranceAmount);
		tvAmount.setText(String.format("%,d원", mInsurance.getAmount()));

		TextView tvCompany = (TextView)findViewById(R.id.TVInsuranceInstitution);
		tvCompany.setText(mInsurance.getCompany());
		
		TextView tvCreateDate = (TextView)findViewById(R.id.TVInsuranceCreateDate);
		tvCreateDate.setText(mInsurance.getCreateDateString());
		TextView tvEndDate = (TextView)findViewById(R.id.TVInsuranceEndDate);
		tvEndDate.setText(mInsurance.getExpriyDateString());

		TextView tvPaymentAmount = (TextView)findViewById(R.id.TVInsurancePaymentAmount);
		tvPaymentAmount.setText(String.format("%,d원", mInsurance.getPayment()));

		TextView tvMemo = (TextView)findViewById(R.id.TVInsuranceMemo);
		tvMemo.setText(mInsurance.getMemo());
		setPorgress();
		updateDeleteBtnText();
		
		addInsurancePaymentList();
	}
	
	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("보험 내역");
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mInsurance = (AssetsInsuranceItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		mListItems = DBMgr.getAssetsChangeStateItems(mInsurance.getID());
	}
	
	private void setPorgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
		int monthPeriodTerm = mInsurance.getMonthPeriodTerm();
		int monthProgressCount = mInsurance.getMonthProcessCount() ;
		
		progress.setMax(monthPeriodTerm);
		progress.setProgress(monthProgressCount);
		
		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
		tvProgrss.setText(String.format("진행(%d/%d)", mInsurance.getMonthProcessCount(), mInsurance.getMonthPeriodTerm()));
		tvProgrss.invalidate();
	}

	protected void updateDeleteBtnText() {
		if (mInsurance.isOverExpirationDate()) {
			((Button)findViewById(R.id.BtnStateDelete)).setText("해약");
		}
		else {
			((Button)findViewById(R.id.BtnStateDelete)).setText("만기");
		}
		
	}

	@Override
	public void onEditBtnClick() {
		Intent intent = new Intent(this, InputAssetsInsuranceLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mInsurance.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
	protected void onDeleteBtnClick() {
//		Intent intent = new Intent(this, InputIncomeFromAssetsLayout.class);
//		intent.putExtra(MsgDef.ExtraNames.ITEM, createIncomeItem());
//		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ITEM);
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
//			if (category.getName().compareTo(mInsurance.getCategory().getName()) == 0) {
//				category.setExtndType(ItemDef.ExtendAssets.Insurance);
//				income.setCategory(category);
//				break;
//			}
//		}
//		
//		income.setAmount(mInsurance.getTotalAmount());
////		income.setCount(getItem().getCount());
////		income.setCreateDate(getItem().getCreateDate());
//		
//		return income;
//	}
//	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_ADD_ITEM) {
			if (resultCode == RESULT_OK) {
				completionAssets(data.getIntExtra(MsgDef.ExtraNames.ADD_ITEM_ID, -1));
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void completionAssets(int incomeID) {
		mInsurance.setState(FinanceItem.STATE_COMPLEATE);
		DBMgr.updateFinanceItem(mInsurance);
		DBMgr.addIncomeFromAssets(incomeID, mInsurance.getID());
		finish();
	}
	
	protected void addInsurancePaymentList() {
		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLInsurancePayment);
		llPayment.removeAllViewsInLayout();
		
		int size = mListItems.size();
		for (int index = 0; index < size; index++) {
			AssetsChangeItem item = mListItems.get(index);
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.report_detail_insurance_payment, null);
			((TextView)llMember.findViewById(R.id.TVPaymentDate)).setText(item.getChangeDateString());
			((TextView)llMember.findViewById(R.id.TVPaymentAmount)).setText(String.format("%,d원", item.getChangeAmount()));
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			llPayment.addView(llMember, params);
		}
	}
	
	
	protected void setListViewText(FinanceItem financeItem, View convertView) {
    	
		((TextView)convertView.findViewById(R.id.TVPaymentDate)).setText(financeItem.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVPaymentAmount)).setText(String.format("%,d원", financeItem.getAmount()));
	
	}
	
	public class ReportInsurancePaymentItemAdapter extends ArrayAdapter<FinanceItem> {
		private int mResource;
    	private LayoutInflater mInflater;

		public ReportInsurancePaymentItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			AssetsInsuranceItem item = (AssetsInsuranceItem)getItem(position);

			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);	
			}
			
			setListViewText(item, convertView);

			return convertView;
		}
		
//		public View createSeparator(LayoutInflater inflater, ViewGroup parent, FinanceItem item) {
//			View convertView = inflater.inflate(R.layout.list_separators, parent, false);
//			TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
//			tvTitle.setText(item.getSeparatorTitle());
//			tvTitle.setTextColor(Color.BLACK);
//			convertView.setBackgroundColor(Color.WHITE);
//			return convertView;
//		}
		
		@Override
		public boolean isEnabled(int position) {
			return !mListItems.get(position).isSeparator();
		}
    }

}