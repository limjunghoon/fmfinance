package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsDepositItem;
import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.AssetsInsuranceItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.Revenue;

public class ReportAssetsLayout extends ReportBaseLayout {
	private long mTotalAmount = 0L;
	public static final int ACT_ADD_ASSETS = MsgDef.ActRequest.ACT_ADD_ASSETS;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        findViewById(R.id.TVAmount).setVisibility(View.VISIBLE);
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
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	Intent intent = null;
    	
    	if (item.getExtendType() == ItemDef.ExtendAssets.DEPOSIT) {
    		intent = new Intent(this, StateAssetsDepositLayout.class);
    	}
    	else if (item.getExtendType() == ItemDef.ExtendAssets.SAVINGS) {
    		intent = new Intent(this, StateAssetsSavingsLayout.class);
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
		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceTitle)).setText("제목 : " + insurance.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceCreateDate)).setText("개설일자 : " + insurance.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceExpiryDate)).setText("만료일자 : " + insurance.getExpriyDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceAmount)).setText(String.format("금액 : %,d원 (%d/%d)", insurance.getAmount(), 1, insurance.getMonthPeriodTerm()));
		((TextView)convertView.findViewById(R.id.TVAssetsInsurancePayment)).setText(String.format("납입금 : %,d원", insurance.getPayment()));
		((TextView)convertView.findViewById(R.id.TVAssetsInsuranceMemo)).setText("메모 : " + insurance.getMemo());
	}

	private void setListViewTextFund(AssetsFundItem item, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVAssetsFundTitle)).setText("제목 : " + item.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsFundAmount)).setText(String.format("평가금액 :  %,d원", item.getAmount()));
		
		((TextView)convertView.findViewById(R.id.TVAssetsFundMeanPrice)).setText(String.format("평균구입 금액 : %,d원", DBMgr.getAssetsMeanPrice(item.getID())));
		((TextView)convertView.findViewById(R.id.TVAssetsFundMemo)).setText("메모 : " + item.getMemo());
		
		// 속도 개선 필요
		long purchasePrice = DBMgr.getAssetsPurchasePrice(item.getID());
		((TextView)convertView.findViewById(R.id.TVAssetsFundRevenue)).setText(String.format("수익율 : %s", Revenue.getString(purchasePrice, item.getAmount())));
	}

	protected void setListViewTextDeposit(AssetsDepositItem deposit, View convertView) {
		if (deposit.getAccount() != null) {
			((TextView)convertView.findViewById(R.id.TVAssetsDepositBankingName)).setText("은행 : " + deposit.getAccount().getCompany().getName());
		}
		((TextView)convertView.findViewById(R.id.TVAssetsDepositTitle)).setText("제목 : " + deposit.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsDepositCreateDate)).setText("개설일자 : " + deposit.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsDepositExpiryDate)).setText("만료일자 : " + deposit.getExpriyDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsDepositAmount)).setText(String.format("금액 : %,d원", deposit.getAmount()));
		((TextView)convertView.findViewById(R.id.TVAssetsDepositRate)).setText("이율 : "+ deposit.getRate());
		((TextView)convertView.findViewById(R.id.TVAssetsDepositMemo)).setText("메모 : " + deposit.getMemo());
	}
    
    protected void setListViewTextSavings(AssetsSavingsItem savings, View convertView) {
		if (savings.getAccount() != null) {
			((TextView)convertView.findViewById(R.id.TVAssetsSavingsBankingName)).setText("은행 : " + savings.getAccount().getCompany().getName());
		}
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsTitle)).setText("제목 : " + savings.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsCreateDate)).setText("개설일자 : " + savings.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsExpiryDate)).setText("만료일자 : " + savings.getExpriyDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsAmount)).setText(String.format("금액 : %,d원 (%d/%d)", savings.getAmount(), savings.getMonthProcessCount(), savings.getMonthPeriodTerm()));
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsPayment)).setText(String.format("납입금 : %,d원", savings.getPayment()));
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsRate)).setText("이율 : "+ savings.getRate());
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsMemo)).setText("메모 : " + savings.getMemo());
    }
    
    protected void setListViewTextStock(AssetsStockItem stock, View convertView) {
		((TextView)convertView.findViewById(R.id.TVAssetsStockTitle)).setText("종목 : " + stock.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsStockAmount)).setText(String.format("평가금액 :  %,d원", stock.getAmount()));
		((TextView)convertView.findViewById(R.id.TVAssetsStockMeanPrice)).setText(String.format("평균구입 금액 : %,d원", DBMgr.getAssetsMeanPrice(stock.getID())));
		((TextView)convertView.findViewById(R.id.TVAssetsStockCurrentPrice)).setText(String.format("현재가 : %,d원", stock.getPeresentPrice()));
		((TextView)convertView.findViewById(R.id.TVAssetsStockCount)).setText("수량 : " + stock.getTotalCount() + "주");
		((TextView)convertView.findViewById(R.id.TVAssetsStockMemo)).setText("메모 : " + stock.getMemo());
		
		// 속도 개선 필요
		long purchasePrice = DBMgr.getAssetsPurchasePrice(stock.getID());
		((TextView)convertView.findViewById(R.id.TVAssetsStockRevenue)).setText(String.format("수익율 : %s", Revenue.getString(purchasePrice, stock.getAmount())));
    }
    
    protected void setListViewTextDefault(FinanceItem item, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText("제목 : " + item.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		//String categoryText = String.format(item.getCategory().getName());
		//((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setText(categoryText);
		((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setVisibility(View.GONE);
		((TextView)convertView.findViewById(R.id.TVAssetsReportListMemo)).setText(String.format("메모 : %s", item.getMemo()));
		
		// 속도 개선 필요
		long purchasePrice = DBMgr.getAssetsPurchasePrice(item.getID());
		((TextView)convertView.findViewById(R.id.TVAssetsReportListRevenue)).setText(String.format("수익율 : %s", Revenue.getString(purchasePrice, item.getAmount())));
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
		Intent intent = new Intent(ReportAssetsLayout.this, SelectCategoryAssetsLayout.class);
		startActivityForResult(intent, ACT_ADD_ASSETS);
	}

	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_assets;
	}

	protected void updateListItem() {
		int itemSize = mItems.size();
		int itemCategoryID = -1;
		mTotalAmount = 0L;
		
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = mItems.get(index);
			
			if (item.getState() == FinanceItem.STATE_COMPLEATE) {
				continue;
			}
			
			if (item.getCategory().getID() != itemCategoryID) {
				itemCategoryID = item.getCategory().getID();
				AssetsItem separator = new AssetsItem();
				separator.setSeparatorTitle(item.getCategory().getName());
				mListItems.add(separator);
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
}