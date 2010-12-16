package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;

public class ReportLiabilityLayout extends ReportBaseLayout {
	private long mTotalAmount = 0L;
	public static final int ACT_ADD_LIABLITY = MsgDef.ActRequest.ACT_ADD_LIABLITY;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.TVAmount).setVisibility(View.VISIBLE);
    }
    
    protected void updateChildView() {
		TextView tvTatalAmount = (TextView) findViewById(R.id.TVAmount);
		tvTatalAmount.setText(String.format("부채 : %,d원", mTotalAmount));
	}
    
    @Override
    protected void setTitleBtn() {
    	setTitle("부채 목록");
    	
    	super.setTitleBtn();
    }
    
    @Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	
    	Intent intent = new Intent(this, StateLiabilityDefaultLayout.class);
    	intent.putExtra(MsgDef.ExtraNames.ITEM, item);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_STATE_VIEW);
	}
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	LiabilityItem item = (LiabilityItem)financeItem;
    	((TextView)convertView.findViewById(R.id.TVLiabilityReportListTitle)).setText("제목 : " + item.getTitle());
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		
//		((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setText("분류 : " + item.getCategory().getName());
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setVisibility(View.GONE);
	}
    
    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportLiabilityDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
		btnDelete.setVisibility(View.GONE);
    }

	@Override
	protected int getItemType() {
		// TODO Auto-generated method stub
		return LiabilityItem.TYPE;
	}

	@Override
	protected void onClickAddButton() {
		Intent intent = new Intent(this, SelectCategoryLiabilityLayout.class);
		startActivityForResult(intent, ACT_ADD_LIABLITY);
	}
	
	protected void updateListItem() {
		int itemSize = mItems.size();
		int itemCategoryID = -1;
		mTotalAmount = 0L;
		
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = mItems.get(index);
			if (item.getCategory().getID() != itemCategoryID) {
				itemCategoryID = item.getCategory().getID();
				LiabilityItem separator = new LiabilityItem();
				separator.setSeparatorTitle(item.getCategory().getName());
				mListItems.add(separator);
			}
			
			mTotalAmount += item.getAmount();
			mListItems.add(item);
		}
	}
	
	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_liability;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_LIABLITY) {
			if (resultCode == RESULT_OK) {
				getDate();
		        setAdapterList();
		        updateChildView();
    		}
    	}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

}