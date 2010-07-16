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
        
        Button runInputExpense = (Button)findViewById(R.id.BtnInputExpense);
        runInputExpense.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(FmFinanceLayout.this, InputExpenseLayout.class);
				startActivity(intent);
			}
        });
        
        Button runInputIncome = (Button)findViewById(R.id.BtnInputIncome);
        runInputIncome.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(FmFinanceLayout.this, InputIncomeLayout.class);
				startActivity(intent);
			}
        });
    }
}