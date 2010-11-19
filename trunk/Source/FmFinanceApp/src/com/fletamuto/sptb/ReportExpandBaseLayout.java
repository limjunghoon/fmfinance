package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.ReportBaseLayout.ReportItemAdapter;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class ReportExpandBaseLayout extends ListActivity  {
protected static final int ACT_ITEM_EDIT = 0;
	
	protected ArrayList<FinanceItem> mItems = null;
	protected ReportItemAdapter mItemAdapter = null;
	private int mLatestSelectPosition = -1;
	protected int mMonth = -1;
	protected int mYear = -1;
	protected int mCategoryID = -1;
	
	protected abstract void setListViewText(FinanceItem financeItem, View convertView);
	protected abstract void setDeleteBtnListener(View convertView, int itemId, int position);
	protected abstract int	deleteItemToDB(int id);
	protected abstract FinanceItem getItemInstance(int id);
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initialize();
    }
	
	public void initialize() {
		mYear = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_YEAR, -1);
		mMonth = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_MONTH, -1);
		mCategoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		mLatestSelectPosition = position;
    }
	
	protected void startEditInputActivity(Class<?> cls, int itemId) {
		Intent intent = new Intent(ReportExpandBaseLayout.this, cls);
    	intent.putExtra("EDIT_ITEM_ID", itemId);
    	startActivityForResult(intent, ACT_ITEM_EDIT);
	}
	
	protected boolean getItemsFromDB(int itemType) {
		if (mYear != -1 && mMonth != -1 && mCategoryID != -1) {
			mItems = DBMgr.getItemsFromCategoryID(itemType, mCategoryID, mYear, mMonth);
		}
		else if (mCategoryID != -1) {
			mItems = DBMgr.getItemsFromCategoryID(itemType, mCategoryID);
		}
		else {
			mItems = DBMgr.getAllItems(itemType);
		}
    	
        if (mItems == null) {
        	return false;
        }
        return true;
    }
	
	protected boolean getItemsFromDB(int itemType, Calendar calendar) {
		mItems = DBMgr.getItems(itemType, calendar);
        if (mItems == null) {
        	return false;
        }
        return true;
    }
	
	protected void setListAdapter(int id) {
		mItemAdapter = new ReportItemAdapter(this, id, mItems);
		setListAdapter(mItemAdapter); 
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
			setDeleteBtnListener(reportListView, item.getID(), position);
			
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
				mItems.remove(position.intValue());
				mItemAdapter.notifyDataSetChanged();
			}
		}
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
    	if (requestCode == ACT_ITEM_EDIT) {
    		if (resultCode == RESULT_OK) {
    			FinanceItem item = getItemInstance(data.getIntExtra("EDIT_ITEM_ID", -1));
    			
    			if (mLatestSelectPosition != -1 && item != null) {
    				mItems.set(mLatestSelectPosition, item);
    				mItemAdapter.notifyDataSetChanged();
    			}
    		}
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
