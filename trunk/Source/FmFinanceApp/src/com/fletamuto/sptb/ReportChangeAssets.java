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
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.Revenue;

public class ReportChangeAssets extends FmBaseActivity {
	private LineGraph lg;
	private int mYear = Calendar.getInstance().get(Calendar.YEAR);
	private long mMonthAmount[] = new long[12];
	private long mTotalAmount = 0L;
	private long mStartAmount = 0L;
	ArrayList<String> mMonthArr = new ArrayList<String>();
	ArrayList<Long> mMonthCategoryAmount = new ArrayList<Long>();
	
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_assets_change, true);
        
        setBtnClickListener();
        getData();
        updateChildView();
    }
    
    protected void clearMonthAmount() {
    	mStartAmount = 0L;
    	mTotalAmount = 0L;
    	mMonthCategoryAmount.clear();
    	for (int month = 0; month < 12; month++){
    		mMonthAmount[month] = 0L;
		}
    }
    
    private void getData() {
    	clearMonthAmount();
    	ArrayList<Category> assetsCategory = DBMgr.getCategory(AssetsItem.TYPE);
    	int size = assetsCategory.size();
    	
    	// 속도 개선 필요 //////////
    	for (int index = 0; index < size; index++) {
    		ArrayList<Long> categoryAmount = DBMgr.getLastAmountMonthInYear(assetsCategory.get(index).getID(), mYear);
    		for (int month = 0; month < 12; month++){
    			mMonthAmount[month] += categoryAmount.get(month);
    		}
    	}
    	
    	for (int month = 0; month < 12; month++){
    		mMonthCategoryAmount.add(mMonthAmount[month]);
    		
    		if (mStartAmount == 0L && mMonthAmount[month] != 0L) {
				mStartAmount = mMonthAmount[month];
			}
		}
    	
    	mTotalAmount = DBMgr.getTotalAmount(AssetsItem.TYPE);
	}

	@Override
    protected void initialize() {
        
        String []monthInYear = getResources().getStringArray(R.array.year_in_month_name);
		for (int monthIndex = 0; monthIndex < monthInYear.length; monthIndex++) {
			mMonthArr.add(monthInYear[monthIndex]);
		}
        
    	super.initialize();
    }
    
    @Override
    protected void setTitleBtn() {
    	setTitle("자산변동 추이");
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
		
		TextView tvTotalAmount = (TextView) findViewById(R.id.TVTotalAssetsAmount);
		tvTotalAmount.setText(String.format("자산:%,d원", mTotalAmount));
		
		TextView tvPersent = (TextView) findViewById(R.id.TVPercentAssetsAmount);
		tvPersent.setText("년간 손익율:" + Revenue.getString(mStartAmount, mTotalAmount));
		
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