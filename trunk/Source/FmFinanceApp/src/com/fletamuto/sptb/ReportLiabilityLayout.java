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
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportLiabilityLayout extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<FinanceItem> items = DBMgr.getInstance().getAllItems(LiabilityItem.TYPE);
        if (items == null) return;
        
        LiabilityItemAdapter adapter = new LiabilityItemAdapter(this, R.layout.report_list_liability, items);
		setListAdapter(adapter); 
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }
    
    public class LiabilityItemAdapter extends ArrayAdapter<FinanceItem> {
    	int resource;

		public LiabilityItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.resource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout LiabilityListView;
			LiabilityItem item = (LiabilityItem)getItem(position);
			
			if (convertView == null) {
				LiabilityListView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)getContext().getSystemService(inflater);
				li.inflate(resource, LiabilityListView, true);
			}
			else {
				LiabilityListView = (LinearLayout)convertView;
			}
			
			((TextView)LiabilityListView.findViewById(R.id.TVLiabilityReportListTitle)).setText(item.getTitle());
			((TextView)LiabilityListView.findViewById(R.id.TVLiabilityReportListDate)).setText(item.getDateString());			
			((TextView)LiabilityListView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("%,d¿ø", item.getAmount()));
			((TextView)LiabilityListView.findViewById(R.id.TVLiabilityReportListCategory)).setText(item.getCategory().getName());
			return LiabilityListView;
		}
    }
}