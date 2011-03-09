package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import com.fletamuto.common.control.fmgraph.BarGraph;
import com.fletamuto.common.control.fmgraph.Constants;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.view.FmBaseLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReportSettleAccountsLayout extends FmBaseActivity {
	private static final int MOVE_SENSITIVITY = ItemDef.MOVE_SENSITIVITY;
	
	private static final int VIEW_MONTH = 0; 
	private static final int VIEW_YEAR = 1;
	
	private Calendar mMonthCalendar = Calendar.getInstance();
	private int mYear = Calendar.getInstance().get(Calendar.YEAR);
	
	private long mIncomeAmount = 0L;
	private long mExpenseAmount = 0L;
	private long mAssetsAmount = 0L;
	private long mLiabilityAmount = 0L;
	private long mBudgetAmount = 0L;
	
	private int mViewMode = VIEW_MONTH;
	
	private float mTouchMove;
	private boolean mTouchMoveFlag = false;
	
	private BarGraph bg;
	
	private ArrayList<Long> iv = new ArrayList<Long>();
	private ArrayList<Integer> ap = new ArrayList<Integer>();
	private ArrayList<String> at = new ArrayList<String>();
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_settle_accounts, true);
    	
    	getDate();
    	updateChildView();
    	
    	setListener();
    }
    
	private void setListener() {
		findViewById(R.id.LLSettleIncome).setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				setMoveViewMotionEvent(event);
		    	return false;
			}
		});
		
		findViewById(R.id.LLSettleIncome).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if (mTouchMoveFlag == false) 
					return;
				
				Intent intent = new Intent(ReportSettleAccountsLayout.this, ReportMonthOfYearLayout.class);
				intent.putExtra(MsgDef.ExtraNames.VIEW_MODE, ReportMonthOfYearLayout.VIEW_INCOME_EXPENSE);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.LLSettleBudget).setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				setMoveViewMotionEvent(event);
		    	return false;
			}
		});
		
		findViewById(R.id.LLSettleBudget).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if (mTouchMoveFlag == false) 
					return;
				
				Intent intent = new Intent(ReportSettleAccountsLayout.this, ReportMonthOfYearLayout.class);
				intent.putExtra(MsgDef.ExtraNames.VIEW_MODE, ReportMonthOfYearLayout.VIEW_BUDGET);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.LLSettleAssets).setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				setMoveViewMotionEvent(event);
		    	return false;
			}
		});
		
		findViewById(R.id.LLSettleAssets).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if (mTouchMoveFlag == false) 
					return;
				
				Intent intent = new Intent(ReportSettleAccountsLayout.this, ReportMonthOfYearLayout.class);
				intent.putExtra(MsgDef.ExtraNames.VIEW_MODE, ReportMonthOfYearLayout.VIEW_ASSETS);
				startActivity(intent);
			}
		});
		
		changeBtnListener();
	}

	public void updateChildView() {
		updateBarGraph();
		
		TextView tvDate = (TextView) findViewById(R.id.TVSettleDate);
		if (mViewMode == VIEW_MONTH) {
			tvDate.setText(String.format("%d년 %d월", mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1));
			setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "년");
		} else {
			tvDate.setText(String.format("%d년", mYear));
			setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "월");
		}
		
		
				
		TextView tvIncome = (TextView) findViewById(R.id.TVSettleIncomeAmount);
		tvIncome.setText(String.format("%,d원", mIncomeAmount));
		((ImageView) findViewById (R.id.IVsettleIncomeColor)).setBackgroundColor(bg.getDefaultGraphColors(0));
		
		TextView tvExpense = (TextView) findViewById(R.id.TVSettleExpenseAmount);
		tvExpense.setText(String.format("%,d원", mExpenseAmount));
		((ImageView) findViewById (R.id.IVsettleExpenseColor)).setBackgroundColor(bg.getDefaultGraphColors(1));
		
		TextView tvIncomeExpenseDifference = (TextView) findViewById(R.id.TVSettleIncomeExpenseDifference);
		tvIncomeExpenseDifference.setText(String.format("%,d원", mIncomeAmount - mExpenseAmount));
		
		TextView tvBudgetAmount = (TextView) findViewById(R.id.TVSettleBudgetAmount);
		tvBudgetAmount.setText(String.format("%,d원", mBudgetAmount));
		((ImageView) findViewById (R.id.IVsettleBudgetColor)).setBackgroundColor(bg.getDefaultGraphColors(2));
		
		TextView tvBudgetExpenseAmount = (TextView) findViewById(R.id.TVSettleBudgetExpeseAmount);
		tvBudgetExpenseAmount.setText(String.format("%,d원", mExpenseAmount));
		((ImageView) findViewById (R.id.IVsettleBudgetExpenseColor)).setBackgroundColor(bg.getDefaultGraphColors(3));
		
		TextView tvBudgetBudgetDifference = (TextView) findViewById(R.id.TVSettleBudgetDifference);
		tvBudgetBudgetDifference.setText(String.format("%,d원", mBudgetAmount - mExpenseAmount));
		
		TextView tvAssets = (TextView) findViewById(R.id.TVSettleAssetsAmount);
		tvAssets.setText(String.format("%,d원", mAssetsAmount));
		((ImageView) findViewById (R.id.IVsettleAssetsColor)).setBackgroundColor(bg.getDefaultGraphColors(4));
		
		TextView tvLiability = (TextView) findViewById(R.id.TVSettleLiabilityAmount);
		tvLiability.setText(String.format("%,d원", mLiabilityAmount));
		((ImageView) findViewById (R.id.IVsettleLiabilityColor)).setBackgroundColor(bg.getDefaultGraphColors(5));
		
		TextView tvAssetsLiabilityDifference = (TextView) findViewById(R.id.TVSettleAssetsLiabilityDifference);
		tvAssetsLiabilityDifference.setText(String.format("%,d원", mAssetsAmount - mLiabilityAmount));
	}

	public void getDate() {
		if (mViewMode == VIEW_MONTH) {
			mIncomeAmount = DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1);
			mExpenseAmount = DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1);
			mAssetsAmount = DBMgr.getTotalAmountMonth(AssetsItem.TYPE, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1);
			mLiabilityAmount = DBMgr.getTotalAmountMonth(LiabilityItem.TYPE, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1);
		}
		else {
			mIncomeAmount = DBMgr.getTotalAmountYear(IncomeItem.TYPE, mYear);
			mExpenseAmount = DBMgr.getTotalAmountYear(ExpenseItem.TYPE, mYear);
			mAssetsAmount = DBMgr.getTotalAmountYear(AssetsItem.TYPE, mYear);
			mLiabilityAmount = DBMgr.getTotalAmountYear(LiabilityItem.TYPE, mYear);
		}
		
	}

	@Override
	protected void setTitleBtn() {
    	setTitle("결산");
        
    	setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "편집");
		setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
		
		super.setTitleBtn();
	}
	
	private void changeBtnListener() {
		setTitleButtonListener(FmBaseLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				if (mViewMode == VIEW_MONTH) {
					mViewMode = VIEW_YEAR;
				} else {
					mViewMode = VIEW_MONTH;
				}
				
				chageView();
			}
		});
	}

	public void chageView() {
		getDate();
    	updateChildView();
	}

	private void updateBarGraph() {
		//전역으로 이동
//		final BarGraph bg;
	
//		ArrayList<Long> iv = new ArrayList<Long>();
//		ArrayList<Integer> ap = new ArrayList<Integer>();
//		ArrayList<String> at = new ArrayList<String>();
		
		iv.add(mIncomeAmount);
		iv.add(mExpenseAmount);
		at.add("수입/지출");
		
		iv.add(mBudgetAmount);
		iv.add(mExpenseAmount);
		at.add("예산/지출");
		
		iv.add(mAssetsAmount);
		iv.add(mLiabilityAmount);
		at.add("자산/부채");
		
		ap.add(Constants.BAR_AXIS_X_BOTTOM);
//		ap.add(Constants.BAR_AXIS_Y_LEFT);
    
        bg = (BarGraph) findViewById (R.id.bgraph);
 
        bg.makeUserTypeGraph(ap, Constants.BAR_AXIS_X_BOTTOM, iv, 2, at);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bg.getLayoutParams();

        params.width = (320 > bg.getBarGraphWidth()) ? 320 : bg.getBarGraphWidth();
        params.height = (150 > bg.getBarGraphHeight()) ? 150 : bg.getBarGraphHeight();
       
        bg.setLayoutParams(params);
        
        //Bar 그래프 세부 보정
        bg.setBarWidth(20);
        bg.setbargroupAndBargroupGap(55);
        bg.setDetailedMoveAllBars(-40);
        bg.setDetailedMoveTitles(-23);
        
        bg.setOnTouchListener(new View.OnTouchListener() {
			
        	public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		int sel;
		    		sel = bg.FindTouchItemID((int)event.getX(), (int)event.getY());

		    		if (sel == -1) {
		    			return true;
		    		} else {
		    			Toast.makeText(bg.getContext(), "ID = " + sel + " 그래프 터치됨", Toast.LENGTH_SHORT).show();
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
		if (mViewMode == VIEW_MONTH) {
			mMonthCalendar.add(Calendar.MONTH, dayValue);
		}
		else {
			mYear += dayValue;
		}
		
		getDate();
    	updateChildView();
	}
	
}