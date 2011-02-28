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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.data.CardExpenseInfo;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.CardPayment;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.FinanceCurrentDate;
import com.fletamuto.sptb.util.FinanceDataFormat;

/**
 * 메인 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class MainIncomeAndExpenseLayout extends FmBaseActivity { 
	private static final int LAST_DAY_OF_MONTH = ItemDef.LAST_DAY_OF_MONTH;
	private static final int MOVE_SENSITIVITY = ItemDef.MOVE_SENSITIVITY;
	
	private static boolean mDBInit = false; 
	
	protected ArrayList<FinanceItem> mIncomeDailyItems = null;
	protected ArrayList<FinanceItem> mExpenseDailyItems = null;
	protected ArrayList<FinanceItem> mIncomeMonthlyItems = null;
	protected ArrayList<FinanceItem> mExpenseMonthlyItems = null;
	
	protected DailyItem mMonthlyItems[] = new DailyItem[LAST_DAY_OF_MONTH];
//	protected ReportDailyItemAdapter mIncomeDailyAdapter = null;
	protected ReportDailyItemAdapter mExpenseDailyAdapter = null;
	protected ReportMonthlyItemAdapter mMonthlyAdapter = null;
	
	private int mCurrentViewMode = ItemDef.VIEW_DAY_OF_MONTH;
	private Calendar mCalendarMonthly = Calendar.getInstance();
	private float mTouchMove;
	private boolean mTouchMoveFlag = false;
	private int mSelectedCategoryId = -1;
	
	protected ArrayList<CardExpenseInfo> mCardAlarmItems = new ArrayList<CardExpenseInfo>();
	protected ReportAlarmItemAdapter mAlarmAdapter = null;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_income_and_expense, false);
        
        if (mDBInit == false) {
        	DBMgr.initialize(getApplicationContext());
            DBMgr.addRepeatItems();
           
            settlementDay();
            setAlarmView();
            
            mDBInit = true;
        }
        
        initAlarmView();
        setRootView(true);
        setBtnClickListener();
        setTitle(getResources().getString(R.string.app_name));
        
    	changeViewMode();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == MsgDef.ActRequest.ACT_ALARM_CARD_PAYMENT) {
    		settlementDay();
            setAlarmView();
    	}
    	else if (requestCode == MsgDef.ActRequest.ACT_SELECT_DATE) {
    		if (resultCode == RESULT_OK) {
    			
    			int[] values = data.getIntArrayExtra("SELECTED_DATE");
    			
    			Calendar calender = FinanceCurrentDate.getDate();
    			
    			if (mCurrentViewMode == ItemDef.VIEW_MONTH) {
    				calender = mCalendarMonthly;
    			}
    			
    			calender.set( values[0], values[1], values[2]);
				moveCurrentDate(calender);
				
    		}
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }

    
    protected void initAlarmView() {
 //   	mLLAlarm = (LinearLayout) findViewById(R.id.LLAlarm);
    	findViewById(R.id.contentLayout).setBackgroundColor(Color.YELLOW);
    }
    
    protected void setAlarmView() {
    	findViewById(R.id.LLAlarmSliding).setVisibility((mCardAlarmItems.size() == 0) ? View.GONE : View.VISIBLE);
    	
    	setAlarmAdapterList();
    }

	protected void settlementDay() {
		mCardAlarmItems.clear();
		ArrayList<CardItem> cardItems = DBMgr.getCardItems(CardItem.CREDIT_CARD);
		int cardCount = cardItems.size();
		Calendar toDay = Calendar.getInstance();
		toDay.set(toDay.get(Calendar.YEAR), toDay.get(Calendar.MONTH), toDay.get(Calendar.DAY_OF_MONTH), toDay.get(Calendar.HOUR_OF_DAY), 0, 0);
//		toDay.set(toDay.get(Calendar.YEAR), toDay.get(Calendar.MONTH), 1, toDay.get(Calendar.HOUR_OF_DAY), 0, 0);
		
		for (int index = 0; index < cardCount; index++) {
			CardItem card = cardItems.get(index);
			CardPayment cardPayment = DBMgr.getCardPaymentLastItem(card.getID());
			
			if (cardPayment == null) {
				CardExpenseInfo cardInfo = makeCardExpenseInfo(card, Calendar.getInstance());
				if (cardInfo.getBillingExpenseAmount() != 0L) {
					mCardAlarmItems.add(cardInfo);
				}
			}
			else {
				Calendar targetDay = Calendar.getInstance();
				targetDay.set(targetDay.get(Calendar.YEAR), targetDay.get(Calendar.MONTH), card.getSettlementDay(), 0, 0, 0);
				targetDay.add(Calendar.SECOND, -1);
				
				Log.i("aa", FinanceDataFormat.getFullDateFormat(cardPayment.getPaymentDate().getTime()));
				
				// 결제일이 이번달인지 확인
				if (toDay.before(targetDay)) {
					targetDay.add(Calendar.MONTH, -1);
				}
				
				if (cardPayment.getPaymentDate().after(targetDay)) {
					// 치리되었다
					Log.i("aa", "처리되었다");
					
	//				mCardAlarmItems.add(makeCardExpenseInfo(card));
					
				}
				else {
					mCardAlarmItems.add(makeCardExpenseInfo(card, targetDay));
					// 처리되지 않았다.
					Log.i("aa", "처리되지 않았다.");
				}
			}
		}
		
		
//		ArrayList<CardItem> items =DBMgr.getCardItems();
//		int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//		int size = items.size();
//		
//		for (int index = 0; index < size; index++) {
//			CardItem card = items.get(index);
//			
//			if (card.getSettlementDay() == today) {
//				// 결제
//			}
//		}
		
	}
	
	private CardExpenseInfo makeCardExpenseInfo(CardItem card, Calendar targetDate) {
		CardExpenseInfo cardInfo = new CardExpenseInfo(card);
		targetDate.set(Calendar.DAY_OF_MONTH, card.getSettlementDay());
		long billingExpenseAmout = DBMgr.getCardTotalExpense(card.getID(), card.getStartBillingPeriod(targetDate), card.getEndBillingPeriod(targetDate));
		cardInfo.setBillingExpenseAmount(billingExpenseAmout);
		cardInfo.setSettlementDate(targetDate);
		return cardInfo;
	}

	private void addCardPaymentAlarm(CardItem card) {
//		mLLAlarm.addView(btnBookmark, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//		btnBookmark.setOnClickListener(mClickListener);
	}

	/**
     * activity가 다시 시작할 때
     */
    protected void onResume() {
    	
    	getDailyListItem();
        getMonthlyListItem();
        
        setDailyAdapterList();
        setMonthlyAdapterList();
    	updateViewText();
    	super.onResume();
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
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	setMoveViewMotionEvent(event);
    	return true;
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
    
    /**
     * 수입, 지출 프로그레스 바 설정
     */
    private void setIncomeExpenseProgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.IncomeExpensePrograss);
		
		long incomeAmount = DBMgr.getTotalAmount(IncomeItem.TYPE);
		long expenseAmount = DBMgr.getTotalAmount(ExpenseItem.TYPE);
		long sumAmount = incomeAmount - expenseAmount;
		 
		TextView tvIncomeExpense = (TextView)findViewById(R.id.TVIncomeExpenseAmount);
		tvIncomeExpense.setText(String.format("%,d원", sumAmount));
		
		
		if (sumAmount < 0) {
			progress.setMax(100);
			progress.setProgress(5);
			tvIncomeExpense.setTextColor(Color.RED);
		}
		else {
			// 테스트 코드
			int max = (int)(incomeAmount/100);
			int pos = max - (int)(expenseAmount/100);
			
			progress.setMax(max);
			progress.setProgress(pos);
			tvIncomeExpense.setTextColor(Color.BLUE);
			
		}
	}


    /** 버튼 클릭시 리슨너 등록 */
    protected void setBtnClickListener() {
    	setCurrentButtonListener();
    	
    	findViewById(R.id.BtnInput).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainIncomeAndExpenseLayout.this, InputExpenseLayout.class);
				startActivity(intent);
			}
		});
    	
    	findViewById(R.id.TBViewMode).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if (mCurrentViewMode == ItemDef.VIEW_DAY_OF_MONTH) {
					mCurrentViewMode = ItemDef.VIEW_MONTH;
				}
				else {
					mCurrentViewMode = ItemDef.VIEW_DAY_OF_MONTH;
				}
				
				changeViewMode();
			}


		});
    	
    	findViewById(R.id.BtnCurrentday).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainIncomeAndExpenseLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_SELECT_DATE);
			}
		});
    }
    
    private void changeViewMode() {
		findViewById(R.id.LLDailyView).setVisibility((mCurrentViewMode == ItemDef.VIEW_DAY_OF_MONTH) ? View.VISIBLE : View.GONE);
		findViewById(R.id.LLMonthlyView).setVisibility((mCurrentViewMode == ItemDef.VIEW_MONTH) ? View.VISIBLE : View.GONE);
	}
    
    /**
     * 현재날짜 이동 버튼 클릭시
     */
    private void setCurrentButtonListener() {
    	Button btnPrevious = (Button)findViewById(R.id.BtnPreviousDay);
    	btnPrevious.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveCurrentDate(-1);
			}
		});
    	
    	Button btnNext = (Button)findViewById(R.id.BtnNextDay);
    	btnNext.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveCurrentDate(1);
			}
		});
	}
    
	protected void moveCurrentDate(int dayValue) {
		if (mCurrentViewMode == ItemDef.VIEW_DAY_OF_MONTH) {
			FinanceCurrentDate.moveCurrentDay(dayValue);
			getDailyListItem();
			setDailyAdapterList();
			updateViewText();
		}
		else  {
			mCalendarMonthly.add(Calendar.MONTH, dayValue);
			getMonthlyListItem();
			setMonthlyAdapterList();
			updateCurrentDateText();
		}
		
	}
	
	protected void moveCurrentDate(Calendar moveDate) {
		if (mCurrentViewMode == ItemDef.VIEW_DAY_OF_MONTH) {
			FinanceCurrentDate.setDate(moveDate);
			getDailyListItem();
			setDailyAdapterList();
			updateViewText();
		}
		else  {
			mCalendarMonthly = moveDate;
			getMonthlyListItem();
			setMonthlyAdapterList();
			updateCurrentDateText();
		}
		
	}
    
    /** 화면 텍스트 갱신 */
    protected void updateViewText() {
    	updateTotalIncomeText();
    	updateTotalExpenseText();
    	updateCurrentDateText();
    	updateDailyTitleText();
    	setIncomeExpenseProgress();
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
    	tvDailyExpenseAmount.setText(String.format("%,d원", -amount));
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
    	Button btnCurrentDay = (Button)findViewById(R.id.BtnCurrentday);
    	
    	if (mCurrentViewMode == ItemDef.VIEW_DAY_OF_MONTH) {
    		btnCurrentDay.setText(dateFormat.format(FinanceCurrentDate.getTime()));
    	}
    	else {
    		btnCurrentDay.setText(dateFormat.format(mCalendarMonthly.getTime()));
    	}
	}
    

    
    protected void getDailyListItem() {
    	mIncomeDailyItems = DBMgr.getItems(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	mExpenseDailyItems = DBMgr.getItems(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	
    	mSelectedCategoryId = -1;
    }
    
    protected void getDailyListItem(DailyListItem dailyItem) {
    	
    	if (DailyListItem.EXPENSE_CATEGORY == dailyItem.getType()) {
    		mIncomeDailyItems = DBMgr.getItems(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    		
    		mExpenseDailyItems.clear();
    		ArrayList<FinanceItem> expenseItems = DBMgr.getItems(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    		int size = expenseItems.size();
    		for (int index = 0; index < size; index++) {
    			FinanceItem item = expenseItems.get(index);
    			if (dailyItem.mCategoyAmount.getCategoryID() == item.getCategory().getID()) {
    				mExpenseDailyItems.add(item);
    				expenseItems.remove(index);
    				size--;
    			}
    		}
    		mExpenseDailyItems.addAll(expenseItems);
    		mSelectedCategoryId = dailyItem.getCategoyAmount().getCategoryID();
    	}
    	else {
    		getDailyListItem();
    	}
    	
    }
    
    protected void getMonthlyListItem() {
    	mIncomeMonthlyItems = DBMgr.getItems(IncomeItem.TYPE, mCalendarMonthly.get(Calendar.YEAR), mCalendarMonthly.get(Calendar.MONDAY)+1);
    	mExpenseMonthlyItems = DBMgr.getItems(ExpenseItem.TYPE, mCalendarMonthly.get(Calendar.YEAR), mCalendarMonthly.get(Calendar.MONDAY)+1);
    	updateMonthlyItems();
	}

	protected void setDailyAdapterList() {
//		setDailyIncomeAdapterList();
//		setDailyExpenseAdapterList();
		setDailyExpenseAdapterList();
    }
	
	protected void updateMonthlyItems() {
		clearMonthlyItems();
		
		int incomeItemSize = mIncomeMonthlyItems.size();
		for (int index = 0; index < incomeItemSize; index++) {
			addMonthlyItem(mIncomeMonthlyItems.get(index));
		}
		
		int expenseItemSize = mExpenseMonthlyItems.size();
		for (int index = 0; index < expenseItemSize; index++) {
			addMonthlyItem(mExpenseMonthlyItems.get(index));
		}
		
	}
	
	protected void addMonthlyItem(FinanceItem item) {
		int day = item.getCreateDate().get(Calendar.DAY_OF_MONTH);
		
		if (mMonthlyItems[day] == null) {
			mMonthlyItems[day] = new DailyItem(item.getCreateDate());
		}
		
		if (item.getType() == IncomeItem.TYPE) {
			mMonthlyItems[day].addIncomeItem(item);
		}
		else if (item.getType() == ExpenseItem.TYPE) {
			mMonthlyItems[day].addExpenseItem(item);
		}
		
	}

	protected void setMonthlyAdapterList() {
		final ArrayList<DailyListItem> monthlyListItems = getMonthlyItem();
    	final ListView listMonthly = (ListView)findViewById(R.id.LVMonthly);
    	
    	mMonthlyAdapter = new ReportMonthlyItemAdapter(this, R.layout.report_list_daily, monthlyListItems);
    	listMonthly.setAdapter(mMonthlyAdapter);
    	
    	listMonthly.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				DailyListItem item = mMonthlyAdapter.getItem(position);
				FinanceCurrentDate.setDate(item.getCalendar());
				getDailyListItem(item);
				setDailyAdapterList();
				updateViewText();
				
				mCurrentViewMode = ItemDef.VIEW_DAY_OF_MONTH;
				changeViewMode();
				
			}
		});
    	
    	listMonthly.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				setMoveViewMotionEvent(event);
		    	return false;
			}
		});
    }
	
	protected void clearMonthlyItems() {
		for (int daily = 0; daily < LAST_DAY_OF_MONTH; daily++) {
			mMonthlyItems[daily] = null;
		}
	}
	
	protected ArrayList<DailyListItem> getMonthlyItem() {
		ArrayList<DailyListItem> monthItem = new ArrayList<DailyListItem>();
		
		for (int daily = 0; daily < LAST_DAY_OF_MONTH; daily++) {
			DailyItem dailyItem = mMonthlyItems[daily];
			if (dailyItem != null) {
//				monthItem.add(mMonthlyItems[daily]);
				//makeMonthlyListItem(mMonthlyItems[daily]);
				DailyListItem listTitle = new DailyListItem(dailyItem);
				monthItem.add(listTitle);
				
				if (dailyItem.getIncomeDailyItems().size() > 0) {
					DailyListItem listIncomeTitle = new DailyListItem(DailyListItem.INCOME_TITLE, dailyItem.getDate());
					monthItem.add(listIncomeTitle);
					
					ArrayList<FinanceItem> incomeItems = dailyItem.getIncomeDailyItems();
					int incomeItemSize = incomeItems.size();
					for (int index = 0; index < incomeItemSize; index++) {
						FinanceItem item = incomeItems.get(index);
						DailyListItem listIncome = new DailyListItem(DailyListItem.INCOME_ITEM, dailyItem.getDate());
						listIncome.setItem(item);
						
						
//						monthItem.add(listIncome);
						
						dailyItem.addIncomeCategoryItem(item);
						
					}
					
					ArrayList<CategoryAmount> incomeCategory = dailyItem.getIncomeDailyCategory();
					int incomeCategorySize = incomeCategory.size();
					for (int index = 0; index < incomeCategorySize; index++) {
						CategoryAmount categoryAmount = incomeCategory.get(index);
						DailyListItem listIncomeCategory = new DailyListItem(DailyListItem.INCOME_CATEGORY, dailyItem.getDate());
						listIncomeCategory.setCategoyAmount(categoryAmount);
						
						monthItem.add(listIncomeCategory);
						
					}
					
				}
				if (dailyItem.getExpenseDailyItems().size() > 0) {
					DailyListItem listExpenseTitle = new DailyListItem(DailyListItem.EXPENSE_TITLE, dailyItem.getDate());
					monthItem.add(listExpenseTitle);
					
					ArrayList<FinanceItem> expenseItems = dailyItem.getExpenseDailyItems();
					int expenseItemSize = expenseItems.size();
					for (int index = 0; index < expenseItemSize; index++) {
						FinanceItem item = expenseItems.get(index);
						DailyListItem listExpense = new DailyListItem(DailyListItem.EXPENSE_ITEM, dailyItem.getDate());
						listExpense.setItem(item);
						
//						monthItem.add(listExpense);
						
						dailyItem.addExpenseCategoryItem(item);
					}
					
					ArrayList<CategoryAmount> expenseCategory = dailyItem.getExpenseDailyCategory();
					int expenseCategorySize = expenseCategory.size();
					for (int index = 0; index < expenseCategorySize; index++) {
						CategoryAmount categoryAmount = expenseCategory.get(index);
						DailyListItem listExpenseCategory = new DailyListItem(DailyListItem.EXPENSE_CATEGORY, dailyItem.getDate());
						listExpenseCategory.setCategoyAmount(categoryAmount);
						
						monthItem.add(listExpenseCategory);
						
					}
				}
			}
		}
		
		return monthItem;
	}
	
	protected void setDailyExpenseAdapterList() {
    	
		
		final ArrayList<FinanceItem> mIncomeExpenseItems = new ArrayList<FinanceItem> ();
		if (mIncomeDailyItems.size() > 0) {
			IncomeItem incomeTitle = new IncomeItem();
			incomeTitle.setSeparatorTitle("수입");
			mIncomeExpenseItems.add(incomeTitle);
			mIncomeExpenseItems.addAll(mIncomeDailyItems);
		}
		if (mExpenseDailyItems.size() > 0) {
			ExpenseItem expenseTitle = new ExpenseItem();
			expenseTitle.setSeparatorTitle("지출");
			mIncomeExpenseItems.add(expenseTitle);
			mIncomeExpenseItems.addAll(mExpenseDailyItems);
		}
		
    	final ListView listExpense = (ListView)findViewById(R.id.LVDailyExpense);
    	mExpenseDailyAdapter = new ReportDailyItemAdapter(this, R.layout.report_list_normal, mIncomeExpenseItems);
    	listExpense.setAdapter(mExpenseDailyAdapter);
    	
    	listExpense.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FinanceItem item = mIncomeExpenseItems.get(position);
				
		    	if (item.getType() == ExpenseItem.TYPE) {
		    		startEditInputActivity(InputExpenseLayout.class, item.getID());
		    	}
		    	else {
		    		startEditInputActivity(InputIncomeLayout.class, item.getID());
		    	}
				
			}
		});
    	
    	listExpense.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				setMoveViewMotionEvent(event);
		    	return false;
			}
		});
	}
    
