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
 * ���� ���̾ƿ� Ŭ����
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
     * activity�� �ٽ� ������ ��
     */
    protected void onResume() {
    	updateViewText();
    	setIncomeExpenseProgress();
    	super.onResume();
    }

    /** ��ư Ŭ���� ������ ��� */
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
     * ���糯¥ �̵� ��ư Ŭ����
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
     * ��ư Ŭ����  Activityȭ�� ��ȯ ����
     * @param id ��ư ���̵�
     */
    protected void setChangeActivityBtnClickListener(int id) {
    	((Button)findViewById(id)).setOnClickListener(startActivity);
    }

    
    /** ȭ�� �ؽ�Ʈ ���� */
    protected void updateViewText() {
    	updateTotalIncomeText();
    	updateTotalExpenseText();
    	updateCurrentDateText();
    	updateCurrentIncomeText();
    	updateCurrentExpenseText();
    }
    
    /** �� ���� �׼� ���� */
    protected void updateTotalIncomeText() {
    	long amount = DBMgr.getTotalAmount(IncomeItem.TYPE);
    	TextView totalIncome = (TextView)findViewById(R.id.TVTotalIncome);
    	totalIncome.setText(String.format("%,d��", amount));
    	totalIncome.setTextColor(Color.BLUE);
	}
    
    /** �� ���� �׼� ���� */
    protected void updateTotalExpenseText() {
    	long amount = DBMgr.getTotalAmount(ExpenseItem.TYPE);
    	TextView totalExpense = (TextView)findViewById(R.id.TVTotalExpense);
    	totalExpense.setText(String.format("%,d��", -amount));
    	totalExpense.setTextColor(Color.RED);
	}
    
    /** ���� ��¥ ���� */
    protected void updateCurrentDateText() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy�� MM�� dd��");
    	TextView tvCurrentDay = (TextView)findViewById(R.id.TVCurrentday);
    	tvCurrentDay.setText(dateFormat.format(FinanceCurrentDate.getTime()));
	}
    
    /** ���� ���� �������� */
    protected void updateCurrentIncomeText() {
    	int count = DBMgr.getItemCount(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	long amount = DBMgr.getTotalAmountDay(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	Button incomeCurrnet = (Button)findViewById(R.id.BtnTodayIncome);
    	incomeCurrnet.setText(String.format("����(%d��) \t\t\t\t\t %,d��", count, amount));
	}
    
    /** ���� ���� �������� */
    protected void updateCurrentExpenseText() {
    	int count = DBMgr.getItemCount(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	long amount = DBMgr.getTotalAmountDay(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	Button expenseCurrnet = (Button)findViewById(R.id.BtnTodayExpense);
    	expenseCurrnet.setText(String.format("����(%d��) \t\t\t\t\t %,d��", count, -amount));
	}
    
    /**
     * ���� ȭ�鿡�� ȭ����ȯ �̺�Ʈ �߻� �� ȭ�� ����
     */
    public class ChangeActivity implements View.OnClickListener {
    	
    	/** ��ư Ŭ�� �� */
		public void onClick(View arg0) {
			changeActivity(arg0.getId());
		}
		
		/**
		 * Activity ȭ����ȯ
		 * @param id Ŭ���� ��ư ���̵�
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
     * ����, ���� ���α׷��� �� ����
     */
    private void setIncomeExpenseProgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.IncomeExpensePrograss);
		
		long incomeAmount = DBMgr.getTotalAmount(IncomeItem.TYPE);
		long expenseAmount = DBMgr.getTotalAmount(ExpenseItem.TYPE);
		long sumAmount = incomeAmount - expenseAmount;
		 
		TextView tvIncomeExpense = (TextView)findViewById(R.id.TVIncomeExpenseAmount);
		tvIncomeExpense.setText(String.format("%,d��", sumAmount));
		
		
		if (sumAmount < 0) {
			progress.setMax(100);
			progress.setProgress(5);
			tvIncomeExpense.setTextColor(Color.RED);
		}
		else {
			// �׽�Ʈ �ڵ�
			int max = (int)(incomeAmount/100);
			int pos = max - (int)(expenseAmount/100);
			
			progress.setMax(max);
			progress.setProgress(pos);
			tvIncomeExpense.setTextColor(Color.BLUE);
			
		}
			
		
	}
}