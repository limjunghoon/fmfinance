package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportMonthLayout extends FmBaseActivity {
	private Calendar mCalendar = Calendar.getInstance();
	private ArrayList<FinanceItem> mIncomeItems;
	private ArrayList<FinanceItem> mExpenseItems;
	private ArrayList<FinanceItem> mAssetsItems;
	private ArrayList<FinanceItem> mLiabilityItems;
	private ArrayList<FinanceItem> mTotalItems = new ArrayList<FinanceItem>();
	private ReportExpandableListAdapter mItemAdapter = null;
	private ArrayList<ArrayList<FinanceItem>> mChildItems = new ArrayList<ArrayList<FinanceItem>>();
	private ArrayList<String> mParentItems = new ArrayList<String>();
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.report_month_expand_list, true);
        updateExpandList();
    }
    
    @Override
    protected void setTitleBtn() {
    	setTitle("월간 입력사항");
    	super.setTitleBtn();
    }
    
    public void setExpandListAdapter() {
		ExpandableListView elvItems = (ExpandableListView)findViewById(R.id.ELVBase);
        mItemAdapter = new ReportExpandableListAdapter(this);
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
                    ViewGroup.LayoutParams.FILL_PARENT, 35);

            TextView textView = new TextView(mContext);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            textView.setTextColor(Color.MAGENTA);
            textView.setTextSize(18);
            return textView;
        }
        
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
        	
        	LinearLayout reportListView;
			FinanceItem item = mChildItems.get(groupPosition).get(childPosition);
			
//			if (convertView == null) {
				reportListView = new LinearLayout(mContext);
				reportListView.setPadding(36, 0, 0, 0);
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)mContext.getSystemService(inflater);
				li.inflate(getChildLayoutResourceID(item.getType()), reportListView, true);
//			}
//			else {
//				reportListView = (LinearLayout)convertView;
//			}
			
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
	
	
	protected boolean getItems() {
		mIncomeItems = DBMgr.getItems(IncomeItem.TYPE, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONDAY)+1);
		mExpenseItems = DBMgr.getItems(ExpenseItem.TYPE, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONDAY)+1);
		mAssetsItems = DBMgr.getItems(AssetsItem.TYPE, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONDAY)+1);
		mLiabilityItems = DBMgr.getItems(LiabilityItem.TYPE, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONDAY)+1);
        
		mTotalItems.addAll(mIncomeItems);
		mTotalItems.addAll(mExpenseItems);
		mTotalItems.addAll(mAssetsItems);
		mTotalItems.addAll(mLiabilityItems);
        updateReportItem();
        
        return true;
    }
	
	private void setListViewText(FinanceItem financeItem, LinearLayout convertView) {
		if (financeItem.getType() == IncomeItem.TYPE) {
			IncomeItem item = (IncomeItem)financeItem;
			((TextView)convertView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
			((TextView)convertView.findViewById(R.id.TVIncomeReportListMemo)).setText("메모 : " + item.getMemo());
			((TextView)convertView.findViewById(R.id.TVIncomeReportListCategory)).setText("분류 : " + item.getCategory().getName());
		}
		if (financeItem.getType() == ExpenseItem.TYPE) {
			ExpenseItem item = (ExpenseItem)financeItem;
			((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
			String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
			((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText("분류 : " + categoryText);
			((TextView)convertView.findViewById(R.id.TVExpenseReportListPaymentMethod)).setText("결제 : " + item.getPaymentMethod().getText());
		}
		if (financeItem.getType() == AssetsItem.TYPE) {
			AssetsItem item = (AssetsItem)financeItem;
			((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
			((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText(String.format("제목 : %s", item.getTitle()));
			((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setText(String.format("분류 : %s", item.getCategory().getName()));
		}
		if (financeItem.getType() == LiabilityItem.TYPE) {
			LiabilityItem item = (LiabilityItem)financeItem;
			((TextView)convertView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
			((TextView)convertView.findViewById(R.id.TVLiabilityReportListTitle)).setText(String.format("제목 : %s", item.getTitle()));
			((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setText(String.format("분류 : %s", item.getCategory().getName()));
		}
		
		
	}
	
	protected int getChildLayoutResourceID(int type) {
		if (type == IncomeItem.TYPE) {
			return R.layout.report_list_income_expand;
		}
		else if (type == ExpenseItem.TYPE) {
			return R.layout.report_list_expense_expand;
		}
		else if (type == AssetsItem.TYPE) {
			return R.layout.report_list_assets_expand;
		}
		else if (type == LiabilityItem.TYPE) {
			return R.layout.report_list_liability_expand;
		}
		else {
			Log.e(LogTag.LAYOUT, "ERROE TYPE");
			return -1;
		}
	}
	
	public void updateReportItem() {
		mParentItems.clear();
		mChildItems.clear();
		
		if (mTotalItems == null) {
			Log.e(LogTag.LAYOUT, ":: EMPTY ITEM");
			return;
		}
		int itemSize = mTotalItems.size();
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = mTotalItems.get(index);
			
			String comapre = getCompareText(item);
			int findIndex = findItemFromParentItem(comapre);
			if (findIndex != -1) {
				mChildItems.get(findIndex).add(item);
			}
			else {
				mParentItems.add(comapre);
				ArrayList<FinanceItem> childItems = new ArrayList<FinanceItem>();
				childItems.add(item);
				mChildItems.add(childItems);
			}
		}
	}
	
	protected String getCompareText(final FinanceItem item) {
		return String.format("%d일", item.getCreateDate().get(Calendar.DAY_OF_MONTH));
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
		if (getItems() == false) {
        	return;
        }
        
        setExpandListAdapter();
	}
}