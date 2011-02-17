package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.AssetsChangeItem;
import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.db.DBMgr;

public class DetailAssetsFundLayout extends DetailBaseLayout {  	
	private AssetsFundItem mFund;
	private long mPaymentAmount = 0L;
	protected ArrayList<AssetsChangeItem> mListItems = new ArrayList<AssetsChangeItem>();
//	protected ReportFundPaymentItemAdapter mFundPaymentItemAdapter = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_assets_fund, true);
        
 //       findViewById(R.id.LLPrograss).setVisibility(View.VISIBLE);
        
        setButtonListener();
        updateChildView();
    }

	public void setButtonListener() {		
		final ToggleButton tbPayment = (ToggleButton) findViewById(R.id.TBFundPeruse);
		tbPayment.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				findViewById(R.id.LLFundPayment).setVisibility((tbPayment.isChecked()) ? View.VISIBLE : View.GONE);
			}
		});
		
		findViewById(R.id.BtnFundBuy).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				addBuyStateActivtiy();
			}
		});
		
		findViewById(R.id.BtnFundSell).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				addSellStateActivtiy();
			}
		});
	}
	
	protected void addBuyStateActivtiy() {
		Intent intent = new Intent(this, InputAssetsFundLayout.class);
		intent.putExtra(MsgDef.ExtraNames.INPUT_CHANGE_MODE, true);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mFund.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
	private void addSellStateActivtiy() {
//		Intent intent = new Intent(this, InputIncomeFromStockLayout.class);
//		intent.putExtra(MsgDef.ExtraNames.ITEM, createIncomeItem());
//		intent.putExtra(MsgDef.ExtraNames.STOCK_ID, mFund.getID());
//		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ITEM);
	}

	public void updateChildView() {
		addFundPaymentList();
		
		TextView tvName = (TextView)findViewById(R.id.TVFundName);
		tvName.setText(mFund.getTitle());
		TextView tvAmount = (TextView)findViewById(R.id.TVFundAmount);
		tvAmount.setText(String.format("%,d원", mFund.getAmount()));
		TextView tvCreateDate = (TextView)findViewById(R.id.TVFundCreateDate);
		tvCreateDate.setText(mFund.getCreateDateString());
		TextView tvEndDate = (TextView)findViewById(R.id.TVFundEndDate);
		tvEndDate.setText(mFund.getExpriyDateString());
		TextView tvKind = (TextView)findViewById(R.id.TVFundKind);
		tvKind.setText(mFund.getKindString());
		TextView tvPaymentAmount = (TextView)findViewById(R.id.TVFundPaymentAmount);
		tvPaymentAmount.setText(String.format("%,d원", mPaymentAmount));
		TextView tvStore = (TextView)findViewById(R.id.TVFundStore);
		tvStore.setText(mFund.getStore());
		TextView tvMemo = (TextView)findViewById(R.id.TVFundMemo);
		tvMemo.setText(mFund.getMemo());
	}
	
	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("펀드 내역");
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mFund = (AssetsFundItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		mListItems = DBMgr.getAssetsChangeStateItems(mFund.getID());
	}
	
//	private void setPorgress() {
//		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
//		int monthPeriodTerm = mFund.getMonthPeriodTerm();
//		int monthProgressCount = mFund.getMonthProcessCount() ;
//		
//		progress.setMax(monthPeriodTerm);
//		progress.setProgress(monthProgressCount);
//		
//		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
//		tvProgrss.setText(String.format("진행(%d/%d)", mFund.getMonthProcessCount(), mFund.getMonthPeriodTerm()));
//		tvProgrss.invalidate();
//	}

//	protected void updateDeleteBtnText() {
//		if (mFund.isOverExpirationDate()) {
//			((Button)findViewById(R.id.BtnStateDelete)).setText("해약");
//		}
//		else {
//			((Button)findViewById(R.id.BtnStateDelete)).setText("만기");
//		}
//		
//	}

	@Override
	public void onEditBtnClick() {
		Intent intent = new Intent(this, InputAssetsFundLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mFund.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
//	protected void onDeleteBtnClick() {
//		Intent intent = new Intent(this, InputIncomeFromAssetsLayout.class);
//		intent.putExtra(MsgDef.ExtraNames.ITEM, createIncomeItem());
//		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ITEM);
//	}
//	
//	protected IncomeItem createIncomeItem() {
//		IncomeItem income = new IncomeItem();
//		
//		ArrayList<Category> categories = DBMgr.getCategory(IncomeItem.TYPE, ItemDef.ExtendAssets.NONE);
//		int categorySize = categories.size();
//		
//		for (int index = 0; index < categorySize; index++) {
//			Category category = categories.get(index); 
//			if (category.getName().compareTo(mFund.getCategory().getName()) == 0) {
//				category.setExtndType(ItemDef.ExtendAssets.Fund);
//				income.setCategory(category);
//				break;
//			}
//		}
//		
//		income.setAmount(mFund.getTotalAmount());
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
//		mFund.setState(FinanceItem.STATE_COMPLEATE);
//		DBMgr.updateFinanceItem(mFund);
//		DBMgr.addIncomeFromAssets(incomeID, mFund.getID());
//		finish();
//	}
//	
	protected void addFundPaymentList() {
		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLFundPayment);
		llPayment.removeAllViewsInLayout();
		mPaymentAmount = 0;
		
		int size = mListItems.size();
		for (int index = 0; index < size; index++) {
			AssetsChangeItem item = mListItems.get(index);
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.report_detail_savings_payment, null);
			((TextView)llMember.findViewById(R.id.TVPaymentDate)).setText(item.getChangeDateString());
			((TextView)llMember.findViewById(R.id.TVPaymentAmount)).setText(String.format("%,d원", item.getChangeAmount()));
			
			mPaymentAmount += item.getChangeAmount();
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			llPayment.addView(llMember, params);
		}
	}
//	
//	
//	protected void setListViewText(FinanceItem financeItem, View convertView) {
//    	
//		((TextView)convertView.findViewById(R.id.TVPaymentDate)).setText(financeItem.getCreateDateString());
//		((TextView)convertView.findViewById(R.id.TVPaymentAmount)).setText(String.format("%,d원", financeItem.getAmount()));
//	
//	}
//	
//	public class ReportFundPaymentItemAdapter extends ArrayAdapter<FinanceItem> {
//		private int mResource;
//    	private LayoutInflater mInflater;
//
//		public ReportFundPaymentItemAdapter(Context context, int resource,
//				 List<FinanceItem> objects) {
//			super(context, resource, objects);
//			this.mResource = resource;
//			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			
//			AssetsFundItem item = (AssetsFundItem)getItem(position);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_EDIT_ITEM) {
    		if (resultCode == RESULT_OK) {
    			long changeAmount = data.getLongExtra(MsgDef.ExtraNames.CHANGE_AMOUNT, 0L);
    			int id = data.getIntExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, -1);
    			if (id != -1) {
    				mFund = (AssetsFundItem) DBMgr.getItem(AssetsItem.TYPE, id);
    				mFund.setAmount(mFund.getAmount() + changeAmount);
    				DBMgr.updateAmountFinanceItem(mFund);
    				mListItems = DBMgr.getAssetsChangeStateItems(mFund.getID());
    				updateChildView();
    			}
    		}
    	}
		else if (requestCode == MsgDef.ActRequest.ACT_ADD_ITEM) {
			if (resultCode == RESULT_OK) {

//				mFund = (AssetsStockItem) DBMgr.getItem(mFund.getType(), mFund.getID());
//				mListItems = DBMgr.getAssetsChangeStateItems(mFund.getID());
//				updateChildView();
//				
//				if (mFund.getTotalCount() <= 0L) {
//					completionAssets(data.getIntExtra(MsgDef.ExtraNames.ADD_ITEM_ID, -1));
//				}
//				
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}