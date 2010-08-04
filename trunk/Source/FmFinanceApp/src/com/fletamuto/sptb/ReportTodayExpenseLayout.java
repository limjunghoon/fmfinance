package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportTodayExpenseLayout extends ReportExpenseLayout {
	ArrayList<FinanceItem> items = null;
	ExpenseItemAdapter adapter = null;
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        items = DBMgr.getInstance().getItems(ExpenseItem.TYPE, Calendar.getInstance());
        if (items == null) return;
        
        adapter = new ExpenseItemAdapter(this, R.layout.report_list_expense, items);
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
			((TextView)expenseListView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("±Ý¾× : %,d¿ø", item.getAmount()));
			((TextView)expenseListView.findViewById(R.id.TVExpenseReportListMemo)).setText(item.getMemo());
			String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
			((TextView)expenseListView.findViewById(R.id.TVExpenseReportListCategory)).setText(categoryText);
			
			Button btn = (Button)expenseListView.findViewById(R.id.BtnReportExpenseDelete);
			btn.setTag(R.id.delete_id, new Integer(item.getId()));
			btn.setTag(R.id.delete_position, new Integer(position));
			btn.setOnClickListener(deleteBtn);
			
			return expenseListView;
		}
    }
    
    	Button.OnClickListener deleteBtn = new  Button.OnClickListener(){
    	
		public void onClick(View arg0) {
			Integer position = (Integer)arg0.getTag(R.id.delete_position);
			Integer id = (Integer)arg0.getTag(R.id.delete_id);
			if (DBMgr.getInstance().deleteItem(ExpenseItem.TYPE, id) == 0) {
				Log.e(LogTag.LAYOUT, "== noting delete id : " + id);
			}
			else {
				items.remove(position.intValue());
				adapter.notifyDataSetChanged();
			}
		}
    };
}