package com.fletamuto.sptb;


import java.util.ArrayList;
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
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportLiabilityLayout extends ListActivity {
	ArrayList<FinanceItem> items = null;
	LiabilityItemAdapter adapter = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getItemsFromDB() == false) {
        	return;
        }
        
        adapter = new LiabilityItemAdapter(this, R.layout.report_list_liability, items);
		setListAdapter(adapter); 
    }
    
    protected boolean getItemsFromDB() {
    	items = DBMgr.getInstance().getAllItems(LiabilityItem.TYPE);
        if (items == null) {
        	return false;
        }
        return true;
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
			LinearLayout liabilityListView;
			LiabilityItem item = (LiabilityItem)getItem(position);
			
			if (convertView == null) {
				liabilityListView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)getContext().getSystemService(inflater);
				li.inflate(resource, liabilityListView, true);
			}
			else {
				liabilityListView = (LinearLayout)convertView;
			}
			
			((TextView)liabilityListView.findViewById(R.id.TVLiabilityReportListTitle)).setText("제목 : " + item.getTitle());
			((TextView)liabilityListView.findViewById(R.id.TVLiabilityReportListDate)).setText("날짜 : " + item.getDateString());			
			((TextView)liabilityListView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
			((TextView)liabilityListView.findViewById(R.id.TVLiabilityReportListCategory)).setText("분류 : " + item.getCategory().getName());
			
			Button btn = (Button)liabilityListView.findViewById(R.id.BtnReportLiabilityDelete);
			btn.setTag(R.id.delete_id, new Integer(item.getId()));
			btn.setTag(R.id.delete_position, new Integer(position));
			btn.setOnClickListener(deleteBtn);
			
			return liabilityListView;
		}
    }
    
    Button.OnClickListener deleteBtn = new  Button.OnClickListener(){
    	
		public void onClick(View arg0) {
			Integer position = (Integer)arg0.getTag(R.id.delete_position);
			Integer id = (Integer)arg0.getTag(R.id.delete_id);
			if (DBMgr.getInstance().deleteItem(LiabilityItem.TYPE, id) == 0) {
				Log.e(LogTag.LAYOUT, "== noting delete id : " + id);
			}
			else {
				items.remove(position.intValue());
				adapter.notifyDataSetChanged();
			}
		}
	};
}