package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class ReportBaseLayout extends ListActivity {
	protected static final int ACT_ITEM_EDIT = 0;
	
	protected ArrayList<FinanceItem> items = null;
	protected ReportItemAdapter adapter = null;
	private int latestSelectPosition = -1;
	
	protected abstract void setListViewText(FinanceItem financeItem, View convertView);
	protected abstract void setDeleteBtnListener(View convertView, int itemId, int position);
	protected abstract int	deleteItemToDB(int id);
	protected abstract FinanceItem getItemInstance(int id);
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
	protected void onListItemClick(ListView l, View v, int position, long id) {
		latestSelectPosition = position;
    }
	
	protected void startEditInputActivity(Class<?> cls, int itemId) {
		Intent intent = new Intent(ReportBaseLayout.this, cls);
    	intent.putExtra("EDIT_ITEM_ID", itemId);
    	startActivityForResult(intent, ACT_ITEM_EDIT);
	}
	
	protected boolean getItemsFromDB(int itemType) {
    	items = DBMgr.getInstance().getAllItems(itemType);
        if (items == null) {
        	return false;
        }
        return true;
    }
	
	protected boolean getItemsFromDB(int itemType, Calendar calendar) {
		items = DBMgr.getInstance().getItems(itemType, calendar);
        if (items == null) {
        	return false;
        }
        return true;
    }
	
	protected void setListAdapter(int id) {
		adapter = new ReportItemAdapter(this, id, items);
		setListAdapter(adapter); 
	}
	
	public class ReportItemAdapter extends ArrayAdapter<FinanceItem> {
    	int resource;

		public ReportItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.resource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout reportListView;
			FinanceItem item = (FinanceItem)getItem(position);
			
			if (convertView == null) {
				reportListView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)getContext().getSystemService(inflater);
				li.inflate(resource, reportListView, true);
			}
			else {
				reportListView = (LinearLayout)convertView;
			}
			
			setListViewText(item, reportListView);
			setDeleteBtnListener(reportListView, item.getId(), position);
			
			return reportListView;
		}
    }
	
	Button.OnClickListener deleteBtnListener = new  Button.OnClickListener(){
    	
		public void onClick(View arg0) {
			Integer position = (Integer)arg0.getTag(R.id.delete_position);
			Integer id = (Integer)arg0.getTag(R.id.delete_id);
			if (deleteItemToDB(id) == 0) {
				Log.e(LogTag.LAYOUT, "== noting delete id : " + id);
			}
			else {
				items.remove(position.intValue());
				adapter.notifyDataSetChanged();
			}
		}
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
    	if (requestCode == ACT_ITEM_EDIT) {
    		if (resultCode == RESULT_OK) {
    			FinanceItem item = getItemInstance(data.getIntExtra("EDIT_ITEM_ID", -1));
    			
    			if (latestSelectPosition != -1 && item != null) {
    				items.set(latestSelectPosition, item);
    				adapter.notifyDataSetChanged();
    			}
    		}
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
	
}
