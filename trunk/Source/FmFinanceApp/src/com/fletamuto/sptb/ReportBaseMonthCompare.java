package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.view.FmBaseLayout;

public abstract class ReportBaseMonthCompare extends ReportBaseCompare {
	private static final int MOVE_SENSITIVITY = ItemDef.MOVE_SENSITIVITY;
	protected static final int VIEW_MONTH = 0;
	protected static final int VIEW_YEAR = 1;
	
	protected int mYear = Calendar.getInstance().get(Calendar.YEAR);
	private Calendar mMonthCalendar = Calendar.getInstance();
	protected ReportExpandableListAdapter mAdapterItem;
	protected ArrayList<ArrayList<FinanceItem>> mChildItems = new ArrayList<ArrayList<FinanceItem>>();
	protected ArrayList<String> mParentItems = new ArrayList<String>();
	protected int mViewMode = VIEW_MONTH;
	
	private float mTouchMove;
	private boolean mTouchMoveFlag = false;
	
	protected abstract void setListViewText(FinanceItem financeItem, View convertView);
	protected abstract int getChildLayoutResourceID();
	
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_compare, true);
    	
    	getData();
    	setListener();
    	
    	
    	updateChildView();
    }
	
	public Calendar getMonthCalender() {
		return mMonthCalendar;
	}
	
	@Override
	protected void setTitleBtn() {
		setTitleBtnName();
		setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
		
		super.setTitleBtn();
	}
	
	public void setTitleBtnName() {
		if (mViewMode == VIEW_MONTH) {
			setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "년");
		} else {
			setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "월");
		}
	}
	
    
    protected void getData() {
    	if (mViewMode == VIEW_MONTH) {
    		mFinanceItems = DBMgr.getItems(mType, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1);
    		mTotalAmout = DBMgr.getTotalAmountMonth(mType, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1);
    	}
    	else {
    		mFinanceItems = DBMgr.getItems(mType, mYear);
    		mTotalAmout = DBMgr.getTotalAmountYear(mType, mYear);
    	}
		
		updateMapCategory();
		updateReportItem();
	}
    
    @Override
    protected void initialize() {
    	mViewMode = getIntent().getIntExtra(MsgDef.ExtraNames.VIEW_MODE, VIEW_MONTH);
    	if (mViewMode == VIEW_MONTH) {
    		mMonthCalendar = (Calendar) getIntent().getSerializableExtra(MsgDef.ExtraNames.CALENDAR);
    		if (mMonthCalendar == null) {
    			mMonthCalendar = Calendar.getInstance();
    		}
    	}
    	else {
    		mYear = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_YEAR, Calendar.getInstance().get(Calendar.YEAR));
    	}
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
		if (mViewMode == VIEW_MONTH) {
			tvMonth.setText(String.format("%d년 %d월", mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1));
		}
		else {
			tvMonth.setText(String.format("%d년", mYear));
		}
		
		Button btnTotalAmount = (Button)findViewById(R.id.BtnTotalAmount);
		btnTotalAmount.setText(String.format("금액 : %,d", mTotalAmout));
		TextView tvDayOfMonthTitle = (TextView)findViewById(R.id.TVDayOfMonthTitle);
		
		if (mViewMode == VIEW_MONTH) {
			tvDayOfMonthTitle.setText(String.format("%d년 %d월", mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1));
		}
		else {
			tvDayOfMonthTitle.setText(String.format("%d년", mYear));
		}
		
		
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

		// 일별보기는 지원 미정이기때문에 일단 주석처리
//		setExpandListAdapter();
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
                    ViewGroup.LayoutParams.FILL_PARENT, 35);

            TextView textView = new TextView(mContext);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            textView.setTextSize(18);
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
	
	public void moveNextMonth() {
		mMonthCalendar.add(Calendar.MONTH, 1);
		
		getData();
		updateChildView();
	}
	
	public void movePreviousMonth() {
		mMonthCalendar.add(Calendar.MONTH, -1);
		getData();
		updateChildView();
	}
	
	public void setListener() {
		setButtonClickListener();
		setChangeViewBtnListener();
//		findViewById(R.id.ScrollView01).setOnTouchListener(new View.OnTouchListener() {
//			
//			public boolean onTouch(View v, MotionEvent event) {
//				setMoveViewMotionEvent(event);
//		    	return true;
//			}
//		});
		
		findViewById(R.id.BtnTotalAmount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onClickTotalAmountBtn();
			}
		});
		
		
	}
	
	/**
	 * 총액 버튼 클릭 시
	 */
	protected void onClickTotalAmountBtn() {
	}
	
	public void setChangeViewBtnListener() {
		setTitleButtonListener(FmBaseLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				onChageViewMode();
			}
		});
	}
	
	public void setMoveViewMotionEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		mTouchMove = event.getX();
    		mTouchMoveFlag = true;
    	}
    	else if (event.getAction() == MotionEvent.ACTION_MOVE && mTouchMoveFlag == true) {
    		
    		if (mTouchMove-event.getX()< -MOVE_SENSITIVITY) {
    			mTouchMoveFlag = false;
    			moveNextMonth();
    		}
    		if (mTouchMove-event.getX()> MOVE_SENSITIVITY) {
    			mTouchMoveFlag = false;
    			movePreviousMonth();
    		}
    	}
    }

	protected void onChageViewMode() {
		if (mViewMode == VIEW_MONTH) {
			mViewMode = VIEW_YEAR;
		} else {
			mViewMode = VIEW_MONTH;
		}
		
		setTitleBtnName();
		getData();
		updateChildView();
	}
}