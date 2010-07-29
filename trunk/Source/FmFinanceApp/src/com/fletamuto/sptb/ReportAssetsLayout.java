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

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportAssetsLayout extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<FinanceItem> items = DBMgr.getInstance().getAllItems(AssetsItem.TYPE);
        if (items == null) return;
        
        AssetsItemAdapter adapter = new AssetsItemAdapter(this, R.layout.report_list_assets, items);
		setListAdapter(adapter); 
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }

    public class AssetsItemAdapter extends ArrayAdapter<FinanceItem> {
    	int resource;

		public AssetsItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.resource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout AssetsListView;
			AssetsItem item = (AssetsItem)getItem(position);
			
			if (convertView == null) {
				AssetsListView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)getContext().getSystemService(inflater);
				li.inflate(resource, AssetsListView, true);
			}
			else {
				AssetsListView = (LinearLayout)convertView;
			}
			
			((TextView)AssetsListView.findViewById(R.id.TVAssetsReportListTitle)).setText(item.getTitle());
			((TextView)AssetsListView.findViewById(R.id.TVAssetsReportListDate)).setText(item.getDateString());			
			((TextView)AssetsListView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.valueOf(item.getAmount()));
			String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
			((TextView)AssetsListView.findViewById(R.id.TVAssetsReportListCategory)).setText(categoryText);
			return AssetsListView;
		}
    }
}