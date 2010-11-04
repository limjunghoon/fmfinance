package com.fletamuto.sptb;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.FinanceCurrentDate;

/**
 * 메인 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class FmFinanceLayout extends FmBaseActivity {  	
	//private FinanceCurrentDate mCurrentDay;
	private ChangeActivity startActivity = new ChangeActivity();
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main, false);
        
        DBMgr.initialize(getApplicationContext());
        DBMgr.addRepeatItems();
        
        setBtnClickListener();
        updateViewText();
        
        setTitle(getResources().getString(R.string.app_name));
        setIncomeExpenseProgress();
    }
    

	/**
     * activity가 다시 시작할 때
     */
    protected void onResume() {
    	updateViewText();
    	setIncomeExpenseProgress();
    	super.onResume();
    }

    /** 버튼 클릭시 리슨너 등록 */
    protected void setBtnClickListener() {
    	setChangeActivityBtnClickListener(R.id.BtnInputExpense);
    	setChangeActivityBtnClickListener(R.id.BtnInputIncome);
    	setChangeActivityBtnClickListener(R.id.BtnAssetsLiability);
    	setChangeActivityBtnClickListener(R.id.BtnReport);
    	setChangeActivityBtnClickListener(R.id.BtnTodayExpense);
    	setChangeActivityBtnClickListener(R.id.BtnTodayIncome);
    	setChangeActivityBtnClickListener(R.id.BtnSetting);
    	setChangeActivityBtnClickListener(R.id.BtnCard);
    	setChangeActivityBtnClickListener(R.id.BtnAccount);
    	setChangeActivityBtnClickListener(R.id.BtnBudget);
    	setChangeActivityBtnClickListener(R.id.BtnPurpose);
    	setChangeActivityBtnClickListener(R.id.BtnSearch);
    	
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
    	updateCurrentIncomeText();
    	updateCurrentExpenseText();
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
    
    /** 현재 수입 정보갱신 */
    protected void updateCurrentIncomeText() {
    	int count = DBMgr.getItemCount(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	long amount = DBMgr.getTotalAmountDay(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	Button incomeCurrnet = (Button)findViewById(R.id.BtnTodayIncome);
    	incomeCurrnet.setText(String.format("수입(%d건) \t\t\t\t\t %,d원", count, amount));
	}
    
    /** 현재 지출 정보갱신 */
    protected void updateCurrentExpenseText() {
    	int count = DBMgr.getItemCount(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	long amount = DBMgr.getTotalAmountDay(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	Button expenseCurrnet = (Button)findViewById(R.id.BtnTodayExpense);
    	expenseCurrnet.setText(String.format("지출(%d건) \t\t\t\t\t %,d원", count, -amount));
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
			else if (id == R.id.BtnInputIncome) changeClass = InputIncomeLayout.class;
			else if (id == R.id.BtnAssetsLiability) changeClass = InputAssetsOrLiability.class;
			else if (id == R.id.BtnReport) 	changeClass = MainReportLayout.class;
			else if (id == R.id.BtnTodayExpense) changeClass = ReportCurrentExpenseLayout.class;
			else if (id == R.id.BtnTodayIncome) changeClass = ReportCurrentIncomeLayout.class;
			else if (id == R.id.BtnSetting) changeClass = MainSettingLayout.class;
			else if (id == R.id.BtnCard) changeClass = CardLayout.class;
			else if (id == R.id.BtnAccount) changeClass = AccountLayout.class;
			else if (id == R.id.BtnBudget) changeClass = BudgetLayout.class;
			else if (id == R.id.BtnPurpose) changeClass = PurposeLayout.class;
			else if (id == R.id.BtnSearch) changeClass = SearchLayout.class;
			else {
				Log.e(LogTag.LAYOUT, "== unregistered event hander ");
				return;
			}
			
	    	Intent intent = new Intent(FmFinanceLayout.this, changeClass);
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
}