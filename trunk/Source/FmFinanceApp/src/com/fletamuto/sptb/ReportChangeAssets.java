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
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.Revenue;

public class ReportChangeAssets extends FmBaseActivity {
	private static final int VIEW_ALL = 0;
	private static final int VIEW_CATEGORY = 1;
	private static final int VIEW_ITEM = 2;
	
	private Calendar mMonthCalendar = Calendar.getInstance();
	private ArrayList<Long> mItemAmount = new ArrayList<Long>();
//	private long mTotalAmount = 0L;
	private ArrayList<String> mMonthArr = new ArrayList<String>();
	private Category mMainCategory = null;
	private FinanceItem mItem = null;
	private int mViewMode = VIEW_ALL;
	private int mItemType = AssetsItem.TYPE;
	private int mPeriodTerm = ItemDef.BASE_PERIOD_MONTH_TERM;
	
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
//    	mTotalAmount = 0L;
    	mItemAmount.clear();
    	
    	for (int index = 0; index < mPeriodTerm; index++) {
    		mItemAmount.add(0L);
    	}
    }
    
    private void getData() {
    	clearMonthAmount();
    	
    	if (mViewMode == VIEW_ALL) {
    		ArrayList<FinanceItem> Items = DBMgr.getAllItems(mItemType);
        	int size = Items.size();
        	
        	// 속도 개선 필요 //////////
        	for (int index = 0; index < size; index++) {
        		ArrayList<Long> categoryAmount = DBMgr.getLastAmountMonth(mItemType, Items.get(index).getID(), mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
        		for (int month = 0; month < mPeriodTerm; month++){
        			mItemAmount.set(month, mItemAmount.get(month)+categoryAmount.get(month));
        		}
        	}
        	
 //       	mTotalAmount = DBMgr.getTotalAmount(mItemType);
    	}
    	else if (mViewMode == VIEW_CATEGORY) {
    		ArrayList<FinanceItem> Items = DBMgr.getItemsFromCategoryID(mItemType, mMainCategory.getID());
        	int size = Items.size();
        	
        	// 속도 개선 필요 //////////
        	for (int index = 0; index < size; index++) {
        		ArrayList<Long> categoryAmount = DBMgr.getLastAmountMonth(mItemType, Items.get(index).getID(), mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
        		for (int month = 0; month < mPeriodTerm; month++){
        			mItemAmount.set(month, mItemAmount.get(month)+categoryAmount.get(month));
        		}
        	}
        	
//        	mTotalAmount = DBMgr.getTotalMainCategoryAmount(mItemType, mMainCategory.getID());
    	}
    	else {
    		ArrayList<Long> categoryAmount = DBMgr.getLastAmountMonth(mItemType, mItem.getID(), mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
    		for (int month = 0; month < mPeriodTerm; month++){
    			mItemAmount.set(month, mItemAmount.get(month)+categoryAmount.get(month));
    		}
    		
//    		mTotalAmount = mItem.getAmount();
    	}
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
		mItemType = getIntent().getIntExtra(MsgDef.ExtraNames.ITEM_TYPE, AssetsItem.TYPE);
		
        int categoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
        if (categoryID != -1) {
        	mMainCategory = DBMgr.getCategoryFromID(mItemType, categoryID) ;
        	mViewMode = VIEW_CATEGORY;
        }
        else {
        	int itemID = getIntent().getIntExtra(MsgDef.ExtraNames.ITEM_ID, -1);
        	if (itemID != -1) {
        		mItem = DBMgr.getItem(mItemType, itemID);
        		mViewMode = VIEW_ITEM;
        	}
        }
        
    	super.initialize();
    }
    
    @Override
    protected void setTitleBtn() {
    	if (mViewMode == VIEW_ALL) {
    		setTitle("자산 변동내역");
    	}
    	else if (mViewMode == VIEW_CATEGORY) {
    		setTitle(mMainCategory.getName() + " 변동내역");
    	}
    	else {
    		setTitle(mItem.getTitle() + " 변동내역");
    	}
    	
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
		
		final LineGraph lg = (LineGraph) findViewById (R.id.lgraph);
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