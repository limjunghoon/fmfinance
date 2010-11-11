package com.fletamuto.sptb;

import java.util.Calendar;

import com.fletamuto.common.control.fmgraph.BarGraph;
import com.fletamuto.common.control.fmgraph.Constants;
import com.fletamuto.common.control.fmgraph.PieGraph;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ReportMonthOfYearLayout extends FmBaseActivity {
	private PieGraph pg;
	private BarGraph bg;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
//    	
//    	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//    	for (int index = 0; index < Calendar.DECEMBER; index++) {
//    		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, currentYear, index);
//    	}
    	
    	long[] iv = new long[] {1000, 1000, 1000, 1000, 1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000};
//    	
//        long[] iv = new long[] {
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 1), 
// //       		DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 1), 
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 2), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 2), 
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 3), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 3), 
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 4), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 4), 
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 5), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 5), 
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 6), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 6), 
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 7), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 7),
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 8), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 8), 
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 9), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 9), 
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 10), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 10),
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 11), 
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 11),
//        		DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, 2010, 12)};
//        		//DBMgr.getTotalAmountMonth(IncomeItem.TYPE, 2010, 12)}; 
//        
//      
       
        int[] ap = new int[] {Constants.BAR_AXIS_X_BOTTOM, Constants.BAR_AXIS_Y_LEFT};
        String[] at = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        setContentView(R.layout.bar_graph, true);
        
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
}