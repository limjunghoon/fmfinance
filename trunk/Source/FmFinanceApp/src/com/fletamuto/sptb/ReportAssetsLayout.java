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

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportAssetsLayout extends ListActivity {
	ArrayList<FinanceItem> items = null;
	AssetsItemAdapter adapter = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getItemsFromDB() == false) {
        	return;
        }
        
        adapter = new AssetsItemAdapter(this, R.layout.report_list_assets, items);
		setListAdapter(adapter); 
    }
    
    protected boolean getItemsFromDB() {
    	items = DBMgr.getInstance().getAllItems(AssetsItem.TYPE);
        if (items == null) {
        	return false;
        }
        return true;
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
			LinearLayout assetsListView;
			AssetsItem item = (AssetsItem)getItem(position);
			
			if (convertView == null) {
				assetsListView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)getContext().getSystemService(inflater);
				li.inflate(resource, assetsListView, true);
			}
			else {
				assetsListView = (LinearLayout)convertView;
			}
			
			((TextView)assetsListView.findViewById(R.id.TVAssetsReportListTitle)).setText("제목 : " + item.getTitle());
			((TextView)assetsListView.findViewById(R.id.TVAssetsReportListDate)).setText("날짜 : " + item.getDateString());			
			((TextView)assetsListView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
			String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
			((TextView)assetsListView.findViewById(R.id.TVAssetsReportListCategory)).setText("분류: " + categoryText);
			
			Button btn = (Button)assetsListView.findViewById(R.id.BtnReportAssetsDelete);
			btn.setTag(R.id.delete_id, new Integer(item.getId()));
			btn.setTag(R.id.delete_position, new Integer(position));
			btn.setOnClickListener(deleteBtn);
			
			return assetsListView;
		}
    }
    
    Button.OnClickListener deleteBtn = new  Button.OnClickListener(){
    	
		public void onClick(View arg0) {
			Integer position = (Integer)arg0.getTag(R.id.delete_position);
			Integer id = (Integer)arg0.getTag(R.id.delete_id);
			if (DBMgr.getInstance().deleteItem(AssetsItem.TYPE, id) == 0) {
				Log.e(LogTag.LAYOUT, "== noting delete id : " + id);
			}
			else {
				items.remove(position.intValue());
				adapter.notifyDataSetChanged();
			}
		}
	};
}