package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.ReportExpandBaseLayout.ReportExpandableListAdapter;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class ReportBaseMonthCompare extends ReportBaseCompare {
	
	protected int mMonth;
	protected int mYear;
	protected ReportExpandableListAdapter mAdapterItem;
	protected ArrayList<ArrayList<FinanceItem>> mChildItems = new ArrayList<ArrayList<FinanceItem>>();
	protected ArrayList<String> mParentItems = new ArrayList<String>();
	
//	protected abstract void setAdapterList();
	protected abstract void setListViewText(FinanceItem financeItem, View convertView);
	protected abstract int getChildLayoutResourceID();
	
	
	public int getMonth() {
		return mMonth;
	}
	
	public int getYear() {
		return mYear;
	}
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_compare, true);
    	
    	getData();
    	setButtonClickListener();
    	
    	updateChildView();
    }
    
    
    protected void getData() {
		mFinanceItems = DBMgr.getItems(mType, mYear, mMonth);
		mTotalAmout = DBMgr.getTotalAmountMonth(mType, mYear, mMonth);
		updateMapCategory();
		updateReportItem();
	}
    
    @Override
    protected void initialize() {
    	mMonth = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_MONTH, Calendar.getInstance().get(Calendar.MONTH)+1);
    	mYear = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_YEAR, Calendar.getInstance().get(Calendar.YEAR));
    }
    
    
	private void setButtonClickListener() {
		final ToggleButton tbMonth = (ToggleButton)findViewById(R.id.TBMonth);
		final ToggleButton tbDayMonth = (ToggleButton)findViewById(R.id.TBDayOfMonth);
		
		tbMonth.setChecked(true);
		tbMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				tbMonth.setChecked(true);
				tbDayMonth.setChecked(false);
				updateChildView();
			}
		});
		
		tbDayMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				tbDayMonth.setChecked(true);
				tbMonth.setChecked(false);
				updateChildView();
			}
		});
		
		Button btnPreviousMonth = (Button)findViewById(R.id.BtnPreviusMonth);
		Button btnNextMonth = (Button)findViewById(R.id.BtnNextMonth);
		
		btnPreviousMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				movePreviousMonth();
			}
		});
		
		btnNextMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveNextMonth();
			}
		});
	}

	protected void updateChildView() {
		LinearLayout monthLayout = (LinearLayout)findViewById(R.id.LLMonth);
		LinearLayout dayOfMonthLayout = (LinearLayout)findViewById(R.id.LLDayofMonth);
		ToggleButton tbMonth = (ToggleButton)findViewById(R.id.TBMonth);
		TextView tvMonth = (TextView)findViewById(R.id.TVCurrentMonth);
		tvMonth.setText(String.format("%d년 %d월", mYear, mMonth));
		TextView tvTotalAmount = (TextView)findViewById(R.id.TVTotalAmount);
		tvTotalAmount.setText(String.format("금액 : %,d", mTotalAmout));
		
		monthLayout.setVisibility(View.INVISIBLE);
		dayOfMonthLayout.setVisibility(View.INVISIBLE);
		
		if (tbMonth.isChecked()) {
			monthLayout.setVisibility(View.VISIBLE);
		}
		else {
			dayOfMonthLayout.setVisibility(View.VISIBLE);
		}
		
		updateBarGraph();
		addButtonInLayout();
		
		setExpandListAdapter();
	}
	
	
	protected void setExpandListAdapter() {
		if (mFinanceItems == null) return;
        
    	ExpandableListView elvDayofMonthItems = (ExpandableListView)findViewById(R.id.ELVReportDayOfMonth);
    	mAdapterItem = new ReportExpandableListAdapter(ReportBaseMonthCompare.this);
    	elvDayofMonthItems.setAdapter(mAdapterItem);
        
        int groupCount = mAdapterItem.getGroupCount();
        for (int index = 0; index < groupCount; index++) {
        	elvDayofMonthItems.expandGroup(index);
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
//			setDeleteBtnListener(reportListView, item.getID(), groupPosition, childPosition);
			
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
	
	public void updateReportItem() {
		mParentItems.clear();
		mChildItems.clear();
		
		int itemSize = mFinanceItems.size();
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = mFinanceItems.get(index);
			
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
	
//	public class ItemAdapter extends ArrayAdapter<FinanceItem> {
//    	private int mResource;
//    	private LayoutInflater mInflater;
//
//		public ItemAdapter(Context context, int resource,
//				 List<FinanceItem> objects) {
//			super(context, resource, objects);
//			this.mResource = resource;
//			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			FinanceItem item = (FinanceItem)getItem(position);
//			
//			if (convertView == null) {
//				convertView = mInflater.inflate(mResource, parent, false);
//				
//				setListViewText(item, convertView);	
//			}
//			return convertView;
//		}
//    }
	
	public void moveNextMonth() {
		if (12 == mMonth) {
			mYear++;
			mMonth = 1;
		}
		else {
			mMonth++;
		}
		
		getData();
		updateChildView();
	}
	
	public void movePreviousMonth() {
		if (1 == mMonth) {
			mYear--;
			mMonth = 12;
		}
		else {
			mMonth--;
		}
		getData();
		updateChildView();
	}
}