package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.common.control.fmgraph.LineGraph;
import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ViewHolder;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportMonthOfYearCategoryLayout extends FmBaseActivity {
	private static final int MOVE_SENSITIVITY = ItemDef.MOVE_SENSITIVITY;
	private static final int CATEGORY_ALL = 0;
	private static final int CATEGORY_MAIN = 1;
	private static final int CATEGORY_SUB = 2;
	
	private ArrayList<Long> mItemAmount = new ArrayList<Long>();
	protected ArrayList<FinanceItem> mMonthlyItems = null;
	private ArrayList<String> mMonthName = new ArrayList<String>();
	private LineGraph mLineGraph;
	private Calendar mMonthCalendar = Calendar.getInstance();
	private Calendar mMonthItemListDate = Calendar.getInstance();
	private int mItemType = ExpenseItem.TYPE;
	
	private float mTouchMove;
	private boolean mTouchMoveFlag = false;
	private int mPeriodTerm = ItemDef.BASE_PERIOD_MONTH_TERM;
	private Category mMainCategory = null;
	private Category mSubCategory = null;
	private int mCategoryMode = CATEGORY_ALL;
	
	protected ReportMonthlyItemAdapter mMonthlyAdapter = null;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_of_year_category, true);
    	
    	getDate();
    	updateChildView();
    }
    
    protected void setMonthlyAdapterList() {
    	final ListView listMonthItem = (ListView)findViewById(R.id.LVMonthItem);
    	mMonthlyAdapter = new ReportMonthlyItemAdapter(this, R.layout.report_list_normal, mMonthlyItems);
    	listMonthItem.setAdapter(mMonthlyAdapter);
    	
    	listMonthItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
	
				
			}
		});
