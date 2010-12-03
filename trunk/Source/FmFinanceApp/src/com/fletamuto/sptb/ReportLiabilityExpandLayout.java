package com.fletamuto.sptb;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportLiabilityExpandLayout extends ReportExpandBaseLayout {
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        updateChildView();
    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	LiabilityItem item = (LiabilityItem)financeItem;
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListTitle)).setText(String.format("제목 : %s", item.getTitle()));
		((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setText(String.format("분류 : %s", item.getCategory().getName()));
	}
    
    protected void setDeleteBtnListener(final View convertView, final int id, final int groupPosition, final int childPosition) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportLiabilityDelete);
    	
		btnDelete.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				if (DBMgr.deleteItem(getItemType(), id) == 0 ) {
					Log.e(LogTag.LAYOUT, "can't delete Item  ID : " + id);
				}

				updateExpandList();
			}
		});
	}
   

	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_liability_expand;
	}

	@Override
	protected int getItemType() {
		return LiabilityItem.TYPE;
	}
}