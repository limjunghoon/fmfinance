package com.fletamuto.sptb;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportIncomeLayout extends ReportBaseLayout {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        if (getItemsFromDB(IncomeItem.TYPE) == false) {
//        	return;
//        }
//        
//        setListAdapter(R.layout.report_list_income);
    }
    
    @Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	startEditInputActivity(InputIncomeLayout.class, item.getID());
	}
    
//    
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
//    	startEditInputActivity(InputIncomeLayout.class, item.getID());
//    	super.onListItemClick(l, v, position, id);
//    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	IncomeItem item = (IncomeItem)financeItem;
    	((TextView)convertView.findViewById(R.id.TVIncomeReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVIncomeReportListMemo)).setText("메모 : " + item.getMemo());
		((TextView)convertView.findViewById(R.id.TVIncomeReportListCategory)).setText("분류 : " + item.getCategory().getName());
	}
    
    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportIncomeDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
    }
    
    @Override
	protected int deleteItemToDB(int id) {
		return DBMgr.deleteItem(IncomeItem.TYPE, id);
	}


	@Override
	protected FinanceItem getItemInstance(int id) {
		return DBMgr.getItem(IncomeItem.TYPE, id);
	}


	@Override
	protected int getItemType() {
		// TODO Auto-generated method stub
		return IncomeItem.TYPE;
	}

	@Override
	protected void onClickAddButton() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_income;
	}

	protected void updateListItem() {
		mListItems = mItems;
	}
	
	@Override
	protected int getLayoutResources(FinanceItem item) {
		return getAdapterResource();
	}
}