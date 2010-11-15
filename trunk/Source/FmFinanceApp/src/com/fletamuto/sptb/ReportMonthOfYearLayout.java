package com.fletamuto.sptb;

import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.BarGraph;
import com.fletamuto.common.control.fmgraph.Constants;
import com.fletamuto.common.control.fmgraph.PieGraph;
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
//				Toast toast = Toast.makeText(getApplicationContext(), "변경버튼 클릭", Toast.LENGTH_SHORT);
//				toast.show();
				if (barGraphDifferenceMode == false)
				{
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
//		PieGraph pg;
//		BarGraph bg;
//    	
//    	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//    	for (int index = 0; index < Calendar.DECEMBER; index++) {
//    		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, currentYear, index);
//    	}
		
		 long[] iv = new long[] {
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 1), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 1), 
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 2), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 2), 
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 3), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 3), 
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 4), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 4), 
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 5), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 5), 
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 6), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 6), 
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 7), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 7),
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 8), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 8), 
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 9), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 9), 
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 10), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 10),
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 11), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 11),
	        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 12), 
	        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 12)}; 
	        
	      
	       
	        int[] ap = new int[] {Constants.BAR_AXIS_X_BOTTOM, Constants.BAR_AXIS_Y_LEFT};
	        String[] at = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

//	        setContentView(R.layout.report_month_of_year, true);
	        
	        bg = (BarGraph) findViewById (R.id.bgraph);
	 
	        bg.makeUserTypeGraph(ap, Constants.BAR_AXIS_X_BOTTOM, iv, 2, at);

	        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bg.getLayoutParams();

	        params.width = (320 > bg.getBarGraphWidth()) ? 320 : bg.getBarGraphWidth();
	        params.height = (350 > bg.getBarGraphHeight()) ? 320 : bg.getBarGraphHeight();
	       
	        bg.setLayoutParams(params);
	        
	/*
	        pg = (PieGraph) findViewById (R.id.pgraph);
	        pg.setItemValues(iv);
	        pg.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
			    		int sel;
			    		sel = pg.FindTouchItemID((int)event.getX(), (int)event.getY());
			    		Log.d("jptest1", "onTouchEvent = " + sel);
			    		Log.d("jptest1", "getRawX" + (int)event.getXPrecision());
			    		Log.d("jptest1", "getRawY" + (int)event.getYPrecision());
			    		if (sel == -1) {
			    			return true;
			    		} else {
			    			Toast.makeText(pg.getContext(), "ID = " + sel + " Value = " + pg.getItemValue(sel) + " Color = " + pg.getItemColor(sel), Toast.LENGTH_SHORT).show();
			    			return true;
			    		}
			    	}
					return false;
				}
			});
	*/
		
	}
	
	private void updateBarGraphDifferenceMode() {
		barGraphDifferenceMode = true;
		
		long[] iv = new long[] {
			 	DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 1) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 1), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 2) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 2), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 3) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 3), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 4) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 4), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 5) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 5), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 6) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 6), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 7) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 7), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 8) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 8), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 9) - 
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 9), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 10) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 10), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 11) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 11), 
        		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, mYear, 12) -
        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, mYear, 12)}; 
        
      
       
        int[] ap = new int[] {Constants.BAR_AXIS_X_CENTER, Constants.BAR_AXIS_Y_LEFT};
        String[] at = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

//        setContentView(R.layout.report_month_of_year, true);
        
        bg = (BarGraph) findViewById (R.id.bgraph);
 
        bg.makeUserTypeGraph(ap, Constants.BAR_AXIS_X_CENTER, iv, 1, at);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bg.getLayoutParams();

        params.width = (320 > bg.getBarGraphWidth()) ? 320 : bg.getBarGraphWidth();
        params.height = (350 > bg.getBarGraphHeight()) ? 320 : bg.getBarGraphHeight();
       
        bg.setLayoutParams(params);
	}
}