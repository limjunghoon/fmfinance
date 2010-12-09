package com.fletamuto.sptb;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class ReportIncomeExpandLayout extends ReportExpandBaseLayout {
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        updateChildView();
    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	IncomeItem item = (IncomeItem)financeItem;
		((TextView)convertView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVIncomeReportListMemo)).setText("메모 : " + item.getMemo());
		((TextView)convertView.findViewById(R.id.TVIncomeReportListCategory)).setText("분류 : " + item.getCategory().getName());
	}
    
    protected void setDeleteBtnListener(final View convertView, final int id, final int groupPosition, final int childPosition) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportIncomeDelete);
    	
		btnDelete.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				if (DBMgr.deleteItem(getItemType(), id) == 0 ) {
					Log.e(LogTag.LAYOUT, "can't delete Item  ID : " + id);
				}

				updateExpandList();
			}
		});
	}
    
//    @Override
//	protected int deleteItemToDB(int id) {
//		return DBMgr.deleteItem(ExpenseItem.TYPE, id);
//	}

//	@Override
//	protected FinanceItem getItemInstance(int id) {
//		return DBMgr.getItem(ExpenseItem.TYPE, id);
//	}

	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_income_expand;
	}

	@Override
	protected int getItemType() {
		return IncomeItem.TYPE;
	}
}