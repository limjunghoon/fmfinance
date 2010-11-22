package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class ReportExpandBaseLayout extends FmBaseActivity  {
	protected static final int ACT_ITEM_EDIT = 0;
	protected ArrayList<FinanceItem> mItems = null;
	protected ReportExpandableListAdapter mItemAdapter = null;
	protected ArrayList<ArrayList<FinanceItem>> mChildItems = new ArrayList<ArrayList<FinanceItem>>();
	protected ArrayList<String> mParentItems = new ArrayList<String>();
	protected int mMonth = -1;
	protected int mYear = -1;
	protected int mCategoryID = -1;
	
	protected abstract void setListViewText(FinanceItem financeItem, View convertView);
	protected abstract void setDeleteBtnListener(View convertView, int itemId, int groupPosition, int childPosition);
	protected abstract int	deleteItemToDB(int id);
	protected abstract FinanceItem getItemInstance(int id);
	protected abstract int getChildLayoutResourceID();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_expand_list, true);
        initialize();
    }
	
	public void initialize() {
		mYear = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_YEAR, -1);
		mMonth = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_MONTH, -1);
		mCategoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
	}
	
	public void setExpandListAdapter() {
		ExpandableListView elvItems = (ExpandableListView)findViewById(R.id.ELVBase);
        mItemAdapter = new ReportExpandableListAdapter(ReportExpandBaseLayout.this);
        elvItems.setAdapter(mItemAdapter);
        
        int groupCount = mItemAdapter.getGroupCount();
        for (int index = 0; index < groupCount; index++) {
        	elvItems.expandGroup(index);
        }
	}
	
	public class ReportExpandableListAdapter extends BaseExpandableListAdapter {
		private Context mContext;
        
        ReportExpandableListAdapter(Context context) {
        	mContext = context;
        }
        
        public Object getChild(int groupPosition, int childPosition) {
            return mChildItems.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return mChildItems.get(groupPosition).size();
        }

        public TextView getGenericView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 64);

            TextView textView = new TextView(mContext);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            textView.setTextColor(Color.MAGENTA);
            return textView;
        }
        
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
        	
        	LinearLayout reportListView;
			FinanceItem item = mChildItems.get(groupPosition).get(childPosition);
			
			if (convertView == null) {
				reportListView = new LinearLayout(mContext);
				reportListView.setPadding(36, 0, 0, 0);
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)mContext.getSystemService(inflater);
				li.inflate(getChildLayoutResourceID(), reportListView, true);
			}
			else {
				reportListView = (LinearLayout)convertView;
			}
			
			setListViewText(item, reportListView);
			setDeleteBtnListener(reportListView, item.getID(), groupPosition, childPosition);
			
			return reportListView;
        }

        public Object getGroup(int groupPosition) {
            return mParentItems.get(groupPosition);
        }

        public int getGroupCount() {
            return mParentItems.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getGroup(groupPosition).toString());
            return textView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }
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
        
        updateReportItem();
        
        return true;
    }
	
	public void updateReportItem() {
		mParentItems.clear();
		mChildItems.clear();
		
		int itemSize = mItems.size();
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = mItems.get(index);
			
			String createDate = item.getCreateDateString();
			int findIndex = findItemFromParentItem(createDate);
			if (findIndex != -1) {
				mChildItems.get(findIndex).add(item);
			}
			else {
				mParentItems.add(createDate);
				ArrayList<FinanceItem> childItems = new ArrayList<FinanceItem>();
				childItems.add(item);
				mChildItems.add(childItems);
				
			}
		}
	}
	
	public int findItemFromParentItem(String date) {
		int size = mParentItems.size();
		for (int index = 0; index < size; index++) {
			if (mParentItems.get(index).compareTo(date) == 0) {
				return index;
			}
		}
		return -1;
	}
	
	public void updateExpandList() {
		if (getItemsFromDB(ExpenseItem.TYPE) == false) {
        	return;
        }
        
        setExpandListAdapter();
	}
}
