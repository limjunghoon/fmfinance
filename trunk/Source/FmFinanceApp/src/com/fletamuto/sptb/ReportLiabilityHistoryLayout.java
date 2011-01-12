package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportLiabilityHistoryLayout extends ReportBaseLayout {
    private LiabilityItem mItem;
//	private long mPurchasePrice = 0L;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    public void initialize() {
    	mItem = (LiabilityItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
 //   	mPurchasePrice = DBMgr.getLiabilityPurchasePrice(mItem.getID());
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
    	//startEditInputActivity(InputLiabilityLayout.class, item.getID());
	}

    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	LiabilityItem item = (LiabilityItem)financeItem;
		
    	//((TextView)convertView.findViewById(R.id.TVLiabilityReportListTitle)).setText("제목 : " + item.getTitle());
    	((TextView)convertView.findViewById(R.id.TVLiabilityReportListTitle)).setVisibility(View.GONE);
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setVisibility(View.GONE);
		
//		if (item.getMemo() == null) {
//			((TextView)convertView.findViewById(R.id.TVLiabilityReportListMemo)).setVisibility(View.GONE);
//		}
//		else {
//			((TextView)convertView.findViewById(R.id.TVLiabilityReportListMemo)).setText(String.format("메모 : %s", item.getMemo()));
//		}
//		
//		((TextView)convertView.findViewById(R.id.TVLiabilityReportListRevenue)).setText(String.format("수익율 : %s", Revenue.getString(mPurchasePrice, item.getAmount())));
		
	}
    
    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportLiabilityDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
		btnDelete.setVisibility(View.GONE);
    }
    
    protected void getData() {
    	mItems = DBMgr.getLiabilityStateItems(mItem.getID());
		
		mListItems.clear();
		updateListItem();
	}

	@Override
	protected int getItemType() {
		// TODO Auto-generated method stub
		return LiabilityItem.TYPE;
	}

	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_liability;
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