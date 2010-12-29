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
	int mGraphHeigth = 280;
	
	protected abstract void startChangeStateActivtiy();
	protected abstract void getData();
	protected abstract Class<?> getActivityClass();
	protected abstract void onDeleteBtnClick();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.state_view, true);
        
        setBtnClickListener();
        updateChildView();
    }
	
	public FinanceItem getItem() {
		return mItem;
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
		findViewById(R.id.BtnStateEdit).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startChangeStateActivtiy();
			}
		});
		
		findViewById(R.id.BtnPreviusYear).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				movePreviousYear();
			}
		});
		
		findViewById(R.id.BtnNextYear).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveNextYear();
			}
		});
		
		findViewById(R.id.BtnStateDelete).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onDeleteBtnClick();
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
	
	protected void updateLineView() {
		if (mAmountMonthInYear == null) return;
		
		mLineGraph = (LineGraph) findViewById (R.id.lgraph);
		mLineGraph.makeUserTypeGraph(mAmountMonthInYear, null, null, mMonthNameArr);
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLineGraph.getLayoutParams();
		
		params.width = (320 > mLineGraph.getLineGraphWidth()) ? 320 : mLineGraph.getLineGraphWidth();
		params.height = (350 > mLineGraph.getLineGraphHeight()) ? mGraphHeigth : mLineGraph.getLineGraphHeight();
		
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
		
		TextView tvYear = (TextView) findViewById(R.id.TVCurrentYear);
		tvYear.setText(String.format("%d년", mYear));
		
		updateLineView();
	}
	
	public void moveNextYear() {
		mYear++;
		getData();
    	updateChildView();
	}

	public void movePreviousYear() {
		mYear--;
		getData();
    	updateChildView();
	}
	
	public void setGraphHeigth(int height) {
		mGraphHeigth = height;
	}
}