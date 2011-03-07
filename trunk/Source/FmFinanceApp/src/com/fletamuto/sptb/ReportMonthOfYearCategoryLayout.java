package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.common.control.fmgraph.LineGraph;
import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ViewHolder;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportMonthOfYearCategoryLayout extends FmBaseActivity {
	private static final int MOVE_SENSITIVITY = ItemDef.MOVE_SENSITIVITY;
	protected static final int VIEW_MONTH = 0;
	protected static final int VIEW_YEAR = 1;
	private static final int CATEGORY_ALL = 0;
	private static final int CATEGORY_MAIN = 1;
	private static final int CATEGORY_SUB = 2;
	
	private ArrayList<Long> mItemAmount = new ArrayList<Long>();
	protected ArrayList<MonthAmountItem> mMonthlyItems = new ArrayList<MonthAmountItem>();
	private ArrayList<String> mMonthName = new ArrayList<String>();
	private LineGraph mLineGraph;
	private Calendar mMonthCalendar = Calendar.getInstance();
	protected int mYear = Calendar.getInstance().get(Calendar.YEAR);
	private int mItemType = ExpenseItem.TYPE;
	
	private float mTouchMove;
	private boolean mTouchMoveFlag = false;
	private int mPeriodTerm = ItemDef.BASE_PERIOD_MONTH_TERM;
	private Category mMainCategory = null;
	private Category mSubCategory = null;
	private int mCategoryMode = CATEGORY_ALL;
	private int mViewMode = VIEW_MONTH;
	
	protected ReportMonthlyItemAdapter mMonthlyAdapter = null;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_of_year_category, true);
    	
    	getDate();
    	updateChildView();
    }
    
    protected void setMonthlyAdapterList() {
    	final ListView listMonthItem = (ListView)findViewById(R.id.LVMonthItem);
    	mMonthlyAdapter = new ReportMonthlyItemAdapter(this, R.layout.report_month_of_year_category_list_item, mMonthlyItems);
    	listMonthItem.setAdapter(mMonthlyAdapter);
    	
    	listMonthItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				MonthAmountItem monthAmount = mMonthlyAdapter.getItem(position);

				Intent intent = null;
				
				if (mItemType == ExpenseItem.TYPE) {
					intent = new Intent(ReportMonthOfYearCategoryLayout.this, ReportExpenseExpandLayout.class);
				}
				else if (mItemType == IncomeItem.TYPE) {
					intent = new Intent(ReportMonthOfYearCategoryLayout.this, ReportIncomeExpandLayout.class);
				}
				else {
					return;
				}
					
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, monthAmount.mMonth);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, monthAmount.mYear);
				
				if (mCategoryMode == CATEGORY_MAIN) {
					intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
					intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
				}
				else if (mCategoryMode == CATEGORY_SUB) {
					intent.putExtra(MsgDef.ExtraNames.CATEGORY_SUB_ID, mSubCategory.getID());
					intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mSubCategory.getName());
				}
				
				startActivity(intent);
			}
		});
