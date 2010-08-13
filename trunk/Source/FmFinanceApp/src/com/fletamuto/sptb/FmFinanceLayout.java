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
 * ���� ���̾ƿ� Ŭ����
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
     * activity�� �ٽ� ������ ��
     */
    protected void onResume() {
    	updateViewText();
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
    }
    
    /**
     * ��ư Ŭ����  Activityȭ�� ��ȯ ����
     * @param id ��ư ���̵�
     */
    protected void setChangeActivityBtnClickListener(int id) {
    	((Button)findViewById(id)).setOnClickListener(new ChangeActivity());
    }
    
    /** ȭ�� �ؽ�Ʈ �ʱ�ȭ */
    protected void InitViewText() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	TextView tvToday = (TextView)findViewById(R.id.TVToday);
    	tvToday.setText(dateFormat.format(Calendar.getInstance().getTime()));
    	
    }
    
    /** ȭ�� �ؽ�Ʈ ���� */
    protected void updateViewText() {
    	updateTotalIncomeText();
    	updateTotalExpenseText();
    	updateTodayDateText();
    	updateTodayIncomeText();
    	updateTodayExpenseText();
    }
    
    /** �� ���� �׼� ���� */
    protected void updateTotalIncomeText() {
    	long amount = DBMgr.getInstance().getTotalAmount(IncomeItem.TYPE);
    	TextView totalIncome = (TextView)findViewById(R.id.TVTotalIncome);
    	totalIncome.setText(String.format("%,d��", amount));
    	totalIncome.setTextColor(Color.BLUE);
	}
    
    /** �� ���� �׼� ���� */
    protected void updateTotalExpenseText() {
    	long amount = DBMgr.getInstance().getTotalAmount(ExpenseItem.TYPE);
    	TextView totalExpense = (TextView)findViewById(R.id.TVTotalExpense);
    	totalExpense.setText(String.format("%,d��", -amount));
    	totalExpense.setTextColor(Color.RED);
	}
    
    /** ���� ��¥ ���� */
    protected void updateTodayDateText() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	TextView tvToday = (TextView)findViewById(R.id.TVToday);
    	tvToday.setText(dateFormat.format(Calendar.getInstance().getTime()));
	}
    
    /** ���� ���� �������� */
    protected void updateTodayIncomeText() {
    	int count = DBMgr.getInstance().getItemCount(IncomeItem.TYPE, Calendar.getInstance());
    	long amount = DBMgr.getInstance().getTotalAmountDay(IncomeItem.TYPE, Calendar.getInstance());
    	Button incomeToday = (Button)findViewById(R.id.BtnTodayIncome);
    	incomeToday.setText(String.format("����(%d��) \t\t\t\t\t %,d��", count, amount));
	}
    
    /** ���� ���� �������� */
    protected void updateTodayExpenseText() {
    	int count = DBMgr.getInstance().getItemCount(ExpenseItem.TYPE, Calendar.getInstance());
    	long amount = DBMgr.getInstance().getTotalAmountDay(ExpenseItem.TYPE, Calendar.getInstance());
    	Button expenseToday = (Button)findViewById(R.id.BtnTodayExpense);
    	expenseToday.setText(String.format("����(%d��) \t\t\t\t\t %,d��", count, -amount));
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