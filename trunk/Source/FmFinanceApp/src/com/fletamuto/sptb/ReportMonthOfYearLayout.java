package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.BarGraph;
import com.fletamuto.common.control.fmgraph.Constants;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportMonthOfYearLayout extends FmBaseActivity {
	private int mYear = Calendar.getInstance().get(Calendar.YEAR);
	
	private BarGraph bg;
	private boolean barGraphDifferenceMode = false;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_of_year, true);
    	
    	updateChildView();
//    	setBunClickListener();
    }
    
	@Override
	protected void setTitleBtn() {
    	setTitle("연간 월별 수입/지출 비교");
    	setChangeButtonListener();
        setTitle(getResources().getString(R.string.btn_category_select));
        setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "변경");
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        
		super.setTitleBtn();
	}
	
	public void setChangeButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if (barGraphDifferenceMode == false) {
					updateBarGraphDifferenceMode();
				} else {
					updateBarGraph();
				}
			}
		});
	}
    
//    private void setBunClickListener() {
//		Button btnPreviusYear = (Button)findViewById(R.id.BtnPreviousYear);
//		btnPreviusYear.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				mYear--;
//				updateChildView();
//			}
//		});
//		
//		Button btnNextYear = (Button)findViewById(R.id.BtnNextYear);
//		btnNextYear.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				mYear++;
//				updateChildView();
//			}
//		});
//	}

	private void updateChildView() {
		updateBarGraph();
		
		TextView tvCurrent = (TextView)findViewById(R.id.TVCurrentYear);
		tvCurrent.setText(String.format("%d 년", mYear));
		
		long expenseYear = DBMgr.getTotalAmountYear(ExpenseItem.TYPE, mYear);
		long incomeYear = DBMgr.getTotalAmountYear(IncomeItem.TYPE, mYear);
		
		TextView tvIncomeYear = (TextView)findViewById(R.id.TVTotalIncome);
		tvIncomeYear.setText(String.format("총 수입 : %,d원", incomeYear));
		TextView tvExpenseYear = (TextView)findViewById(R.id.TVTotalExpense);
		tvExpenseYear.setText(String.format("총 지출 : %,d원", expenseYear));
	}

	private void updateBarGraph() {
		barGraphDifferenceMode = false;
	
		ArrayList<Long> iv = new ArrayList<Long>();
		ArrayList<Integer> ap = new ArrayList<Integer>();
		ArrayList<String> at = new ArrayList<String>();
		
		for (int i=0; i<12; i++) {
			iv.add(DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, i+1));
			iv.add(DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, i+1));
			at.add(String.valueOf(i+1));
		}
		
		ap.add(Constants.BAR_AXIS_X_BOTTOM);
		ap.add(Constants.BAR_AXIS_Y_LEFT);
    
        bg = (BarGraph) findViewById (R.id.bgraph);
 
        bg.makeUserTypeGraph(ap, Constants.BAR_AXIS_X_BOTTOM, iv, 2, at);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bg.getLayoutParams();

        params.width = (320 > bg.getBarGraphWidth()) ? 320 : bg.getBarGraphWidth();
        params.height = (350 > bg.getBarGraphHeight()) ? 320 : bg.getBarGraphHeight();
       
        bg.setLayoutParams(params);
        
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
	
	private void updateBarGraphDifferenceMode() {
		barGraphDifferenceMode = true;
		
		ArrayList<Long> iv = new ArrayList<Long>();
		ArrayList<Integer> ap = new ArrayList<Integer>();
		ArrayList<String> at = new ArrayList<String>();
		
			for (int i=0; i<12; i++) {
				iv.add(DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, i+1) - DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, i+1));
				at.add(String.valueOf(i+1));
			}
			
			ap.add(Constants.BAR_AXIS_X_CENTER);
			ap.add(Constants.BAR_AXIS_Y_LEFT);

			//        setContentView(R.layout.report_month_of_year, true);
        
        bg = (BarGraph) findViewById (R.id.bgraph);
 
        bg.makeUserTypeGraph(ap, Constants.BAR_AXIS_X_CENTER, iv, 1, at);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bg.getLayoutParams();

        params.width = (320 > bg.getBarGraphWidth()) ? 320 : bg.getBarGraphWidth();
        params.height = (350 > bg.getBarGraphHeight()) ? 320 : bg.getBarGraphHeight();
       
        bg.setLayoutParams(params);
        
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
}