//    	
//    	listMonthItem.setOnTouchListener(new View.OnTouchListener() {
//			
//			public boolean onTouch(View v, MotionEvent event) {
//				setMoveViewMotionEvent(event);
//		    	return false;
//			}
//		});
    }
    
    @Override
    protected void initialize() {
    	mItemType = getIntent().getIntExtra(MsgDef.ExtraNames.ITEM_TYPE, ExpenseItem.TYPE);
    	int mainCategory = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
    	int subCategory = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_SUB_ID, -1);
    	
    	if (mainCategory == -1 && subCategory == -1) {
    		mCategoryMode = CATEGORY_ALL;
    	}
    	else {
    		if (mainCategory != -1) {
    			mCategoryMode = CATEGORY_MAIN;
    			mMainCategory = DBMgr.getCategoryFromID(mItemType, mainCategory);
    		}
    		else {
    			mCategoryMode = CATEGORY_SUB;
    			mMainCategory = DBMgr.getSubCategoryFromID(mItemType, subCategory);
    		}
    	}
    	super.initialize();
    }
    
	@Override
	protected void setTitleBtn() {
		if (mItemType == IncomeItem.TYPE) {
			setTitle("수입 변동내역");
		} 
		else if (mItemType == ExpenseItem.TYPE) {
			setTitle("지출 변동내역");
		} 
		else if (mItemType == AssetsItem.TYPE) {
			setTitle("자산 변동내역");
		} 
		else if (mItemType == LiabilityItem.TYPE) {
			setTitle("부채 변동내역");
		} 
        
		super.setTitleBtn();
	}

	private void updateChildView() {
		setMonthlyAdapterList();
		updateLineView();
		updateItemList();
	}
	
	public void updateItemList() {
		TextView tvMonth = (TextView) findViewById(R.id.TVMonth);
		tvMonth.setText(String.format("%d년 %d월", mMonthItemListDate.get(Calendar.YEAR), mMonthItemListDate.get(Calendar.MONTH)+1));
	}

	public void getDate() {
		setMonthName();
		getMonthAmount();
		getMonthItems();
	}

	
	public void getMonthAmount() {
		if (mCategoryMode == CATEGORY_ALL) {
			mItemAmount = DBMgr.getTotalAmount(mItemType, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
		}
		else if (mCategoryMode == CATEGORY_MAIN) {
			mItemAmount = DBMgr.getTotalMainCategoryAmount(mItemType, mMainCategory.getID(), mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
		}
		else if (mCategoryMode == CATEGORY_SUB) {
			mItemAmount = DBMgr.getTotalMainCategoryAmount(mItemType, mSubCategory.getID(), mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1, mPeriodTerm);
		}
		
	}

	public void getMonthItems() {
		mMonthlyItems = DBMgr.getItems(mItemType, mMonthItemListDate.get(Calendar.YEAR), mMonthItemListDate.get(Calendar.MONTH)+1);
	}

	private void updateLineView() {
		mLineGraph = (LineGraph) findViewById (R.id.lgraph);
		mLineGraph.makeUserTypeGraph(mItemAmount, null, null, mMonthName);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLineGraph.getLayoutParams();
		
		params.width = (320 > mLineGraph.getLineGraphWidth()) ? 320 : mLineGraph.getLineGraphWidth();
		params.height = (140 > mLineGraph.getLineGraphHeight()) ? 140 : mLineGraph.getLineGraphHeight();
		
		mLineGraph.setLayoutParams(params);
		mLineGraph.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		int sel;
		    		sel = mLineGraph.FindTouchItemID((int)event.getX(), (int)event.getY());
		
		    		if (sel == -1) {
		    			return true;
		    		} else {
		    			return true;
		    		}
		    	}
				return false;
			}
		});
	}
	


	protected void setListViewText(LinearLayout llMember, final int index) {
//		if (mViewMode == VIEW_ASSETS) {
//			((TextView)llMember.findViewById(R.id.TVListLeftTop)).setText(mMonthName.get(index));
//			((TextView)llMember.findViewById(R.id.TVListRightTop)).setText(String.format("%,d원", mItemDifference.get(index)));
//			((TextView)llMember.findViewById(R.id.TVListLeftBottom)).setText(String.format("%,d원", mItem1.get(index)));
//			((TextView)llMember.findViewById(R.id.TVListRightBottom)).setText(String.format("%,d원", mItem2.get(index)));
//		}
//		else if (mViewMode == VIEW_BUDGET) {
//		}
//		else {
//			((TextView)llMember.findViewById(R.id.TVListLeftTop)).setText(mMonthName.get(index));
//			((TextView)llMember.findViewById(R.id.TVListRightTop)).setText(String.format("%,d원", mItemDifference.get(index)));
//			((TextView)llMember.findViewById(R.id.TVListLeftBottom)).setText(String.format("%,d원", mItem1.get(index)));
//			((TextView)llMember.findViewById(R.id.TVListRightBottom)).setText(String.format("%,d원", mItem2.get(index)));
//		}
		
	}
	
	public void setMoveViewMotionEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		mTouchMove = event.getX();
    		mTouchMoveFlag = true;
    	}
    	else if (event.getAction() == MotionEvent.ACTION_MOVE && mTouchMoveFlag == true) {
    		
    		if (mTouchMove-event.getX()< -MOVE_SENSITIVITY) {
    			mTouchMoveFlag = false;
    			moveCurrentDate(1);
    		}
    		if (mTouchMove-event.getX()> MOVE_SENSITIVITY) {
    			mTouchMoveFlag = false;
    			moveCurrentDate(-1);
    		}
    	}
    }
	
	protected void moveCurrentDate(int dayValue) {
//		mYear += dayValue;
//		
//		getDate();
//    	updateChildView();
	}
	
	private void setMonthName() {
		mMonthName.clear();
		
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
			targetMonth++;
		}
	}
	
	 public class ReportMonthlyItemAdapter extends ArrayAdapter<FinanceItem> {
	    	int mResource;
	    	private LayoutInflater mInflater;

			public ReportMonthlyItemAdapter(Context context, int resource,
					 List<FinanceItem> objects) {
				super(context, resource, objects);
				this.mResource = resource;
				mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				FinanceItem item = (FinanceItem)getItem(position);
				
				if (convertView == null) {
					convertView = mInflater.inflate(mResource, parent, false);
					
					ViewHolder viewHolder = new ViewHolder(
							(TextView)convertView.findViewById(R.id.TVTitle),
							(TextView)convertView.findViewById(R.id.TVListLeft), 
							(TextView)convertView.findViewById(R.id.TVListCenterTop), 
							(TextView)convertView.findViewById(R.id.TVListCenterBottom), 
							(TextView)convertView.findViewById(R.id.TVListRightTop), 
							(TextView)convertView.findViewById(R.id.TVListRightBottom));
					
					convertView.setTag(viewHolder);
				}
				
				
				convertView.findViewById(R.id.LLTitle).setVisibility(View.GONE);
				convertView.findViewById(R.id.LLBody).setVisibility(View.VISIBLE);
				setListViewText(item, convertView);

				
				return convertView;
			}
	    }
	 
	 protected void setListViewText(FinanceItem item, View convertView) {
    	if (item.getType() == IncomeItem.TYPE) {
    		setIncomeListViewText((IncomeItem)item, convertView);
    	}
    	else if (item.getType() == ExpenseItem.TYPE) {
    		setExpenseListViewText((ExpenseItem)item, convertView);
    	}
	}
	 
	 protected void setIncomeListViewText(IncomeItem income, View convertView) {
    	ViewHolder viewHolder = (ViewHolder) convertView.getTag();
    	viewHolder.getLeftTextView().setText(income.getCategory().getName());
		TextView tvTitle = viewHolder.getCenterTopTextView();
		if (income.getTitle().length() != 0) {
			tvTitle.setText(income.getTitle());
		}
		else {
			tvTitle.setVisibility(View.GONE);
		}
		
		TextView tvAmount = viewHolder.getRightTopTextView();
		tvAmount.setText(String.format("%,d원", income.getAmount()));
		tvAmount.setTextColor(Color.BLUE);
		
		TextView tvMemo = viewHolder.getCenterBottomTextView();
		if (income.getMemo().length() != 0) {
			tvMemo.setText(income.getMemo());
		}
		else {
			tvMemo.setVisibility(View.GONE);
		}
		
		TextView tvMothod = viewHolder.getRightBottomTextView();
		tvMothod.setText(income.getAccountText());
    }
	    
    protected void setExpenseListViewText(ExpenseItem expense, View convertView) {
    	ViewHolder viewHolder = (ViewHolder) convertView.getTag();
    	viewHolder.getLeftTextView().setText(expense.getCategory().getName());
    	
    	if (expense.getCategory().getExtndType() != ItemDef.NOT_CATEGORY) {
    		TextView tvSubCategory = viewHolder.getCenterTopTextView() ;
    		tvSubCategory.setText(expense.getSubCategory().getName());
    	}
		
		TextView tvAmount = viewHolder.getRightTopTextView(); 
		tvAmount.setText(String.format("%,d원", -expense.getAmount()));
		tvAmount.setTextColor(Color.RED);
		
		TextView tvMemo = viewHolder.getCenterBottomTextView() ;
		if (expense.getMemo().length() != 0) {
			tvMemo.setText(expense.getMemo());
		}
		else {
			tvMemo.setVisibility(View.GONE);
		}
		
		TextView tvMothod = viewHolder.getRightBottomTextView(); 
		tvMothod.setText(expense.getPaymentMethod().getText());
    }
    
}