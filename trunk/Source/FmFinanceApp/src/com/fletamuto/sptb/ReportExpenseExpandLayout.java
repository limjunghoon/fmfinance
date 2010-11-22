package com.fletamuto.sptb;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportExpenseExpandLayout extends ReportExpandBaseLayout {
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        updateExpandList();
    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	ExpenseItem item = (ExpenseItem)financeItem;
		
		((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText("분류 : " + categoryText);
		((TextView)convertView.findViewById(R.id.TVExpenseReportListPaymentMethod)).setText("결제 : " + item.getPaymentMethod().getText());
	}
    
    protected void setDeleteBtnListener(final View convertView, final int id, final int groupPosition, final int childPosition) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportExpenseDelete);
    	
		btnDelete.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				if (DBMgr.deleteItem(ExpenseItem.TYPE, id) == 0 ) {
					Log.e(LogTag.LAYOUT, "can't delete Item  ID : " + id);
				}

				updateExpandList();
			}
		});
	}
    
    @Override
	protected int deleteItemToDB(int id) {
		return DBMgr.deleteItem(ExpenseItem.TYPE, id);
	}

	@Override
	protected FinanceItem getItemInstance(int id) {
		return DBMgr.getItem(ExpenseItem.TYPE, id);
	}

	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_expense_expand;
	}
	


}