//    	
//    	listMonthItem.setOnTouchListener(new View.OnTouchListener() {
//			
//			public boolean onTouch(View v, MotionEvent event) {
//				setMoveViewMotionEvent(event);
//		    	return false;
//			}
//		});
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
    	
    	mItemType = getIntent().getIntExtra(MsgDef.ExtraNames.ITEM_TYPE, ExpenseItem.TYPE);
    	int mainCategory = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
    	int subCategory = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_SUB_ID, -1);
    	
    	if (mainCategory == -1 && subCategory == -1) {
    		mCategoryMode = CATEGORY_ALL;
    	}
    	else {
    		if (mainCategory != -1) {
    			mCategoryMode = CATEGORY_MAIN;
    			mMainCategory = DBMgr.getCategoryFromID(mItemType, mainCategory);
    		}
    		else {
    			mCategoryMode = CATEGORY_SUB;
    			mSubCategory = DBMgr.getSubCategoryFromID(mItemType, subCategory);
    		}
    	}
    	super.initialize();
    }
    
	@Override
	protected void setTitleBtn() {
		if (mMainCategory != null) {
			setTitle(mMainCategory.getName() + " 변동내역");
		}
		else if (mSubCategory != null) {
			setTitle(mSubCategory.getName() + " 변동내역");
		}
		else {
			if (mItemType == IncomeItem.TYPE) {
				setTitle("수입 변동내역");
			} 
			else if (mItemType == ExpenseItem.TYPE) {
				setTitle("지출 변동내역");
			} 
			else if (mItemType == AssetsItem.TYPE) {
				setTitle("자산 변동내역");
			} 
			else if (mItemType == LiabilityItem.TYPE) {
				setTitle("부채 변동내역");
			} 
		}
        
		super.setTitleBtn();
	}

	private void updateChildView() {
		setMonthlyAdapterList();
		updateLineView();
		updateItemList();
	}
	
	public void updateItemList() {
//		TextView tvMonth = (TextView) findViewById(R.id.TVMonth);
//		tvMonth.setText(String.format("%d년 %d월", mMonthItemListDate.get(Calendar.YEAR), mMonthItemListDate.get(Calendar.MONTH)+1));
	}

	public void getDate() {
		getMonthAmount();
		setMonthData();
	}
	
	

	
	public void getMonthAmount() {
		if (mCategoryMode == CATEGORY_ALL) {
			if (mViewMode == VIEW_MONTH) {
				mItemAmount = DBMgr.getTotalAmount(mItemType, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
			}
			else {
				mItemAmount = DBMgr.getTotalAmountMonth(mItemType, mYear);
			}
		}
		else if (mCategoryMode == CATEGORY_MAIN) {
			if (mViewMode == VIEW_MONTH) {
				mItemAmount = DBMgr.getTotalMainCategoryAmount(mItemType, mMainCategory.getID(), mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
			}
			else {
				mItemAmount = DBMgr.getTotalMainCategoryAmount(mItemType, mMainCategory.getID(), mYear);
			}
		}
		else if (mCategoryMode == CATEGORY_SUB) {
			if (mViewMode == VIEW_MONTH) {
				mItemAmount = DBMgr.getTotalSubCategoryAmount(mItemType, mSubCategory.getID(), mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
			}
			else {
				mItemAmount = DBMgr.getTotalSubCategoryAmount(mItemType, mSubCategory.getID(), mYear);
			}	
		}
	}

	private void updateLineView() {
		mLineGraph = (LineGraph) findViewById (R.id.lgraph);
		mLineGraph.makeUserTypeGraph(mItemAmount, null, null, mMonthName);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLineGraph.getLayoutParams();
		
		params.width = (320 > mLineGraph.getLineGraphWidth()) ? 320 : mLineGraph.getLineGraphWidth();
		params.height = (140 > mLineGraph.getLineGraphHeight()) ? 140 : mLineGraph.getLineGraphHeight();
		
		mLineGraph.setLayoutParams(params);
		mLineGraph.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		int sel;
		    		sel = mLineGraph.FindTouchItemID((int)event.getX(), (int)event.getY());
		
		    		if (sel == -1) {
		    			return true;
		    		} else {
		    			return true;
		    		}
		    	}
				return false;
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
    			moveCurrentDate(1);
    		}
    		if (mTouchMove-event.getX()> MOVE_SENSITIVITY) {
    			mTouchMoveFlag = false;
    			moveCurrentDate(-1);
    		}
    	}
    }
	
	protected void moveCurrentDate(int dayValue) {
//		mYear += dayValue;
//		
//		getDate();
//    	updateChildView();
	}
	
	private void setMonthData() {
		mMonthName.clear();
		mMonthlyItems.clear();
		
		if (mViewMode == VIEW_MONTH) {
			int year = mMonthCalendar.get(Calendar.YEAR);
			int month = mMonthCalendar.get(Calendar.MONTH) +1;
			
			int targetMonth = month - mPeriodTerm;
			if (targetMonth <= 0) {
				targetMonth += 12 + 1;
				year--;
			}
			
			for (int index = 0; index < mPeriodTerm; index++) {
				
				if (targetMonth > 12) {
					targetMonth = 1;
					year++;
				}
				
				mMonthName.add(String.format("%d월", targetMonth));
				mMonthlyItems.add(0, new MonthAmountItem(year, targetMonth, mItemAmount.get(index)));
				targetMonth++;
			}
		}
		else {
			for (int index = 0; index < 12; index++) {
				mMonthName.add(String.format("%d월", index+1));
				mMonthlyItems.add(new MonthAmountItem(mYear, index+1, mItemAmount.get(index)));
			}
		}
	}
	
	public class MonthAmountItem {
		int mYear;
		int mMonth;
		long mAmount;
		
		public MonthAmountItem(int year, int month, long amount) {
			mYear = year;
			mMonth = month;
			mAmount = amount;
		}
	}
	
	 public class ReportMonthlyItemAdapter extends ArrayAdapter<MonthAmountItem> {
	    	int mResource;
	    	private LayoutInflater mInflater;

			public ReportMonthlyItemAdapter(Context context, int resource,
					 List<MonthAmountItem> objects) {
				super(context, resource, objects);
				this.mResource = resource;
				mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				MonthAmountItem item = (MonthAmountItem)getItem(position);
				
				if (convertView == null) {
					convertView = mInflater.inflate(mResource, parent, false);
				}
				
				TextView tvMonth = (TextView) convertView.findViewById(R.id.TVMonth);
				tvMonth.setText(item.mMonth+"월");
				TextView tvAmount = (TextView) convertView.findViewById(R.id.TVAmount);
				tvAmount.setText(String.format("%,d원", item.mAmount));
				
				return convertView;
			}
	    }
	 
	
    
}