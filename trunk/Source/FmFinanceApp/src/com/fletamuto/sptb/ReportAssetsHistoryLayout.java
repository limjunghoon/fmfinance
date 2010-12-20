package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.FinanceItem;
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
    	
    	setTitle(mItem.getCategory().getName() + " 변동사항");
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
		
    	//((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText("제목 : " + item.getTitle());
    	((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setVisibility(View.GONE);
		((TextView)convertView.findViewById(R.id.TVAssetsReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setVisibility(View.GONE);
		
		if (item.getMemo() == null) {
			((TextView)convertView.findViewById(R.id.TVAssetsReportListMemo)).setVisibility(View.GONE);
		}
		else {
			((TextView)convertView.findViewById(R.id.TVAssetsReportListMemo)).setText(String.format("메모 : %s", item.getMemo()));
		}
		
		((TextView)convertView.findViewById(R.id.TVAssetsReportListRevenue)).setText(String.format("수익율 : %s", Revenue.getString(mPurchasePrice, item.getAmount())));
		
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
		return R.layout.report_list_assets;
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