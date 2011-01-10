package com.fletamuto.sptb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.ReportBaseCardLayout.CardItemAdapter;
import com.fletamuto.sptb.ReportBaseLayout.ReportItemAdapter;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.BudgetItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.FinanceCurrentDate;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

/**
 * 메인 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class MainIncomeAndExpenseLayout extends FmBaseActivity { 
	
	protected ArrayList<FinanceItem> mIncomeItems = null;
	protected ArrayList<FinanceItem> mExpenseItems = null;
	protected ReportItemAdapter mIncomeDailyAdapter = null;
	protected ReportItemAdapter mExpenseDailyAdapter = null;
	
	private int mCurrentViewMode = ItemDef.VIEW_DAY_OF_MONTH;
	private Calendar mCalendarMonthly = Calendar.getInstance();
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_income_and_expense, false);
        
        DBMgr.initialize(getApplicationContext());
        DBMgr.addRepeatItems();
        
        setBtnClickListener();
        setTitle(getResources().getString(R.string.app_name));
        
       
        
//        updateViewText();
//        setIncomeExpenseProgress();
    }
    
    @Override
    protected void initialize() {
    	super.initialize();
    	
    	TextView tvIncomeTitle = (TextView)findViewById(R.id.TVDailyIncomeTilte); 
    	tvIncomeTitle.setTextColor(Color.BLACK);
    	tvIncomeTitle.setBackgroundColor(Color.WHITE);
    	
    	TextView tvExpenseTitle = (TextView)findViewById(R.id.TVDailyExpenseTitle); 
    	tvExpenseTitle.setTextColor(Color.BLACK);
    	tvExpenseTitle.setBackgroundColor(Color.WHITE);
    }

	/**
     * activity가 다시 시작할 때
     */
    protected void onResume() {
    	getListItem();
    	setAdapterList();
    	updateViewText();
    	super.onResume();
    }

    /** 버튼 클릭시 리슨너 등록 */
    protected void setBtnClickListener() {
    	setCurrentButtonListener();
    	
    	findViewById(R.id.BtnInput).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
//		    	Intent intent = new Intent(MainIncomeAndExpenseLayout.this, SelectCategoryIncomeLayout.class);
//				startActivity(intent);
				
				Intent intent = new Intent(MainIncomeAndExpenseLayout.this, InputExpenseLayout.class);
				startActivity(intent);
			}
		});
    }
    
    /**
     * 현재날짜 이동 버튼 클릭시
     */
    private void setCurrentButtonListener() {
    	Button btnPrevious = (Button)findViewById(R.id.BtnPreviousDay);
    	btnPrevious.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveCurrentDay(-1);
			}
		});
    	
    	Button btnNext = (Button)findViewById(R.id.BtnNextDay);
    	btnNext.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveCurrentDay(1);
			}
		});
	}
    
	protected void moveCurrentDay(int dayValue) {
		FinanceCurrentDate.moveCurrentDay(dayValue);
		getListItem();
		setAdapterList();
		updateViewText();
	}
    
    /** 화면 텍스트 갱신 */
    protected void updateViewText() {
    	updateTotalIncomeText();
    	updateTotalExpenseText();
    	updateCurrentDateText();
    	updateDailyTitleText();
    }
    
    /** 현재 수입 정보갱신 */
    protected void updateCurrentIncomeText() {
    	int count = DBMgr.getItemCount(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	TextView tvDailyIncome = (TextView)findViewById(R.id.TVDailyIncome);
    	tvDailyIncome.setText(String.format("수입(%d건)", count));
    	
    	long amount = DBMgr.getTotalAmountDay(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	TextView tvDailyIncomeAmount = (TextView)findViewById(R.id.TVDailyIncomeAmount);
    	tvDailyIncomeAmount.setText(String.format("%,d원", amount));
    	tvDailyIncomeAmount.setTextColor(Color.BLUE);
	}
    
    /** 현재 지출 정보갱신 */
    protected void updateCurrentExpenseText() {
    	int count = DBMgr.getItemCount(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	TextView tvDailyExpense = (TextView)findViewById(R.id.TVDailyExpense);
    	tvDailyExpense.setText(String.format("지출(%d건)", count));
    	
    	long amount = DBMgr.getTotalAmountDay(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	TextView tvDailyExpenseAmount = (TextView)findViewById(R.id.TVDailyExpenseAmount);
    	tvDailyExpenseAmount.setText(String.format("-%,d원", amount));
    	tvDailyExpenseAmount.setTextColor(Color.RED);
	}

	protected void updateDailyTitleText() {
		TextView tvDaily = (TextView)findViewById(R.id.TVDailyDay);
		tvDaily.setText(String.valueOf(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_MONTH)));
		
		TextView tvWeely = (TextView)findViewById(R.id.TVDailyWeekly);
		tvWeely.setText("(" + FinanceDataFormat.getWeekText(FinanceCurrentDate.getDate()) + ")");
		
		updateCurrentIncomeText();
		updateCurrentExpenseText();
	}

	/** 총 수입 액수 갱신 */
    protected void updateTotalIncomeText() {
    	long amount = DBMgr.getTotalAmount(IncomeItem.TYPE);
    	TextView totalIncome = (TextView)findViewById(R.id.TVTotalIncome);
    	totalIncome.setText(String.format("수입 %,d원", amount));
    	totalIncome.setTextColor(Color.BLUE);
	}
    
    /** 총 지출 액수 갱신 */
    protected void updateTotalExpenseText() {
    	long amount = DBMgr.getTotalAmount(ExpenseItem.TYPE);
    	TextView totalExpense = (TextView)findViewById(R.id.TVTotalExpense);
    	totalExpense.setText(String.format("지출 %,d원", -amount));
    	totalExpense.setTextColor(Color.RED);
	}
    
    /** 현재 날짜 갱신 */
    protected void updateCurrentDateText() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MM");
    	TextView tvCurrentDay = (TextView)findViewById(R.id.TVCurrentday);
    	tvCurrentDay.setText(dateFormat.format(FinanceCurrentDate.getTime()));
	}
    

    
    protected void getListItem() {
    	mIncomeItems = DBMgr.getItems(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	mExpenseItems = DBMgr.getItems(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	
    	updateListItem();
    }
    
	protected void setAdapterList() {
		setDailyIncomeAdapterList();
		setDailyExpenseAdapterList();
    }
    

    protected void setDailyExpenseAdapterList() {
    	if (mExpenseItems == null) return;
    	
    	findViewById(R.id.LLDailyExpense).setVisibility((mExpenseItems.size() == 0) ? View.GONE : View.VISIBLE);
        
    	final ListView listExpense = (ListView)findViewById(R.id.LVDailyExpense);
    	mExpenseDailyAdapter = new ReportItemAdapter(this, R.layout.report_list_normal, mExpenseItems);
    	listExpense.setAdapter(mExpenseDailyAdapter);
    	
    	listExpense.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	//			setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
				
			}
		});
	}

	protected void setDailyIncomeAdapterList() {
    	if (mIncomeItems == null) return;
    	
    	findViewById(R.id.LLDailyIncome).setVisibility((mIncomeItems.size() == 0) ? View.GONE : View.VISIBLE);
        
    	final ListView listIncome = (ListView)findViewById(R.id.LVDailyIncome);
    	mIncomeDailyAdapter = new ReportItemAdapter(this, R.layout.report_list_normal, mIncomeItems);
    	listIncome.setAdapter(mIncomeDailyAdapter);
    	
    	listIncome.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	//			setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
				
			}
		});
	}

    
    public class ReportItemAdapter extends ArrayAdapter<FinanceItem> {
    	int mResource;
    	private LayoutInflater mInflater;

		public ReportItemAdapter(Context context, int resource,
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
			}
			
			setListViewText(item, convertView);
			return convertView;
		}
    }
    
    protected void updateListItem() {
    	
	}
    
    protected void setListViewText(FinanceItem item, View convertView) {
    	if (item.getType() == IncomeItem.TYPE) {
    		setIncomeListViewText((IncomeItem)item, convertView);
    	}
    	else {
    		setExpenseListViewText((ExpenseItem)item, convertView);
    	}
	}
    
    protected void setIncomeListViewText(IncomeItem income, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVListLeft)).setText(income.getCategory().getName());
		TextView tvTitle = (TextView)convertView.findViewById(R.id.TVListCenterTop) ;
		if (income.getTitle().length() != 0) {
			tvTitle.setText(income.getTitle());
		}
		else {
			tvTitle.setVisibility(View.GONE);
		}
		
		TextView tvAmount = (TextView)convertView.findViewById(R.id.TVListRightTop); 
		tvAmount.setText(String.format("%,d원", income.getAmount()));
		tvAmount.setTextColor(Color.BLUE);
		
		TextView tvMemo = (TextView)convertView.findViewById(R.id.TVListCenterBottom) ;
		if (income.getMemo().length() != 0) {
			tvMemo.setText("메모 : " + income.getMemo());
		}
		else {
			tvMemo.setVisibility(View.GONE);
		}
		
		TextView tvMothod = (TextView)convertView.findViewById(R.id.TVListRightBottom); 
		tvMothod.setText(income.getAccountText());
    }
    
    protected void setExpenseListViewText(ExpenseItem expense, View convertView) {
    	
    	((TextView)convertView.findViewById(R.id.TVListLeft)).setText(expense.getCategory().getName());
		TextView tvSubCategory = (TextView)convertView.findViewById(R.id.TVListCenterTop) ;
		tvSubCategory.setText(expense.getSubCategory().getName());
		
		TextView tvAmount = (TextView)convertView.findViewById(R.id.TVListRightTop); 
		tvAmount.setText(String.format("-%,d원", expense.getAmount()));
		tvAmount.setTextColor(Color.RED);
		
		TextView tvMemo = (TextView)convertView.findViewById(R.id.TVListCenterBottom) ;
		if (expense.getMemo().length() != 0) {
			tvMemo.setText("메모 : " + expense.getMemo());
		}
		else {
			tvMemo.setVisibility(View.GONE);
		}
		
		TextView tvMothod = (TextView)convertView.findViewById(R.id.TVListRightBottom); 
		tvMothod.setText(expense.getPaymentMethod().getText());
    }
	
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
//    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
//    	if (item.getType() == ExpenseItem.TYPE) {
// //   		startEditInputActivity(InputExpenseLayout.class, item.getID());
//    	}
//    	else {
// //   		startEditInputActivity(InputIncomeLayout.class, item.getID());
//    	}
	}
	
	protected void startEditInputActivity(Class<?> cls, int itemId) {
		Intent intent = new Intent(this, cls);
    	intent.putExtra("EDIT_ITEM_ID", itemId);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
}