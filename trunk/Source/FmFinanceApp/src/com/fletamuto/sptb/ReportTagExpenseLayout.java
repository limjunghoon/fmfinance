package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
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

import com.fletamuto.common.control.fmgraph.LineGraph;
import com.fletamuto.sptb.ReportMonthOfYearCategoryLayout.MonthAmountItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;

public class ReportTagExpenseLayout extends FmBaseActivity {
	protected static final int VIEW_MONTH = 0;
	protected static final int VIEW_YEAR = 1;
	
	private LineGraph mLineGraph;
	private int mTagID = -1;
	private String mTagName;
	protected ArrayList<MonthAmountItem> mMonthlyItems = new ArrayList<MonthAmountItem>();
	ArrayList<String> mMonthName = new ArrayList<String>();
	ArrayList<Long> mMonthTagAmount = null;
	private Calendar mMonthCalendar = Calendar.getInstance();
	protected int mYear = Calendar.getInstance().get(Calendar.YEAR);
	private int mViewMode = VIEW_MONTH;
	private int mPeriodTerm = ItemDef.BASE_PERIOD_MONTH_TERM;
	
	protected ReportMonthlyItemAdapter mMonthlyAdapter = null;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_tag, true);
        
        setBtnClickListener();
        getData();
        updateChildView();
    }
    
    protected void setMonthlyAdapterList() {
    	final ListView listMonthItem = (ListView)findViewById(R.id.LVMonthItem);
    	mMonthlyAdapter = new ReportMonthlyItemAdapter(this, R.layout.report_month_of_year_category_list_item, mMonthlyItems);
    	listMonthItem.setAdapter(mMonthlyAdapter);
    	
    }
    
    private void getData() {
    	getMonthAmount();
    	setMonthData();
	}
    
    private void getMonthAmount() {
    	if (mViewMode == VIEW_MONTH) {
    		mMonthTagAmount = DBMgr.getTotalTagAmountMonth(mTagID, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
    	}
    	else {
    		mMonthTagAmount = DBMgr.getTotalTagAmountMonthInYear(mTagID, mYear);
    	}
	}

	private void setMonthData() {
		mMonthName.clear();
		mMonthlyItems.clear();
		
		if (mViewMode == VIEW_MONTH) {
			int year = mMonthCalendar.get(Calendar.YEAR);
			int month = mMonthCalendar.get(Calendar.MONTH) +1;
			
			int targetMonth = month - mPeriodTerm;
			if (targetMonth <= 0) {
				targetMonth += 12 + 1;
				year--;
			}
			
			for (int index = 0; index < mPeriodTerm; index++) {
				
				if (targetMonth > 12) {
					targetMonth = 1;
					year++;
				}
				
				mMonthName.add(String.format("%d월", targetMonth));
				mMonthlyItems.add(0, new MonthAmountItem(year, targetMonth, mMonthTagAmount.get(index)));
				targetMonth++;
			}
		}
		else {
			for (int index = 0; index < 12; index++) {
				mMonthName.add(String.format("%d월", index+1));
				mMonthlyItems.add(new MonthAmountItem(mYear, index+1, mMonthTagAmount.get(index)));
			}
		}
	}
    
    @Override
    protected void initialize() {
    	mTagID = getIntent().getIntExtra(MsgDef.ExtraNames.TAG_ID, -1);
		mTagName = getIntent().getStringExtra(MsgDef.ExtraNames.TAG_NAME);
		
    	mViewMode = getIntent().getIntExtra(MsgDef.ExtraNames.VIEW_MODE, VIEW_MONTH);
    	if (mViewMode == VIEW_MONTH) {
    		mMonthCalendar = (Calendar) getIntent().getSerializableExtra(MsgDef.ExtraNames.CALENDAR);
    		if (mMonthCalendar == null) {
    			mMonthCalendar = Calendar.getInstance();
    		}
    	}
    	else {
    		mYear = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_YEAR, Calendar.getInstance().get(Calendar.YEAR));
    	}
    	
 
    	super.initialize();
    }

    
    @Override
    protected void setTitleBtn() {
    	setTitle(mTagName + " 변동내역");
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
		setMonthlyAdapterList();
		
		TextView tvYear = (TextView) findViewById(R.id.TVCurrentYear);
		tvYear.setText(String.format("%d년", mYear));
		
		updateLineView();
	}

	private void updateLineView() {
		if (mMonthTagAmount == null) return;
		
		mLineGraph = (LineGraph) findViewById (R.id.lgraph);
		mLineGraph.makeUserTypeGraph(mMonthTagAmount, null, null, mMonthName);
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLineGraph.getLayoutParams();
		
		params.width = (320 > mLineGraph.getLineGraphWidth()) ? 320 : mLineGraph.getLineGraphWidth();
		params.height = (170 > mLineGraph.getLineGraphHeight()) ? 170 : mLineGraph.getLineGraphHeight();
		
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
	
	public class MonthAmountItem {
		int mYear;
		int mMonth;
		long mAmount;
		
		public MonthAmountItem(int year, int month, long amount) {
			mYear = year;
			mMonth = month;
			mAmount = amount;
		}
	}
	
	 public class ReportMonthlyItemAdapter extends ArrayAdapter<MonthAmountItem> {
	    	int mResource;
	    	private LayoutInflater mInflater;

			public ReportMonthlyItemAdapter(Context context, int resource,
					 List<MonthAmountItem> objects) {
				super(context, resource, objects);
				this.mResource = resource;
				mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				MonthAmountItem item = (MonthAmountItem)getItem(position);
				
				if (convertView == null) {
					convertView = mInflater.inflate(mResource, parent, false);
				}
				
				TextView tvMonth = (TextView) convertView.findViewById(R.id.TVMonth);
				tvMonth.setText(item.mMonth+"월");
				TextView tvAmount = (TextView) convertView.findViewById(R.id.TVAmount);
				tvAmount.setText(String.format("%,d원", item.mAmount));
				
				return convertView;
			}
	    }
    
}