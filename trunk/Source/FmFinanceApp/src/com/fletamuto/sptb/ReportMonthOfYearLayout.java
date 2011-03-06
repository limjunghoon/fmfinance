package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.LineGraph;
import com.fletamuto.sptb.data.AssetsChangeItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportMonthOfYearLayout extends FmBaseActivity {
	private static final int MOVE_SENSITIVITY = ItemDef.MOVE_SENSITIVITY;
	public static final int VIEW_INCOME_EXPENSE = 0;
	public static final int VIEW_ASSETS = 1;
	public static final int VIEW_BUDGET = 2;
	
	
	private ArrayList<Long> mItem1 = new ArrayList<Long>();
	private ArrayList<Long> mItem2 = new ArrayList<Long>();
	private ArrayList<Long> mItemDifference = new ArrayList<Long>();
	private LineGraph mLineGraph;
	private ArrayList<String> mMonthName = new ArrayList<String>();
	
	private Calendar mMonthCalendar = Calendar.getInstance();
	private int mViewMode = VIEW_INCOME_EXPENSE;
	
	private float mTouchMove;
	private boolean mTouchMoveFlag = false;
	private int mPeriodTerm = ItemDef.BASE_PERIOD_MONTH_TERM;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_of_year, true);
    	
    	
    	getDate();
    	updateChildView();
//    	setBunClickListener();
    	
    	findViewById(R.id.ScrollView01).setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				setMoveViewMotionEvent(event);
		    	return false;
			}
		});

    }
    
    @Override
    protected void initialize() {
    	mViewMode = getIntent().getIntExtra(MsgDef.ExtraNames.VIEW_MODE, VIEW_INCOME_EXPENSE);
    	super.initialize();
    }
    
	@Override
	protected void setTitleBtn() {
		if (mViewMode == VIEW_ASSETS) {
			setTitle("자산  변동추이");
		}
		else if (mViewMode == VIEW_BUDGET) {
			setTitle("예산 변동추이");
		}
		else {
			setTitle("수입/지출 변동추이");
		}
        
		super.setTitleBtn();
	}

	private void updateChildView() {
		updateLineView();
		
		TextView tvCurrent = (TextView)findViewById(R.id.TVCurrentYear);
		tvCurrent.setText(String.format("%d 년", mMonthCalendar.get(Calendar.YEAR)));
		
		addItemInfoList();
		
//		long expenseYear = DBMgr.getTotalAmountYear(ExpenseItem.TYPE, mYear);
//		long incomeYear = DBMgr.getTotalAmountYear(IncomeItem.TYPE, mYear);
//		
//		TextView tvIncomeYear = (TextView)findViewById(R.id.TVTotalIncome);
//		tvIncomeYear.setText(String.format("총 수입 : %,d원", incomeYear));
//		TextView tvExpenseYear = (TextView)findViewById(R.id.TVTotalExpense);
//		tvExpenseYear.setText(String.format("총 지출 : %,d원", expenseYear));
	}
	
	public void getDate() {
		
		setMonthName();
		
		
		if (mViewMode == VIEW_INCOME_EXPENSE) {
//			mItem1 = DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear);
//			mItem2 = DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear);
			
			mItem1 = DBMgr.getTotalAmount(IncomeItem.TYPE, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
			mItem2 = DBMgr.getTotalAmount(ExpenseItem.TYPE, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
		} 
		else if (mViewMode == VIEW_ASSETS) {
			mItem1 = DBMgr.getTotalAmountMonth(AssetsItem.TYPE, mMonthCalendar.get(Calendar.YEAR));
			mItem2 = DBMgr.getTotalAmountMonth(LiabilityItem.TYPE, mMonthCalendar.get(Calendar.YEAR));
		}
		else if (mViewMode == VIEW_BUDGET) {
		//	mItem1 = DBMgr.getTotalAmountMonth(AssetsItem.TYPE, mYear);
			//mItem2 = DBMgr.getTotalAmountMonth(LiabilityItem.TYPE, mYear);
		}
		else {
			mItem1 = DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mMonthCalendar.get(Calendar.YEAR));
			mItem2 = DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mMonthCalendar.get(Calendar.YEAR));
		}
		
		
		int size = mItem1.size();
		mItemDifference.clear();
		for (int index = 0; index < size; index++) {
			mItemDifference.add(mItem1.get(index)- mItem2.get(index));
		}
	}

	
	private void setMonthName() {
		mMonthName.clear();
		
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
			targetMonth++;
		}
	}

	private void updateLineView() {
		mLineGraph = (LineGraph) findViewById (R.id.lgraph);
		mLineGraph.makeUserTypeGraph(mItem1, mItem2, mItemDifference, mMonthName);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLineGraph.getLayoutParams();
		
		params.width = (320 > mLineGraph.getLineGraphWidth()) ? 320 : mLineGraph.getLineGraphWidth();
		params.height = (180 > mLineGraph.getLineGraphHeight()) ? 180 : mLineGraph.getLineGraphHeight();
		
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
	
	protected void addItemInfoList() {
		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLAddView);
		llPayment.removeAllViewsInLayout();
		
		int size = mMonthName.size();
		for (int index = 0; index < size; index++) {
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.report_list_normal2, null);
			
			setListViewText(llMember, size-index-1);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			llPayment.addView(llMember, params);
		}
	}

	protected void setListViewText(LinearLayout llMember, final int index) {
		if (mViewMode == VIEW_ASSETS) {
			((TextView)llMember.findViewById(R.id.TVListLeftTop)).setText(mMonthName.get(index));
			((TextView)llMember.findViewById(R.id.TVListRightTop)).setText(String.format("%,d원", mItemDifference.get(index)));
			((TextView)llMember.findViewById(R.id.TVListLeftBottom)).setText(String.format("%,d원", mItem1.get(index)));
			((TextView)llMember.findViewById(R.id.TVListRightBottom)).setText(String.format("%,d원", mItem2.get(index)));
		}
		else if (mViewMode == VIEW_BUDGET) {
		}
		else {
			((TextView)llMember.findViewById(R.id.TVListLeftTop)).setText(mMonthName.get(index));
			((TextView)llMember.findViewById(R.id.TVListRightTop)).setText(String.format("%,d원", mItemDifference.get(index)));
			((TextView)llMember.findViewById(R.id.TVListLeftBottom)).setText(String.format("%,d원", mItem1.get(index)));
			((TextView)llMember.findViewById(R.id.TVListRightBottom)).setText(String.format("%,d원", mItem2.get(index)));
		}
		
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
		mMonthCalendar.add(Calendar.MONTH, dayValue);
		
		getDate();
    	updateChildView();
	}
}