package com.fletamuto.sptb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FmFinanceLayout extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SetBtnClickListener();
       
    }
    
    protected void SetBtnClickListener() {
    	SetExpenseBtnClickListener();
    	SetIncomeBtnClickListener();
    	SetAssetsLiabilityBtnClickListener();
    	SetReportBtnClickListener();
    }
    
    protected void SetExpenseBtnClickListener() {
    	
    	 Button runInputExpense = (Button)findViewById(R.id.BtnInputExpense);
         runInputExpense.setOnClickListener(new Button.OnClickListener() {

 			public void onClick(View v) {
 				Intent intent = new Intent(FmFinanceLayout.this, InputExpenseLayout.class);
 				startActivity(intent);
 			}
         });
    }
    
    protected void SetIncomeBtnClickListener() {
    	
		Button runInputIncome = (Button)findViewById(R.id.BtnInputIncome);
		runInputIncome.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(FmFinanceLayout.this, InputIncomeLayout.class);
				startActivity(intent);
			}
		 });
   }
    
    protected void SetReportBtnClickListener() {
    	
		Button runInputAssetsLiability = (Button)findViewById(R.id.BtnAssetsLiability);
		runInputAssetsLiability.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(FmFinanceLayout.this, InputAssetsOrLiability.class);
				startActivity(intent);
			}
		 });
   }
    
    protected void SetAssetsLiabilityBtnClickListener() {
    	
		Button runReport = (Button)findViewById(R.id.BtnReport);
		runReport.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(FmFinanceLayout.this, MainReportLayout.class);
				startActivity(intent);
			}
		 });
	   }
}