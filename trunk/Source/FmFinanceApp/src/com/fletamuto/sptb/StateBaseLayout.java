package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.LineGraph;
import com.fletamuto.sptb.data.FinanceItem;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public abstract class StateBaseLayout extends FmBaseActivity {  
	protected FinanceItem mItem;
	protected ArrayList<Long> mAmountMonthInYear; 
	ArrayList<String> mMonthNameArr = new ArrayList<String>();
	private LineGraph mLineGraph;
	int mYear = Calendar.getInstance().get(Calendar.YEAR);
	
	protected abstract void startChangeStateActivtiy();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.state_view, true);
        
        setBtnClickListener();
        updateChildView();
        updateLineView();
    }
	
	
	@Override
	protected void initialize() {
		super.initialize();
		
		mItem = (FinanceItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		
		String []monthInYear = getResources().getStringArray(R.array.year_in_month_name);
		for (int monthIndex = 0; monthIndex < monthInYear.length; monthIndex++) {
			mMonthNameArr.add(monthInYear[monthIndex]);
		}
	}
	
	private void setBtnClickListener() {
		findViewById(R.id.BtnStateAdd).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startChangeStateActivtiy();
			}
		});
	}
	


	@Override
	protected void setTitleBtn() {
		setTitle(mItem.getCategory().getName());
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "편집");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		
		
		super.setTitleBtn();
	}
	
	private void updateLineView() {
		if (mAmountMonthInYear == null) return;
		
		mLineGraph = (LineGraph) findViewById (R.id.lgraph);
		mLineGraph.makeUserTypeGraph(mAmountMonthInYear, null, null, mMonthNameArr);
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLineGraph.getLayoutParams();
		
		params.width = (320 > mLineGraph.getLineGraphWidth()) ? 320 : mLineGraph.getLineGraphWidth();
		params.height = (350 > mLineGraph.getLineGraphHeight()) ? 320 : mLineGraph.getLineGraphHeight();
		
		mLineGraph.setLayoutParams(params);
		mLineGraph.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		int sel;
		    		sel = mLineGraph.FindTouchItemID((int)event.getX(), (int)event.getY());
		
		    		if (sel == -1) {
		    			return true;
		    		} else {
		    			Toast.makeText(mLineGraph.getContext(), "ID = " + sel + " 그래프 터치됨", Toast.LENGTH_SHORT).show();
		    			return true;
		    		}
		    	}
				return false;
			}
		});
	}
	
	protected void updateChildView() {
		TextView tvAmount = (TextView)findViewById(R.id.TVStateTitle);
		tvAmount.setText(String.format("현재가 : %,d원", mItem.getAmount()));
	}

}