//
//    protected void setDailyExpenseAdapterList() {
//    	if (mExpenseDailyItems == null) return;
//    	
//    	findViewById(R.id.LLDailyExpense).setVisibility((mExpenseDailyItems.size() == 0) ? View.GONE : View.VISIBLE);
//        
//    	final ListView listExpense = (ListView)findViewById(R.id.LVDailyExpense);
//    	mExpenseDailyAdapter = new ReportDailyItemAdapter(this, R.layout.report_list_normal, mExpenseDailyItems);
//    	listExpense.setAdapter(mExpenseDailyAdapter);
//    	
//    	listExpense.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				FinanceItem item = mExpenseDailyItems.get(position);
//				
//		    	if (item.getType() == ExpenseItem.TYPE) {
//		    		startEditInputActivity(InputExpenseLayout.class, item.getID());
//		    	}
//		    	else {
//		    		startEditInputActivity(InputIncomeLayout.class, item.getID());
//		    	}
//				
//			}
//		});
//    	
//    	listExpense.setOnTouchListener(new View.OnTouchListener() {
//			
//			public boolean onTouch(View v, MotionEvent event) {
//				setMoveViewMotionEvent(event);
//		    	return false;
//			}
//		});
//	}
//
//	protected void setDailyIncomeAdapterList() {
//    	if (mIncomeDailyItems == null) return;
//    	
//    	findViewById(R.id.LLDailyIncome).setVisibility((mIncomeDailyItems.size() == 0) ? View.GONE : View.VISIBLE);
//        
//    	final ListView listIncome = (ListView)findViewById(R.id.LVDailyIncome);
//    	mIncomeDailyAdapter = new ReportDailyItemAdapter(this, R.layout.report_list_normal, mIncomeDailyItems);
//    	listIncome.setAdapter(mIncomeDailyAdapter);
//    	
//    	listIncome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//	//			setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
//				
//			}
//		});
//    	
//    	listIncome.setOnTouchListener(new View.OnTouchListener() {
//			
//			public boolean onTouch(View v, MotionEvent event) {
//				setMoveViewMotionEvent(event);
//		    	return false;
//			}
//		});
//	}
//	
	public static class ViewHolder {
		private TextView mTvTitle;
		private TextView mTvLeft;
		private TextView mTvCenterTop;
		private TextView mTvCenterBottom;
		private TextView mTvRightTop;
		private TextView mTvRightBottom;
		
		public ViewHolder(TextView tvTitle, TextView tvLeft, TextView tvCenterTop, TextView tvCenterBottom, TextView tvRightTop, TextView tvRightBottom) {
			mTvTitle = tvTitle;
			mTvLeft	= tvLeft;
			mTvCenterTop = tvCenterTop;
			mTvCenterBottom = tvCenterBottom;
			mTvRightTop = tvRightTop;
			mTvRightBottom = tvRightBottom;
		}
		
		public TextView getTitleView() {
			return mTvTitle;
		}
		
		public TextView getLeftTextView() {
			return mTvLeft;
		}

		public TextView getCenterTopTextView() {
			return mTvCenterTop;
		}

		public TextView getCenterBottomTextView() {
			return mTvCenterBottom;
		}

		public TextView getRightTopTextView() {
			return mTvRightTop;
		}

		public TextView getRightBottomTextView() {
			return mTvRightBottom;
		}
	}

    
    public class ReportDailyItemAdapter extends ArrayAdapter<FinanceItem> {
    	int mResource;
    	private LayoutInflater mInflater;

		public ReportDailyItemAdapter(Context context, int resource,
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
			
			if (item.isSeparator()){
				convertView.findViewById(R.id.LLTitle).setVisibility(View.VISIBLE);
				convertView.findViewById(R.id.LLBody).setVisibility(View.GONE);
				ViewHolder viewHolder = (ViewHolder) convertView.getTag();
				
				TextView tvTitle = viewHolder.getTitleView();
				tvTitle.setText(item.getSeparatorTitle());
				tvTitle.setTextColor(Color.BLACK);
				tvTitle.setBackgroundColor(Color.WHITE);
				
			} else {
				convertView.findViewById(R.id.LLTitle).setVisibility(View.GONE);
				convertView.findViewById(R.id.LLBody).setVisibility(View.VISIBLE);
				convertView.setBackgroundColor((mSelectedCategoryId == item.getCategory().getID()) ? Color.MAGENTA : Color.BLACK);
				setListViewText(item, convertView);
			}
			
			return convertView;
		}
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
	
	protected void startEditInputActivity(Class<?> cls, int itemId) {
		Intent intent = new Intent(this, cls);
    	intent.putExtra("EDIT_ITEM_ID", itemId);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
	public class DailyItem {
		private Calendar mCalendar;
		private int mTotalIncomeAmount;
		private int mTatalExpenseAmount;
		private int mIncomeCount;
		private int mExpenseCount;
		
		protected ArrayList<FinanceItem> mIncomeDailyItems = new ArrayList<FinanceItem>();
		protected ArrayList<FinanceItem> mExpenseDailyItems = new ArrayList<FinanceItem>();
		
		protected ArrayList<CategoryAmount> mIncomeDailyCategory = new ArrayList<CategoryAmount>();
		protected ArrayList<CategoryAmount> mExpenseDailyCategory = new ArrayList<CategoryAmount>();
		
		public DailyItem(Calendar calendar) {
			mCalendar = calendar;
		}
		
		public int getTotalIncomeAmount() {
			return mTotalIncomeAmount;
		}
		
		public int getTotalExpenseAmount() {
			return mTatalExpenseAmount;
		}
		
		public int getIncomeCount() {
			return mIncomeCount;
		}
		
		public int getExpenseCount() {
			return mExpenseCount;
		}
		
		public int getDay() {
			return mCalendar.get(Calendar.DAY_OF_MONTH);
		}
		
		public Calendar getDate() {
			return mCalendar;
		}
		
		public ArrayList<FinanceItem> getIncomeDailyItems() {
			return mIncomeDailyItems;
		}
		
		public ArrayList<FinanceItem> getExpenseDailyItems() {
			return mExpenseDailyItems;
		}
		
		public void addIncomeItem(FinanceItem item) {
			mTotalIncomeAmount += item.getAmount();
			mIncomeCount++;
			mIncomeDailyItems.add(item);
		}
		
		public void addExpenseItem(FinanceItem item) {
			mTatalExpenseAmount += item.getAmount();
			mExpenseCount++;
			mExpenseDailyItems.add(item);
		}
		
		public ArrayList<CategoryAmount> getIncomeDailyCategory() {
			return mIncomeDailyCategory;
		}
		
		public ArrayList<CategoryAmount> getExpenseDailyCategory() {
			return mExpenseDailyCategory;
		}
		
		public void addIncomeCategoryItem(FinanceItem item) {
			CategoryAmount categoryAmount = getIncomeCategoryItem(item);
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount(item.getType());
				categoryAmount.set(item.getCategory().getID(), item.getCategory().getName(), item.getAmount());
				mIncomeDailyCategory.add(categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getAmount());
			}
		}
		
		public CategoryAmount getIncomeCategoryItem(FinanceItem item) {
			int categorySize = mIncomeDailyCategory.size();
			for (int index = 0; index < categorySize; index++) {
				CategoryAmount categoryAmount = mIncomeDailyCategory.get(index);
				if (categoryAmount.getCategoryID() == item.getCategory().getID()) {
					return categoryAmount;
				}
			}
			return null;
		}
		
		public void addExpenseCategoryItem(FinanceItem item) {
			CategoryAmount categoryAmount = getExpenseCategoryItem(item);
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount(item.getType());
				categoryAmount.set(item.getCategory().getID(), item.getCategory().getName(), item.getAmount());
				mExpenseDailyCategory.add(categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getAmount());
			}
		}
		
		public CategoryAmount getExpenseCategoryItem(FinanceItem item) {
			int categorySize = mExpenseDailyCategory.size();
			for (int index = 0; index < categorySize; index++) {
				CategoryAmount categoryAmount = mExpenseDailyCategory.get(index);
				if (categoryAmount.getCategoryID() == item.getCategory().getID()) {
					return categoryAmount;
				}
			}
			return null;
		}
	}
	
	public class DailyListItem {
		public static final int DAY_TITLE = 0;
		public static final int INCOME_TITLE = 1;
		public static final int INCOME_ITEM = 2;
		public static final int INCOME_CATEGORY = 3;
		public static final int EXPENSE_TITLE = 4;
		public static final int EXPENSE_ITEM = 5;
		public static final int EXPENSE_CATEGORY = 6;
		
		private int mType = DAY_TITLE;
		private Calendar mCalendar;
		private int mTotalIncomeAmount;
		private int mTotalExpenseAmount;
		private int mIncomeCount;
		private int mExpenseCount;
		private FinanceItem mItem;
		private CategoryAmount mCategoyAmount;
		
		DailyListItem(DailyItem item) {
			mType = DAY_TITLE;
			mCalendar = item.getDate();
			mTotalIncomeAmount = item.getTotalIncomeAmount();
			mTotalExpenseAmount = item.getTotalExpenseAmount();
			mIncomeCount = item.getIncomeCount();
			mExpenseCount = item.getExpenseCount();
		}
		
		DailyListItem(int type, Calendar claendar) {
			mType = type;
			mCalendar = claendar;
		}
		
		public void setType(int mType) {
			this.mType = mType;
		}
		public int getType() {
			return mType;
		}
		public void setCalendar(Calendar mCalendar) {
			this.mCalendar = mCalendar;
		}
		public Calendar getCalendar() {
			return mCalendar;
		}
		public int getTotalIncomeAmount() {
			return mTotalIncomeAmount;
		}
		
		public int getTotalExpenseAmount() {
			return mTotalExpenseAmount;
		}
		
		public int getIncomeCount() {
			return mIncomeCount;
		}
		
		public int getExpenseCount() {
			return mExpenseCount;
		}
		
		public int getDay() {
			return mCalendar.get(Calendar.DAY_OF_MONTH);
		}
		
		public Calendar getDate() {
			return mCalendar;
		}
		public void setItem(FinanceItem item) {
			this.mItem = item;
		}
		public FinanceItem getItem() {
			return mItem;
		}

		public void setCategoyAmount(CategoryAmount categoyAmount) {
			this.mCategoyAmount = categoyAmount;
		}

		public CategoryAmount getCategoyAmount() {
			return mCategoyAmount;
		}
	}
	
	
	
	public class ReportMonthlyItemAdapter extends ArrayAdapter<DailyListItem> {
    	int mResource;
    	private LayoutInflater mInflater;

		public ReportMonthlyItemAdapter(Context context, int resource,
				 List<DailyListItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DailyListItem item = (DailyListItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
				
//				ViewHolder viewHolder = new ViewHolder(
//						(TextView)convertView.findViewById(R.id.TVListLeft), 
//						(TextView)convertView.findViewById(R.id.TVListCenterTop), 
//						(TextView)convertView.findViewById(R.id.TVListCenterBottom), 
//						(TextView)convertView.findViewById(R.id.TVListRightTop), 
//						(TextView)convertView.findViewById(R.id.TVListRightBottom));
//				
//				convertView.setTag(viewHolder);
			}
			
			convertView.findViewById(R.id.LLTitle).setVisibility(View.GONE);
			convertView.findViewById(R.id.LLIncomeTitle).setVisibility(View.GONE);
			convertView.findViewById(R.id.LLExpenseTitle).setVisibility(View.GONE);
			convertView.findViewById(R.id.LLItem).setVisibility(View.GONE);
			convertView.findViewById(R.id.LLCategory).setVisibility(View.GONE);
			
			if (item.getType() == DailyListItem.DAY_TITLE) {
				convertView.findViewById(R.id.LLTitle).setVisibility(View.VISIBLE);
				
				TextView tvDaily = (TextView)convertView.findViewById(R.id.TVDailyDay);
				tvDaily.setText(String.valueOf(item.getDay()));
				
				TextView tvWeely = (TextView)convertView.findViewById(R.id.TVDailyWeekly);
				tvWeely.setText("(" + FinanceDataFormat.getWeekText(item.getDate()) + ")");
				
				TextView tvIncomeCount = (TextView)convertView.findViewById(R.id.TVDailyIncome);
				tvIncomeCount.setText(String.format("수입(%d건)", item.getIncomeCount()));
		    	
		    	TextView tvDailyIncomeAmount = (TextView) convertView.findViewById(R.id.TVDailyIncomeAmount);
		    	tvDailyIncomeAmount.setText(String.format("%,d원", item.getTotalIncomeAmount()));
		    	tvDailyIncomeAmount.setTextColor(Color.BLUE);
		    	
		    	TextView tvExpenseCount = (TextView)convertView.findViewById(R.id.TVDailyExpense);
		    	tvExpenseCount.setText(String.format("지출(%d건)", item.getExpenseCount()));
		    	
		    	TextView tvDailyExpenseAmount = (TextView) convertView.findViewById(R.id.TVDailyExpenseAmount);
		    	tvDailyExpenseAmount.setText(String.format("%,d원", item.getTotalExpenseAmount()));
		    	tvDailyExpenseAmount.setTextColor(Color.RED);
			}
			else if (item.getType() == DailyListItem.INCOME_TITLE) {
				convertView.findViewById(R.id.LLIncomeTitle).setVisibility(View.VISIBLE);
				
				TextView incomeTitle = (TextView) convertView.findViewById(R.id.TVIncomeTitle);
				incomeTitle.setTextColor(Color.BLACK);
				incomeTitle.setBackgroundColor(Color.WHITE);
			}
			else if (item.getType() == DailyListItem.INCOME_ITEM) {
				IncomeItem income = (IncomeItem) item.getItem();
				convertView.findViewById(R.id.LLItem).setVisibility(View.VISIBLE);
				
				TextView tvLeft = (TextView) convertView.findViewById(R.id.TVListLeft);
		    	tvLeft.setText(income.getCategory().getName());
		    	
		    	TextView tvCenterTop = (TextView) convertView.findViewById(R.id.TVListCenterTop);
//				if (income.getTitle().length() != 0) {
					tvCenterTop.setText(income.getTitle());
//				}
//				else {
//					tvCenterTop.setVisibility(View.GONE);
//				}
				
				TextView tvRightTop = (TextView) convertView.findViewById(R.id.TVListRightTop);
				tvRightTop.setText(String.format("%,d원", income.getAmount()));
				tvRightTop.setTextColor(Color.BLUE);
				
				TextView tvCenterBottom = (TextView) convertView.findViewById(R.id.TVListCenterBottom);
//				if (income.getMemo().length() != 0) {
					tvCenterBottom.setText(income.getMemo());
//				}
//				else {
//					tvCenterBottom.setVisibility(View.GONE);
//				}
				
				TextView tvRightBottom = (TextView) convertView.findViewById(R.id.TVListRightBottom);
				tvRightBottom.setText(income.getAccountText());
			}
			else if (item.getType() == DailyListItem.INCOME_CATEGORY) {
				CategoryAmount categoryAmount = item.getCategoyAmount();
				convertView.findViewById(R.id.LLCategory).setVisibility(View.VISIBLE);
				
				TextView tvLeft = (TextView) convertView.findViewById(R.id.TVCategoryName);
		    	tvLeft.setText(String.format("%s(%d)", categoryAmount.getName(), categoryAmount.getCount()));
		    	
		    	TextView tvRightTop = (TextView) convertView.findViewById(R.id.TVCategoryAmount);
				tvRightTop.setText(String.format("%,d원", categoryAmount.getTotalAmount()));
				tvRightTop.setTextColor(Color.BLUE);
			}
			else if (item.getType() == DailyListItem.EXPENSE_TITLE) {
				convertView.findViewById(R.id.LLExpenseTitle).setVisibility(View.VISIBLE);
				
				TextView epxenseTitle = (TextView) convertView.findViewById(R.id.TVExpenseTitle);
				epxenseTitle.setTextColor(Color.BLACK);
				epxenseTitle.setBackgroundColor(Color.WHITE);
			}
			else if (item.getType() == DailyListItem.EXPENSE_ITEM) {
				ExpenseItem expense = (ExpenseItem) item.getItem();
				convertView.findViewById(R.id.LLItem).setVisibility(View.VISIBLE);
				
				TextView tvLeft = (TextView) convertView.findViewById(R.id.TVListLeft);
		    	tvLeft.setText(expense.getCategory().getName());
		    	
		    	TextView tvCenterTop = (TextView) convertView.findViewById(R.id.TVListCenterTop);
		    	tvCenterTop.setText(expense.getSubCategory().getName());
				
				TextView tvRightTop = (TextView) convertView.findViewById(R.id.TVListRightTop);
				tvRightTop.setText(String.format("%,d원", -expense.getAmount()));
				tvRightTop.setTextColor(Color.BLUE);
				
				TextView tvCenterBottom = (TextView) convertView.findViewById(R.id.TVListCenterBottom);
				if (expense.getMemo().length() != 0) {
					tvCenterBottom.setText(expense.getMemo());
				}
				else {
					tvCenterBottom.setVisibility(View.GONE);
				}
				
				TextView tvRightBottom = (TextView) convertView.findViewById(R.id.TVListRightBottom);
				tvRightBottom.setText(expense.getPaymentMethod().getText());
				
			}
			else if (item.getType() == DailyListItem.EXPENSE_CATEGORY) {
				CategoryAmount categoryAmount = item.getCategoyAmount();
				convertView.findViewById(R.id.LLCategory).setVisibility(View.VISIBLE);
				
				TextView tvLeft = (TextView) convertView.findViewById(R.id.TVCategoryName);
		    	tvLeft.setText(String.format("%s(%d)", categoryAmount.getName(), categoryAmount.getCount()));
		    	
		    	TextView tvRightTop = (TextView) convertView.findViewById(R.id.TVCategoryAmount);
				tvRightTop.setText(String.format("%,d원", -categoryAmount.getTotalAmount()));
				tvRightTop.setTextColor(Color.RED);
			}
    	
			return convertView;
		}
    }
	
	public class ReportAlarmItemAdapter extends ArrayAdapter<CardExpenseInfo> {
    	int mResource;
    	private LayoutInflater mInflater;

		public ReportAlarmItemAdapter(Context context, int resource,
				 List<CardExpenseInfo> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CardExpenseInfo item = (CardExpenseInfo)getItem(position);
			
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
			
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
	    	viewHolder.getLeftTextView().setText(item.getCard().getCompenyName().getName());
			viewHolder.getCenterTopTextView().setText(item.getCard().getNumber());
			viewHolder.getRightTopTextView().setText(String.format("%,d원", item.getBillingExpenseAmount()));
			viewHolder.getRightTopTextView().setTextColor(Color.RED);
			
			viewHolder.getCenterBottomTextView().setText(item.getCard().getName());
			
			return convertView;
		}
    }
	
	protected void setAlarmAdapterList() {
    	final ListView listAlarm = (ListView)findViewById(R.id.LVAlarm);
    	
    	mAlarmAdapter= new ReportAlarmItemAdapter(this, R.layout.report_list_normal, mCardAlarmItems);
    	listAlarm.setAdapter(mAlarmAdapter);
    	
    	listAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//Intent intent = new Intent(MainIncomeAndExpenseLayout.this, CardPaymentLayout.class);
				Intent intent = new Intent(MainIncomeAndExpenseLayout.this, CardDetailCreditLayout.class);
				intent.putExtra("Action", CardDetailCreditLayout.ACTION_NOTIFICATION_INTO);
				intent.putExtra(MsgDef.ExtraNames.CARD_EXPENSE_INFO_ITEM, mAlarmAdapter.getItem(position));
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ALARM_CARD_PAYMENT);
			}
		});
    	
    }
}