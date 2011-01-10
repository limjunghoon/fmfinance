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

import com.fletamuto.sptb.ReportBaseLayout.ReportItemAdapter;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.BudgetItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.FinanceCurrentDate;
import com.fletamuto.sptb.util.LogTag;

/**
 * 메인 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class MainIncomeAndExpenseLayout extends FmBaseActivity {  	
	private ChangeActivity startActivity = new ChangeActivity();
	
	protected ArrayList<FinanceItem> mIncomeItems = null;
	protected ArrayList<FinanceItem> mExpenseItems = null;
	protected ArrayList<FinanceItem> mListItems = new ArrayList<FinanceItem>();
	protected ReportItemAdapter mItemAdapter = null;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_income_and_expense);
        
        DBMgr.initialize(getApplicationContext());
        DBMgr.addRepeatItems();
        
        setBtnClickListener();
        setTitle(getResources().getString(R.string.app_name));
        
//        updateViewText();
//        setIncomeExpenseProgress();
    }

	/**
     * activity가 다시 시작할 때
     */
    protected void onResume() {
    	updateViewText();
    	setIncomeExpenseProgress();
    	getListItem();
    	setAdapterList();
    	super.onResume();
    }

    /** 버튼 클릭시 리슨너 등록 */
    protected void setBtnClickListener() {
    	setChangeActivityBtnClickListener(R.id.BtnInputExpense);
    	setChangeActivityBtnClickListener(R.id.BtnInputIncome);
    	setCurrentButtonListener();
    	
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


	/**
     * 버튼 클릭시  Activity화면 전환 설정
     * @param id 버튼 아이디
     */
    protected void setChangeActivityBtnClickListener(int id) {
    	((Button)findViewById(id)).setOnClickListener(startActivity);
    }

    
    /** 화면 텍스트 갱신 */
    protected void updateViewText() {
    	updateTotalIncomeText();
    	updateTotalExpenseText();
    	updateCurrentDateText();
    }

	/** 총 수입 액수 갱신 */
    protected void updateTotalIncomeText() {
    	long amount = DBMgr.getTotalAmount(IncomeItem.TYPE);
    	TextView totalIncome = (TextView)findViewById(R.id.TVTotalIncome);
    	totalIncome.setText(String.format("%,d원", amount));
    	totalIncome.setTextColor(Color.BLUE);
	}
    
    /** 총 지출 액수 갱신 */
    protected void updateTotalExpenseText() {
    	long amount = DBMgr.getTotalAmount(ExpenseItem.TYPE);
    	TextView totalExpense = (TextView)findViewById(R.id.TVTotalExpense);
    	totalExpense.setText(String.format("%,d원", -amount));
    	totalExpense.setTextColor(Color.RED);
	}
    
    /** 현재 날짜 갱신 */
    protected void updateCurrentDateText() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
    	TextView tvCurrentDay = (TextView)findViewById(R.id.TVCurrentday);
    	tvCurrentDay.setText(dateFormat.format(FinanceCurrentDate.getTime()));
	}
 
    
    /**
     * 메인 화면에서 화면전환 이벤트 발생 시 화면 변경
     */
    public class ChangeActivity implements View.OnClickListener {
    	
    	/** 버튼 클릭 시 */
		public void onClick(View arg0) {
			changeActivity(arg0.getId());
		}
		
		/**
		 * Activity 화면전환
		 * @param id 클릭된 버튼 아이디
		 */
		private void changeActivity(int id) {
			Class<?> changeClass = null;
			
			if (id == R.id.BtnInputExpense)	changeClass = InputExpenseLayout.class;
			else if (id == R.id.BtnInputIncome) changeClass = SelectCategoryIncomeLayout.class;
			else {
				Log.e(LogTag.LAYOUT, "== unregistered event hander ");
				return;
			}
			
	    	Intent intent = new Intent(MainIncomeAndExpenseLayout.this, changeClass);
			startActivity(intent);
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
    
    protected void getListItem() {
    	mIncomeItems = DBMgr.getItems(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	mExpenseItems = DBMgr.getItems(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	
    	updateListItem();
    }
    
    protected void setAdapterList() {
    	if (mListItems == null) return;
        
    	final ListView listItem = (ListView)findViewById(R.id.LVIncomeExpense);
    	mItemAdapter = new ReportItemAdapter(this, R.layout.report_list_expense, mListItems);
    	listItem.setAdapter(mItemAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onClickListItem(parent, view, position, id);
			}
		});
    }

    protected int getAdapterResource(int type) {
    	if (type == IncomeItem.TYPE) {
    		return R.layout.report_list_income;
    	}
    	else {
    		return R.layout.report_list_expense;
    	}
	}
    
    public class ReportItemAdapter extends ArrayAdapter<FinanceItem> {
    	private LayoutInflater mInflater;

		public ReportItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
//			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FinanceItem item = (FinanceItem)getItem(position);
			
			if (item.isSeparator()) {
				return createSeparator(mInflater, parent, item);
			}
			else {
				convertView = mInflater.inflate(getAdapterResource(item.getType()), parent, false);
			}
			
			setListViewText(item, convertView);
			setDeleteBtnListener(convertView, item, position);
			
			return convertView;
		}
		
		public View createSeparator(LayoutInflater inflater, ViewGroup parent, FinanceItem item) {
			View convertView = inflater.inflate(R.layout.list_separators, parent, false);
			TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
			tvTitle.setText(item.getSeparatorTitle());
			tvTitle.setTextColor(Color.BLACK);
			convertView.setBackgroundColor(Color.WHITE);
			return convertView;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return !mListItems.get(position).isSeparator();
		}
    }
    
    protected void updateListItem() {
    	mListItems.clear();
    	
    	if (mIncomeItems.size() > 0) {
    		IncomeItem separator = new IncomeItem();
    		separator.setSeparatorTitle("수입");
    		mListItems.add(separator);
        	mListItems.addAll(mIncomeItems);
    	}
    	if (mExpenseItems.size() > 0) {
    		ExpenseItem separator = new ExpenseItem();
    		separator.setSeparatorTitle("지출");
    		mListItems.add(separator);
    		mListItems.addAll(mExpenseItems);
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
    	((TextView)convertView.findViewById(R.id.TVIncomeReportListDate)).setVisibility(View.GONE);
		((TextView)convertView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("금액 : %,d원", income.getAmount()));
		
		TextView tvMemo = (TextView)convertView.findViewById(R.id.TVIncomeReportListMemo) ;
		if (income.getMemo().length() != 0) {
			tvMemo.setText("메모 : " + income.getMemo());
		}
		else {
			tvMemo.setVisibility(View.GONE);
		}
		
		((TextView)convertView.findViewById(R.id.TVIncomeReportListCategory)).setText("분류 : " + income.getCategory().getName());
    }
    
    protected void setExpenseListViewText(ExpenseItem expense, View convertView) {
    	((TextView)convertView.findViewById(R.id.TVExpenseReportListDate)).setVisibility(View.GONE);
		((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("금액 : %,d원", expense.getAmount()));
		
		TextView tvMemo = (TextView)convertView.findViewById(R.id.TVExpenseReportListMemo) ;
		if (expense.getMemo().length() != 0) {
			tvMemo.setText("메모 : " + expense.getMemo());
		}
		else {
			tvMemo.setVisibility(View.GONE);
		}
		
		String categoryText = String.format("%s - %s", expense.getCategory().getName(), expense.getSubCategory().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText("분류 : " + categoryText);
		
		TextView tvTag = (TextView)convertView.findViewById(R.id.TVExpenseReportListTag) ;
		if (expense.getMemo().compareTo("none") == 0) {
			tvTag.setText("태그 : " + expense.getTag().getName());
		}
		else {
			tvTag.setVisibility(View.GONE);
		}
		((TextView)convertView.findViewById(R.id.TVExpenseReportListPaymentMethod)).setText("결제 : " + expense.getPaymentMethod().getText());
    }
    
    
    protected void setDeleteBtnListener(View convertView, FinanceItem item, int position) {
    	Button btnDelete = null;
    	
    	if (item.getType() == IncomeItem.TYPE) {
    		btnDelete = (Button)convertView.findViewById(R.id.BtnReportIncomeDelete);
    	}
    	else {
    		btnDelete = (Button)convertView.findViewById(R.id.BtnReportExpenseDelete);
    	}
    	
		btnDelete.setTag(R.id.delete_id, new Integer(item.getID()));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(mDeleteBtnListener);
    }
    
    protected Button.OnClickListener mDeleteBtnListener = new  Button.OnClickListener(){
    	
		public void onClick(View arg0) {
			Integer position = (Integer)arg0.getTag(R.id.delete_position);
			Integer id = (Integer)arg0.getTag(R.id.delete_id);
			FinanceItem item = (FinanceItem)mListItems.get(position.intValue());
			
			if (deleteItemToDB(item.getType(), id) == 0) {
				Log.e(LogTag.LAYOUT, "== noting delete id : " + id);
			}
			else {
				if (item.getType() == ExpenseItem.TYPE) {
					ExpenseItem expense = (ExpenseItem) item;
			    	PaymentMethod paymentMethod = expense.getPaymentMethod();
			    	
					if (paymentMethod.getType() == PaymentMethod.ACCOUNT) {
						PaymentAccountMethod accountMethod = (PaymentAccountMethod) paymentMethod;
						if (accountMethod.getAccount() == null) {
							accountMethod.setAccount(DBMgr.getAccountItem(paymentMethod.getMethodItemID()));
						}
						AccountItem fromItem = accountMethod.getAccount();
						
		    			long fromItemBalance = fromItem.getBalance();
						fromItem.setBalance(fromItemBalance + expense.getAmount());
						DBMgr.updateAccount(fromItem);
					}
					
				}

				mListItems.remove(position.intValue());
				mItemAdapter.notifyDataSetChanged();

			}
		}
	};
	
	protected int deleteItemToDB(int type, int id) {
		return DBMgr.deleteItem(type, id);
	}
	
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	if (item.getType() == ExpenseItem.TYPE) {
    		startEditInputActivity(InputExpenseLayout.class, item.getID());
    	}
    	else {
    		startEditInputActivity(InputIncomeLayout.class, item.getID());
    	}
	}
	
	protected void startEditInputActivity(Class<?> cls, int itemId) {
		Intent intent = new Intent(this, cls);
    	intent.putExtra("EDIT_ITEM_ID", itemId);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
}