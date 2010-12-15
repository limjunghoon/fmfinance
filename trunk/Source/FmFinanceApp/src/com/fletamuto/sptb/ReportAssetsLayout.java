package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.FinanceItem;

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
    	
    	Intent intent = new Intent(this, StateAssetsDefaultLayout.class);
    	intent.putExtra(MsgDef.ExtraNames.ITEM, item);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_STATE_VIEW);
    	//startEditInputActivity(InputAssetsLayout.class, item.getID());
	}

    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	AssetsItem item = (AssetsItem)financeItem;
		
    	((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText("제목 : " + item.getTitle());
		((TextView)convertView.findViewById(R.id.TVAssetsReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		//String categoryText = String.format(item.getCategory().getName());
		//((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setText(categoryText);
		((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setVisibility(View.GONE);
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
		// TODO Auto-generated method stub
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
}