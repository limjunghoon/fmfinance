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

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class ReportBaseLayout extends ListActivity {
	protected static final int ACT_ITEM_EDIT = 0;
	
	ArrayList<FinanceItem> items = null;
	ReportItemAdapter adapter = null;
	
	protected abstract void setListViewText(FinanceItem financeItem, View convertView);
	protected abstract void setDeleteBtnListener(View convertView, int itemId, int position);
	protected abstract int	deleteItemToDB(int id);
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
	protected void onListItemClick(ListView l, View v, int position, long id) {

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
    			
    			// 해당 방식을 리스트가 많을경우 느려지는 문제가 있어 변경 요망
    			items.clear();
    			if (getItemsFromDB(ExpenseItem.TYPE) == false) {
    				return;
    			}
            
    			setListAdapter(R.layout.report_list_expense);
    		}
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
	
}
