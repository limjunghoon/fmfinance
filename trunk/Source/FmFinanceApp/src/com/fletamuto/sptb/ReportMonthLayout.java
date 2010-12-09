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
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class ReportMonthLayout extends FmBaseActivity {
	private Calendar mCalendar = Calendar.getInstance();
	private ArrayList<ArrayList<FinanceItem>> mItems = new ArrayList<ArrayList<FinanceItem>>();
	private ReportExpandableListAdapter mItemAdapter = null;
	private ArrayList<ArrayList<FinanceItem>> mChildItems = new ArrayList<ArrayList<FinanceItem>>();
	private ArrayList<String> mParentItems = new ArrayList<String>();
	private boolean mItemVisible[] = {true, true, true, true};
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.report_month_expand_list, true);
        setButtonListener();
        updateExpandList();
    }
    
    private void setButtonListener() {
  //  	final ExpandableListView elvItems = (ExpandableListView)findViewById(R.id.ELVBase);
		final ToggleButton tbIncome = (ToggleButton)findViewById(R.id.TBReportIncome);
		tbIncome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mItemVisible[IncomeItem.TYPE] = tbIncome.isChecked();
				updateReportItem();
				mItemAdapter.notifyDataSetChanged();
			}
		});
		
		final ToggleButton tbExpense = (ToggleButton)findViewById(R.id.TBReportExpense);
		tbExpense.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mItemVisible[ExpenseItem.TYPE] = tbExpense.isChecked();
				updateReportItem();
				mItemAdapter.notifyDataSetChanged();
			}
		});
		
		final ToggleButton tbAssets = (ToggleButton)findViewById(R.id.TBReportAssets);
		tbAssets.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mItemVisible[AssetsItem.TYPE] = tbAssets.isChecked();
				updateReportItem();
				mItemAdapter.notifyDataSetChanged();
			}
		});
		
		final ToggleButton tbLiability = (ToggleButton)findViewById(R.id.TBReportLiability);
		tbLiability.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mItemVisible[LiabilityItem.TYPE] = tbLiability.isChecked();
				updateReportItem();
				mItemAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
    protected void initialize() {
    	super.initialize();
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
				li.inflate(getChildLayoutResourceID(item.getType(), item.getID()), reportListView, true);
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
		mItems.clear();
		mItems.add(DBMgr.getItems(IncomeItem.TYPE, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONDAY)+1));
		mItems.add(DBMgr.getItems(ExpenseItem.TYPE, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONDAY)+1));
		mItems.add(DBMgr.getItems(AssetsItem.TYPE, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONDAY)+1));
		mItems.add(DBMgr.getItems(LiabilityItem.TYPE, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONDAY)+1));
        updateReportItem();
        
        return true;
    }
	
	private void setListViewText(FinanceItem financeItem, LinearLayout convertView) {
		if (financeItem.getID() == -1) {
			TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
			tvTitle.setText(financeItem.getTitle());
			tvTitle.setTextColor(Color.BLACK);
			convertView.setBackgroundColor(Color.WHITE);
			convertView.setEnabled(false);
		}
		else {
			if (financeItem.getType() == IncomeItem.TYPE) {
				IncomeItem item = (IncomeItem)financeItem;
				((TextView)convertView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
				((TextView)convertView.findViewById(R.id.TVIncomeReportListMemo)).setText("메모 : " + item.getMemo());
				((TextView)convertView.findViewById(R.id.TVIncomeReportListCategory)).setText("분류 : " + item.getCategory().getName());
			}
			else if (financeItem.getType() == ExpenseItem.TYPE) {
				ExpenseItem item = (ExpenseItem)financeItem;
				((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
				String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
				((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText("분류 : " + categoryText);
				((TextView)convertView.findViewById(R.id.TVExpenseReportListPaymentMethod)).setText("결제 : " + item.getPaymentMethod().getText());
			}
			else if (financeItem.getType() == AssetsItem.TYPE) {
				AssetsItem item = (AssetsItem)financeItem;
				((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
				((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText(String.format("제목 : %s", item.getTitle()));
				((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setText(String.format("분류 : %s", item.getCategory().getName()));
			}
			else if (financeItem.getType() == LiabilityItem.TYPE) {
				LiabilityItem item = (LiabilityItem)financeItem;
				((TextView)convertView.findViewById(R.id.TVLiabilityReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
				((TextView)convertView.findViewById(R.id.TVLiabilityReportListTitle)).setText(String.format("제목 : %s", item.getTitle()));
				((TextView)convertView.findViewById(R.id.TVLiabilityReportListCategory)).setText(String.format("분류 : %s", item.getCategory().getName()));
			}
		}
		
		
		
	}
	
	protected int getChildLayoutResourceID(int type, int id) {
		if (id != -1) {
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
		else {
			return R.layout.list_separators;
		}
	}
	
	public void updateReportItem() {
		mParentItems.clear();
		mChildItems.clear();
		
		for (int day = 1; day <= 31; day++) {
			ArrayList<FinanceItem> findItems = getItem(day);
			if (findItems == null) {
				continue;
			}
			mParentItems.add(String.format("%d일", day));
			mChildItems.add(findItems);
			
		}
	}
	
	private ArrayList<FinanceItem> getItem(int day) {
		ArrayList<FinanceItem> findItems = null; 
		
		ArrayList<FinanceItem> findIncomeItems = getItem(IncomeItem.TYPE, day);
		if (findIncomeItems != null) {
			if (findItems == null) findItems = new ArrayList<FinanceItem>();
			findItems.add(createSeparator(IncomeItem.TYPE));
			findItems.addAll(findIncomeItems);
		}
		
		ArrayList<FinanceItem> findExepnsItems = getItem(ExpenseItem.TYPE, day);
		if (findExepnsItems != null) {
			if (findItems == null) findItems = new ArrayList<FinanceItem>();
			findItems.add(createSeparator(ExpenseItem.TYPE));
			findItems.addAll(findExepnsItems);
		}
		
		ArrayList<FinanceItem> findAssetsItems = getItem(AssetsItem.TYPE, day);
		if (findAssetsItems != null) {
			if (findItems == null) findItems = new ArrayList<FinanceItem>();
			findItems.add(createSeparator(AssetsItem.TYPE));
			findItems.addAll(findAssetsItems);
		}
		
		ArrayList<FinanceItem> findLiabilityItems = getItem(LiabilityItem.TYPE, day);
		if (findLiabilityItems != null) {
			if (findItems == null) findItems = new ArrayList<FinanceItem>();
			findItems.add(createSeparator(LiabilityItem.TYPE));
			findItems.addAll(findLiabilityItems);
		}
		return findItems;
	}
	
	private FinanceItem createSeparator(int type) {
		FinanceItem item = null;
		
		if (type == IncomeItem.TYPE) {
			item = new IncomeItem();
			item.setTitle("수입");
		}
		else if (type == ExpenseItem.TYPE) {
			item = new ExpenseItem();
			item.setTitle("지출");
		}
		else if (type == AssetsItem.TYPE) {
			item = new AssetsItem();
			item.setTitle("자산");
		}
		else if (type == LiabilityItem.TYPE) {
			item = new LiabilityItem();
			item.setTitle("부채");
		}
		return item;
	}

	private ArrayList<FinanceItem> getItem(int type, int day) {
		if (mItemVisible[type] == false) return null;
		
		ArrayList<FinanceItem> items = mItems.get(type);
		ArrayList<FinanceItem> findItems = null; 
		int itemSize = items.size();
		
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = items.get(index);
			
			if (item.getCreateDate().get(Calendar.DAY_OF_MONTH) == day) {
				if (findItems == null) {
					findItems = new ArrayList<FinanceItem>();
				}
				
				findItems.add(item);
			}
		}
		return findItems;
	}
	
	public void updateExpandList() {
		if (getItems() == false) {
        	return;
        }
        
        setExpandListAdapter();
	}
}