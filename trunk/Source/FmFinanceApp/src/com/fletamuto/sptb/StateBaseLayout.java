package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.BarGraph;
import com.fletamuto.common.control.fmgraph.Constants;
import com.fletamuto.common.control.fmgraph.LineGraph;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

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
		mAmountMonthInYear = DBMgr.getTotalAssetAmountMonthInYear(mItem.getID(), mYear);
		
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
	
	protected void startChangeStateActivtiy() {
		Intent intent = new Intent(this, InputAssetsLayout.class);
		intent.putExtra(MsgDef.ExtraNames.INPUT_CHANGE_MODE, true);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mItem.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_CHANGE_STATE);
	}

	@Override
	protected void setTitleBtn() {
		setTitle(mItem.getCategory().getName());
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "편집");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
//		setEidtButtonListener();
		
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