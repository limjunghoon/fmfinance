package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsDepositItem;
import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.AssetsInsuranceItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;

public class ReportAssetsLayout extends ReportSeparationLayout {
	public static final int ACT_ADD_ASSETS = MsgDef.ActRequest.ACT_ADD_ASSETS;
	
	private long mTotalAmount = 0L;
	private long mTotalAccountAmount = 0L;
	private ArrayList<AccountItem> mArrDemand;
	protected ReportAccountItemAdapter mAccountItemAdapter = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    public void initialize() {
    	super.initialize();
    }
    
	protected void updateChildView() {
		TextView tvTatalAmount = (TextView) findViewById(R.id.TVAmount);
		tvTatalAmount.setText(String.format("자산 : %,d원", mTotalAmount));
	}
    
    @Override
    protected void setTitleBtn() {
    	setTitle("자산 목록");
    	super.setTitleBtn();
    }
    
    @Override
	protected void getSeparationData() {
    	mArrDemand = DBMgr.getAccountAllItems();
    	mTotalAccountAmount = 0L;
		int size = mArrDemand.size();
		for (int index = 0; index < size; index++) {
			AccountItem account = mArrDemand.get(index);
			
			if (account.getType() == AccountItem.TIME_DEPOSIT || account.getType() == AccountItem.SAVINGS) {
				mArrDemand.remove(index);
				index--; size--;
			}
			
			mTotalAccountAmount += account.getBalance();
		}
		
		super.getSeparationData();
	}
    
    @Override
    protected void setAdapterList() {
    	if (isSelectedAccount()) {
    		setAccountAdapterList();
		}
    	else {
    		super.setAdapterList();
    	}
    }
    
    protected boolean isSelectedAccount() {
    	return (mSeparationAdapter.getItem(getSelectedSection()).getType() == ItemDef.FinanceDef.ACCOUNT);
    }
    
    @Override
    protected ArrayList<CategoryAmount> getListItems() {
    	ArrayList<CategoryAmount> listItems = new ArrayList<CategoryAmount>();
    	Collection<CategoryAmount> categoryAmountItems = mCategoryItems.values();
    	
    	if (mArrDemand.size() != 0) {
    		CategoryAmount categoryAmount = new CategoryAmount(ItemDef.FinanceDef.ACCOUNT);
    		categoryAmount.set(-1, "보통예금", mTotalAccountAmount);
    		categoryAmount.setCount(mArrDemand.size());
    		listItems.add(categoryAmount);
    	}
    	
    	for (CategoryAmount iterator:categoryAmountItems) {
			listItems.add(iterator);
		}
    	
    	return listItems;
    }
    
