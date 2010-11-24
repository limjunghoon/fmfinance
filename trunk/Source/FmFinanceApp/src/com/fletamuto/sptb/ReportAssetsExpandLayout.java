package com.fletamuto.sptb;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportAssetsExpandLayout extends ReportExpandBaseLayout {
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        updateChildView();
    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	AssetsItem item = (AssetsItem)financeItem;
		
		((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("�ݾ� : %,d��", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText(String.format("���� : %s", item.getTitle()));
		((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setText(String.format("�з� : %s", item.getCategory().getName()));
	}
    
    protected void setDeleteBtnListener(final View convertView, final int id, final int groupPosition, final int childPosition) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportAssetsDelete);
    	
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
		return R.layout.report_list_assets_expand;
	}

	@Override
	protected int getItemType() {
		return AssetsItem.TYPE;
	}
}