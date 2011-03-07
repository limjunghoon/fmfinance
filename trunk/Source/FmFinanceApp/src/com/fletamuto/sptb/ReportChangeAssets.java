package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
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
import com.fletamuto.sptb.ReportMonthOfYearCategoryLayout.ReportMonthlyItemAdapter;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.Revenue;

public class ReportChangeAssets extends FmBaseActivity {
	private LineGraph lg;
//	private int mYear = Calendar.getInstance().get(Calendar.YEAR);
	private Calendar mMonthCalendar = Calendar.getInstance();
	private ArrayList<Long> mItemAmount = new ArrayList<Long>();
	private long mTotalAmount = 0L;
	private long mStartAmount = 0L;
	ArrayList<String> mMonthArr = new ArrayList<String>();
//	ArrayList<Long> mMonthCategoryAmount = new ArrayList<Long>();
	private int mPeriodTerm = ItemDef.BASE_PERIOD_MONTH_TERM;
	private Category mMainCategory = null;
	
	protected ReportMonthlyItemAdapter mMonthlyAdapter = null;
	protected ArrayList<MonthAmountItem> mMonthlyItems = new ArrayList<MonthAmountItem>();
	
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_assets_change, true);
        
        setBtnClickListener();
        getData();
        setMonthData();
        setMonthlyAdapterList();
        updateChildView(); 
    }
    
    protected void clearMonthAmount() {
    	mStartAmount = 0L;
    	mTotalAmount = 0L;
    	mItemAmount.clear();
    	
    	for (int index = 0; index < mPeriodTerm; index++) {
    		mItemAmount.add(0L);
    	}
    }
    
    private void getData() {
    	clearMonthAmount();
    	ArrayList<Category> assetsCategory = DBMgr.getCategory(AssetsItem.TYPE);
    	int size = assetsCategory.size();
    	
    	// 속도 개선 필요 //////////
    	for (int index = 0; index < size; index++) {
    		long monthAmount = 0L;
    		ArrayList<Long> categoryAmount = DBMgr.getLastAmountMonth(AssetsItem.TYPE, assetsCategory.get(index).getID(), mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
    		for (int month = 0; month < mPeriodTerm; month++){
    			mItemAmount.set(month, mItemAmount.get(month)+categoryAmount.get(month));
    		}
    		
    	}
    	
//    	for (int month = 0; month < 12; month++){
//    		mMonthCategoryAmount.add(mMonthAmount[month]);
//    		
//    		mMonthlyItems.add(new MonthAmountItem(mYear, month+1, mMonthAmount[month]));
//    		
//    		if (mStartAmount == 0L && mMonthAmount[month] != 0L) {
//				mStartAmount = mMonthAmount[month];
//			}
//		}
    	
    	mTotalAmount = DBMgr.getTotalAmount(AssetsItem.TYPE);
	}
    
    private void setMonthData() {
		
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
			
			mMonthArr.add(String.format("%d월", targetMonth));
			mMonthlyItems.add(0, new MonthAmountItem(year, targetMonth, mItemAmount.get(index)));
			targetMonth++;
		}
	}

	@Override
    protected void initialize() {
        
    	super.initialize();
    }
    
    @Override
    protected void setTitleBtn() {
    	setTitle("자산변동 추이");
    	super.setTitleBtn();
    }
    
	protected void setMonthlyAdapterList() {
    	final ListView listMonthItem = (ListView)findViewById(R.id.LVMonthItem);
    	mMonthlyAdapter = new ReportMonthlyItemAdapter(this, R.layout.report_month_of_year_category_list_item, mMonthlyItems);
    	listMonthItem.setAdapter(mMonthlyAdapter);
    	
    	listMonthItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
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
//		mYear++;
//		getData();
//    	updateChildView();
	}
	
	public void movePreviousYear() {
//		mYear--;
//		getData();
//    	updateChildView();
	}
	
	private void updateChildView() {
//		TextView tvYear = (TextView) findViewById(R.id.TVCurrentYear);
//		tvYear.setText(String.format("%d년", mYear));
//		
//		TextView tvTotalAmount = (TextView) findViewById(R.id.TVTotalAssetsAmount);
//		tvTotalAmount.setText(String.format("자산:%,d원", mTotalAmount));
//		
//		TextView tvPersent = (TextView) findViewById(R.id.TVPercentAssetsAmount);
//		tvPersent.setText("년간 손익율:" + Revenue.getString(mStartAmount, mTotalAmount));
		
		updateLineView();
	}

	private void updateLineView() {
		if (mItemAmount == null) return;
		
		lg = (LineGraph) findViewById (R.id.lgraph);
		lg.makeUserTypeGraph(mItemAmount, null, null, mMonthArr);
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lg.getLayoutParams();
		
		params.width = (320 > lg.getLineGraphWidth()) ? 320 : lg.getLineGraphWidth();
		params.height = (140 > lg.getLineGraphHeight()) ? 140 : lg.getLineGraphHeight();
		
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