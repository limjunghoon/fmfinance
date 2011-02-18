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
import com.fletamuto.sptb.data.AssetsRealEstateItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;

public class DetailAssetsRealEstateLayout extends DetailBaseLayout {  	
	private AssetsRealEstateItem mRealEstate;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_assets_real_estate, true);
        
        setButtonListener();
        updateChildView();
    }

	public void setButtonListener() {
//		findViewById(R.id.BtnStateDelete).setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				onDeleteBtnClick();
//			}
//		});
//		
//		final ToggleButton tbPayment = (ToggleButton) findViewById(R.id.TBSavingPeruse);
//		tbPayment.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				findViewById(R.id.LLRealEstatePayment).setVisibility((tbPayment.isChecked()) ? View.VISIBLE : View.GONE);
//			}
//		});
	}

	public void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.TVRealEstateName);
		tvName.setText(mRealEstate.getTitle());
		TextView tvAmount = (TextView)findViewById(R.id.TVRealEstateAmount);
		tvAmount.setText(String.format("%,d원", mRealEstate.getAmount()));
		TextView tvCreateDate = (TextView)findViewById(R.id.TVRealEstateCreateDate);
		tvCreateDate.setText(mRealEstate.getCreateDateString());
		TextView tvScale = (TextView)findViewById(R.id.TVRealEstateScale);
		tvScale.setText(mRealEstate.getScale());
		TextView tvMemo = (TextView)findViewById(R.id.TVRealEstateMemo);
		tvMemo.setText(mRealEstate.getMemo());
		updateDeleteBtnText();
	}
	
	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("부동산 내역");
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mRealEstate = (AssetsRealEstateItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
//		mListItems = DBMgr.getAssetsChangeStateItems(mRealEstate.getID());
	}
	
	private void setPorgress() {
//		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
//		int monthPeriodTerm = mRealEstate.getMonthPeriodTerm();
//		int monthProgressCount = mRealEstate.getMonthProcessCount() ;
//		
//		progress.setMax(monthPeriodTerm);
//		progress.setProgress(monthProgressCount);
//		
//		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
//		tvProgrss.setText(String.format("진행(%d/%d)", mRealEstate.getMonthProcessCount(), mRealEstate.getMonthPeriodTerm()));
//		tvProgrss.invalidate();
	}

	protected void updateDeleteBtnText() {
//		if (mRealEstate.isOverExpirationDate()) {
//			((Button)findViewById(R.id.BtnStateDelete)).setText("해약");
//		}
//		else {
//			((Button)findViewById(R.id.BtnStateDelete)).setText("만기");
//		}
//		
	}

	@Override
	public void onEditBtnClick() {
		Intent intent = new Intent(this, InputAssetsRealEstateLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mRealEstate.getID());
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
//			if (category.getName().compareTo(mRealEstate.getCategory().getName()) == 0) {
//				category.setExtndType(ItemDef.ExtendAssets.RealEstate);
//				income.setCategory(category);
//				break;
//			}
//		}
//		
//		income.setAmount(mRealEstate.getTotalAmount());
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
//		mRealEstate.setState(FinanceItem.STATE_COMPLEATE);
//		DBMgr.updateFinanceItem(mRealEstate);
//		DBMgr.addIncomeFromAssets(incomeID, mRealEstate.getID());
//		finish();
	}
	
//	protected void addRealEstatePaymentList() {
//		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLRealEstatePayment);
//		llPayment.removeAllViewsInLayout();
//		
//		int size = mListItems.size();
//		for (int index = 0; index < size; index++) {
//			AssetsChangeItem item = mListItems.get(index);
//			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.report_detail_RealEstate_payment, null);
//			((TextView)llMember.findViewById(R.id.TVPaymentDate)).setText(item.getChangeDateString());
//			((TextView)llMember.findViewById(R.id.TVPaymentAmount)).setText(String.format("%,d원", item.getChangeAmount()));
//			
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
//			llPayment.addView(llMember, params);
//		}
//	}
	
	
//	protected void setListViewText(FinanceItem financeItem, View convertView) {
//    	
//		((TextView)convertView.findViewById(R.id.TVPaymentDate)).setText(financeItem.getCreateDateString());
//		((TextView)convertView.findViewById(R.id.TVPaymentAmount)).setText(String.format("%,d원", financeItem.getAmount()));
//	
//	}
	
//	public class ReportRealEstatePaymentItemAdapter extends ArrayAdapter<FinanceItem> {
//		private int mResource;
//    	private LayoutInflater mInflater;
//
//		public ReportRealEstatePaymentItemAdapter(Context context, int resource,
//				 List<FinanceItem> objects) {
//			super(context, resource, objects);
//			this.mResource = resource;
//			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			
//			AssetsRealEstateItem item = (AssetsRealEstateItem)getItem(position);
//
//			if (convertView == null) {
//				convertView = mInflater.inflate(mResource, parent, false);	
//			}
//			
//			setListViewText(item, convertView);
//
//			return convertView;
//		}
		
//		public View createSeparator(LayoutInflater inflater, ViewGroup parent, FinanceItem item) {
//			View convertView = inflater.inflate(R.layout.list_separators, parent, false);
//			TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
//			tvTitle.setText(item.getSeparatorTitle());
//			tvTitle.setTextColor(Color.BLACK);
//			convertView.setBackgroundColor(Color.WHITE);
//			return convertView;
//		}
//		
//		@Override
//		public boolean isEnabled(int position) {
//			return !mListItems.get(position).isSeparator();
//		}
//    }

}