package com.fletamuto.sptb;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public abstract class InputBaseLayout extends Activity {
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	Calendar dateCalendar = Calendar.getInstance();
	
	protected abstract void updateDate();
	protected abstract void saveData();
	
	DatePickerDialog.OnDateSetListener dateDlg = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			
			dateCalendar.set(Calendar.YEAR, year);
			dateCalendar.set(Calendar.MONTH, monthOfYear);
			dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDate();
		}
	};
    
    protected void updateBtnDateText(int btnID) {				
    	Button inputDate = (Button)findViewById(btnID);
        inputDate.setText(dateFormat.format(dateCalendar.getTime()));
    }
    
    protected void SetDateBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				new DatePickerDialog(InputBaseLayout.this, dateDlg, 
						dateCalendar.get(Calendar.YEAR),
						dateCalendar.get(Calendar.MONTH), 
						dateCalendar.get(Calendar.DAY_OF_MONTH)).show(); 				
			}
		 });
    }
    
    protected void SetAmountBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputBaseLayout.this, InputAmountDialog.class);
				startActivity(intent);				
			}
		 });
    }
    
    protected void SetSaveBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				saveData();			
			}
		 });
    }
   
 
}
