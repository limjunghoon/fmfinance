package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportExpenseLayout extends ListActivity {
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ArrayList<FinanceItem> items = DBMgr.getInstance().getAllItems(ExpenseItem.TYPE);
        if (items == null) return;
        
        ExpenseItemAdapter adapter = new ExpenseItemAdapter(this, R.layout.report_list_expense, items);
		setListAdapter(adapter); 
        
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }
    
    public class ExpenseItemAdapter extends ArrayAdapter<FinanceItem> {
    	int resource;

		public ExpenseItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.resource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout expenseListView;
			ExpenseItem item = (ExpenseItem)getItem(position);
			
			if (convertView == null) {
				expenseListView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)getContext().getSystemService(inflater);
				li.inflate(resource, expenseListView, true);
			}
			else {
				expenseListView = (LinearLayout)convertView;
			}
			
			((TextView)expenseListView.findViewById(R.id.TVExpenseReportListDate)).setText(item.getDateString());			
			((TextView)expenseListView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("%,d¿ø", item.getAmount()));
			((TextView)expenseListView.findViewById(R.id.TVExpenseReportListMemo)).setText(item.getMemo());
			String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
			((TextView)expenseListView.findViewById(R.id.TVExpenseReportListCategory)).setText(categoryText);
			return expenseListView;
		}
    }
}