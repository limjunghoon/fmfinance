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
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.Revenue;
import com.fletamuto.sptb.view.FmBaseLayout;

public class DetailAssetsSavingsLayout extends DetailBaseLayout {  	
	private AssetsSavingsItem mSavings;
	protected ArrayList<AssetsChangeItem> mListItems = new ArrayList<AssetsChangeItem>();
	protected ReportSavingsPaymentItemAdapter mSavingsPaymentItemAdapter = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_assets_savings, true);
        
      //완료된 자산에서 결산 보여 주기 위한
        if (mSavings.getState() == FinanceItem.STATE_COMPLEATE) {
        	findViewById(R.id.SavingOriginalAmountLL).setVisibility(View.VISIBLE);
			findViewById(R.id.SavingGainAndLossAmountLL).setVisibility(View.VISIBLE);
			findViewById(R.id.SavingExpiryAmountLL).setVisibility(View.VISIBLE);
			
			findViewById(R.id.SavingDeleteLL).setVisibility(View.GONE);
			
        } else {
        	findViewById(R.id.SavingPrograssLL).setVisibility(View.VISIBLE);
        }
        
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
				findViewById(R.id.LLSavingsPayment).setVisibility((tbPayment.isChecked()) ? View.VISIBLE : View.GONE);
			}
		});
	}

	public void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.TVSavingsName);
		tvName.setText(mSavings.getTitle());
		TextView tvAmount = (TextView)findViewById(R.id.TVSavingsAmount);
		tvAmount.setText(String.format("%,d원", mSavings.getAmount()));
		if (mSavings.getAccount() != null) {
			TextView tvNumber = (TextView)findViewById(R.id.TVSavingsNumber);
			tvNumber.setText(mSavings.getAccount().getNumber());
			TextView tvInstitution = (TextView)findViewById(R.id.TVSavingsInstitution);
			tvInstitution.setText(mSavings.getAccount().getCompany().getName());
		}
		
		TextView tvCreateDate = (TextView)findViewById(R.id.TVSavingsCreateDate);
		tvCreateDate.setText(mSavings.getCreateDateString());
		TextView tvEndDate = (TextView)findViewById(R.id.TVSavingsEndDate);
		tvEndDate.setText(mSavings.getExpriyDateString());
		TextView tvRate = (TextView)findViewById(R.id.TVSavingsRateDate);
		tvRate.setText(String.format("%d%%", mSavings.getRate()));
		TextView tvPaymentAmount = (TextView)findViewById(R.id.TVSavingsPaymentAmount);
		tvPaymentAmount.setText(String.format("%,d원", mSavings.getPayment()));
		TextView tvExpect = (TextView)findViewById(R.id.TVSavingsExpectAmount);
		tvExpect.setText(String.format("%,d원", mSavings.getExpectAmount()));
		TextView tvMemo = (TextView)findViewById(R.id.TVSavingsMemo);
		tvMemo.setText(mSavings.getMemo());
		
		//완료된 자산에서 결산 보여 주기 위한
		if (mSavings.getState() == FinanceItem.STATE_COMPLEATE) {
			
			TextView tvOiriginalAmount = (TextView)findViewById(R.id.SavingOriginalAmount);
			tvOiriginalAmount.setText(String.format("%,d원", mSavings.getTotalAmount()));
			
			ArrayList<Integer> incomeItemID = DBMgr.getIncomeFromAssets(mSavings.getID());
			if (incomeItemID.isEmpty())
    		{
				TextView tvExpiryAmount = (TextView)findViewById(R.id.SavingExpiryAmount);
				tvExpiryAmount.setText(String.format("%,d원", mSavings.getTotalAmount()));
				TextView tvGainAndLossAmount = (TextView)findViewById(R.id.SavingGainAndLossAmount);
				tvGainAndLossAmount.setText("수익율 : 0%");
    		} else {
    			FinanceItem incomeItem = DBMgr.getItem(IncomeItem.TYPE,incomeItemID.get(0));
    			
    			TextView tvExpiryAmount = (TextView)findViewById(R.id.SavingExpiryAmount);
    			tvExpiryAmount.setText(String.format("%,d원", incomeItem.getAmount()));
    			
    			TextView tvGainAndLossAmount = (TextView)findViewById(R.id.SavingGainAndLossAmount);
    			tvGainAndLossAmount.setText(Revenue.getString(mSavings.getAmount(), incomeItem.getAmount()));
    		}
		} else {
			setPorgress();
			updateDeleteBtnText();
			
			addSavingsPaymentList();
		}
	}
	
	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("정기예금 내역");
		
		//완료된 자산에서 결산 보여 주기 위한
		if (mSavings.getState() == FinanceItem.STATE_COMPLEATE) {
			setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.INVISIBLE);
		}
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mSavings = (AssetsSavingsItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		mListItems = DBMgr.getAssetsChangeStateItems(mSavings.getID());
	}
	
	private void setPorgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
		int monthPeriodTerm = mSavings.getMonthPeriodTerm();
		int monthProgressCount = mSavings.getMonthProcessCount() ;
		
		progress.setMax(monthPeriodTerm);
		progress.setProgress(monthProgressCount);
		
		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
		tvProgrss.setText(String.format("진행(%d/%d)", mSavings.getMonthProcessCount(), mSavings.getMonthPeriodTerm()));
		tvProgrss.invalidate();
	}

	protected void updateDeleteBtnText() {
		if (mSavings.isOverExpirationDate()) {
			((Button)findViewById(R.id.BtnStateDelete)).setText("해약");
		}
		else {
			((Button)findViewById(R.id.BtnStateDelete)).setText("만기");
		}
		
	}

	@Override
	public void onEditBtnClick() {
		Intent intent = new Intent(this, InputAssetsSavingsLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mSavings.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
	protected void onDeleteBtnClick() {
		Intent intent = new Intent(this, InputIncomeFromAssetsLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, createIncomeItem());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ITEM);
	}
	
	protected IncomeItem createIncomeItem() {
		IncomeItem income = new IncomeItem();
		
		ArrayList<Category> categories = DBMgr.getCategory(IncomeItem.TYPE, ItemDef.ExtendAssets.NONE);
		int categorySize = categories.size();
		
		for (int index = 0; index < categorySize; index++) {
			Category category = categories.get(index); 
			if (category.getName().compareTo(mSavings.getCategory().getName()) == 0) {
				category.setExtndType(ItemDef.ExtendAssets.SAVINGS);
				income.setCategory(category);
				break;
			}
		}
		
		income.setAmount(mSavings.getTotalAmount());
//		income.setCount(getItem().getCount());
//		income.setCreateDate(getItem().getCreateDate());
		
		return income;
	}
	
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
		mSavings.setState(FinanceItem.STATE_COMPLEATE);
		DBMgr.updateFinanceItem(mSavings);
		DBMgr.addIncomeFromAssets(incomeID, mSavings.getID());
		finish();
	}
	
	protected void addSavingsPaymentList() {
		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLSavingsPayment);
		llPayment.removeAllViewsInLayout();
		
		int size = mListItems.size();
		for (int index = 0; index < size; index++) {
			AssetsChangeItem item = mListItems.get(index);
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.report_detail_savings_payment, null);
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
	
	public class ReportSavingsPaymentItemAdapter extends ArrayAdapter<FinanceItem> {
		private int mResource;
    	private LayoutInflater mInflater;

		public ReportSavingsPaymentItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			AssetsSavingsItem item = (AssetsSavingsItem)getItem(position);
			
//				if (item.isSeparator()) {
//					return createSeparator(mInflater, parent, item);
//				}
//				else {
//					convertView = mInflater.inflate(mResource, parent, false);
//				}
					
				if (convertView == null) {
					convertView = mInflater.inflate(mResource, parent, false);	
				}
				
				setListViewText(item, convertView);
//				setDeleteBtnListener(convertView, item.getID(), position);
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