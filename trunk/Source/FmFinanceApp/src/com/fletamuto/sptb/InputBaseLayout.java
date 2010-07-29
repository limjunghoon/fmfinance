package com.fletamuto.sptb;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.FinanceItem;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public abstract class InputBaseLayout extends Activity {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	protected FinanceItem item;
	
	protected final static int ACT_AMOUNT = 0;
	protected final static int ACT_CATEGORY = 1;
	
	protected abstract void updateDate();
	protected abstract void updateCategory(int id, String name);
	protected abstract void saveData();
	protected abstract void createInfoDataInstance();
	protected abstract void onCategoryClick();
	
	DatePickerDialog.OnDateSetListener dateDlg = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			item.getCreateDate().set(Calendar.YEAR, year);
			item.getCreateDate().set(Calendar.MONTH, monthOfYear);
			item.getCreateDate().set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDate();
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createInfoDataInstance();
    }
	
	public FinanceItem getItem() {
		return item;
	}
	
	protected SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
    
    protected void updateBtnDateText(int btnID) {				
    	((Button)findViewById(btnID)).setText(dateFormat.format(item.getCreateDate().getTime()));
    }
    
    protected void updateBtnAmountText(int btnID) {
    	((Button)findViewById(btnID)).setText(String.format("%,d¿ø", item.getAmount()));
    }
    
    protected void updateBtnCategoryText(int btnID) {
    	((Button)findViewById(btnID)).setText(item.getCategory().getName());
    }
    
    protected void updateAmount(Long amount) {
    	item.setAmount(amount);
    }
    
    protected void SetDateBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				new DatePickerDialog(InputBaseLayout.this, dateDlg, 
						item.getCreateDate().get(Calendar.YEAR),
						item.getCreateDate().get(Calendar.MONTH), 
						item.getCreateDate().get(Calendar.DAY_OF_MONTH)).show(); 				
			}
		 });
    }
    
    protected void SetAmountBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputBaseLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, ACT_AMOUNT);
			}
		 });
    }
    
    protected void SetSaveBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				saveData();		
				
				finish();
			}
		 });
    }
    
    protected void SetCategoryClickListener(int btnID) {
    	((Button)findViewById(btnID)).setOnClickListener(new Button.OnClickListener() {
    		
    		public void onClick(View v) {
    			onCategoryClick();
    		}
        });    
    }    
    
    
    public Calendar getCreateDate() {
    	return item.getCreateDate();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
    	if (requestCode == ACT_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			updateAmount(data.getLongExtra("AMOUNT", 0L));
    		}
    	}
    	else if (requestCode == ACT_CATEGORY) {
    		if (resultCode == RESULT_OK) {
    			updateCategory(data.getIntExtra("CATEGORY_ID", 0), data.getStringExtra("CATEGORY_NAME"));
    		}
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
