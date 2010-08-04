package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ListActivity;
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

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportTodayIncomeLayout extends ListActivity {
	ArrayList<FinanceItem> items = null;
	IncomeItemAdapter adapter = null;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        items = DBMgr.getInstance().getItems(IncomeItem.TYPE, Calendar.getInstance());
        if (items == null) return;
        
        adapter = new IncomeItemAdapter(this, R.layout.report_list_income, items);
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
			LinearLayout incomeListView;
			IncomeItem item = (IncomeItem)getItem(position);
			
			if (convertView == null) {
				incomeListView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)getContext().getSystemService(inflater);
				li.inflate(resource, incomeListView, true);
			}
			else {
				incomeListView = (LinearLayout)convertView;
			}
			
			((TextView)incomeListView.findViewById(R.id.TVIncomeReportListDate)).setText(item.getDateString());			
			((TextView)incomeListView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("±Ý¾× : %,d¿ø", item.getAmount()));
			((TextView)incomeListView.findViewById(R.id.TVIncomeReportListMemo)).setText(item.getMemo());
			((TextView)incomeListView.findViewById(R.id.TVIncomeReportListCategory)).setText(item.getCategory().getName());
			
			Button btn = (Button)incomeListView.findViewById(R.id.BtnReportIncomeDelete);
			btn.setTag(R.id.delete_id, new Integer(item.getId()));
			btn.setTag(R.id.delete_position, new Integer(position));
			btn.setOnClickListener(deleteBtn);
			
			return incomeListView;
		}
    }
    
    Button.OnClickListener deleteBtn = new  Button.OnClickListener(){
    	
		public void onClick(View arg0) {
			Integer position = (Integer)arg0.getTag(R.id.delete_position);
			Integer id = (Integer)arg0.getTag(R.id.delete_id);
			if (DBMgr.getInstance().deleteItem(IncomeItem.TYPE, id) == 0) {
				Log.e(LogTag.LAYOUT, "== noting delete id : " + id);
			}
			else {
				items.remove(position.intValue());
				adapter.notifyDataSetChanged();
			}
		}
	};
}