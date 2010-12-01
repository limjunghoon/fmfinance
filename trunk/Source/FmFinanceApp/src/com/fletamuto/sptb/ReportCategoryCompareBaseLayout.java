package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.LineGraph;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class ReportCategoryCompareBaseLayout extends FmBaseActivity {  	
	private LineGraph lg;
	private int mCategoryID = -1;
	private String mCategoryName;
	private int mItemType = -1;
	private int mYear = Calendar.getInstance().get(Calendar.YEAR);
	ArrayList<String> mMonthArr = new ArrayList<String>();
	ArrayList<Long> mMonthCategoryAmount = null;
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_categoyr_compare_base, true);
        
        setBtnClickListener();
        getData();
        updateChildView();
    }
    
    private void getData() {
    	if (mItemType == -1) return;
    	mMonthCategoryAmount = DBMgr.getTotalAmountMonthInYear(mItemType, mCategoryID, mYear);
	}

	@Override
    protected void initialize() {
		mItemType = getIntent().getIntExtra(MsgDef.ExtraNames.ITEM_TYPE, -1);
    	mCategoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
        mCategoryName = getIntent().getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME);
        
        String []monthInYear = getResources().getStringArray(R.array.year_in_month_name);
		for (int monthIndex = 0; monthIndex < monthInYear.length; monthIndex++) {
			mMonthArr.add(monthInYear[monthIndex]);
		}
        
    	super.initialize();
    }
    
    @Override
    protected void setTitleBtn() {
    	setTitle(mCategoryName);
    	super.setTitleBtn();
    }
    
    protected void setBtnClickListener() {
    	Button btnPreviousMonth = (Button)findViewById(R.id.BtnPreviusYear);
		Button btnNextMonth = (Button)findViewById(R.id.BtnNextYear);
		
		btnPreviousMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				movePreviousYear();
			}
		});
		
		btnNextMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveNextYear();
			}
		});
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
	
	private void updateChildView() {
		TextView tvYear = (TextView) findViewById(R.id.TVCurrentYear);
		tvYear.setText(String.format("%d년", mYear));
		
		updateLineView();
	}

	private void updateLineView() {
		if (mMonthCategoryAmount == null) return;
		
		lg = (LineGraph) findViewById (R.id.lgraph);
		lg.makeUserTypeGraph(mMonthCategoryAmount, null, null, mMonthArr);
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lg.getLayoutParams();
		
		params.width = (320 > lg.getLineGraphWidth()) ? 320 : lg.getLineGraphWidth();
		params.height = (350 > lg.getLineGraphHeight()) ? 320 : lg.getLineGraphHeight();
		
		lg.setLayoutParams(params);
		lg.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		int sel;
		    		sel = lg.FindTouchItemID((int)event.getX(), (int)event.getY());
		
		    		if (sel == -1) {
		    			return true;
		    		} else {
		    			Toast.makeText(lg.getContext(), "ID = " + sel + " 그래프 터치됨", Toast.LENGTH_SHORT).show();
		    			return true;
		    		}
		    	}
				return false;
			}
		});
	}

	
}