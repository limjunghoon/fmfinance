package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import com.fletamuto.common.control.fmgraph.BarGraph;
import com.fletamuto.common.control.fmgraph.Constants;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.BudgetItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.view.FmBaseLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReportBudgetLayout extends FmBaseActivity {
	private static final int MOVE_SENSITIVITY = ItemDef.MOVE_SENSITIVITY;
	
	private Calendar mMonthCalendar = Calendar.getInstance();
	private float mTouchMove;
	private boolean mTouchMoveFlag = false;
	
	protected ArrayList<BudgetItem> mBudgetItems = new ArrayList<BudgetItem>();
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_budget, true);
    	
    	getDate();
    	updateChildView();
    	
    	setListener();
    }
    
	private void setListener() {
		
	}

	public void updateChildView() {
		updateBarGraph();
		
		TextView tvDate = (TextView) findViewById(R.id.TVSettleDate);
		tvDate.setText(String.format("%d년 %d월", mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1));
	}

	public void getDate() {
		mBudgetItems = DBMgr.getBudgetItems(mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1);
	}

	@Override
	protected void setTitleBtn() {
    	setTitle("예산");
        
		super.setTitleBtn();
	}
	
	private void updateBarGraph() {
		final BarGraph bg;
	
		ArrayList<Long> iv = new ArrayList<Long>();
		ArrayList<Integer> ap = new ArrayList<Integer>();
		ArrayList<String> at = new ArrayList<String>();
		
		int size = mBudgetItems.size();
		for (int index = 0; index < size; index++) {
			BudgetItem budget = mBudgetItems.get(index);
			
			iv.add(budget.getAmount());
			iv.add(budget.getExpenseAmountMonth());
			
			if (budget.getExpenseCategory() == null) {
				at.add("총 예산");
			}
			else {
				at.add(budget.getExpenseCategory().getName());
			}
		}
		ap.add(Constants.BAR_AXIS_X_BOTTOM);
		ap.add(Constants.BAR_AXIS_Y_LEFT);
    
        bg = (BarGraph) findViewById (R.id.bgraph);
 
        bg.makeUserTypeGraph(ap, Constants.BAR_AXIS_X_BOTTOM, iv, 2, at);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bg.getLayoutParams();

        params.width = (320 > bg.getBarGraphWidth()) ? 320 : bg.getBarGraphWidth();
        params.height = (250 > bg.getBarGraphHeight()) ? 250 : bg.getBarGraphHeight();
       
        bg.setLayoutParams(params);
        
        //Bar 그래프 세부 보정
        bg.setBarWidth(20);
        bg.setbargroupAndBargroupGap(30);
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
		mMonthCalendar.add(Calendar.MONTH, dayValue);
		
		getDate();
    	updateChildView();
	}
	
}