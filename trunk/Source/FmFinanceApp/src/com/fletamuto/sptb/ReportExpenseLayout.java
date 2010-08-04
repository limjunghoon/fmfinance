package com.fletamuto.sptb;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportExpenseLayout extends ReportBaseLayout {
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getItemsFromDB(ExpenseItem.TYPE) == false) {
        	return;
        }
        
        setListAdapter(R.layout.report_list_expense);
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	ExpenseItem item = (ExpenseItem)financeItem;
		
		((TextView)convertView.findViewById(R.id.TVExpenseReportListDate)).setText(item.getDateString());			
		((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("±Ý¾× : %,d¿ø", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVExpenseReportListMemo)).setText(item.getMemo());
		String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText(categoryText);
	}
    
    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportExpenseDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
    }
    
    @Override
	protected int deleteItemToDB(int id) {
		return DBMgr.getInstance().deleteItem(ExpenseItem.TYPE, id);
	}
}