    protected void setAccountAdapterList() {
        
    	mAccountItemAdapter = new ReportAccountItemAdapter(this, R.layout.report_list_account, mArrDemand);
    	mMemberList.setAdapter(mAccountItemAdapter);
    	
    	mMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(ReportAssetsLayout.this, DetailAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, mAccountItemAdapter.getItem(position));
				startActivity(intent);
			}
		});
    }
    
    @Override
	protected void getData() {
		if (!isSelectedAccount()) {
			super.getData();
		}
		else {
			((TextView)findViewById(R.id.TVListTitleRight)).setText(String.format("총 금액 %,d원", mTotalAccountAmount));
		}
	}
    
	@Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	Intent intent = null;
    	
    	if (item.getExtendType() == ItemDef.ExtendAssets.DEPOSIT) {
    		intent = new Intent(this, DetailAssetsDepositLayout.class);
    	}
    	else if (item.getExtendType() == ItemDef.ExtendAssets.SAVINGS) {
    		intent = new Intent(this, DetailAssetsSavingsLayout.class);
    	}
    	else if (item.getExtendType() == ItemDef.ExtendAssets.STOCK) {
    		intent = new Intent(this, StateAssetsStockLayout.class);
    	}
    	else {
    		intent = new Intent(this, StateAssetsDefaultLayout.class);
    	}
    	
    	intent.putExtra(MsgDef.ExtraNames.ITEM, item);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_STATE_VIEW);
    	//startEditInputActivity(InputAssetsLayout.class, item.getID());
	}
    
    protected void setListViewText(FinanceItem item, View convertView) {
    	int extendType = item.getExtendType();
    	
    	if (extendType == ItemDef.ExtendAssets.DEPOSIT) {
    		setListViewTextDeposit((AssetsDepositItem)item, convertView);
		}
    	else if (extendType == ItemDef.ExtendAssets.SAVINGS) {
    		setListViewTextSavings((AssetsSavingsItem)item, convertView);
		}
    	else if (extendType == ItemDef.ExtendAssets.STOCK) {
    		setListViewTextStock((AssetsStockItem)item, convertView);
		}
    	else if (extendType == ItemDef.ExtendAssets.FUND) {
    		setListViewTextFund((AssetsFundItem)item, convertView);
		}
    	else if (extendType == ItemDef.ExtendAssets.ENDOWMENT_MORTGAGE) {
    		setListViewTextInsurance((AssetsInsuranceItem)item, convertView);
		}
    	else {
    		setListViewTextDefault(item, convertView);
    	}
	}
    
    private void setListViewTextInsurance(AssetsInsuranceItem insurance,
			View convertView) {
    	((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(insurance.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(insurance.getCreateDateString() + "~" + insurance.getExpriyDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d원", insurance.getAmount()));
		
//		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceTitle)).setText("제목 : " + insurance.getTitle());
//		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceCreateDate)).setText("개설일자 : " + insurance.getCreateDateString());
//		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceExpiryDate)).setText("만료일자 : " + insurance.getExpriyDateString());
//		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceAmount)).setText(String.format("금액 : %,d원 (%d/%d)", insurance.getAmount(), 1, insurance.getMonthPeriodTerm()));
//		((TextView)convertView.findViewById(R.id.TVAssetsInsurancePayment)).setText(String.format("납입금 : %,d원", insurance.getPayment()));
//		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceMemo)).setText("메모 : " + insurance.getMemo());
	}

	private void setListViewTextFund(AssetsFundItem item, View convertView) {
		((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(item.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(item.getCreateDateString() + "~" + item.getExpriyDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d원", item.getAmount()));
//    	((TextView)convertView.findViewById(R.id.TVAssetsFundTitle)).setText("제목 : " + item.getTitle());
//		((TextView)convertView.findViewById(R.id.TVAssetsFundAmount)).setText(String.format("평가금액 :  %,d원", item.getAmount()));
//		
//		((TextView)convertView.findViewById(R.id.TVAssetsFundMeanPrice)).setText(String.format("평균구입 금액 : %,d원", DBMgr.getAssetsMeanPrice(item.getID())));
//		((TextView)convertView.findViewById(R.id.TVAssetsFundMemo)).setText("메모 : " + item.getMemo());
//		
//		// 속도 개선 필요
//		long purchasePrice = DBMgr.getAssetsPurchasePrice(item.getID());
//		((TextView)convertView.findViewById(R.id.TVAssetsFundRevenue)).setText(String.format("수익율 : %s", Revenue.getString(purchasePrice, item.getAmount())));
	}

	protected void setListViewTextDeposit(AssetsDepositItem deposit, View convertView) {
		((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(deposit.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(deposit.getCreateDateString() + "~" + deposit.getExpriyDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d원", deposit.getAmount()));
		
//		if (deposit.getAccount() != null) {
//			((TextView)convertView.findViewById(R.id.TVAssetsDepositBankingName)).setText("은행 : " + deposit.getAccount().getCompany().getName());
//		}
//		((TextView)convertView.findViewById(R.id.TVAssetsDepositTitle)).setText("제목 : " + deposit.getTitle());
//		((TextView)convertView.findViewById(R.id.TVAssetsDepositCreateDate)).setText("개설일자 : " + deposit.getCreateDateString());
//		((TextView)convertView.findViewById(R.id.TVAssetsDepositExpiryDate)).setText("만료일자 : " + deposit.getExpriyDateString());
//		((TextView)convertView.findViewById(R.id.TVAssetsDepositAmount)).setText(String.format("금액 : %,d원", deposit.getAmount()));
//		((TextView)convertView.findViewById(R.id.TVAssetsDepositRate)).setText("이율 : "+ deposit.getRate());
//		((TextView)convertView.findViewById(R.id.TVAssetsDepositMemo)).setText("메모 : " + deposit.getMemo());
	}
    
    protected void setListViewTextSavings(AssetsSavingsItem savings, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(savings.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(savings.getCreateDateString() + "~" + savings.getExpriyDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d원", savings.getAmount()));
//		if (savings.getAccount() != null) {
//			((TextView)convertView.findViewById(R.id.TVAssetsSavingsBankingName)).setText("은행 : " + savings.getAccount().getCompany().getName());
//		}
//		((TextView)convertView.findViewById(R.id.TVAssetsSavingsTitle)).setText("제목 : " + savings.getTitle());
//		((TextView)convertView.findViewById(R.id.TVAssetsSavingsCreateDate)).setText("개설일자 : " + savings.getCreateDateString());
//		((TextView)convertView.findViewById(R.id.TVAssetsSavingsExpiryDate)).setText("만료일자 : " + savings.getExpriyDateString());
//		((TextView)convertView.findViewById(R.id.TVAssetsSavingsAmount)).setText(String.format("금액 : %,d원 (%d/%d)", savings.getAmount(), savings.getMonthProcessCount(), savings.getMonthPeriodTerm()));
//		((TextView)convertView.findViewById(R.id.TVAssetsSavingsPayment)).setText(String.format("납입금 : %,d원", savings.getPayment()));
//		((TextView)convertView.findViewById(R.id.TVAssetsSavingsRate)).setText("이율 : "+ savings.getRate());
//		((TextView)convertView.findViewById(R.id.TVAssetsSavingsMemo)).setText("메모 : " + savings.getMemo());
    }
    
    protected void setListViewTextStock(AssetsStockItem stock, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(stock.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(stock.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d원", stock.getAmount()));
//		((TextView)convertView.findViewById(R.id.TVAssetsStockTitle)).setText("종목 : " + stock.getTitle());
//		((TextView)convertView.findViewById(R.id.TVAssetsStockAmount)).setText(String.format("평가금액 :  %,d원", stock.getAmount()));
//		((TextView)convertView.findViewById(R.id.TVAssetsStockMeanPrice)).setText(String.format("평균구입 금액 : %,d원", DBMgr.getAssetsMeanPrice(stock.getID())));
//		((TextView)convertView.findViewById(R.id.TVAssetsStockCurrentPrice)).setText(String.format("현재가 : %,d원", stock.getPeresentPrice()));
//		((TextView)convertView.findViewById(R.id.TVAssetsStockCount)).setText("수량 : " + stock.getTotalCount() + "주");
//		((TextView)convertView.findViewById(R.id.TVAssetsStockMemo)).setText("메모 : " + stock.getMemo());
//		
//		// 속도 개선 필요
//		long purchasePrice = DBMgr.getAssetsPurchasePrice(stock.getID());
//		((TextView)convertView.findViewById(R.id.TVAssetsStockRevenue)).setText(String.format("수익율 : %s", Revenue.getString(purchasePrice, stock.getAmount())));
    }
    
    protected void setListViewTextDefault(FinanceItem item, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVSectionLine1Left)).setText(item.getTitle());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Left)).setText(item.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVSectionLine2Right)).setText(String.format("%,d원", item.getAmount()));
//    	((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText("제목 : " + item.getTitle());
//		((TextView)convertView.findViewById(R.id.TVAssetsReportListDate)).setText("날짜 : " + item.getCreateDateString());			
//		((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
//		//String categoryText = String.format(item.getCategory().getName());
//		//((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setText(categoryText);
//		((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setVisibility(View.GONE);
//		((TextView)convertView.findViewById(R.id.TVAssetsReportListMemo)).setText(String.format("메모 : %s", item.getMemo()));
//		
//		// 속도 개선 필요
//		long purchasePrice = DBMgr.getAssetsPurchasePrice(item.getID());
//		((TextView)convertView.findViewById(R.id.TVAssetsReportListRevenue)).setText(String.format("수익율 : %s", Revenue.getString(purchasePrice, item.getAmount())));
    }

	protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportAssetsDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
		btnDelete.setVisibility(View.GONE);
    }

	@Override
	protected int getItemType() {
		return AssetsItem.TYPE;
	}

	@Override
	protected void onClickAddButton() {
		Intent intent = null;
		Category category = DBMgr.getCategoryFromID(AssetsItem.TYPE, getSelectedCategoryID());
		if (category == null) return;
		
		if (category.getExtndType() == ItemDef.ExtendAssets.DEPOSIT) {
			intent = new Intent(this, InputAssetsDepositLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.SAVINGS) {
			intent = new Intent(this, InputAssetsSavingsLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.STOCK) {
			intent = new Intent(this, InputAssetsStockLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.FUND) {
			intent = new Intent(this, InputAssetsFundLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.ENDOWMENT_MORTGAGE) {
			intent = new Intent(this, InputAssetsInsuranceLayout.class);
		}
		else {
			intent = new Intent(this, InputAssetsLayout.class);
		}
    	
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ASSETS);
	}

	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_assets_section;
	}

	protected void updateListItem() {
		int itemSize = mItems.size();
		mTotalAmount = 0L;
		
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = mItems.get(index);
			
			if (item.getState() == FinanceItem.STATE_COMPLEATE) {
				continue;
			}
			
			mTotalAmount += item.getAmount();
			mListItems.add(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_ASSETS) {
			if (resultCode == RESULT_OK) {
				
    		}
    	}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected int getLayoutResources(FinanceItem item) {
		if (item.getExtendType() == ItemDef.ExtendAssets.DEPOSIT) {
			return R.layout.report_list_assets_deposit;
		}
		else if (item.getExtendType() == ItemDef.ExtendAssets.SAVINGS) {
			return R.layout.report_list_assets_saving;
		}
		else if (item.getExtendType() == ItemDef.ExtendAssets.STOCK) {
			return R.layout.report_list_assets_stock;
		}
		else if (item.getExtendType() == ItemDef.ExtendAssets.FUND) {
			return R.layout.report_list_assets_fund;
		}
		else if (item.getExtendType() == ItemDef.ExtendAssets.ENDOWMENT_MORTGAGE) {
			return R.layout.report_list_assets_insurance;
		}
		else {
			return getAdapterResource();
		}
	}
	
	public class ReportAccountItemAdapter extends ArrayAdapter<AccountItem> {
		private int mResource;
    	private LayoutInflater mInflater;

		public ReportAccountItemAdapter(Context context, int resource,
				 List<AccountItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			AccountItem item = (AccountItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			setAccountListViewText(item, convertView);			
			
			return convertView;
		}
		
    }
	
	protected void setAccountListViewText(AccountItem account, View convertView) {
		
		((TextView)convertView.findViewById(R.id.TVAccountReportListNumer)).setText(account.getNumber());			
		((TextView)convertView.findViewById(R.id.TVAccountReportListBalance)).setText(String.format("%,d원", account.getBalance()));
		((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setText(account.getTypeName());
		((TextView)convertView.findViewById(R.id.TVAccountReportListInstitution)).setText(account.getCompany().getName());
	}
	
	
}