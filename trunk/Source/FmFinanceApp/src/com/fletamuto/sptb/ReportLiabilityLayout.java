package com.fletamuto.sptb;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportLiabilityLayout extends ReportBaseLayout {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getItemsFromDB(LiabilityItem.TYPE) == false) {
        	return;
        }
        
        setListAdapter(R.layout.report_list_liability);
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	startEditInputActivity(InputLiabilityLayout.class, item.getId());
    	super.onListItemClick(l, v, position, id);
    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	LiabilityItem item = (LiabilityItem)financeItem;
    	((TextView)convertView.findViewById(R.id.TVLiabilityReportListTitle)).setText("���� : " + item.getTitle());
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListDate)).setText("��¥ : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("�ݾ� : %,d��", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setText("�з� : " + item.getCategory().getName());
	}
    
    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportLiabilityDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
    }
    
    @Override
	protected int deleteItemToDB(int id) {
		return DBMgr.deleteItem(LiabilityItem.TYPE, id);
	}

	@Override
	protected FinanceItem getItemInstance(int id) {
		return DBMgr.getItem(LiabilityItem.TYPE, id);
	}

}