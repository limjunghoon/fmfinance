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
        
        updateChildView();
        SetDateBtnClickListener(R.id.BtnExpenseDate);
        SetAmountBtnClickListener(R.id.BtnExpenseAmount);
        SetSaveBtnClickListener(R.id.BtnExpenseSave);
        SetCategoryClickListener(R.id.BtnExpenseCategory);
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnExpenseDate);
    } 
    
    protected void saveData() {
    	if (inputMode == InputMode.ADD_MODE) {
    		if (DBMgr.getInstance().addFinanceItem(item) == false) {
        		return;
        	}
        	
        	Intent intent = new Intent(InputExpenseLayout.this, ReportExpenseLayout.class);
    		startActivity(intent);
    	}
    	else if (inputMode == InputMode.EDIT_MODE){
    		if (DBMgr.getInstance().updateFinanceItem(item) == false) {
        		return;
        	}
    		
    		setResult(RESULT_OK, new Intent());
    		finish();
    	}
    	
    }

    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnExpenseAmount);
	}

	@Override
	protected void createItemInstance() {
		item = new ExpenseItem();
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		item = DBMgr.getInstance().getItem(ExpenseItem.TYPE, id);
		if (item == null) return false;
		return true;
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputExpenseLayout.this, SelectCategoryExpenseLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}
	
	@Override
	protected void updateCategory(int id, String name) {
		item.setCategory(new Category(id, name));
		updateBtnCategoryText(R.id.BtnExpenseCategory);
	}
	
	protected void updateBtnCategoryText(int btnID) {
		String categoryText = getResources().getString(R.string.input_select_category);
		ExpenseItem expenseItem = (ExpenseItem)item;
		if (expenseItem.getCategory() != null && expenseItem.getSubCategory()!= null) {
			categoryText = String.format("%s - %s", expenseItem.getCategory().getName(), expenseItem.getSubCategory().getName());
		}
		 
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

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnCategoryText(R.id.BtnExpenseCategory);
		updateBtnAmountText(R.id.BtnExpenseAmount);
		updateEditMemoText(R.id.ETExpenseMemo);
	}

	@Override
	protected void updateData() {
		String memo = ((TextView)findViewById(R.id.ETExpenseMemo)).getText().toString();
    	getItem().setMemo(memo);
	}
}
