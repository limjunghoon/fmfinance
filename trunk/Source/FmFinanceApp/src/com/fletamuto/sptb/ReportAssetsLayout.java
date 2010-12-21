package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsDepositItem;
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
		tvTatalAmount.setText(String.format("�ڻ� : %,d��", mTotalAmount));
	}
    
    @Override
    protected void setTitleBtn() {
    	setTitle("�ڻ� ���");
    	
    	super.setTitleBtn();
    }
    
	@Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	
    	Intent intent = new Intent(this, StateAssetsDefaultLayout.class);
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
    	else {
    		setListViewTextDefault(item, convertView);
    	}
	}
    
    protected void setListViewTextDeposit(AssetsDepositItem deposit, View convertView) {
		if (deposit.getAccount() != null) {
			((TextView)convertView.findViewById(R.id.TVAssetsDepositBankingName)).setText("���� : " + deposit.getAccount().getCompany().getName());
		}
		((TextView)convertView.findViewById(R.id.TVAssetsDepositTitle)).setText("���� : " + deposit.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsDepositCreateDate)).setText("�������� : " + deposit.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsDepositExpiryDate)).setText("�������� : " + deposit.getExpriyDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsDepositAmount)).setText(String.format("�ݾ� : %,d��", deposit.getAmount()));
		((TextView)convertView.findViewById(R.id.TVAssetsDepositRate)).setText("���� : "+ deposit.getRate());
		((TextView)convertView.findViewById(R.id.TVAssetsDepositMemo)).setText("�޸� : " + deposit.getMemo());
	}
    
    protected void setListViewTextSavings(AssetsSavingsItem savings, View convertView) {
		if (savings.getAccount() != null) {
			((TextView)convertView.findViewById(R.id.TVAssetsSavingsBankingName)).setText("���� : " + savings.getAccount().getCompany().getName());
		}
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsTitle)).setText("���� : " + savings.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsCreateDate)).setText("�������� : " + savings.getCreateDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsExpiryDate)).setText("�������� : " + savings.getExpriyDateString());
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsAmount)).setText(String.format("�ݾ� : %,d�� (%d/%d)", savings.getAmount(), 1, savings.getMonthPeriodTerm()));
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsPayment)).setText(String.format("���Ա� : %,d��", savings.getPayment()));
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsRate)).setText("���� : "+ savings.getRate());
		((TextView)convertView.findViewById(R.id.TVAssetsSavingsMemo)).setText("�޸� : " + savings.getMemo());
    }
    
    protected void setListViewTextStock(AssetsStockItem stock, View convertView) {
		((TextView)convertView.findViewById(R.id.TVAssetsStockTitle)).setText("���� : " + stock.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsStockAmount)).setText(String.format("�򰡱ݾ� :  %,d��", stock.getAmount()));
		((TextView)convertView.findViewById(R.id.TVAssetsStockCurrentPrice)).setText(String.format("���簡 : %,d��", stock.getPeresentPrice()));
		((TextView)convertView.findViewById(R.id.TVAssetsStockCount)).setText("���� : " + stock.getTotalCount() + "��");
		((TextView)convertView.findViewById(R.id.TVAssetsStockMemo)).setText("�޸� : " + stock.getMemo());
		
		// �ӵ� ���� �ʿ�
		long purchasePrice = DBMgr.getAssetsPurchasePrice(stock.getID());
		((TextView)convertView.findViewById(R.id.TVAssetsStockRevenue)).setText(String.format("������ : %s", Revenue.getString(purchasePrice, stock.getAmount())));
    }
    
    protected void setListViewTextDefault(FinanceItem item, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText("���� : " + item.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsReportListDate)).setText("��¥ : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("�ݾ� : %,d��", item.getAmount()));
		//String categoryText = String.format(item.getCategory().getName());
		//((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setText(categoryText);
		((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setVisibility(View.GONE);
		((TextView)convertView.findViewById(R.id.TVAssetsReportListMemo)).setText(String.format("�޸� : %s", item.getMemo()));
		
		// �ӵ� ���� �ʿ�
		long purchasePrice = DBMgr.getAssetsPurchasePrice(item.getID());
		((TextView)convertView.findViewById(R.id.TVAssetsReportListRevenue)).setText(String.format("������ : %s", Revenue.getString(purchasePrice, item.getAmount())));
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
				getDate();
		        setAdapterList();
		        updateChildView();
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
		else {
			return getAdapterResource();
		}
	}
}