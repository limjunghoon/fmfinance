package com.fletamuto.sptb;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.Revenue;

public class ReportAssetsHistoryLayout extends ReportBaseLayout {
    private AssetsItem mItem;
	private long mPurchasePrice = 0L;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    public void initialize() {
    	mItem = (AssetsItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
    	mPurchasePrice = DBMgr.getAssetsPurchasePrice(mItem.getID());
    	super.initialize();
    }

    
	protected void updateChildView() {
	}
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	setTitle(mItem.getCategory().getName() + " ��������");
    	setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.INVISIBLE);
    }
    
	@Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
   // 	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	//startEditInputActivity(InputAssetsLayout.class, item.getID());
	}

    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	AssetsItem item = (AssetsItem)financeItem;
    	
    	if (mItem.getExtendType() == AssetsStockItem.EXEND_TYPE) {
    		setListViewTextStock(item, convertView);
		}
    	else {
    		//((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText("���� : " + item.getTitle());
        	((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setVisibility(View.GONE);
    		((TextView)convertView.findViewById(R.id.TVAssetsReportListDate)).setText("��¥ : " + item.getCreateDateString());			
    		((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("�ݾ� : %,d��", item.getAmount()));
    		((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setVisibility(View.GONE);
    		
    		if (item.getMemo() == null) {
    			((TextView)convertView.findViewById(R.id.TVAssetsReportListMemo)).setVisibility(View.GONE);
    		}
    		else {
    			((TextView)convertView.findViewById(R.id.TVAssetsReportListMemo)).setText(String.format("�޸� : %s", item.getMemo()));
    		}
    		
    		((TextView)convertView.findViewById(R.id.TVAssetsReportListRevenue)).setText(String.format("������ : %s", Revenue.getString(mPurchasePrice, item.getAmount())));
    	}
		
    	
	}
    
    protected void setListViewTextStock(AssetsItem stock, View convertView) {
    	TextView tvChangeDate = (TextView) convertView.findViewById(R.id.TVAssetsStockChageDate);
    	tvChangeDate.setVisibility(View.VISIBLE);
    	tvChangeDate.setText("��¥ : " + stock.getCreateDateString());
    	TextView tvTitle = ((TextView)convertView.findViewById(R.id.TVAssetsStockTitle)); 
    	tvTitle.setText((stock.getState() == AssetsStockItem.SELL) ? "�ŵ�" : "�ż�");
    	tvTitle.setTextColor((stock.getState() == AssetsStockItem.SELL) ? Color.BLUE : Color.RED);
		((TextView)convertView.findViewById(R.id.TVAssetsStockAmount)).setText(String.format("�ݾ� :  %,d��", stock.getTotalAmount()));
		((TextView)convertView.findViewById(R.id.TVAssetsStockMeanPrice)).setText(String.format("�ִ� �ݾ� : %,d��", stock.getAmount()));
		((TextView)convertView.findViewById(R.id.TVAssetsStockCount)).setText("���� : " + stock.getCount() + "��");
		((TextView)convertView.findViewById(R.id.TVAssetsStockMemo)).setText("�޸� : " + stock.getMemo());
		((TextView)convertView.findViewById(R.id.TVAssetsStockCurrentPrice)).setVisibility(View.GONE);
		((TextView)convertView.findViewById(R.id.TVAssetsStockRevenue)).setVisibility(View.GONE);
    }
    
    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportAssetsDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
		btnDelete.setVisibility(View.GONE);
    }
    
    protected void getDate() {
    	mItems = DBMgr.getAssetsStateItems(mItem.getID());
		
		mListItems.clear();
		updateListItem();
	}

	@Override
	protected int getItemType() {
		// TODO Auto-generated method stub
		return AssetsItem.TYPE;
	}

	@Override
	protected int getAdapterResource() {
		if (mItem.getExtendType() == AssetsStockItem.EXEND_TYPE) {
			return R.layout.report_list_assets_stock;
		}
		else {
			return R.layout.report_list_assets;
		}
		
	}

	protected void updateListItem() {
		mListItems =mItems;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onClickAddButton() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getLayoutResources(FinanceItem item) {
		return getAdapterResource();
	}
}