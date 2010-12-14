package com.fletamuto.sptb;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;

public class ReportLiabilityLayout extends ReportBaseLayout {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getItemsFromDB(LiabilityItem.TYPE) == false) {
//        	return;
//        }
//        
//        setListAdapter(R.layout.report_list_liability);
    }
    
    @Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	startEditInputActivity(InputLiabilityLayout.class, item.getID());
	}
    
//    
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
//    	startEditInputActivity(InputLiabilityLayout.class, item.getID());
//    	super.onListItemClick(l, v, position, id);
//    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	LiabilityItem item = (LiabilityItem)financeItem;
    	((TextView)convertView.findViewById(R.id.TVLiabilityReportListTitle)).setText("제목 : " + item.getTitle());
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setText("분류 : " + item.getCategory().getName());
	}
    
    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportLiabilityDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
    }

	@Override
	protected int getItemType() {
		// TODO Auto-generated method stub
		return LiabilityItem.TYPE;
	}

	@Override
	protected void onClickAddButton() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_liability;
	}
	
	protected void updateListItem() {
		mListItems = mItems;
	}

}