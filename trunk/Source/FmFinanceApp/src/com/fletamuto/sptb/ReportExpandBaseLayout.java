package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class ReportExpandBaseLayout extends ExpandableListActivity  {
	protected static final int ACT_ITEM_EDIT = 0;
	
	protected ArrayList<FinanceItem> mItems = null;
	protected ReportExpandItemAdapter mItemAdapter = null;
	private int mLatestSelectPosition = -1;
	protected int mMonth = -1;
	protected int mYear = -1;
	protected int mCategoryID = -1;
	protected ExpandableListAdapter mAdapter;
	
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
        mAdapter = new ReportExpandItemAdapter();
        setListAdapter(mAdapter);
        registerForContextMenu(getExpandableListView()); 
	}
	 
	public class ReportExpandItemAdapter extends BaseExpandableListAdapter {
        private String[] groups = { "People Names", "Dog Names", "Cat Names", "Fish Names" };
        private String[][] children = {
                { "Arnold", "Barry", "Chuck", "David" },
                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
                { "Fluffy", "Snuggles" },
                { "Goldy", "Bubbles" }
        };

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getChild(groupPosition, childPosition).toString());
			return textView;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getGroup(groupPosition).toString());
            return textView;
		}

		public boolean hasStableIds() {
			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
//			return true;
			return false;
		}
		
		 public TextView getGenericView() {
	            // Layout parameters for the ExpandableListView
	            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
	                    ViewGroup.LayoutParams.FILL_PARENT, 64);

	            TextView textView = new TextView(ReportExpandBaseLayout.this);
	            textView.setLayoutParams(lp);
	            // Center the text vertically
	            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	            // Set the text starting position
	            textView.setPadding(36, 0, 0, 0);
	            return textView;
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
//				mItemAdapter.notifyDataSetChanged();
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
 //   				mItemAdapter.notifyDataSetChanged();
    			}
    		}
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
	
}
