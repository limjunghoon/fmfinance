package com.fletamuto.sptb;

import java.util.Calendar;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.common.control.fmgraph.PieGraph;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportMonthCompareIncomeLayout extends FmBaseActivity {
	private Calendar currentCalendar = Calendar.getInstance();
	private long monthTotalIncome = 0L;
	private long monthTotalExpense = 0L;

	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_compare_expense_to_income, true);
    	
    	getData();
    	updateChildView();
    	setButtonClickListener();
    }
    
	private void setButtonClickListener() {
		
		Button btnExpense = (Button)findViewById(R.id.BtnTotalMonthExpense);
		btnExpense.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
			}
		});
		
		Button btnIncome = (Button)findViewById(R.id.BtnTotalMonthIncome);
		btnIncome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
			}
		});
		
	}

	private void getData() {
		monthTotalIncome = DBMgr.getTotalAmountMonth(IncomeItem.TYPE, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH)+1);
		monthTotalExpense = DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH)+1);
	}

	@Override
	protected void setTitleBtn() {
    	setTitle("월 수입");

		super.setTitleBtn();
	}

	private void updateChildView() {
		updateBarGraph();
		
		TextView tvCurrentMonth = (TextView)findViewById(R.id.TVCurrentMonth);
		tvCurrentMonth.setText(String.format("%d년 %d월",  currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH)));
		
		TextView btnProfit = (TextView)findViewById(R.id.TVMonthProfit);
		btnProfit.setText(String.format("소득 : %,d원", monthTotalIncome - monthTotalExpense));
		
		Button btnExpense = (Button)findViewById(R.id.BtnTotalMonthExpense);
		btnExpense.setText(String.format("지출                               %,d원", monthTotalExpense));
		
		Button btnIncome = (Button)findViewById(R.id.BtnTotalMonthIncome);
		btnIncome.setText(String.format("수입                               %,d원", monthTotalIncome));
	}

	private void updateBarGraph() {
		final PieGraph pieGraph;	
       
		pieGraph = (PieGraph) findViewById (R.id.pgraph);
		pieGraph.setItemValues(new long[] {monthTotalIncome, monthTotalExpense});
		pieGraph.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		pieGraph.FindTouchItemID((int)event.getX(), (int)event.getY());
		    		return true;
		    	}
				return false;
			}
		});
		
	}
}