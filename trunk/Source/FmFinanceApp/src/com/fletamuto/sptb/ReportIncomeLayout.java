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

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportIncomeLayout extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ArrayList<FinanceItem> items = DBMgr.getInstance().getAllItems(IncomeItem.TYPE);
        if (items == null) return;

        IncomeItemAdapter adapter = new IncomeItemAdapter(this, R.layout.report_list_income, items);
		setListAdapter(adapter); 
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }
    
    public class IncomeItemAdapter extends ArrayAdapter<FinanceItem> {
    	int resource;

		public IncomeItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.resource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout IncomeListView;
			IncomeItem item = (IncomeItem)getItem(position);
			
			if (convertView == null) {
				IncomeListView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)getContext().getSystemService(inflater);
				li.inflate(resource, IncomeListView, true);
			}
			else {
				IncomeListView = (LinearLayout)convertView;
			}
			
			((TextView)IncomeListView.findViewById(R.id.TVIncomeReportListDate)).setText(item.getDateString());			
			((TextView)IncomeListView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.valueOf(item.getAmount()));
			((TextView)IncomeListView.findViewById(R.id.TVIncomeReportListMemo)).setText(item.getMemo());
			((TextView)IncomeListView.findViewById(R.id.TVIncomeReportListCategory)).setText(item.getCategory().getName());
			return IncomeListView;
		}
    }

}