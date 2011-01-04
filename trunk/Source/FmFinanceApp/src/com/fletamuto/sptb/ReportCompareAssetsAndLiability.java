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
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportCompareAssetsAndLiability extends FmBaseActivity {
	
	private int mYear = Calendar.getInstance().get(Calendar.YEAR);
	private BarGraph bg;
	private boolean barGraphDifferenceMode = false;
	private ArrayList<Long> mMonthTotalAssetsTotalAmount = new ArrayList<Long>();
//	private ArrayList<Long> mMonthLiabilityTotalAmount = new ArrayList<Long>();
	private long mMonthAssetsAmount[] = new long[12];
	private long mMonthLiabilityAmount[] = new long[12];
	private long mTotalAssetsAmount = 0L;
	private long mTotalLiabilityAmount = 0L;
	ArrayList<String> mMonthArr = new ArrayList<String>();
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_of_year, true);
    	
    	getData();
    	updateChildView();
//    	setBunClickListener();
    }
    
    @Override
    protected void initialize() {
    	String []monthInYear = getResources().getStringArray(R.array.year_in_month_name);
		for (int monthIndex = 0; monthIndex < monthInYear.length; monthIndex++) {
			mMonthArr.add(monthInYear[monthIndex]);
		}
    	super.initialize();
    }
    
    protected void clearMonthAmount() {
    	mTotalAssetsAmount = 0L;
    	mTotalLiabilityAmount = 0L;
    	mMonthTotalAssetsTotalAmount.clear();
    	for (int month = 0; month < 12; month++){
    		mMonthAssetsAmount[month] = 0L;
    		mMonthLiabilityAmount[month] = 0L;
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
    			mMonthAssetsAmount[month] += categoryAmount.get(month);
    		}
    	}
    	
    	ArrayList<Category> liabilityCategory = DBMgr.getCategory(LiabilityItem.TYPE);
    	size = liabilityCategory.size();
    	for (int index = 0; index < size; index++) {
    		ArrayList<Long> categoryAmount = DBMgr.getTotalLiabilityAmountMonthInYear(liabilityCategory.get(index).getID(), mYear);
    		for (int month = 0; month < 12; month++){
    			mMonthLiabilityAmount[month] += categoryAmount.get(month);
    		}
    	}
    	
    	for (int month = 0; month < 12; month++){
    		mMonthTotalAssetsTotalAmount.add(mMonthAssetsAmount[month]);
    		mMonthTotalAssetsTotalAmount.add(mMonthLiabilityAmount[month]);
		}
    	
    	mTotalAssetsAmount = DBMgr.getTotalAmount(AssetsItem.TYPE);
    	mTotalLiabilityAmount = DBMgr.getTotalAmount(LiabilityItem.TYPE);
	}

	@Override
	protected void setTitleBtn() {
    	setTitle("년간 월별 자산/부채 비교");
    	setChangeButtonListener();
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
		
		TextView tvExpenseYear = (TextView)findViewById(R.id.TVTotalExpense);
		tvExpenseYear.setText(String.format("총  자산 : %,d원", mTotalLiabilityAmount));
		TextView tvIncomeYear = (TextView)findViewById(R.id.TVTotalIncome);
		tvIncomeYear.setText(String.format("총 부채: %,d원", mTotalAssetsAmount));
		
	}

	private void updateBarGraph() {
		barGraphDifferenceMode = false;
		ArrayList<Integer> ap = new ArrayList<Integer>();
		
		ap.add(Constants.BAR_AXIS_X_BOTTOM);
		ap.add(Constants.BAR_AXIS_Y_LEFT);
    
        bg = (BarGraph) findViewById (R.id.bgraph);
 
        bg.makeUserTypeGraph(ap, Constants.BAR_AXIS_X_BOTTOM, mMonthTotalAssetsTotalAmount, 2, mMonthArr);

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