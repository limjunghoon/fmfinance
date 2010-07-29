package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;


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
    	getItem().setMemo(memo);
    	
    	if (DBMgr.getInstance().addFinanceItem(item) == true) {
    		
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
		item = new ExpenseItem();
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputExpenseLayout.this, SelectCategoryExpenseLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}
	
	@Override
	protected void updateCategory(int id, String name) {
		// TODO Auto-generated method stub
		item.setCategory(new Category(id, name));
		updateBtnCategoryText(R.id.BtnExpenseCategory);
	}
	
	protected void updateBtnCategoryText(int btnID) {
		ExpenseItem expenseItem = (ExpenseItem)item;
		String categoryText = String.format("%s - %s", expenseItem.getCategory().getName(), expenseItem.getSubCategory().getName());
    	((Button)findViewById(btnID)).setText(categoryText);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_CATEGORY) {
    		if (resultCode == RESULT_OK) {
    			((ExpenseItem)item).setSubCategory(new Category(data.getIntExtra("SUB_CATEGORY_ID", 0), data.getStringExtra("SUB_CATEGORY_NAME")));
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
