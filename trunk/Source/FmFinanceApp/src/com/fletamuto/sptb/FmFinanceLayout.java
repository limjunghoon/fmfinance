package com.fletamuto.sptb;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 메인 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class FmFinanceLayout extends Activity {  	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         
        DBMgr.getInstance().initialize(getApplicationContext());	
        setBtnClickListener();
        updateViewText();
    }
    
    /**
     * activity가 다시 시작할 때
     */
    protected void onResume() {
    	updateViewText();
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
    }
    
    /**
     * 버튼 클릭시  Activity화면 전환 설정
     * @param id 버튼 아이디
     */
    protected void setChangeActivityBtnClickListener(int id) {
    	((Button)findViewById(id)).setOnClickListener(new ChangeActivity());
    }
    
    /** 화면 텍스트 초기화 */
    protected void InitViewText() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	TextView tvToday = (TextView)findViewById(R.id.TVToday);
    	tvToday.setText(dateFormat.format(Calendar.getInstance().getTime()));
    	
    }
    
    /** 화면 텍스트 갱신 */
    protected void updateViewText() {
    	updateTotalIncomeText();
    	updateTotalExpenseText();
    	updateTodayDateText();
    	updateTodayIncomeText();
    	updateTodayExpenseText();
    }
    
    /** 총 수입 액수 갱신 */
    protected void updateTotalIncomeText() {
    	long amount = DBMgr.getInstance().getTotalAmount(IncomeItem.TYPE);
    	TextView totalIncome = (TextView)findViewById(R.id.TVTotalIncome);
    	totalIncome.setText(String.format("%,d원", amount));
    	totalIncome.setTextColor(Color.BLUE);
	}
    
    /** 총 지출 액수 갱신 */
    protected void updateTotalExpenseText() {
    	long amount = DBMgr.getInstance().getTotalAmount(ExpenseItem.TYPE);
    	TextView totalExpense = (TextView)findViewById(R.id.TVTotalExpense);
    	totalExpense.setText(String.format("%,d원", amount));
    	totalExpense.setTextColor(Color.RED);
	}
    
    /** 오늘 날짜 갱신 */
    protected void updateTodayDateText() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	TextView tvToday = (TextView)findViewById(R.id.TVToday);
    	tvToday.setText(dateFormat.format(Calendar.getInstance().getTime()));
	}
    
    /** 오늘 수입 정보갱신 */
    protected void updateTodayIncomeText() {
    	int count = DBMgr.getInstance().getItemCount(IncomeItem.TYPE, Calendar.getInstance());
    	long amount = DBMgr.getInstance().getTotalAmountDay(IncomeItem.TYPE, Calendar.getInstance());
    	Button incomeToday = (Button)findViewById(R.id.BtnTodayIncome);
    	incomeToday.setText(String.format("수입(%d건) \t\t\t\t\t %,d원", count, amount));
	}
    
    /** 오늘 지출 정보갱신 */
    protected void updateTodayExpenseText() {
    	int count = DBMgr.getInstance().getItemCount(ExpenseItem.TYPE, Calendar.getInstance());
    	long amount = DBMgr.getInstance().getTotalAmountDay(ExpenseItem.TYPE, Calendar.getInstance());
    	Button expenseToday = (Button)findViewById(R.id.BtnTodayExpense);
    	expenseToday.setText(String.format("지출(%d건) \t\t\t\t\t %,d원", count, amount));
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
			
			if (id == R.id.BtnInputExpense) changeClass = InputExpenseLayout.class;
			else if (id == R.id.BtnInputIncome) changeClass = InputIncomeLayout.class;
			else if (id == R.id.BtnAssetsLiability) changeClass = InputAssetsOrLiability.class;
			else if (id == R.id.BtnReport) changeClass = MainReportLayout.class;
			else if (id == R.id.BtnTodayExpense) changeClass = ReportTodayExpenseLayout.class;
			else if (id == R.id.BtnTodayIncome) changeClass = ReportTodayIncomeLayout.class;
			else {
				Log.e(LogTag.LAYOUT, "== unregistered event hander ");
				return;
			}
			
	    	Intent intent = new Intent(FmFinanceLayout.this, changeClass);
			startActivity(intent);
	    }
    }
}