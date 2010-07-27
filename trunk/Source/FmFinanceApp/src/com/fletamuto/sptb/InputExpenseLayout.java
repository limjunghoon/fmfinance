package com.fletamuto.sptb;


import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class InputExpenseLayout extends InputBaseLayout {
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_expense);
        
        updateDate();
        SetDateBtnClickListener(R.id.BtnExpenseDate);
        SetAmountBtnClickListener(R.id.BtnExpenseAmount);
        SetSaveBtnClickListener(R.id.BtnExpenseSave);
        SetCategoryClickListener(R.id.BtnExpenseCategory);
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnExpenseDate);
    } 
    
    protected void saveData() {
    	String memo = ((TextView)findViewById(R.id.ETExpenseMemo)).getText().toString();
    	getData().setMemo(memo);
    	
    	if (DBMgr.getInstance().addExpenseInfo((ExpenseItem)dataInfo) == true) {
    		
    	}
    	else {
    		
    	}
    	
    	Intent intent = new Intent(InputExpenseLayout.this, ReportExpenseLayout.class);
		startActivity(intent);
    }

    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnExpenseAmount);
	}

	@Override
	protected void createInfoDataInstance() {
		dataInfo = new ExpenseItem();
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputExpenseLayout.this, CategoryExpenseLayout.class);
		startActivity(intent);
	}
}
